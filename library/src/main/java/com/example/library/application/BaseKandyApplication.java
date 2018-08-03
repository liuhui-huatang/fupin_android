package com.example.library.application;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.example.library.control.TxtKandy;
import com.example.library.service.KandyCallNotifationService;
import com.example.library.util.LogUtil;

/**
 * Created by DELL on 2017/7/19.
 * 基础application
 */

public class BaseKandyApplication extends Application {
    protected String KEY="";
    protected String SCRET="";
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("BaseKandyApplication", "onCreate:BaseKandyApplication ");
        //初始化kandy相关内容
        TxtKandy.getKandyCall().initKandy(this,null,null,null);
        TxtKandy.getMpvCall();
        TxtKandy.getAccessKandy();
        TxtKandy.getDataMpvConnnect().init(this);
        Intent intent=new Intent(this, KandyCallNotifationService.class);
        startService(intent);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
