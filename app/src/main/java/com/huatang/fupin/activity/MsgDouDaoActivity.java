package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;

import com.huatang.fupin.app.BaseActivity;

class MsgDouDaoActivity extends BaseActivity{
    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, MsgDouDaoActivity.class);
        activity.startActivity(it);
    }
}
