package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.bean.NewChat;
import com.huatang.fupin.bean.NewLeader;
import com.huatang.fupin.http.NewHttpRequest;
import com.huatang.fupin.utils.GlideUtils;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.SkinUtil;
import com.huatang.fupin.utils.ToastUtil;
import com.huatang.fupin.utils.ViewHolderUtil;

import java.util.ArrayList;
import java.util.List;

public class MsgChatListActivity extends BaseActivity implements View.OnClickListener {
    private ImageView leftMenu;
    private TextView rightMenu;
    private Adapter myAdapter;
    private List<NewChat> list ;
    private ListView listview;
    private TextView tv_empty;
    private NewLeader leader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        initView();
        initData();
    }
    private void initView(){
        leftMenu = (ImageView)findViewById(R.id.left_menu);
        rightMenu = (TextView)findViewById(R.id.right_tx_menu);
        leftMenu.setImageResource(SkinUtil.getResouceId(R.mipmap.icon_commen_break));
        leftMenu.setOnClickListener(this);
        rightMenu.setOnClickListener(this);
        leftMenu.setVisibility(View.VISIBLE);
        String type = SPUtil.getString(Config.Type);
        if(type.equals(Config.ADMIN_TYPE) || type.equals(Config.GANBU_TYPE)){
            rightMenu.setVisibility(View.VISIBLE);
          //  rightMenu.setOnClickListener(this);
        }
        tv_empty = (TextView)findViewById(R.id.tv_empty);
        list = new ArrayList<NewChat>();
        listview = (ListView)findViewById(R.id.listview);
        myAdapter = new Adapter(this);
        listview.setAdapter(myAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MsgChatActivity.startIntent(MsgChatListActivity.this,list.get(position));
            }
        });


    }

    private void initData() {
         leader = new NewLeader();
        if(SPUtil.getString(Config.Type).equals(Config.GANBU_TYPE)){
            leader= (NewLeader)SPUtil.getObject(Config.GANBU_KEY);
        }else if(SPUtil.getString(Config.Type).equals(Config.GANBU_TYPE)){
            leader = (NewLeader)SPUtil.getObject(Config.ADMIN_KEY);
        }

        NewHttpRequest.getChatMsgList(this, leader.leader_phone , new NewHttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {

                    list = JsonUtil.toList(json, NewChat.class);
                    if(list.size() > 0){
                        myAdapter.notifyDataSetChanged();
                        listview.setVisibility(View.VISIBLE);
                        tv_empty.setVisibility(View.GONE);
                    }else{
                        listview.setVisibility(View.GONE);
                        tv_empty.setVisibility(View.VISIBLE);
                    }
                }


            @Override
            public void no(String msg) {
                ToastUtil.show(msg);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_menu:
                finish();
                break;
            case R.id.right_tx_menu:
                MsgCreateChatActivity.startIntent(this);
                break;
        }
    }
    /*
     * @ forever 在 17/5/17 下午2:28 创建
     *
     * 描述：跳转到登录页面
     *
     */
    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, MsgChatListActivity.class);
        activity.startActivity(it);
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

            ImageView item_photo = ViewHolderUtil.get(convertView, R.id.item_leader_photo);
            TextView item_name = ViewHolderUtil.get(convertView, R.id.item_content);
            TextView item_title = ViewHolderUtil.get(convertView, R.id.item_title);
            TextView item_content = ViewHolderUtil.get(convertView,R.id.item_content);
            TextView item_number = ViewHolderUtil.get(convertView, R.id.item_number);
            TextView item_createTime = ViewHolderUtil.get(convertView,R.id.item_createTime);
            TextView deleteGroup = ViewHolderUtil.get(convertView,R.id.item_deleteGroup);
            final NewChat chat = list.get(position);
            item_name.setText(chat.getPush_leader_name());
            item_title.setText(chat.getTitle());
            item_content.setText(chat.getContent());
            item_number.setText(chat.getLeader_num()+"人");
            item_createTime.setText(chat.getCreate_time());
            deleteGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDialog(mContext,chat);

                }
            });
            String path = chat.getPush_photo();
                    //BaseConfig.ImageUrl + chat.getPush_leader_photo();

            GlideUtils.LoadCircleImageWithoutBorderColor(this.mContext,path,item_photo);
            return convertView;
        }
    }
    /**
     * 弹出退出登录对话框
     */
    public void deleteDialog(final Context mContext, final NewChat chat) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除对话");
        builder.setMessage("确定删除当前对话？");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                NewHttpRequest.deleteGroup( (Activity)mContext,leader.getLeader_phone(),chat.getId(),new NewHttpRequest.MyCallBack(){

                    @Override
                    public void ok(String json) {
                        ToastUtil.show("删除成功");
                        initData();
                    }
                    @Override
                    public void no(String msg) {
                        ToastUtil.show(msg);
                    }
                });

            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
