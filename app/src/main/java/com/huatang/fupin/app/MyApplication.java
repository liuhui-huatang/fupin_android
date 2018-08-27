package com.huatang.fupin.app;


import android.app.Application;
import android.content.Context;

import com.huatang.fupin.R;
import com.huatang.fupin.galleryfinal.UILImageLoader;
import com.huatang.fupin.galleryfinal.UILPauseOnScrollListener;

import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.SkinUtil;
import com.huatang.fupin.utils.SystemLogHelper;
import com.lzy.okhttputils.OkHttpUtils;
import com.mob.MobSDK;
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
        initTheme(this);


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
        MobSDK.init(this);

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
    public static void initTheme(MyApplication myApplication) {
        switch (SPUtil.getString(SkinUtil.CURSKINTYPEKEY)){
            case SkinUtil.skin_type_blue:
                myApplication.setTheme(R.style.AppThemeBlue);
                break;
            case SkinUtil.skin_type_default:
                //myApplication.setTheme(R.style.AppThemeTransparent);
                break;

        }


    }

}
