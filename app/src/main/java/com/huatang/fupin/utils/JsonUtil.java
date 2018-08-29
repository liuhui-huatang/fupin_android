package com.huatang.fupin.utils;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by author_dang on 16/8/15.
 */
public class JsonUtil {

    public static <T> T json2Bean(String result, Class<T> clz) {

        return JSON.parseObject(result, clz);
    }


    /**
     * 取出json中某个字段的值
     *
     * @param json  数据串
     * @param which 字段名称
     * @return String 类型的值
     * @throws JSONException 异常返回“”
     */
    public static String getString(String json, String which) {
        String result = "";
        if (json != null) {
            try {
                result = new JSONObject(json).getString(which);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static int getInt(String json, String which) {
        int result = 0;
        if (json != null) {
            try {
                String s = new JSONObject(json).getString(which);
                if (!TextUtils.isEmpty(s)) {
                    result = Integer.parseInt(s);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 说明：将json数组转list
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends Object> T toList(String json, Class<?> clazz) {
        List<T> list = (List<T>) JSON.parseArray(json,clazz);
        return (T) list;
    }
    public static JSONObject arrayToJsonObject(String json){
        JSONObject jsonObject = null ;
        try {
            JSONArray  jsonArray = new JSONArray(json);
            jsonObject = jsonArray.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public static String getStringFromArray(String json,String key){
        String result = "";
        JSONObject jsonObject = arrayToJsonObject(json);
        try {
            result = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;

    }

}
