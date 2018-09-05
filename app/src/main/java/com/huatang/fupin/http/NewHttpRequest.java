package com.huatang.fupin.http;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.dou361.dialogui.DialogUIUtils;
import com.huatang.fupin.activity.DanganGanbuActivity;
import com.huatang.fupin.activity.FupinPolicyListActivity;
import com.huatang.fupin.activity.NewLoginActivity;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.bean.MessageBoard;
import com.huatang.fupin.bean.NewLeader;
import com.huatang.fupin.bean.NewPoor;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.MLog;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.SkinUtil;
import com.huatang.fupin.utils.StringUtil;
import com.huatang.fupin.utils.ToastUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.AbsCallback;
import com.lzy.okhttputils.model.HttpParams;

import java.io.File;
import java.util.List;


import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class NewHttpRequest {
    static HttpParams httpParams = getHttpParams();

    private static HttpParams getHttpParams() {
        if (httpParams == null) {
            httpParams = new HttpParams();
        }
        return httpParams;
    }
    private static void executePost(Activity context, String url, HttpParams params, NewHttpRequest.MyCallBack callback){
        DialogUIUtils.showTie(context, "加载中...");
        OkHttpUtils.post(BaseConfig.apiUrl+url)
                .headers(Config.TOKEN, SPUtil.getString(Config.TOKEN))
                .tag(context)
                .params(params)
                .execute(callback);
        params.clear();

    }
    private static void executePost(Activity context, String url, HttpParams params, NewHttpRequest.HtmlCallBack callback){
        DialogUIUtils.showTie(context, "加载中...");
        OkHttpUtils.post(BaseConfig.apiUrl+url)
                .headers(Config.TOKEN, SPUtil.getString(Config.TOKEN))
                .tag(context)
                .params(params)
                .execute(callback);
        params.clear();

    }
    private static void executePost(Activity context, String url, HttpParams params, NewHttpRequest.UploadCallBack callback){
        DialogUIUtils.showTie(context, "加载中...");
        OkHttpUtils.post(url)
                .headers(Config.TOKEN, SPUtil.getString(Config.TOKEN))
                .tag(context)
                .params(httpParams)
                .execute(callback);
        httpParams.clear();

    }




    public static abstract class MyCallBack extends AbsCallback<String> {
        public abstract void ok(String json) ;

        public abstract void no(String msg) ;

        private Context mcontext;
        public MyCallBack(){}
        public MyCallBack(Context context ){
            this.mcontext = context;
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
            String msg = JsonUtil.getString(s, "info");
            MLog.e("onResponse_" + code, s);
            if ("200".equals(code)) {
                ok(data);
            } else {
                if("30003".equals(code)){//token过期
                    SPUtil.logout();
                    msg = "身份已失效，请重新登录";
                    NewLoginActivity.startIntent((Activity) mcontext);
                }
                no(msg);
            }

        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            DialogUIUtils.dismssTie();
            MLog.e("onError", e == null ? response.message() : e.getMessage());
            if (e != null) {
                ToastUtil.show(e.getMessage());
                no(e.getMessage());
            }


        }
    }
    public static abstract class UploadCallBack extends AbsCallback<String> {
        public abstract void callback(String json);


        @Override
        public String parseNetworkResponse(Response response) throws Exception {
            return response.body().string();
        }

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            DialogUIUtils.dismssTie();
            MLog.e("onResponse_" +  s);
            callback(s);
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            DialogUIUtils.dismssTie();
            MLog.e("onError", e == null ? response.message() : e.getMessage());
            if (e != null) {
                ToastUtil.show(e.getMessage());
            }


        }
    }
    public static abstract class HtmlCallBack extends AbsCallback<String> {
        public abstract void callback(String html);


        @Override
        public String parseNetworkResponse(Response response) throws Exception {
            return response.body().string();
        }

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            DialogUIUtils.dismssTie();
            MLog.e("onResponse_" +  s);
            callback(s);
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            DialogUIUtils.dismssTie();
            MLog.e("onError", e == null ? response.message() : e.getMessage());
            if (e != null) {
                ToastUtil.show(e.getMessage());
            }


        }
    }
    /***
     * 3.0版本新接口
     *
     *
     *
     * ***/

    //登录接口
    public static void login(Activity context,String phone,String pwd,String type,NewHttpRequest.MyCallBack callback){
        httpParams = getHttpParams();
        httpParams.put("phone", phone);
        httpParams.put("password", pwd);
        httpParams.put("type",type);
        executePost(context,"login", httpParams, callback);
    }
    //主页中的轮播图和模块信息
    public static void getHome(Activity context,String type,NewHttpRequest.MyCallBack callback){
        httpParams = getHttpParams();
        httpParams.put("type",type);
        executePost(context,"getHome", httpParams, callback);

    }
    //获得极光消息
    public static void getSystemMsg(Activity context,NewHttpRequest.MyCallBack callback){
        httpParams = getHttpParams();
        httpParams.put("page","10");
        executePost(context,"msg/getSystemMsg", httpParams, callback);
    }
    //管理员发送极光消息
    public static void sendPushMsg(Activity context,String content,String title,String leader_id,String leader_name, NewHttpRequest.MyCallBack callback){

        httpParams = getHttpParams();
        httpParams.put("content",content);
        httpParams.put("title",title);
        httpParams.put("leader_name",leader_name);
        httpParams.put("leader_id",leader_id);
        executePost(context,"msg/sendjPushMsg", httpParams, callback);

    }
    //游客注册
    public static void registerToursits(Activity context,String phone,String password,String name, NewHttpRequest.MyCallBack callback){

        httpParams = getHttpParams();
        httpParams.put("phone",phone);
        httpParams.put("password",password);
        httpParams.put("name",name);
        executePost(context,"registerToursits", httpParams, callback);

    }
    // 获取群组消息列表
    public static void getChatMsgList(Activity context,String leader_phone, NewHttpRequest.MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("leader_phone",leader_phone);
        executePost(context,"msg/getGroupMsgList", httpParams, callback);
    }
    // 创建群组聊天群
    public static void createChatMsg(Activity context , String title, String content, NewLeader leader, String push_photo,String num, String accept_phones , MyCallBack callback ){
        httpParams = getHttpParams();
        httpParams.put("title",title);
        httpParams.put("content",content);
        httpParams.put("push_leader_id",leader.getId());
        httpParams.put("push_leader_name",leader.getLeader_name());
        httpParams.put("push_leader_phone",leader.getLeader_phone());
        httpParams.put("push_leader_photo",leader.getLeader_photo());
        httpParams.put("push_photo",push_photo);
        httpParams.put("leader_num",num);
        httpParams.put("accept_phones",accept_phones);
        executePost(context,"msg/createGroupMsg", httpParams, callback);

    }
    // 根据干部姓名 模糊查询干部信息
    public static void searchGanbuList(Activity context ,String Leader_name,MyCallBack callback){
        httpParams = getHttpParams();
        httpParams.put("leader_name",Leader_name);
        executePost(context,"leader/getLeaderInfoLikeLeaderName", httpParams, callback);

    }
    // 根据干部id 获取贫困户姓名 身份证等信
    public static void searchPoorList(Activity context ,String leader_id,MyCallBack callback){
        httpParams = getHttpParams();
        httpParams.put("leader_id",leader_id);
        executePost(context,"leader/getPoorWithLeaderID", httpParams, callback);
    }
    public static void   uploadImage(Activity context,String file,UploadCallBack callback){
        httpParams = getHttpParams();
        httpParams.put("file",new File(file));
        executePost(context,BaseConfig.uploadURl,httpParams,callback);

    }
    public static void deleteGroup(Activity context ,String leader_phone, String group_id, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("leader_phone",leader_phone);
        httpParams.put("group_id",group_id);
        executePost(context,"msg/groupMsgDelete", httpParams, callback);

    }
    public static void sendChatMsg(Activity context,String group_id,String title,String content,NewLeader leader,String push_photo,MyCallBack callback){

        httpParams = getHttpParams();
        httpParams.put("group_id",group_id);
        httpParams.put("title",title);
        httpParams.put("content",content);
        httpParams.put("push_leader_id",leader.getId());
        httpParams.put("push_leader_name",leader.getLeader_name());
        httpParams.put("push_leader_phone",leader.getLeader_phone());
        httpParams.put("push_leader_photo",leader.getLeader_photo());
        httpParams.put("push_photo",push_photo );


        executePost(context,"msg/replyGroupMsg", httpParams, callback);

    }
    public static void getChatMsg(Activity context,String group_id,String page,MyCallBack callback){
        httpParams = getHttpParams();
        httpParams.put("group_id",group_id);
        httpParams.put("page",page);
        executePost(context,"msg/getPdetailWithPage", httpParams, callback);
    }
    public static void getSignList(Activity context,String Leader_id,String page,MyCallBack callback){
        httpParams = getHttpParams();
        httpParams.put("Leader_id",Leader_id);
        httpParams.put("page",page);
        executePost(context,"leader/getLeaderSignList", httpParams, callback);
    }
    public static void addSign(Activity context, NewLeader leader, String title, String content, String signType, String signAddress, String signVillage,String signVillageId,String signTown,String signTownId,String imgs,  String longitude,String dimension,  NewPoor poor, NewHttpRequest.MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("leader_id", leader.getId());
        httpParams.put("basic_fname",poor.getFname());
        httpParams.put("basic_fcard",poor.getFcard());
        httpParams.put("leader_name",leader.getLeader_name());
        httpParams.put("leader_duty",leader.getLeader_duty());
        httpParams.put("work_units",leader.getLeader_unit());
        httpParams.put("unit_level",leader.getUnit_level());
        httpParams.put("leader_andscape",leader.getLeader_andscape());
        httpParams.put("sign_title",title);
        httpParams.put("sign_content",content);
        httpParams.put("sign_type",signType);
        httpParams.put("sign_address",signAddress);
        httpParams.put("longitude",longitude);
        httpParams.put("dimension",dimension);
        httpParams.put("sign_village",signVillage);
        httpParams.put("sign_town",signTown);
        httpParams.put("sign_town_id",signTownId);
        httpParams.put("sign_village_id",signVillageId);
        httpParams.put("leader_phone",leader.getLeader_phone());
        httpParams.put("leader_unit_id",leader.getLeader_unit_id());
        httpParams.put("sign_imgs",imgs);
        executePost(context, "leader/createLeaderSign",httpParams, callback);
    }
    public static void getVillageList(Activity context,String town,MyCallBack callback){
        httpParams = getHttpParams();
        httpParams.put("town", town);
        executePost(context, "village/getVillage",httpParams, callback);
    }
    public static void getArchivesWithLeader(Activity context,String leader_id,String year,MyCallBack callback){
        httpParams = getHttpParams();
        httpParams.put("leader_id", leader_id);
        httpParams.put("year",year);
        executePost(context, "archives/getArchivesWithLeader",httpParams, callback);

    }
    public static void getChatMember(Activity context ,String chat_id,MyCallBack callBack){
        httpParams =  getHttpParams();
        httpParams.put("id",chat_id);
        executePost(context,"chat/getChatMember",httpParams,callBack);
    }
    public static void getNewsWithType(Activity context ,String type,String page,MyCallBack callBack){
        httpParams =  getHttpParams();
        httpParams.put("type",type);
        httpParams.put("page",page);
        executePost(context,"news/getNewsWithType",httpParams,callBack);
    }
    public static void getNewsInfoWithId(Activity context,String id,HtmlCallBack callBack){
        httpParams =  getHttpParams();
        httpParams.put("id",id);
        executePost(context,"news/getNewsInfoWithid",httpParams,callBack);

    }
    //通过Poor查找贫困户的相关信息
    public static void getArchivesWithFcard(Activity context,String fcard,String year,MyCallBack callback){
        httpParams = getHttpParams();
        httpParams.put("fcard", fcard);
        httpParams.put("year",year);
        executePost(context, "archives/getArchivesWithPoorFcard",httpParams, callback);

    }
    //通过Poor查找帮扶干部信息
    public static void getLeaderByPoorFcard(Activity context, String fcard, MyCallBack callback) {
        httpParams = getHttpParams();
        httpParams.put("fcard", fcard);
        executePost(context, "archives/getLeaderByPoorFcard",httpParams, callback);
    }
    //增加聊天室中的leader
    public static void addChatMember(Activity context,String group_id,String leader_phone,MyCallBack callback){
        httpParams = getHttpParams();
        httpParams.put("leader_phone", leader_phone);
        httpParams.put("group_id", group_id);
        executePost(context, "msg/addChatMember",httpParams, callback);

    }
    public static void updatePwd(Activity context,String type,String phone,String pwd,String fcard,String leader_id,MyCallBack callBack){
        httpParams = getHttpParams();
        httpParams.put("phone", phone);
        httpParams.put("password", pwd);
        httpParams.put("type", type);
        httpParams.put("fcard", fcard);
        httpParams.put("id", leader_id);
        executePost(context, "updatePassword",httpParams, callBack);
    }
    public static void getchatById(Activity context,String chat_id,MyCallBack callBack){
        httpParams = getHttpParams();
        httpParams.put("chat_id", chat_id);
        executePost(context, "msg/getchatById",httpParams, callBack);
    }
    public static void editPoorPhotoWithFcard(Activity context,String fcard,String year,String type,String imgPath,MyCallBack callback){
        httpParams = getHttpParams();
        httpParams.put("fcard", fcard);
        httpParams.put("year", year);
        httpParams.put("type", type);
        httpParams.put("imgPath", imgPath);
        executePost(context, "archives/editPoorPhotoWithFcard",httpParams, callback);

    }
    public static void uploadUserPhoto(Activity context,String fcard ,String phone,String type,String imgPath,MyCallBack callBack){
        httpParams = getHttpParams();
        httpParams.put("fcard",fcard);
        httpParams.put("phone",phone);
        httpParams.put("type",type);
        httpParams.put("imgPath",imgPath);
        executePost(context,"archives/uploadUserPhoto",httpParams,callBack);


    }
    public static void createLeaveMsg(Activity activity,String title,String content,String imgs,NewPoor poor,MyCallBack callBack){
        httpParams = getHttpParams();
        httpParams.put("title",title);
        httpParams.put("content",content);
        httpParams.put("imgs",imgs);
        httpParams.put("name",poor.getFname());
        httpParams.put("fcard",poor.getFcard());
        httpParams.put("phone",poor.getFphone());
        httpParams.put("town",poor.getTown());
        httpParams.put("town_name",poor.getTown_name());
        httpParams.put("village",poor.getVillage());
        httpParams.put("village_name",poor.getVillage_name());
        executePost(activity,"leave/createLeaveMsg",httpParams,callBack);
    }
    public static void getLeaveMsgListWithTown(Activity activity,String town,String page,MyCallBack callBack){
        httpParams = getHttpParams();
        httpParams.put("town",town);
        httpParams.put("page",page);
        executePost(activity,"leave/getLeaveMsgListWithTown",httpParams,callBack);
    }
    public static void poorGetLeaveMsgList(Activity activity,String fcard,String page,MyCallBack callBack){
        httpParams = getHttpParams();
        httpParams.put("fcard",fcard);
        httpParams.put("page",page);
        executePost(activity,"leave/poorGetLeaveMsgList",httpParams,callBack);
    }
    public static void getSafeguradList(Activity activity, String type, String page, MyCallBack callBack) {
        httpParams =  getHttpParams();
        httpParams.put("type",type);
        httpParams.put("page",page);
        executePost(activity,"news/getSafeguradList",httpParams,callBack);
    }
    public static void getReplyMsgInfo(Activity activity,String group_id,MyCallBack callBack){
        httpParams = getHttpParams();
        httpParams.put("group_id",group_id);
        executePost(activity,"leave/poorGetReplyMsgInfo",httpParams,callBack);

    }
    public static void poorReplyMsg(Activity activity, String reply, MessageBoard meb,NewPoor poor ,MyCallBack callBack){
        httpParams =  getHttpParams();
        httpParams.put("reply_content",reply);
        httpParams.put("name",poor.getFname());
        httpParams.put("phone",poor.getFphone());
        httpParams.put("reply_content",reply);
        httpParams.put("town",meb.getTown());
        httpParams.put("town_name",meb.getTown_name());
        httpParams.put("village",meb.getVillage());
        httpParams.put("village_name",meb.getVillage_name());
        httpParams.put("is_leader","0");
        httpParams.put("photo",poor.getPhoto());
        httpParams.put("group_id",meb.getId());
        executePost(activity,"leave/poorReplyMsg",httpParams,callBack);
    }
    public static void leaderReplyMsg(Activity activity,String reply,MessageBoard meb ,NewLeader leader,MyCallBack callBack){
        httpParams =  getHttpParams();
        httpParams.put("name",leader.getLeader_name());
        httpParams.put("phone",leader.getLeader_phone());
        httpParams.put("reply_content",reply);
        httpParams.put("town",meb.getTown());
        httpParams.put("town_name",meb.getTown_name());
        httpParams.put("village",meb.getVillage());
        httpParams.put("village_name",meb.getVillage_name());
        httpParams.put("is_leader","1");
        httpParams.put("photo",leader.getLeader_photo());
        httpParams.put("group_id",meb.getId());
        executePost(activity,"leave/leaderReplyMsg",httpParams,callBack);
    }
    public static void  leaderEvaluation(Activity activity,String score,String content,NewPoor poor,NewLeader leader,MyCallBack callBack){
        httpParams =  getHttpParams();
        httpParams.put("poor_name",poor.getFname());
        httpParams.put("poor_fcard",poor.getFcard());
        httpParams.put("poor_phone",poor.getFphone());

        httpParams.put("score",score);
        httpParams.put("content",content);

        httpParams.put("leader_id",leader.getId());
        httpParams.put("leader_name",leader.getLeader_name());
        httpParams.put("leader_name",leader.getLeader_name());
        httpParams.put("leader_phone",leader.getLeader_phone());
        executePost(activity,"leader/leaderEvaluation",httpParams,callBack);
    }
    public static void isUpdateApp(Activity activity ,MyCallBack callBack){
        httpParams =  getHttpParams();
        httpParams.put("type","android");
        executePost(activity,"isUpdateApp",httpParams,callBack);
    }
}
