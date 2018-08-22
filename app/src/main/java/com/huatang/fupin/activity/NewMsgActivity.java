package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.SkinUtil;

import butterknife.ButterKnife;

public class NewMsgActivity extends BaseActivity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        initView();

    }

    private void initView() {
        initHeadView();
        RelativeLayout message_push_layout = (RelativeLayout)findViewById(R.id.message_push_layout);
        RelativeLayout message_chat_layout = (RelativeLayout)findViewById(R.id.message_chat_layout);
        RelativeLayout message_douban_layout = (RelativeLayout)findViewById(R.id.message_doudao_layout);
        ImageView message_push_img = (ImageView) findViewById(R.id.message_push_img);
        ImageView message_chat_img = (ImageView)findViewById(R.id.message_chat_img);
        ImageView message_doudao_img = (ImageView)findViewById(R.id.message_doudao_img);

        message_chat_layout.setOnClickListener(this);
        message_douban_layout.setOnClickListener(this);
        message_push_layout.setOnClickListener(this);
        message_chat_img.setBackgroundResource(SkinUtil.getResouceId(R.mipmap.img_xiaoxi));
        message_doudao_img.setBackgroundResource(SkinUtil.getResouceId(R.mipmap.img_xiaoxi_red));
        message_push_img.setBackgroundResource(SkinUtil.getResouceId(R.mipmap.img_xiaoxi));
        String type = SPUtil.getString(Config.Type);
        if(type.equals(Config.ADMIN_TYPE) || type.equals(Config.GANBU_TYPE)){
            message_chat_layout.setVisibility(View.VISIBLE);
        }else{
            message_chat_layout.setVisibility(View.GONE);
        }
    }

    private void initHeadView() {
        ((TextView)findViewById(R.id.title_tx)).setText(Config.xiaoxitongzhi);
        ImageView left_menu = (ImageView)findViewById(R.id.left_menu);
        left_menu.setImageResource(SkinUtil.getResouceId(R.mipmap.icon_commen_break));
        left_menu.setOnClickListener(this);
        findViewById(R.id.right_menu).setVisibility(View.INVISIBLE);
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
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.message_push_layout:
                MsgPushActivity.startIntent(this);
                break;
            case R.id.message_chat_layout:
                //MsgChatActivity.startIntent(this);
                MsgChatListActivity.startIntent(this);
                break;
            case R.id.message_doudao_layout:
                MsgDouDaoActivity.startIntent(this);
                break;
            case R.id.left_menu:
                this.finish();
                break;
        }

    }

    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, NewMsgActivity.class);
        activity.startActivity(it);
    }
}
