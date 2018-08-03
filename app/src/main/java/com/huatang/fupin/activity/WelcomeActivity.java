package com.huatang.fupin.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.TextView;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.utils.AndroidInfoUtils;
import com.huatang.fupin.utils.SPUtil;

import butterknife.ButterKnife;

/**
 * 欢迎页面
 * Created by forever on 2016/12/7.
 */

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        TextView tvVersion=findViewById(R.id.tv_version);
        tvVersion.setText("V"+ AndroidInfoUtils.versionName());

        /**
         * 界面延时俩秒，进行下一步操作
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String identity = SPUtil.getString("identity");
                if (!TextUtils.isEmpty(identity)) {
                    HomeActivity.startIntent(WelcomeActivity.this);
//                    LoginActivity.startIntent(WelcomeActivity.this);
                } else {
                    LoginActivity.startIntent(WelcomeActivity.this);
                }
                finish();
            }
        }, 3000);
    }



}