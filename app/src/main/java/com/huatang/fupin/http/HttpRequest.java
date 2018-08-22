package com.huatang.fupin.http;

import android.app.Activity;
import android.support.annotation.Nullable;


import com.dou361.dialogui.DialogUIUtils;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.bean.Basic;
import com.huatang.fupin.utils.DateUtil;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.MLog;
import com.huatang.fupin.utils.Md5Util;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.ToastUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.AbsCallback;
import com.lzy.okhttputils.model.HttpParams;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


/**
 * http请求封装类
 * Created by forever on 2016/12/8.
 */
public class HttpRequest {

    static HttpParams httpParams = getHttpParams();

    private static HttpParams getHttpParams() {
        if (httpParams == null) {
            httpParams = new HttpParams();
        }
        return httpParams;
    }

    private static void executePost(Activity context, HttpParams params, MyCallBack callback) {
        DialogUIUtils.showTie(context, "加载中...");
        params.put("token", BaseConfig.token);
        OkHttpUtils.post(BaseConfig.apiUrl)
                .tag(context)
                .params(params)
                .execute(callback);
//        MLog.e("HttpRequest", BaseConfig.apiUrl + "\n" + params.toString());
        params.clear();
    }
    private static void executePost(Activity context,String url, HttpParams params, MyCallBack callback){
        DialogUIUtils.showTie(context, "加载中...");
        OkHttpUtils.post(BaseConfig.apiUrl+url)
                .tag(context)
                .params(params)
                .execute(callback);
//        MLog.e("HttpRequest", BaseConfig.apiUrl + "\n" + params.toString());
        params.clear();

    }


    public static abstract class MyCallBack extends AbsCallback<String> {
        public abstract void ok(String json);

        public void no(String msg) {
        }

        @Override
        public String parseNetworkResponse(Response response) throws Exception {
            return response.body().string();
        }

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            DialogUIUtils.dismssTie();
            String code = JsonUtil.getString(s, "code");
            String data = JsonUtil.getString(s, "data");
            String msg = JsonUtil.getString(s, "msg");
            MLog.e("onResponse_" + code, s);
            if ("200".equals(code)) {
                ok(data);
            } else {
                ToastUtil.show(msg);
                no(msg);
            }

        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            DialogUIUtils.dismssTie();
            MLog.e("onError", e == null ? "null" : e.getMessage());
            if (e != null) {
                ToastUtil.show(e.getMessage());
                no(e.getMessage());
            }


        }
    }


    //************************************************接口部分**************************************
    // 登录
    public static void login(Activity context, String phone, String pwd, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "login");
        httpParams.put("phone", phone);
        httpParams.put("password", pwd);
        executePost(context, httpParams, callback);
    }

    // 登录
    public static void loginToggle(Activity context, String phone, String year, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "loginToggle");
        httpParams.put("phone", phone);
        httpParams.put("year", year);
        executePost(context, httpParams, callback);
    }




    // 修改密码
    public static void updatePwd(Activity context, String phone, String pwd, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "updatePwd");
        httpParams.put("phone", phone);
        httpParams.put("pwd", pwd);
        executePost(context, httpParams, callback);
    }

    // 首页数据
    public static void getHome(Activity context, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "getHome");
        executePost(context, httpParams, callback);
    }

    // 获取帮扶日志
    public static void getSign(Activity context, String leader_id, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "getSign");
        httpParams.put("leader_id", leader_id);
        executePost(context, httpParams, callback);
    }

    // 更正签到位置
    public static void updateSignLocation(Activity context, String sign_id, String town_id, String town_name, String vallage_id, String vallage_name, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "updateSignLocation");
        httpParams.put("sign_id", sign_id);
        httpParams.put("town_id", town_id);
        httpParams.put("town_name", town_name);
        httpParams.put("vallage_id", vallage_id);
        httpParams.put("vallage_name", vallage_name);
        executePost(context, httpParams, callback);
    }


    // 添加帮扶日志
