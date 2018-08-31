package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.bean.YouKe;
import com.huatang.fupin.http.NewHttpRequest;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.SkinUtil;
import com.huatang.fupin.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToursitsRegisterActivity extends BaseActivity implements View.OnClickListener {

    public static final int REGISTER_SUCCESS = 10001;
    private EditText register_pwd;
    private EditText register_phone;
    private EditText register_name;
    private Button register_btn;
    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.right_tx_menu)
    TextView rightMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tours_register);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.title_tx)).setText("游客注册");
        leftMenu.setImageResource(SkinUtil.getResouceId(R.mipmap.icon_commen_break));
        leftMenu.setOnClickListener(this);
        rightMenu.setText("注 册");
        rightMenu.setVisibility(View.VISIBLE);
        rightMenu.setOnClickListener(this);
        register_phone = (EditText) findViewById(R.id.register_username_edt);
        register_name = (EditText) findViewById(R.id.register_name);
        register_pwd = (EditText) findViewById(R.id.register_pwd_edt);
        register_btn = (Button) findViewById(R.id.register_btn);
        register_btn.setOnClickListener(this);

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
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_tx_menu:
                String phone = register_phone.getText().toString().trim();
                String pwd = register_pwd.getText().toString().trim();
                String name = register_name.getText().toString().trim();
                boolean flag = checkEdit(phone, pwd, name);
                if (flag) {
                    registerUser(phone, pwd, name);
                }

                break;
            case R.id.left_menu:
                finish();
                break;
        }
    }

    private boolean checkEdit(String phone, String pwd, String name) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show("请输入手机号码");
            return false;
        }
        if (phone.length() != 11 || !phone.startsWith("1")) {
            ToastUtil.show("请输入正确的手机号码");
            return false;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.show("请输入密码");
            return false;
        }
        if (TextUtils.isEmpty(name)) {
            ToastUtil.show("请输入真实姓名");
            return false;
        }
        return true;

    }

    private void registerUser(final String phone, String password, String name) {
        NewHttpRequest.registerToursits(this, phone, password, name, new NewHttpRequest.MyCallBack(this) {
            @Override
            public void ok(String json) {
                ToastUtil.show("注册成功");
                String token = JsonUtil.getString(json, Config.TOKEN);
                SPUtil.saveString(Config.TOKEN, token);
                SPUtil.saveString(Config.PHONE, JsonUtil.getString(json, Config.PHONE));
                SPUtil.saveString(Config.NAME, JsonUtil.getString(json, Config.NAME));
                try {
                    SPUtil.saveObject(Config.YOUKE, JsonUtil.json2Bean(json, YouKe.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // SPUtil.saveString(Config.Type,Config.YOUKU_TYPE);
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);

            }
        });
    }

    public static void startIntent(Activity activity, String key, String value) {
        Intent it = new Intent(activity, ToursitsRegisterActivity.class);
        activity.startActivity(it);
    }
}
