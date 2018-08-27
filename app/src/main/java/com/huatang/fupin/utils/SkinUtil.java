package com.huatang.fupin.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import com.huatang.fupin.R;
import com.huatang.fupin.app.MyApplication;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.toolsfinal.StringUtils;


public class SkinUtil {
    public static final String SKIN_PATTERN ="skin";
    public static final String CURSKINTYPEKEY = "curSkinTypeKey";
    public static final String skin_type_default ="default";
    public static final String skin_type_blue ="blue";
    public static final String skin_type_red = "red";


    public static String getCurSkinType() {
       String currentSkinType = SPUtil.getString(CURSKINTYPEKEY);
       currentSkinType =  currentSkinType.isEmpty()|| currentSkinType.equals("") ? skin_type_default : currentSkinType;
       return currentSkinType;
    }
    public static void setCurSkinType(String skinType){
        SPUtil.saveString(CURSKINTYPEKEY,skinType);
    }

    public static int  getResouceId(int resouceId){
        String currentSkin = getCurSkinType();
        int skinResouceId = resouceId;
        String resTypeName = MyApplication.getContext().getResources().getResourceTypeName(resouceId);
        String resEntryName = MyApplication.getContext().getResources().getResourceEntryName(resouceId);
        String newResouceName = resEntryName;

        switch (currentSkin){
            case skin_type_default:
                if(resEntryName.endsWith("_skin")){
                    newResouceName = resEntryName.split("_skin")[0];
                }
                break;
            case skin_type_blue:
                if(resEntryName.contains("_skin")){
                    newResouceName = resEntryName.replace(resEntryName.split("_skin")[1],"_"+currentSkin);
                }else{
                    newResouceName = resEntryName +"_skin_"+currentSkin;
                }

                break;
            case skin_type_red:
                if(resEntryName.contains("_skin")){
                    newResouceName = resEntryName.replace(resEntryName.split("_skin")[1],"_"+currentSkin);
                }else{
                    newResouceName = resEntryName +"_skin_"+currentSkin;
                }
                break;


        }
        skinResouceId = MyApplication.getContext().getResources().getIdentifier(newResouceName, resTypeName, MyApplication.getContext().getPackageName());
        skinResouceId = skinResouceId == 0 ? resouceId : skinResouceId;
        return skinResouceId;
    }



}