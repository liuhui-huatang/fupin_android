package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.bean.Chat;
import com.huatang.fupin.bean.ChatMsg;
import com.huatang.fupin.bean.NewChat;
import com.huatang.fupin.bean.NewLeader;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.http.NewHttpRequest;
import com.huatang.fupin.utils.DateUtil;
import com.huatang.fupin.utils.GlideUtils;
import com.huatang.fupin.utils.ImageUtil;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.MLog;
import com.huatang.fupin.utils.PopWindowUtil;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.ToastUtil;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 消息聊天页面
 *
 * @author forever
 * created at 2017/1/9 11:16
 */

public class MsgChatActivity extends BaseActivity implements TextWatcher {


    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.right_tx_menu)
    TextView rightTxMenu;
    @BindView(R.id.right_menu)
    ImageView rightMenu;
    @BindView(R.id.title_tx)
    TextView tile;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.et_msg)
    EditText etMsg;
    @BindView(R.id.bt_send)
    Button btSend;

    private NewLeader leader;
    private int pageNo = 1;
    private String sendPhoto;
    private String content;
    private String bt_send_type = "photo";
    private com.scwang.smartrefresh.layout.api.RefreshLayout refreshLayout;


    /*
     * @ forever 在 17/5/17 下午2:28 创建
     *
     * 描述：跳转到当前页面
     *
     */
    public static void startIntent(Activity activity, NewChat chat) {
        Intent it = new Intent(activity, MsgChatActivity.class);
        it.putExtra("chat", chat);
        activity.startActivity(it);
    }

    /*
     * @ forever 在 17/5/17 下午2:28 创建
     *
     * 描述：页面创建时调用
     *
     */
    Adapter myAdapter;
    NewChat chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatinfo);
        ButterKnife.bind(this);
        chat = (NewChat) getIntent().getSerializableExtra("chat");
        initHeadView();
        etMsg.addTextChangedListener(this);
        myAdapter = new Adapter(MsgChatActivity.this);
        listview.setAdapter(myAdapter);
        refreshLayout = (com.scwang.smartrefresh.layout.api.RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setPrimaryColors(getResources().getColor(R.color.dodgerblue));
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(true));
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setEnableAutoLoadmore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final com.scwang.smartrefresh.layout.api.RefreshLayout freshlayout) {
                freshlayout.finishRefresh(true);
                pageNo++;
                getData();


            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(com.scwang.smartrefresh.layout.api.RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(1000);
                list.clear();
                pageNo = 1;
                getData();

            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.show("点击了一下");
                NewChat chat = list.get(position);
                if (!TextUtils.isEmpty(chat.getPush_photo())) {
                    Intent intent = new Intent();
                    intent.setClass(MsgChatActivity.this, ImageUploadActivity.class);
                    intent.putExtra(ImageUploadActivity.PHOTO_URL, BaseConfig.ImageUrl+chat.getPush_photo());
                    startActivity(intent);

                }

            }
        });

        leader = new NewLeader();
        getData();
        //条目点击事件
    }

    private void initHeadView() {
        tile.setText(chat.getTitle() + "(" + chat.getLeader_num() + "人)");
        rightTxMenu.setText("查看成员");
        rightTxMenu.setVisibility(View.VISIBLE);

    }


    /**
     * 页面点击事件处理
     *
     * @param view
     */
    @OnClick({R.id.left_menu, R.id.right_tx_menu, R.id.bt_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;

            case R.id.bt_send:
                if (bt_send_type.equals("content")) {
                    sendPhoto = "";
                    sendContent();
                } else {
                    content = "";
                    showSelectPicture();
                }

                break;
            case R.id.right_tx_menu:
                Intent intent = new Intent();
                intent.setClass(this, MemberListActivity.class);
                intent.putExtra(MemberListActivity.CHAT, chat);
                //startActivity(intent);
                startActivityForResult(intent, MemberListActivity.REQUEST_CODE_ADD_LEADER);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == MemberListActivity.REQUEST_CODE_ADD_LEADER) {
                int num = data.getIntExtra("num",Integer.valueOf(chat.getLeader_num()));
                tile.setText(chat.getTitle() + "(" + num + "人)");

            }
        }
    }

    private void refreshChat() {
        NewHttpRequest.getchatById(this, chat.getId(), new NewHttpRequest.MyCallBack(this) {
            @Override
            public void ok(String json) {
                chat = JsonUtil.json2Bean(json, NewChat.class);
                tile.setText(chat.getTitle() + "(" + chat.getLeader_num() + "人)");
            }

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    public void sendContent() {
        String msg = etMsg.getText().toString().trim();
        if (TextUtils.isEmpty(msg)) {
            ToastUtil.show("请输入内容");
            return;
        }
        content = msg;
        sendMsg();
    }

    public void sendMsg() {
        NewHttpRequest.sendChatMsg(this, chat.getId(), chat.getTitle(), content, leader, sendPhoto, new NewHttpRequest.MyCallBack(this) {
            @Override
            public void ok(String json) {
                etMsg.setText("");
                btSend.setText("图片");
                bt_send_type = "photo";
                //getData();
                list = JsonUtil.toList(json, NewChat.class);
                myAdapter.notifyDataSetChanged();
                listview.setSelection(ListView.FOCUS_DOWN);//刷新到底部
                //listview.setSelection(list.size() > 1 ? list.size()-1 : 0);
            }

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);

            }
        });
    }

    List<NewChat> list = new ArrayList<>();

    public void getData() {
        getInitLeader();
        NewHttpRequest.getChatMsg(this, chat.getId(), String.valueOf(pageNo), new NewHttpRequest.MyCallBack(this) {
            @Override
            public void ok(String json) {
                List<NewChat> result = JsonUtil.toList(json, NewChat.class);
                if (result == null || result.size() == 0) {
                    ToastUtil.show("没有更多数据了");
                    return;

                }
                list.addAll(0, result);
                myAdapter.notifyDataSetChanged();
                listview.setSelection(ListView.FOCUS_DOWN);//刷新到底部
            }

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);

            }
        });
    }

    private void getInitLeader() {

        if (SPUtil.getString(Config.Type).equals(Config.GANBU_TYPE)) {
            leader = (NewLeader) SPUtil.getObject(Config.GANBU_KEY);
        } else if (SPUtil.getString(Config.Type).equals(Config.GANBU_TYPE)) {
            leader = (NewLeader) SPUtil.getObject(Config.ADMIN_KEY);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        btSend.setText("发送");
        bt_send_type = "content";
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 0) {
            btSend.setText("图片");
            bt_send_type = "photo";
        }

    }


    class Adapter extends BaseAdapter {

        private Context mContext;

        public Adapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            NewChat chat = list.get(position);

                if (chat.getPush_leader_id().equals(leader.getId())) {
                    convertView = View.inflate(mContext, R.layout.item_chat_right, null);
                    holder = new ViewHolder(1, convertView);

                } else {
                    convertView = View.inflate(mContext, R.layout.item_chat_left, null);
                    holder = new ViewHolder(2, convertView);
                }
               // convertView.setTag(holder);


            GlideUtils.displayHomeUrl(holder.itemPhoto, BaseConfig.ImageUrl + chat.getPush_leader_photo(), R.mipmap.header_default);
            holder.itemName.setText(chat.getPush_leader_name());

            if (TextUtils.isEmpty(chat.getContent()) && !TextUtils.isEmpty(chat.getPush_photo())) {
                GlideUtils.displayHomeUrl(holder.chat_upload_img, BaseConfig.ImageUrl +chat.getPush_photo() ,R.mipmap.yifu);
                holder.itemText.setVisibility(View.GONE);
                holder.chat_upload_img.setVisibility(View.VISIBLE);
            } else {
                holder.itemText.setText(chat.getContent());
                holder.itemText.setVisibility(View.VISIBLE);
                holder.chat_upload_img.setVisibility(View.GONE);
            }
            holder.itemTime.setText(chat.getCreate_time());

            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.item_photo)
            ImageView itemPhoto;
            @BindView(R.id.item_name)
            TextView itemName;
            @BindView(R.id.item_text)
            TextView itemText;
            @BindView(R.id.item_time)
            TextView itemTime;
            @BindView(R.id.chat_upload_img)
            ImageView chat_upload_img;

            ViewHolder(int index, View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    // －－－－－－－－－－－－－－－－－－－－－－－－－－－拍照相关的方法
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;

    /*
     * @ forever 在 17/5/17 下午5:01 创建
     *
     * 描述：打开选择照片框
     *
     */
    public void showSelectPicture() {
        final FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setMutiSelectMaxSize(6)
                .build();
        final String[] btString = new String[]{"拍照", "相册"};
        PopWindowUtil.init().show(this, R.id.chat_layout, btString, new PopWindowUtil.OnPopupWindowClickLinstener() {

            @Override
            public void popClick(Button popButton) {
                String btStr = popButton.getText().toString();
                if (btStr.equals(btString[0])) {
                    GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);
                } else if (btStr.equals(btString[1])) {
                    //相册单选
                    GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
                }
            }
        });
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                String imagePath = resultList.get(0).getPhotoPath();
                imagePath = ImageUtil.getCompressedImgPath(imagePath);
                MLog.e("onHanlderSuccess", imagePath);
                imagesUpload(imagePath);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(MsgChatActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    public void imagesUpload(String filePath) {
        /**
         * 图片上传服务器
         */
        NewHttpRequest.uploadImage(this, filePath, new NewHttpRequest.UploadCallBack() {
            @Override
            public void callback(String json) {
                ToastUtil.show("发送成功");
                SPUtil.saveString("photo", JsonUtil.getStringFromArray(json, "url"));
                sendPhoto = JsonUtil.getStringFromArray(json, "url");
                String photoUrl = BaseConfig.ImageUrl + sendPhoto;
                //GlideUtils.displayHome(ivPhoto, photoUrl);
                sendMsg();

            }
        });


    }


}
