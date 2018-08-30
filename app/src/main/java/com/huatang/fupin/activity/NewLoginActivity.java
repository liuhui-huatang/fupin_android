package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.bean.NewFuzeren;
import com.huatang.fupin.bean.NewLeader;
import com.huatang.fupin.bean.NewPoor;
import com.huatang.fupin.bean.YouKe;
import com.huatang.fupin.http.NewHttpRequest;
import com.huatang.fupin.utils.CountDownUtil;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.MLog;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.SkinUtil;
import com.huatang.fupin.utils.ToastUtil;
import com.huatang.fupin.utils.ValidateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class NewLoginActivity extends BaseActivity implements View.OnClickListener{


    TextView ganbuBtn;

    TextView fuzerenBtn;

    TextView pinkuhuBtn;

    TextView adminBtn;

    @BindView(R.id.user_type_tx)
    TextView user_type;

    @BindView(R.id.login_username_edt)
    EditText username_edt;
    @BindView(R.id.login_pwd_edt)
    EditText pwd_edt;
    @BindView(R.id.login_code_edt)
    EditText code_edt;
    @BindView(R.id.sendCode)
    TextView sendCode_btn;
    @BindView(R.id.forget_password)
    Button forget_pass_btn;
    @BindView(R.id.login_submit)
    Button login_submit;
    @BindView(R.id.youkelogin)
    Button youkelogin_btn;
    private String type = Config.YOUKU_TYPE;//默认为游客身份//用户类型:1: 游客2: 贫困户 3: 帮扶干部4: 管理员5: 村负责人
    MyHandler  myHandler;
    private String country ="86";
    private String phone;
    EventHandler eh;
    private String pwd;
    private CountDownUtil countDownUtil;
    BottomSheetDialog bottomSheetDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);
        ButterKnife.bind(this);
        initView();
        initSMSCode();
        initBottomDialog();
        countDownUtil =new CountDownUtil(sendCode_btn);


    }

    private void initView() {
        login_submit.setBackgroundResource(SkinUtil.getResouceId(R.mipmap.login_submit));
        //test
        //SkinUtil.setCurSkinType(SkinUtil.skin_type_default);
        youkelogin_btn.setBackgroundResource(SkinUtil.getResouceId(R.mipmap.youke_login));

    }




    /***
     * 验证码倒计时
     *
     * **/
    private void startCountDown(){

        countDownUtil.setCountDownMillis(60_000L)//倒计时60000ms
                .setCountDownColor(android.R.color.holo_blue_dark,android.R.color.background_dark)//不同状态字体颜色
                .setOnClickListener(this).start();

    }
    private void initSMSCode(){
        myHandler = new MyHandler();
        //注册回调监听，放到发送和验证前注册，注意这里是子线程需要传到主线程中去操作后续提示
        eh=new EventHandler(){

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

    private void initBottomDialog() {

        bottomSheetDialog = new BottomSheetDialog(this);//实例化BottomSheetDialog
        View dialogView= LayoutInflater.from(this).inflate(R.layout.indentity_dialog_layout,null);
        ganbuBtn = dialogView.findViewById(R.id.ganbu);
        pinkuhuBtn = dialogView.findViewById(R.id.pinkunhu);
        fuzerenBtn = dialogView.findViewById(R.id.fuzeren);
        adminBtn = dialogView.findViewById(R.id.admin);
        ganbuBtn.setOnClickListener(this);
        fuzerenBtn.setOnClickListener(this);
        adminBtn.setOnClickListener(this);
        pinkuhuBtn.setOnClickListener(this);

        bottomSheetDialog.setCancelable(true);//设置点击外部是否可以取消
        bottomSheetDialog.setContentView(dialogView);//设置对框框中的布局

    }

    /*
     * @ forever 在 17/5/17 下午2:28 创建
     *
     * 描述：跳转到登录页面
     *
     */
    public static void startIntent(Activity activity) {
        activity.startActivity(new Intent(activity, NewLoginActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onStop(){
        super.onStop();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterEventHandler(eh);
    }



    @Override
    @OnClick({R.id.user_type_tx,R.id.sendCode, R.id.forget_password,R.id.youkelogin,R.id.login_submit})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_type_tx://选择用户类型，底部弹框显示
                if(!bottomSheetDialog.isShowing()){
                    bottomSheetDialog.show();//显示弹窗
                }
                break;
            case R.id.ganbu:
                user_type.setText(ganbuBtn.getText());

                type = Config.GANBU_TYPE;
                bottomSheetDialog.dismiss();
                break;
            case R.id.pinkunhu:
                user_type.setText(pinkuhuBtn.getText());
                type = Config.PENKUNHU_TYPE;
                bottomSheetDialog.dismiss();
                break;
            case R.id.fuzeren:
                user_type.setText(fuzerenBtn.getText());
                type = Config.FUZEREN_TYPE;
                bottomSheetDialog.dismiss();
                break;
            case R.id.admin:
                user_type.setText(adminBtn.getText());
                type = Config.ADMIN_TYPE;
                bottomSheetDialog.dismiss();
                break;
            case R.id.sendCode://发送验证码
                // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
                phone = username_edt.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    ToastUtil.show("请输入手机号码");
                    return;

                }
                if(ValidateUtil.isPhoneNumber(phone)){
                    ToastUtil.show("请输入正确的手机号码");
                    return;
                }
                startCountDown();
                SMSSDK.getVerificationCode(country, phone);
                break;
            case R.id.forget_password:
                PwdUpdateActivity.startIntent(this);
                break;

            case R.id.youkelogin://游客登陆
                type = Config.YOUKU_TYPE;

                login("","",type);
                break;
            case R.id.login_submit://登陆
                phone = username_edt.getText().toString().trim();
                pwd = pwd_edt.getText().toString().trim();
                String code = code_edt.getText().toString().trim();

                boolean flag = checkEdit(phone,pwd,code);//检查是否为null
                if(flag){
                    //SMSSDK.submitVerificationCode(country, phone, code); // 提交验证码，其中的code表示验证码，如“1357”
                    login(phone,pwd,type);//调用登陆接口
                }

                break;




        }

    }



    private  boolean checkEdit(String phone,String pwd,String code){
        if(TextUtils.isEmpty(phone)){
            ToastUtil.show("请输入手机号码");
            return false;
        }
        if(phone.length()!=11 || !phone.startsWith("1")){
            ToastUtil.show("请输入正确的手机号码");
            return false;
        }
        if(TextUtils.isEmpty(pwd)){
           ToastUtil.show("请输入密码");
           return false;
        }
        if(TextUtils.isEmpty(code)){
            ToastUtil.show("请输入验证码");
            return false;
        }
        return true;
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
    /**
     * 登录
     *
     * @param phone
     * @param pwd
     * @param type
     */
    private void login(final String phone, final String pwd,final String type) {
        NewHttpRequest.login(NewLoginActivity.this, phone, pwd,type, new NewHttpRequest.MyCallBack(this) {


            @Override
            public void ok(String json) {
                ToastUtil.show("登录成功！");
                SPUtil.saveString(Config.Type,type);
                SPUtil.saveString(Config.PHONE,phone);
                SPUtil.saveString(Config.PASSWORD,pwd);
                if(type.equals(Config.YOUKU_TYPE)){//游客
                    SPUtil.saveString(Config.TOKEN,JsonUtil.getString(json,Config.TOKEN));
                    SPUtil.saveString(Config.NAME,JsonUtil.getString(json,Config.NAME));
                    YouKe youKe = JsonUtil.json2Bean(json,YouKe.class);
                    try {
                        SPUtil.saveObject(Config.YOUKE,youKe);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if(type.equals(Config.PENKUNHU_TYPE)){

                    NewPoor poor = JsonUtil.json2Bean(json, NewPoor.class);
                    SPUtil.saveString(Config.TOKEN,poor.getToken());
                    try {

                        SPUtil.saveObject(Config.PENKUNHU_KEY,poor);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else if(type.equals(Config.FUZEREN_TYPE)){
                    NewFuzeren fuzeren = JsonUtil.json2Bean(json,NewFuzeren.class);
                    SPUtil.saveString(Config.TOKEN,fuzeren.getToken());
                    try {
                        SPUtil.saveObject(Config.FUZEREN_KEY,fuzeren);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else if(type.equals(Config.GANBU_TYPE)){//
                    NewLeader leader = JsonUtil.json2Bean(json,NewLeader.class);
                    SPUtil.saveString(Config.TOKEN,leader.getToken());
                    try {
                        SPUtil.saveObject(Config.GANBU_KEY,leader);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }else if(type.equals(Config.ADMIN_TYPE)){
                    NewLeader admin = JsonUtil.json2Bean(json,NewLeader.class);
                    SPUtil.saveString(Config.TOKEN,admin.getToken());
                    try {
                        SPUtil.saveObject(Config.ADMIN_KEY,admin);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                NewHomeActivity.startIntent(NewLoginActivity.this);
                finish();
                setJpushAlias(SPUtil.getString(Config.PHONE));
            }

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);
            }
        });



    }



    private class MyHandler extends Handler{
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
                            login(phone,pwd,type);//调用登陆接口
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

}
