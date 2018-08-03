package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.bean.Chat;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.utils.GlideUtils;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.MLog;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.ViewHolderUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消息中心页面
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class MsgActivity extends BaseActivity {


    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.right_menu)
    TextView rightMenu;
    @BindView(R.id.listview)
    ListView listview;

    /*
         * @ forever 在 17/5/17 下午2:28 创建
         *
         * 描述：跳转到登录页面
         *
         */
    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, MsgActivity.class);
        activity.startActivity(it);
    }

    /*
    * @ forever 在 17/5/17 下午2:28 创建
    *
    * 描述：页面创建时调用
    *
    */
    Adapter myAdapter;
    TextView tv_dian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        ButterKnife.bind(this);

        myAdapter = new Adapter(MsgActivity.this);
        listview.setAdapter(myAdapter);
        View pushItem = View.inflate(this, R.layout.item_msg, null);
        tv_dian = pushItem.findViewById(R.id.tv_dian);
        listview.addHeaderView(pushItem);
        //条目点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    MsgPushActivity.startIntent(MsgActivity.this);
                } else {
                    MsgChatActivity.startIntent(MsgActivity.this, list.get(position - 1));
                }
            }
        });

        //贫困户没有发布消息权限
        MLog.e("identity=="+SPUtil.getString("identity"));
        if ("2".equals(SPUtil.getString("identity"))) {
            rightMenu.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (SPUtil.getInt("push", 0) > 0) {
            tv_dian.setVisibility(View.VISIBLE);
        } else {
            tv_dian.setVisibility(View.GONE);
        }
        getData();
    }

    List<Chat> list = new ArrayList<>();

    public void getData() {
        HttpRequest.getGroup(this, SPUtil.getString("phone"), new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                list = JsonUtil.toList(json, Chat.class);
                myAdapter.notifyDataSetChanged();
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
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_chat, null);
            }

            ImageView item_photo = ViewHolderUtil.get(convertView, R.id.item_photo);
            TextView item_name = ViewHolderUtil.get(convertView, R.id.item_name);
            TextView item_number = ViewHolderUtil.get(convertView, R.id.item_number);
            Chat bean = list.get(position);
            GlideUtils.displayHome(item_photo, BaseConfig.apiUrl + bean.getPhoto());
            item_name.setText(bean.getTitle() + "（" + bean.getNum() + "人）");
            item_number.setText("【发布人：" + bean.getCreate_name() + "" + "】");
            return convertView;
        }
    }

    @OnClick({R.id.left_menu, R.id.right_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.right_menu:
                MsgSendActivity.startIntent(this);
                break;
        }
    }
}