//    unit_level 干部单位级别
//    leader_andscape 干部政治面貌
//    sign_town 乡镇 (固日班花苏木)
//    sign_village 村(贫困户村)
//    sign_town_id 镇id
//    sign_village_id 村id
//    year 贫困户建档年度
    public static void addSign(Activity context, String leader_id, String title, String text, String time, String location, String imgs, String latitude, String longitude, String basic_name, String basic_id, String basic_card, String village, String village_id, String year, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "addSign");
        httpParams.put("leader_id", leader_id);
        httpParams.put("leader_name", SPUtil.getString("name"));
        httpParams.put("leader_phone", SPUtil.getString("phone"));
        httpParams.put("leader_duty", SPUtil.getString("duty"));
        httpParams.put("work_units", SPUtil.getString("unit"));

        httpParams.put("unit_level", SPUtil.getString("unit_level"));
        httpParams.put("leader_andscape", SPUtil.getString("leader_andscape"));
        httpParams.put("sign_town", SPUtil.getString("town"));
        httpParams.put("sign_town_id", SPUtil.getString("town_id"));
        httpParams.put("sign_village", village);
        httpParams.put("sign_village_id", village_id);
        httpParams.put("year", year);

        httpParams.put("title", title);
        httpParams.put("text", text);
        httpParams.put("time", time);
        httpParams.put("location", location);
        httpParams.put("imgs", imgs);
        httpParams.put("latitude", latitude);
        httpParams.put("longitude", longitude);

        httpParams.put("basic_fname", basic_name);
        httpParams.put("basic_id", basic_id);
        httpParams.put("basic_fcard", basic_card);
        executePost(context, httpParams, callback);
    }

    //上传图片
    public static void upLoadImg(Activity context, String imgPath, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "upLoadImg");
        httpParams.put("image", new File(imgPath));
        executePost(context, httpParams, callback);
    }


    // 获取贫困户列表
    public static void getBasic(Activity context, String leader_id, String year, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "getBasic");
        httpParams.put("leader_id", leader_id);
        httpParams.put("year", year);
        executePost(context, httpParams, callback);
    }

    // 获取所有年度贫困户列表
    public static void getBasicAll(Activity context, String leader_phone, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "getBasicAll");
        httpParams.put("leader_phone", leader_phone);
        executePost(context, httpParams, callback);
    }

    // 获取贫困村
    public static void getArea(Activity context, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "getArea");
        executePost(context, httpParams, callback);
    }

    // 根据镇获取贫困村
    public static void getAreaById(Activity context, String pid, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "getAreaById");
        httpParams.put("pid", pid);
        executePost(context, httpParams, callback);
    }


    //获取贫困户基本信息
    public static void getBasicInfo(Activity context, String basic_id, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "getSign");
        httpParams.put("basic_id", basic_id);
        executePost(context, httpParams, callback);
    }

    //获取贫困户家庭成员
    public static void getBasicFamily(Activity context, String basic_id, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "getBasicFamily");
        httpParams.put("basic_id", basic_id);
        executePost(context, httpParams, callback);
    }

    //获取贫困户帮扶干部
    public static void getBasicLeader(Activity context, String basic_id, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "getBasicLeader");
        httpParams.put("basic_id", basic_id);
        executePost(context, httpParams, callback);
    }

    //获取贫困户脱贫方案
    public static void getBasicPlan(Activity context, String basic_id, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "getBasicPlan");
        httpParams.put("basic_id", basic_id);
        executePost(context, httpParams, callback);
    }

    //获取贫困户帮扶措施
    public static void getBasicMeasure(Activity context, String basic_id, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "getBasicMeasure");
        httpParams.put("basic_id", basic_id);
        executePost(context, httpParams, callback);
    }

    //获取贫困户收入情况
    public static void getBasicRevenue(Activity context, String basic_id, String year, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "getBasicRevenue");
        httpParams.put("basic_id", basic_id);
        httpParams.put("year", year);
        executePost(context, httpParams, callback);
    }


    //修改头像
    public static void updatePhoto(Activity context, String identity, String id, String imgPath, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "updatePhoto");
        httpParams.put("identity", identity);
        httpParams.put("id", id);
        httpParams.put("imgFile", new File(imgPath));
        executePost(context, httpParams, callback);
    }


    //获取各种类型的新闻
    public static void getCloumn(Activity context, String type, int load, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "getCloumn");
        httpParams.put("type", type);
        httpParams.put("load", load + "");
        executePost(context, httpParams, callback);
    }

    //获取系统消息
    public static void getSystemMsg(Activity context, String phone, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "getSystemMsg");
        httpParams.put("phone", phone);
        executePost(context, httpParams, callback);
    }


    //创建群聊
    public static void createGroup(Activity context, String title, String msg, String leader_id, String num, String time, String name, String photo, String phone, String ids, String phones, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "createGroup");
        httpParams.put("title", title);
        httpParams.put("msg", msg);
        httpParams.put("leader_id", leader_id);
        httpParams.put("num", num);
        httpParams.put("time", time);
        httpParams.put("name", name);
        httpParams.put("photo", photo);
        httpParams.put("phone", phone);
        httpParams.put("ids", ids);
        httpParams.put("phones", phones);
        executePost(context, httpParams, callback);
    }

    //获取群组
    public static void getGroup(Activity context, String phone, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "getGroup");
        httpParams.put("phone", phone);
        executePost(context, httpParams, callback);

    }

    //获取群组消息
    public static void getMsg(Activity context, String phone, String chat_id, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "getMsg");
        httpParams.put("phone", phone);
        httpParams.put("chat_id", chat_id);
        executePost(context, httpParams, callback);
    }

    //发送消息
    public static void sendMsg(Activity context, String chat_id, String text, String time, String name, String photo, String leader_id, String phone, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "sendMsg");
        httpParams.put("chat_id", chat_id);
        httpParams.put("text", text);
        httpParams.put("time", time);
        httpParams.put("name", name);
        httpParams.put("photo", photo);
        httpParams.put("leader_id", leader_id);
        httpParams.put("phone", phone);
        executePost(context, httpParams, callback);
    }

    //获取贫困户
    public static void getBasics(Activity context, String str, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "getBasics");
        httpParams.put("str", str);
        executePost(context, httpParams, callback);
    }

    //获取帮扶干部
    public static void getLeaders(Activity context, String str, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "getLeaders");
        httpParams.put("str", str);
        executePost(context, httpParams, callback);
    }

    //上传扶贫附件
    public static void uploadFupinImg(Activity context, String basic_id, String imgPath, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "uploadFupinImg");
        httpParams.put("basic_id", basic_id);
        httpParams.put("imgPath", imgPath);
        executePost(context, httpParams, callback);
    }

    //获取banner信息
    public static void getBanner(Activity context, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "getBanner");
        executePost(context, httpParams, callback);
    }

    //删除致贫原因图片
    public static void delMainPathImg(Activity context, String basic_id, String imgPath, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "delMainPathImg");
        httpParams.put("basic_id", basic_id);
        httpParams.put("imgPath", imgPath);
        executePost(context, httpParams, callback);
    }

    //删除致贫原因图片
    public static void getMainPathImg(Activity context, String basic_id, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("php", "getMainPathImg");
        httpParams.put("basic_id", basic_id);
        executePost(context, httpParams, callback);
    }




}
