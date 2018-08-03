package com.huatang.fupin.app;


import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.example.library.application.BaseKandyApplication;
import com.example.library.control.TxtKandy;
import com.example.library.service.KandyCallNotifationService;
import com.example.library.util.LogUtil;
import com.huatang.fupin.galleryfinal.UILImageLoader;
import com.huatang.fupin.galleryfinal.UILPauseOnScrollListener;
import com.huatang.fupin.utils.SystemLogHelper;
import com.lzy.okhttputils.OkHttpUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by forever on 2017/1/9.
 */
public class MyApplication extends Application {

    static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        OkHttpUtils.init(this);

        //极光推送初始化
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush

        //建议在application中配置
        //设置主题
//        ThemeConfig theme = ThemeConfig.CYAN;
        ThemeConfig theme = new ThemeConfig.Builder()
                .build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .build();

        CoreConfig coreConfig = new CoreConfig.Builder(this, new UILImageLoader(), theme)
                .setDebug(cn.finalteam.galleryfinal.BuildConfig.DEBUG)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(new UILPauseOnScrollListener(false, true))
                .build();
        GalleryFinal.init(coreConfig);

        initImageLoader(this);

        SystemLogHelper.getmInstance().init(this);
        SystemLogHelper.getmInstance().start();

        initKandy();
    }

    public void initKandy() {
        try {
            LogUtil.d("BaseKandyApplication", "onCreate:BaseKandyApplication ");
            //初始化kandy相关内容
            TxtKandy.getKandyCall().initKandy(this, null, null, null);
            TxtKandy.getMpvCall();
            TxtKandy.getAccessKandy();
            TxtKandy.getDataMpvConnnect().init(this);
            Intent intent = new Intent(this, KandyCallNotifationService.class);
            startService(intent);
        } catch (Exception e) {
            LogUtil.d("BaseKandyApplication--Exception", e.getMessage());
        }

    }

    private void initImageLoader(Context context) {

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }


}
