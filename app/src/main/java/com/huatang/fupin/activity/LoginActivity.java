package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.example.library.control.TxtKandy;
import com.example.library.http.HttpRequestClient;
import com.example.library.login.AccessKandy;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.receiver.JpushReceiver;
import com.huatang.fupin.utils.AndroidInfoUtils;
import com.huatang.fupin.utils.DateUtil;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.MLog;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.ToastUtil;
import com.lzy.okhttputils.model.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


/**
 * 登录
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_login_phone)
    EditText etLoginPhone;
    @BindView(R.id.et_login_pwd)
    EditText etLoginPwd;
    @BindView(R.id.bt_login_submit)
    Button btLoginSubmit;
    @BindView(R.id.tv_pwd_update)
    TextView tvPwdUpdate;


    /*
     * @ forever 在 17/5/17 下午2:28 创建
     *
     * 描述：跳转到登录页面
     *
     */
    public static void startIntent(Activity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }

    /*
    * @ forever 在 17/5/17 下午2:28 创建
    *
    * 描述：页面创建时调用
    *
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
//        etLoginPhone.setText("18871584808");

        TextView tvVersion=findViewById(R.id.tv_version);
        tvVersion.setText("V"+ AndroidInfoUtils.versionName());
    }


    /*
    * @ forever 在 17/5/17 下午2:28 创建
    *
    * 描述：页面点击事件处理
    *
    */
    @OnClick({R.id.bt_login_submit, R.id.tv_pwd_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login_submit:
                String phone = etLoginPhone.getText().toString().trim();
                String pwd = etLoginPwd.getText().toString().trim();

//                if ("123".equals(phone)) {
//                    login(phone, pwd);
//                    return;
//                }

                if (TextUtils.isEmpty(phone) || phone.length() != 11) {
                    ToastUtil.show("请先输入正确的手机号");
                    return;
                }

                if (TextUtils.isEmpty(pwd)) {
                    ToastUtil.show("请先输入正确的验证码");
                    return;
                }
                login(phone, pwd);

                break;
            case R.id.tv_pwd_update:
                PwdUpdateActivity.startIntent(this);
                break;
        }
    }

    /**
     * 登录
     *
     * @param phone
     * @param
     */
    private void login(final String phone, final String pwd) {

        HttpRequest.login(this, phone, pwd, new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                ToastUtil.show("登录成功！");
                String identity = JsonUtil.getString(json, "identity");
                if ("1".equals(identity)) {
                    //干部
                    SPUtil.saveString("id", JsonUtil.getString(json, "id"));
                    SPUtil.saveString("name", JsonUtil.getString(json, "leader_name"));

                    SPUtil.saveString("unit", JsonUtil.getString(json, "leader_unit"));
                    SPUtil.saveString("duty", JsonUtil.getString(json, "leader_duty"));
                    SPUtil.saveString("town", JsonUtil.getString(json, "help_town"));
                    SPUtil.saveString("town_id", JsonUtil.getString(json, "help_town_id"));
                    SPUtil.saveString("phone", JsonUtil.getString(json, "leader_phone"));
                    SPUtil.saveString("photo", JsonUtil.getString(json, "leader_photo"));
                    SPUtil.saveString("password", JsonUtil.getString(json, "password"));
                    SPUtil.saveString("identity", identity);
                    SPUtil.saveString("kandy_user", JsonUtil.getString(json, "kandy_user"));
                    SPUtil.saveString("kandy_pwd", JsonUtil.getString(json, "kandy_pwd"));

                    SPUtil.saveString("unit_level", JsonUtil.getString(json, "unit_level"));
                    SPUtil.saveString("leader_andscape", JsonUtil.getString(json, "leader_andscape"));
                    SPUtil.saveString("year", JsonUtil.getString(json, "year"));

                    setJpushAlias(SPUtil.getString("phone"));

                    if (TextUtils.isEmpty(JsonUtil.getString(json, "kandy_pwd"))) {
                        kandyRegister(phone + SPUtil.getString("id"), "android" + pwd);
                    } else {
//                        kandyLogin(JsonUtil.getString(json, "kandy_user"), JsonUtil.getString(json, "kandy_pwd"));
                        HomeActivity.startIntent(LoginActivity.this);
                        finish();
                    }

                } else if ("2".equals(identity)) {
                    //贫困户
                    SPUtil.saveString("id", JsonUtil.getString(json, "id"));
                    SPUtil.saveString("name", JsonUtil.getString(json, "fname"));
                    SPUtil.saveString("phone", JsonUtil.getString(json, "fphone"));
                    SPUtil.saveString("password", JsonUtil.getString(json, "password"));
                    SPUtil.saveString("photo", JsonUtil.getString(json, "basic_photo"));
                    SPUtil.saveString("identity", identity);

                    setJpushAlias(SPUtil.getString("phone"));

                    HomeActivity.startIntent(LoginActivity.this);
                    finish();
                }
            }
        });

    }

    /**
     * 设置极光推送别名
     *
     * @param phone
     */
    public void setJpushAlias(String phone) {
        JPushInterface.setAlias(this, phone, null);
        MLog.e("JPush==setJpushAlias==setAlias");
    }


    // 通讯注册
    public void kandyRegister(String phone, String pwd) {
        DialogUIUtils.showTie(this, "加载中...");
        Map<String, String> values = new HashMap<String, String>();
        values.put("user_id", phone);
        values.put("user_email", "email@sdkdemo.txtechnology.com.cn");
        values.put("user_password", pwd);
        TxtKandy.getAccessKandy().registerKandyUser(values, new HttpRequestClient.RequestHttpCallBack() {
            @Override
            public void onSuccess(String json) {
                DialogUIUtils.dismssTie();
                MLog.e("kandy==registerKandyUser=onSuccess:" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);

                    if (jsonObject.getString("status").equals("0")) {
                        JSONObject resultJson = jsonObject.getJSONObject("result");

                        String user_id = resultJson.getString("user_id");
                        String domain_name = resultJson.getString("domain_name");
                        String user_api_key = resultJson.getString("user_api_key");
                        String user_api_secret = resultJson.getString("user_api_secret");

                        TxtKandy.getAccessKandy().mDomainName = SPUtil.getString("name");
                        final String user_password = resultJson.getString("user_password");
                        final String full_user_id = resultJson.getString("full_user_id");

                        HttpRequest.saveKandy(LoginActivity.this, SPUtil.getString("id"), full_user_id, user_password, new HttpRequest.MyCallBack() {
                            @Override
                            public void ok(String json) {
                                MLog.e("kandy==ssaveKandy==ok", "ok");
                                SPUtil.saveString("kandy_user", full_user_id);
                                SPUtil.saveString("kandy_pwd", user_password);
                                HomeActivity.startIntent(LoginActivity.this);
                                finish();
                            }
                        });

                    } else {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show("注册失败！");
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String err, int code) {
                DialogUIUtils.dismssTie();
                MLog.d("kandy==onFail=registerKandyUser:" + TxtKandy.getDataMpvConnnect().getCurrentLoginUser());
            }
        });
    }

    //   通讯登录
    public void kandyLogin(String kandy_user, String kandy_pwd) {
        DialogUIUtils.showTie(this, "加载中...");
        TxtKandy.getAccessKandy().userLogin(kandy_user, kandy_pwd, new AccessKandy.LoginRequestCallBack() {
            @Override
            public void onSuccess() {
                DialogUIUtils.dismssTie();
                MLog.e("kandy==onSuccess=userLogin:" + TxtKandy.getDataMpvConnnect().getCurrentLoginUser());
                HomeActivity.startIntent(LoginActivity.this);
                finish();
            }

            @Override
            public void onFail(int i, String s) {
                DialogUIUtils.dismssTie();
                MLog.e("kandy==onFail=userLogin:" + s);
            }

        });
    }

}
