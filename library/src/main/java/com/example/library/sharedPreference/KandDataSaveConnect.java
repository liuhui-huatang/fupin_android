package com.example.library.sharedPreference;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.library.control.TxtKandy;
import com.example.library.model.ConferenceRoom;
import com.example.library.util.LogUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2017/7/26.
 */

public class KandDataSaveConnect {
    private static String TAG=KandDataSaveConnect.class.getSimpleName();
    private SharedPreferences sp;
    private List<ConferenceRoom> mDatas;
    private SharedPreferences.Editor editor;
    private Context mContext;
    private static final String DATA="data";

    private final static String USER="user";
    private final static String PASSWARD="passward";
    public KandDataSaveConnect(){
    }
    public void init(Context context){
        mContext=context;
        sp=context.getSharedPreferences("mpvdata",Context.MODE_PRIVATE);
        editor=sp.edit();
        mDatas=new Gson().fromJson(sp.getString(DATA,""),new TypeToken<List<ConferenceRoom>>(){}.getType());
        LogUtil.d(TAG, "init: mDatas"+mDatas);
    }

     public  List<ConferenceRoom> getRoomData(){
      return mDatas;
     }
     public void clearData(){
         if (mDatas!=null)
                mDatas.clear();
         editor.clear();
         editor.commit();
         TxtKandy.getAccessKandy().clearData();
     }
     public void addRoom(ConferenceRoom room){
         if (mDatas==null){
             mDatas=new ArrayList<>();
         }
         LogUtil.d(TAG, "addRoom: addRoom");
         mDatas.add(room);
         String json=new Gson().toJson(mDatas);
         LogUtil.d(TAG, "addRoom: addRoom json"+json);
         editor.putString(DATA,json);
         editor.commit();
     }

     public void saveKandyUser(String user){
         editor.putString(USER,user);
         editor.commit();
     }

    public void saveKandyPassward(String passward){
        editor.putString(PASSWARD,passward);
        editor.commit();
    }

    public String getCurrentLoginUser(){
           return sp.getString(USER,"");
    }
    public String getCurrentLoginUserPassward(){
        return sp.getString(USER,"");
    }
}
