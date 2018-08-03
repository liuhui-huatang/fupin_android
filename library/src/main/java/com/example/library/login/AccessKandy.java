package com.example.library.login;

import android.net.Uri;
import android.util.Log;

import com.example.library.control.TxtKandy;
import com.example.library.http.HttpRequestClient;
import com.example.library.util.LogUtil;
import com.genband.kandy.api.Kandy;
import com.genband.kandy.api.access.KandyLoginResponseListener;
import com.genband.kandy.api.access.KandyLogoutResponseListener;
import com.genband.kandy.api.provisioning.IKandyValidationResponse;
import com.genband.kandy.api.provisioning.KandyValidationMethoud;
import com.genband.kandy.api.provisioning.KandyValidationResponseListener;
import com.genband.kandy.api.services.calls.KandyRecord;
import com.genband.kandy.api.services.chats.IKandyImageItem;
import com.genband.kandy.api.services.chats.IKandyTransferProgress;
import com.genband.kandy.api.services.common.KandyResponseListener;
import com.genband.kandy.api.services.common.KandyResponseProgressListener;
import com.genband.kandy.api.services.profile.KandyUserProfileParams;
import com.genband.kandy.api.services.profile.KandyUserProfileResposeListener;
import com.genband.kandy.api.utils.KandyIllegalArgumentException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by wdy on 2017/7/19.
 * kandy用户登录接口
 */

public class AccessKandy {
    private static final String TAG = AccessKandy.class.getSimpleName();
    public String mUser;
    public String mPassward;
    public String mDomainName;
    private boolean mIslogin = false;
    private String mDomianAccessToken;

    private String mUserNickName;
    private String mHeadImage;

    public AccessKandy() {
        getDomainAccessToken();
    }

    public void clearData() {
        mUser = "";
        mPassward = "";
        mIslogin = false;
    }

    //kandy 账户登录
    public void userLogin(final String user, final String passward, final LoginRequestCallBack callBack) {
        KandyRecord kandyRecord = null;
        try {
            kandyRecord = new KandyRecord(user);
        } catch (KandyIllegalArgumentException e) {
            return;
        }
        Kandy.getAccess().login(kandyRecord, passward, new KandyLoginResponseListener() {
            @Override
            public void onLoginSucceeded() {
                if (callBack != null) {
                    mUser = user;
                    mIslogin = true;
                    TxtKandy.getDataMpvConnnect().saveKandyUser(mUser);
                    TxtKandy.getDataMpvConnnect().saveKandyPassward(passward);
                    callBack.onSuccess();
                }
            }

            @Override
            public void onRequestFailed(int i, String s) {
                callBack.onFail(i, s);
            }
        });
    }

    //kandy Logout  退出
//    public void logout(final LogoutRequestCallBack callback) {
//        Kandy.getAccess().logout(new KandyLogoutResponseListener() {
//            @Override
//            public void onLogoutSucceeded() {
//                mUser = null;
//                if (callback != null) {
//                    mIslogin = false;
//                    callback.onSuccess();
//                }
//            }
//
//            @Override
//            public void onRequestFailed(int i, String s) {
//                if (callback != null) {
//                    callback.onFail(i, s);
//                }
//            }
//        });
//    }

    //kandy Logout  退出
    public void logout(final LogoutRequestCallBack callback){
        Kandy.getAccess().logout(new KandyLogoutResponseListener() {
            @Override
            public void onLogoutSucceeded() {
                Log.d(TAG, "onLogoutSucceeded: ");
            }
            @Override
            public void onRequestFailed(int i, String s) {
                Log.d(TAG, "onRequestFailed: i"+i+"err:"+s);
            }
        });

        Kandy.getProvisioning().deactivate(new KandyResponseListener() {
            @Override
            public void onRequestSucceded() {
                Log.d(TAG, "onRequestSucceded: ");
                mUser=null;
                if (callback!=null){
                    mIslogin=false;
                    callback.onSuccess();
                }
            }

            @Override
            public void onRequestFailed(int i, String s) {
                Log.d(TAG, "onFail: deactivate errcode"+i+"errmsg"+s);
                if (callback!=null){
                    callback.onFail(i,s);
                }
            }
        });
    }

    //短信验证
    public void validate(String phone, String validationCode, final RequestCallBack callback) {
        String twoLetterISOCountryCode = "CN";
        Kandy.getProvisioning().validateAndProvision(phone, validationCode, twoLetterISOCountryCode, new KandyValidationResponseListener() {

            @Override
            public void onRequestFailed(final int responseCode, final String err) {
                if (callback != null) {
                    callback.onFail(responseCode, err);
                }

            }

            @Override
            public void onRequestSuccess(IKandyValidationResponse response) {
                LogUtil.d(TAG, "domainName:" + response.getDomainName());
                LogUtil.d(TAG, "user:" + response.getUser());
                LogUtil.d(TAG, "userId:" + response.getUserId());
                LogUtil.d(TAG, "userPassword:" + response.getUserPassword());
                mUser = response.getUserId();
                mPassward = response.getUserPassword();
                if (callback != null) {
                    callback.onSuccess();
                }
            }
        });
    }

