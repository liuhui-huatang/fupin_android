package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.http.NewHttpRequest;
import com.huatang.fupin.utils.AndroidInfoUtils;
import com.huatang.fupin.utils.CountDownUtil;
import com.huatang.fupin.utils.MLog;
import com.huatang.fupin.utils.RandomUtil;
import com.huatang.fupin.utils.SmsUtil;
import com.huatang.fupin.utils.TimerUtil;
import com.huatang.fupin.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


/**
 * 修改密码
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class PwdUpdateActivity extends BaseActivity implements View.OnClickListener{


    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.et_login_phone)
    EditText etLoginPhone;
    @BindView(R.id.et_login_code)
    EditText etLoginCode;
    @BindView(R.id.bt_login_get_code)
    TextView btLoginGetCode;
    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;
    @BindView(R.id.bt_login_submit)
    Button btLoginSubmit;
    private static final String country ="86";
    private MyHandler myHandler;
    private EventHandler eh;
    private String code="";
    private String pwd="";
    String phone="";
    /*
         * @ forever 在 17/5/17 下午2:28 创建
         *
         * 描述：跳转到登录页面
         *
         */
    public static void startIntent(Activity activity) {
        activity.startActivity(new Intent(activity, PwdUpdateActivity.class));
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
        setContentView(R.layout.activity_pwdupdate);
        ButterKnife.bind(this);
        ((TextView)findViewById(R.id.title_tx)).setText("设置密码");
        initSMSCode();
    }
    private void initSMSCode(){
        myHandler = new MyHandler();
        //注册回调监听，放到发送和验证前注册，注意这里是子线程需要传到主线程中去操作后续提示
        eh = new EventHandler(){

            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                myHandler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);
    }
    private class MyHandler extends Handler {
        public MyHandler(){

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg2 == SMSSDK.RESULT_COMPLETE){
                //回调完成
                if (msg.arg1 == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show("验证成功");
                            updatePwd();
                        }
                    });

                }else if (msg.arg1 == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //获取验证码成功
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show("验证码已发送");
                            MLog.d("验证码已发送");
                        }
                    });
                }else if (msg.arg1 ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){



                }
            }else{
                ((Throwable)msg.obj).printStackTrace();
                Throwable throwable = (Throwable) msg.obj;
                try {
                    JSONObject obj = new JSONObject(throwable.getMessage());
                    final String des = obj.optString("detail");
                    if (!TextUtils.isEmpty(des)){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show(des);
                                MLog.e(des);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    /*
    * @ forever 在 17/5/17 下午2:28 创建
    *
    * 描述：页面点击事件处理
    *
    */

    @Override
    @OnClick({R.id.left_menu, R.id.bt_login_get_code, R.id.bt_login_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.bt_login_get_code:
                phone = etLoginPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.show("请先输入正确的手机号");
                    return;
                }
                if (phone.length() != 11) {
                    ToastUtil.show("请输入正确的手机号");
                    return;
                }
                startCountDown();
                SMSSDK.getVerificationCode(country, phone);

               // TimerUtil.start(btLoginGetCode);

                break;
            case R.id.bt_login_submit:
                phone = etLoginPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone) || phone.length() != 11) {
                    ToastUtil.show("请先输入正确的手机号");
                    return;
                }
                 pwd = etNewPwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd) ) {
                    ToastUtil.show("请设置新的密码");
                    return;
                }
                code = etLoginCode.getText().toString().trim();
                SMSSDK.submitVerificationCode(country, phone, code);
                break;
        }
    }

    /***
     * 验证码倒计时
     *
     * **/
    private void startCountDown(){
        CountDownUtil countDownUtil =new CountDownUtil(btLoginGetCode);
        countDownUtil.setCountDownMillis(60_000L)//倒计时60000ms
                .setCountDownColor(android.R.color.holo_blue_dark,android.R.color.background_dark)//不同状态字体颜色
                .setOnClickListener(this).start();

    }
    public void updatePwd(){
        NewHttpRequest.updatePwd(this, phone, pwd, new NewHttpRequest.MyCallBack(this) {
            @Override
            public void ok(String json) {
                ToastUtil.show("设置密码成功");
                finish();
            }

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);
            }
        });

    }


}
