package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.bean.NewLeader;
import com.huatang.fupin.http.NewHttpRequest;
import com.huatang.fupin.utils.GlideUtils;
import com.huatang.fupin.utils.ImageUtil;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.MLog;
import com.huatang.fupin.utils.PopWindowUtil;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.ToastUtil;
import com.huatang.fupin.utils.ViewHolderUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 发布消息页面
 *
 * @author forever
 * created at 2017/1/9 11:16
 */

public class MsgCreateChatActivity extends BaseActivity {


    @BindView(R.id.left_menu)
    ImageView leftMenu;

    @BindView(R.id.right_tx_menu)
    TextView right_tx_menu;

    @BindView(R.id.title_tx)
    TextView pageTitle;

    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_text)
    EditText eContent;
    @BindView(R.id.rl_leader)
    RelativeLayout rlLeader;
    @BindView(R.id.rl_hu)
    RelativeLayout rlHu;
    @BindView(R.id.lv_send)
    ListView lvSend;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;


    /*
     * @ forever 在 17/5/17 下午2:28 创建
     *
     * 描述：跳转到登录页面
     *
     */
    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, MsgCreateChatActivity.class);
        activity.startActivity(it);
    }

    /*
     * @ forever 在 17/5/17 下午2:28 创建
     *
     * 描述：页面创建时调用
     *
     */

    NewLeader leader;
    List<NewLeader> leader_list = new ArrayList<>();
    LeaderAdapter leaderAdapter;
    String phones = "";
    String photoUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        ButterKnife.bind(this);
        initHeadView();
        initData();
        rlLeader.setVisibility(View.VISIBLE);
        rlHu.setVisibility(View.GONE);
        leaderAdapter = new LeaderAdapter(MsgCreateChatActivity.this, leader_list);
        lvSend.setAdapter(leaderAdapter);

    }

    private void initData() {
        leader = new NewLeader();
        if (SPUtil.getString(Config.Type).equals(Config.GANBU_TYPE)) {
            leader = (NewLeader) SPUtil.getObject(Config.GANBU_KEY);
        } else if (SPUtil.getString(Config.Type).equals(Config.GANBU_TYPE)) {
            leader = (NewLeader) SPUtil.getObject(Config.ADMIN_KEY);
        }
    }

    private void initHeadView() {
        pageTitle.setText("发布消息");
        right_tx_menu.setText("发布");
        right_tx_menu.setVisibility(View.VISIBLE);


    }

    @OnClick({R.id.left_menu, R.id.right_tx_menu, R.id.rl_leader, R.id.rl_hu, R.id.iv_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.right_tx_menu:
                String title = etTitle.getText().toString().trim();
                String content = eContent.getText().toString().trim();
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
                    ToastUtil.show("请输入标题或内容");
                    return;
                }
                if (TextUtils.isEmpty(phones)) {
                    ToastUtil.show("请选择消息发布对象");
                    return;
                }
                phones += leader.getLeader_phone();
                createGroup(title, content);
                break;
            case R.id.rl_leader:
                Intent intent = new Intent(this, MsgSendSearchActivity.class);
                intent.putExtra(MsgSendSearchActivity.TAG, Config.GANBU_KEY);
                startActivityForResult(intent, MsgSendSearchActivity.LEADER_RESULT_CODE);
                break;
            case R.id.rl_hu:
                break;
            case R.id.iv_photo:
                showSelectPicture();
                break;
        }
    }


    /**
     * 创建群聊
     */
    public void createGroup(String title, String content) {
        NewHttpRequest.createChatMsg(this, title, content, leader, photoUrl, String.valueOf(leader_list.size()), phones, new NewHttpRequest.MyCallBack(this) {
            @Override
            public void ok(String json) {
                MLog.e("createGroup", json);
                ToastUtil.show("创建成功");
                finish();
            }

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        if (resultCode == MsgSendSearchActivity.LEADER_RESULT_CODE) {
            NewLeader leader = (NewLeader) data.getSerializableExtra(Config.GANBU_KEY);

            if (!TextUtils.isEmpty(phones) && phones.contains(leader.getLeader_phone())) {
                ToastUtil.show("当前用户已选择");
                return;
            }
            leader_list.add(leader);
            phones += leader.getLeader_phone() + ",";
            leaderAdapter.setData(leader_list);
            leaderAdapter.notifyDataSetChanged();
        }


    }


    class LeaderAdapter extends BaseAdapter {

        private Context mContext;

        private List<NewLeader> leaderList;

        public LeaderAdapter(Context context, List<NewLeader> leaderList) {
            mContext = context;
            this.leaderList = leaderList;
        }

        public void setData(List<NewLeader> leaderList) {
            this.leaderList = leaderList;

        }

        @Override
        public int getCount() {
            return leaderList.size();
        }

        @Override
        public Object getItem(int position) {
            return leaderList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_list, null);
            }
            TextView item_name = ViewHolderUtil.get(convertView, R.id.item_name);
            TextView item_phone = ViewHolderUtil.get(convertView, R.id.item_phone);
            RadioButton item_radio = ViewHolderUtil.get(convertView, R.id.item_radio);
            item_radio.setChecked(true);
            item_name.setText(leaderList.get(position).getLeader_name());
            item_phone.setText(leaderList.get(position).getLeader_phone());
            return convertView;
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
        PopWindowUtil.init().show(this, R.id.add_chat_layout, btString, new PopWindowUtil.OnPopupWindowClickLinstener() {

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
            Toast.makeText(MsgCreateChatActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };


    public void imagesUpload(String filePath) {
        /**
         * 图片上传服务器
         */
        NewHttpRequest.uploadImage(this, filePath, new NewHttpRequest.UploadCallBack() {
            @Override
            public void callback(String json) {
                ToastUtil.show("修改成功");
                photoUrl = JsonUtil.getStringFromArray(json, "url");
                GlideUtils.displayHome(ivPhoto, BaseConfig.ImageUrl + photoUrl);

            }
        });


    }

}