    //当前登录的账户名
    public String getLoginUser() {
        return mUser;
    }

    //发送短信验证码
    public void sendVerificationCode(String phone, final RequestCallBack callBall) {
        String twoLetterISOCountryCode = "CN";
        String callerPhonePrefix = null;
        Kandy.getProvisioning().requestCode(KandyValidationMethoud.SMS, phone, twoLetterISOCountryCode, callerPhonePrefix, new KandyResponseListener() {
            @Override
            public void onRequestSucceded() {
                if (callBall != null)
                    callBall.onSuccess();
            }

            @Override
            public void onRequestFailed(int i, final String s) {
                if (callBall != null)
                    callBall.onFail(i, s);
            }
        });
    }

    public boolean userIsLogin() {
        return mIslogin;
    }

    //获取DomianAccessToken
    public void getDomainAccessToken() {
        String url = "https://api.kandycn.com/v1.3/domains/accessTokens?";
        StringBuilder builder = new StringBuilder(url);
        builder.append("key=" + TxtKandy.getKandyCall().API_KEY);
        builder.append("&domain_api_secret=" + TxtKandy.getKandyCall().API_SECRET);
        HttpRequestClient.getIntance().get(builder.toString(), new HttpRequestClient.RequestHttpCallBack() {
            @Override
            public void onSuccess(String json) {
                LogUtil.d(TAG, "onSuccess: json" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getString("status").equals("0")) {
                        String accessToken = jsonObject.getJSONObject("result").getString("domain_access_token");
                        LogUtil.d(TAG, "onSuccess: accessToken" + accessToken);
                        mDomianAccessToken = accessToken;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String err, int code) {

            }
        });
    }

    //用户名和密码进行注册
    public void registerKandyUser(Map<String, String> values, HttpRequestClient.RequestHttpCallBack callback) {
        String url = "https://api.kandycn.com/v1.3/domains/users/user_id?";
        StringBuilder builder = new StringBuilder(url);
        builder.append("key=" + mDomianAccessToken);
        HttpRequestClient.getIntance().post(builder.toString(), values, callback);
    }


    //获取nicknameAndHeadImage
    public void getNikeNameAnHead(final RequestNickNameAndHeadImageCallback callback) {
        Kandy.getServices().getProfileService().getUserProfile(new KandyUserProfileResposeListener() {
            @Override
            public void onRequestSuccess(KandyUserProfileParams kandyUserProfileParams) {
                if (kandyUserProfileParams != null) {
                    if (callback != null) {
                        String nickaName = kandyUserProfileParams.getUserProfileName();
                        mUserNickName = nickaName;
                        IKandyImageItem item = kandyUserProfileParams.getProfileImage();
                        callback.onRequestSuccess(nickaName, "");
                    }
                } else {
                    if (callback == null) {
                        callback.onRequestFailed(-1, "request is null");
                    }
                }
            }

            @Override
            public void onRequestFailed(int i, String s) {
                callback.onRequestFailed(i, s);

            }
        });
    }

    //更新用户nickname
    public void updateNickName(String nickname, final RequestCallBack callback) {
        Kandy.getServices().getProfileService().updateUserName(nickname, new KandyResponseListener() {
            @Override
            public void onRequestSucceded() {
                LogUtil.d(TAG, "onRequestSucceded: 修改成功！");
                if (callback != null) {
                    callback.onSuccess();
                }
            }

            @Override
            public void onRequestFailed(int i, String s) {
                LogUtil.d(TAG, "onRequestSucceded: 修改失败！");
                if (callback != null) {
                    callback.onFail(i, s);
                }
            }
        });
    }

    //更新用户headImaage
    public void updateHeadImage(Uri uri, final RequestCallBack callback) {
        Kandy.getServices().getProfileService().updateUserImage(uri, new KandyResponseListener() {
            @Override
            public void onRequestSucceded() {
                if (callback != null)
                    callback.onSuccess();
            }

            @Override
            public void onRequestFailed(int i, String s) {
                if (callback != null) {
                    callback.onFail(i, s);
                }

            }
        });

    }

    public void downloadMediaPath(IKandyImageItem item) {
        Kandy.getServices().geCloudStorageService().downloadMedia(item, new KandyResponseProgressListener() {
            @Override
            public void onRequestSucceded(Uri uri) {

            }

            @Override
            public void onProgressUpdate(IKandyTransferProgress iKandyTransferProgress) {

            }

            @Override
            public void onRequestFailed(int i, String s) {

            }
        });

    }

    public String getNickName() {
        return mUserNickName;
    }

    public interface LoginRequestCallBack {
        public void onSuccess();

        public void onFail(int i, String s);
    }

    public interface LogoutRequestCallBack {
        public void onSuccess();

        public void onFail(int i, String s);
    }

    public interface RequestCallBack {
        public void onSuccess();

        public void onFail(int i, String s);
    }

    public interface RequestNickNameAndHeadImageCallback {
        public void onRequestSuccess(String name, String image);

        public void onRequestFailed(int i, String s);
    }


}

