package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.bean.Chat;
import com.huatang.fupin.bean.ChatMsg;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.utils.DateUtil;
import com.huatang.fupin.utils.GlideUtils;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.ToastUtil;

import org.apache.commons.codec.language.bm.Lang;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消息聊天页面
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class MsgChatActivity extends BaseActivity {


    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.right_menu)
    TextView rightMenu;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_msg)
    EditText etMsg;
    @BindView(R.id.bt_send)
    Button btSend;

    /*
         * @ forever 在 17/5/17 下午2:28 创建
         *
         * 描述：跳转到当前页面
         *
         */
    public static void startIntent(Activity activity, Chat bean) {
        Intent it = new Intent(activity, MsgChatActivity.class);
        it.putExtra("chat", bean);
        activity.startActivity(it);
    }

    /*
    * @ forever 在 17/5/17 下午2:28 创建
    *
    * 描述：页面创建时调用
    *
    */
    Adapter myAdapter;
    Chat bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatinfo);
        ButterKnife.bind(this);

        bean = (Chat) getIntent().getSerializableExtra("chat");
        tvTitle.setText(bean.getTitle() + "(" + bean.getNum() + "人)");
        myAdapter = new Adapter(MsgChatActivity.this);
        listview.setAdapter(myAdapter);
        getData();
        //条目点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.show("点击了一下");
            }
        });
    }


    /**
     * 页面点击事件处理
     *
     * @param view
     */
    @OnClick({R.id.left_menu, R.id.right_menu, R.id.bt_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;

            case R.id.bt_send:
                String msg = etMsg.getText().toString().trim();
                if (TextUtils.isEmpty(msg)) {
                    ToastUtil.show("请输入内容");
                    return;
                }
                sendMsg(msg);
                break;
        }
    }

    public void sendMsg(String msg) {
        HttpRequest.sendMsg(this, bean.getId(), msg, String.valueOf(DateUtil.getMillis() / 1000), SPUtil.getString("name"), SPUtil.getString("photo"), SPUtil.getString("id"), SPUtil.getString("phone"), new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                etMsg.setText("");
                getData();
            }
        });
    }

    List<ChatMsg> list = new ArrayList<>();

    public void getData() {
        HttpRequest.getMsg(this, SPUtil.getString("phone"), bean.getId(), new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                list = JsonUtil.toList(json, ChatMsg.class);
                myAdapter.notifyDataSetChanged();
                listview.setSelection(ListView.FOCUS_DOWN);//刷新到底部
            }
        });
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
            if (SPUtil.getString("id").equals(list.get(position).getLeader_id())) {
                convertView = View.inflate(mContext, R.layout.item_chat_right, null);
                holder = new ViewHolder(1, convertView);
            } else {
                convertView = View.inflate(mContext, R.layout.item_chat_left, null);
                holder = new ViewHolder(2, convertView);
            }

            ChatMsg bean = list.get(position);
            GlideUtils.displayHome(holder.itemPhoto, BaseConfig.apiUrl + bean.getPhoto());
            holder.itemName.setText(bean.getName());
            holder.itemText.setText(bean.getText());
            holder.itemTime.setText(DateUtil.getStandardTime(Long.parseLong(bean.getTime())));

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

            ViewHolder(int index, View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}
