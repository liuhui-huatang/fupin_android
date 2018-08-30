package com.huatang.fupin.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Base64;

import com.huatang.fupin.app.Config;
import com.huatang.fupin.app.MyApplication;
import com.huatang.fupin.bean.NewLeader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * Created by author_dang on 16/8/15.
 */
public class SPUtil {

    private final static String SP_NAME = "ml_config";
    private static SharedPreferences sharedPreferences = null;
    private static boolean shouldCommit = Integer.parseInt(Build.VERSION.SDK) < 9;


    private static SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null) {
            synchronized (SP_NAME) {
                if (sharedPreferences == null) {
                    sharedPreferences =
                            MyApplication.getContext().getSharedPreferences(SP_NAME,
                                    Context.MODE_PRIVATE);
                }
            }
        }
        return sharedPreferences;
    }

    private static void save(SharedPreferences.Editor editor) {

        if (shouldCommit) {
            editor.commit();
        } else {
            editor.apply();
        }


    }

    public static void saveBoolean(String key, boolean value) {
        save(getSharedPreferences().edit().putBoolean(key, value));
    }

    public static void removeValue(String key) {
        save(getSharedPreferences().edit().remove(key));
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getSharedPreferences().getBoolean(key, defValue);
    }

    public static void saveString(String key, String value) {
        save(getSharedPreferences().edit().putString(key, value));
    }

    public static void saveInt(String key, int value) {
        save(getSharedPreferences().edit().putInt(key, value));
    }

    public static int getInt(String key, int defValue) {
        return getSharedPreferences().getInt(key, defValue);
    }

    public static String getString(String key) {
        return getSharedPreferences().getString(key, "");
    }

    public static void clear() {
        getSharedPreferences().edit().clear();
    }


    /**
     * @param object
     */
    public static void saveObject(String key,Object object) throws Exception {
        if(object instanceof Serializable) {
            SharedPreferences sharedPreferences = getSharedPreferences();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(object);//把对象写到流里
                String temp = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
                oos.close();
                baos.close();

                editor.putString(key, temp);
                editor.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            throw new Exception("User must implements Serializable");
        }
    }

    public static Object getObject(String key ) {
        SharedPreferences sharedPreferences=getSharedPreferences();
        String temp = sharedPreferences.getString(key, "");
        ByteArrayInputStream bais =  new ByteArrayInputStream(Base64.decode(temp.getBytes(), Base64.DEFAULT));
        Object object = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            object = (Object) ois.readObject();
        } catch (IOException e) {
        }catch(ClassNotFoundException e1) {

        }
        return object;
    }
    public static NewLeader getLeaderFromSharePref(){
        NewLeader leader = new NewLeader();
        if(SPUtil.getString(Config.Type).equals(Config.GANBU_TYPE)){
            leader= (NewLeader)SPUtil.getObject(Config.GANBU_KEY);
        }else if(SPUtil.getString(Config.Type).equals(Config.GANBU_TYPE)){
            leader = (NewLeader)SPUtil.getObject(Config.ADMIN_KEY);
        }
        return  leader;
    }
    public static void logout(){
        SPUtil.removeValue(Config.TOKEN);
        SPUtil.removeValue(Config.Type);
        SPUtil.removeValue(Config.PHONE);
        SPUtil.removeValue(Config.NAME);
        SPUtil.removeValue(Config.YEAR);
        SPUtil.removeValue(Config.PASSWORD);
        SPUtil.removeValue(Config.HEAD_PHOTO);

    }

}
