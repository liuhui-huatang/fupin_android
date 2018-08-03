package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.library.login.AccessKandy;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.utils.AndroidInfoUtils;
import com.huatang.fupin.utils.RandomUtil;
import com.huatang.fupin.utils.SmsUtil;
import com.huatang.fupin.utils.TimerUtil;
import com.huatang.fupin.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 修改密码
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class PwdUpdateActivity extends BaseActivity {


    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.et_login_phone)
    EditText etLoginPhone;
    @BindView(R.id.et_login_code)
    EditText etLoginCode;
    @BindView(R.id.bt_login_get_code)
    Button btLoginGetCode;
    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;
    @BindView(R.id.bt_login_submit)
    Button btLoginSubmit;

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

    }


    /*
    * @ forever 在 17/5/17 下午2:28 创建
    *
    * 描述：页面点击事件处理
    *
    */
    String phone="";
    String number="";
    @OnClick({R.id.left_menu, R.id.bt_login_get_code, R.id.bt_login_submit})
    public void onViewClicked(View view) {
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

                number = RandomUtil.randomNumber(4);
                SmsUtil.sendSMS(this, phone, "您的短信验证码是：" + number+"。(奈曼旗扶贫）");
                TimerUtil.start(btLoginGetCode);

                break;
            case R.id.bt_login_submit:
                phone = etLoginPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone) || phone.length() != 11) {
                    ToastUtil.show("请先输入正确的手机号");
                    return;
                }

//                String code = etLoginCode.getText().toString().trim();
//                if (TextUtils.isEmpty(code) || code.length() != 4) {
//                    ToastUtil.show("请先输入正确的验证码");
//                    return;
//                }
//                if (!code.equals(number)) {
//                    ToastUtil.show("验证码错误");
//                    return;
//                }
                String pwd = etNewPwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd) ) {
                    ToastUtil.show("请设置新的密码");
                    return;
                }
                updatePwd(phone,pwd);
                break;
        }
    }


    public void updatePwd(String phone,String pwd){

        HttpRequest.updatePwd(this, phone, pwd, new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {

                finish();
            }
        });

    }
}
