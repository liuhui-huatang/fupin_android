package com.example.library.control;

import android.util.Log;
import com.example.library.login.AccessKandy;
import com.example.library.sharedPreference.KandDataSaveConnect;
import com.example.library.call.KandyCall;
import com.example.library.media.MediaPlayControl;
import com.example.library.mpv.TxtMpvCallManmger;
import com.example.library.util.LogUtil;

/**
 * Created by DELL on 2017/7/19.
 */

public class TxtKandy {
    private final static String TAG=TxtKandy.class.getSimpleName();
    public static AccessKandy mAccessKandy;
    public static KandyCall mKandyCall;
    public static TxtMpvCallManmger mpvCallManmger;

    private static KandDataSaveConnect mDataMpv;

    public static ConnectCall mConnect;
    public static MediaPlayControl mMediaControl;

    //关闭log日志打印
    public static final boolean ISSHOWLOGOUT=true;

    //获取登录模块的实例
    public static AccessKandy getAccessKandy(){

        if (mAccessKandy==null)
        {
            mAccessKandy=new AccessKandy();
        }
        return mAccessKandy;
    }
    //获取kandy Call功能模块的实例
    public static KandyCall getKandyCall(){
        if (mKandyCall==null){
            mKandyCall=new KandyCall();
        }
        return mKandyCall;
    }
    //获取mpv会议功能模块实例
    public static TxtMpvCallManmger getMpvCall(){
        if (mpvCallManmger==null){
            mpvCallManmger=new TxtMpvCallManmger();
        }
        return mpvCallManmger;
    }
    //控制页面跳转实例
    public static ConnectCall getConnnectCall(){
        if (mConnect==null){
            mConnect=new ConnectCall();
        }
        return mConnect;
    }
    //存储mpv历史会话消息
    public static KandDataSaveConnect getDataMpvConnnect(){
        LogUtil.d(TAG, "getDataMpvConnnect: ");
        if (mDataMpv==null){
            mDataMpv=new KandDataSaveConnect();
        }
        return mDataMpv;
    }

    //获取来电，去电声音播放实例
    public static MediaPlayControl getMediaConnnect(){
        LogUtil.d(TAG, "getDataMpvConnnect: ");
        if (mMediaControl==null){
            mMediaControl=new MediaPlayControl();
        }
        return mMediaControl;
    }
}
