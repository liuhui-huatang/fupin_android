package com.huatang.fupin.app;

import android.content.Context;
import android.content.SharedPreferences;

public class SkinConfigManager {

    private static SkinConfigManager mSkinConfigManager;
    public static final String SKINCONFIG = "SkinConfig";
    public static final String CURSKINTYPEKEY = "curSkinTypeKey";

    private static SharedPreferences mSharedPreferences;

    private SkinConfigManager(Context context){
        mSharedPreferences = context.getSharedPreferences(SKINCONFIG, 0);
    }

    public synchronized static SkinConfigManager getInstance(Context context) {
        if (mSkinConfigManager == null) {
            mSkinConfigManager = new SkinConfigManager(context);
        }
        return mSkinConfigManager;
    }


    /**
     * 设置储存当前选择的皮肤类型值(int 类型值)到 SharedPreferences
     * @param skinType  皮肤类型
     */
    public void setCurSkinType(int skinType){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(CURSKINTYPEKEY, skinType);
        editor.commit();
    }


    /**
     * 获得当前储存在SharedPreferences中的 当前皮肤类型<CURSKINTYPE> 值
     * @return
     */
    public int getCurSkinType(){
        if (mSharedPreferences != null) {
            return mSharedPreferences.getInt(CURSKINTYPEKEY, 0);
        }
        return 0;
    }


    /**
     * 获得assets文件夹下面当前皮肤资源所对应的皮肤文件夹名
     * @return
     */
    public String getSkin () {
        String skin = null;
        switch (getCurSkinType()){
            case 0:
                // 默认的皮肤类型
                break;
            case 1:
                skin = "blue";
                break;
            case 2:
                skin = "orange";
                break;
            case 3:
                skin = "red";
                break;
        }
        return skin;
    }


    public SharedPreferences getSkinConfigPreferences () {
        return mSharedPreferences;
    }
}
