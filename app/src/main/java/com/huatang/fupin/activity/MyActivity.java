package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.example.library.control.TxtKandy;
import com.example.library.http.HttpRequestClient;
import com.example.library.login.AccessKandy;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.bean.Leader;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.utils.AppManager;
import com.huatang.fupin.utils.GlideUtils;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.MLog;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.SystemLogHelper;
import com.huatang.fupin.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * 我的
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class MyActivity extends BaseActivity {


    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rl_geren)
    RelativeLayout rlGeren;
    @BindView(R.id.rl_gengxin)
    RelativeLayout rlGengxin;
    @BindView(R.id.rl_fankui)
    RelativeLayout rlFankui;
    @BindView(R.id.bt_exit)
    Button btExit;
    @BindView(R.id.tv_zhiwu)
    TextView tvZhiwu;
    @BindView(R.id.rl_xinxi)
    RelativeLayout rlXinxi;
    @BindView(R.id.rl_mima)
    RelativeLayout rlMima;

    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.rl_year)
    RelativeLayout rlYear;

    /*
         * @ forever 在 17/5/17 下午2:28 创建
         *
         * 描述：跳转到登录页面
         *
         */
    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, MyActivity.class);
        activity.startActivity(it);
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
        setContentView(R.layout.activity_my);
        ButterKnife.bind(this);
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!TextUtils.isEmpty(SPUtil.getString("photo"))) {
            GlideUtils.displayHome(ivPhoto, BaseConfig.apiUrl + SPUtil.getString("photo"));
        }
    }

    private void initView() {
        tvYear.setText(SPUtil.getString("year"));
        tvName.setText(SPUtil.getString("name"));
        tvZhiwu.setText(SPUtil.getString("duty"));

        if ("2".equals(SPUtil.getString("identity"))) {
            rlYear.setVisibility(View.GONE);
            rlFankui.setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.left_menu, R.id.rl_geren, R.id.rl_gengxin, R.id.rl_fankui, R.id.bt_exit, R.id.rl_xinxi, R.id.rl_mima, R.id.rl_year})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.rl_geren:
                MyInfoActivity.startIntent(this);
                break;
            case R.id.rl_xinxi:
                MyInfoActivity.startIntent(this);
                break;
            case R.id.rl_fankui:
                MyLocationActivity.startIntent(this);
                break;
            case R.id.rl_mima:
                PwdUpdateActivity.startIntent(this);
                break;
            case R.id.rl_gengxin:
                AppUpdateActivity.startIntent(this);
                break;
            case R.id.bt_exit:
                exitDialog();
                break;
            case R.id.rl_year:
                String[] words2 = new String[5];
                words2[0] = "2016";
                words2[1] = "2017";
                words2[2] = "2018";
                words2[3] = "2019";
                words2[4] = "2020";

                DialogUIUtils.showSingleChoose(MyActivity.this, "请选择年度", -1, words2, new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {

                        if (TextUtils.isEmpty(text)) {
                            return;
                        }
                        tvYear.setText(text);
                        login(SPUtil.getString("phone"), text.toString());
                    }
                }).show();
                break;
        }
    }

    int num = 0;

    public void kandyRegisterAll() {
        num=0;
        HttpRequest.getKandyNoLeader(this, new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {

//                final Leader bean = JsonUtil.json2Bean(json, Leader.class);
                List<Leader> beans = JsonUtil.toList(json, Leader.class);

                DialogUIUtils.showTie(MyActivity.this, "通讯注册...");
                for (final Leader bean : beans) {

//                    HttpRequest.saveKandy(MyActivity.this, bean.getId(), bean.getLeader_phone() + bean.getId()+"@huatang.txtechnology.com.cn", "kandy" + bean.getPassword(), new HttpRequest.MyCallBack() {
//                        @Override
//                        public void ok(String json) {
//                            ToastUtil.show("注册成功！");
//                            MLog.e("kandy==ssaveKandy==ok", "ok");
//                        }
//                    });



                    Map<String, String> values = new HashMap<String, String>();
                    values.put("user_id", bean.getLeader_phone() + bean.getId());
                    values.put("user_email", "email@sdkdemo.txtechnology.com.cn");
                    values.put("user_password", "kandy" + bean.getPassword());
                    TxtKandy.getAccessKandy().registerKandyUser(values, new HttpRequestClient.RequestHttpCallBack() {
                        @Override
                        public void onSuccess(final String json) {
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                if (jsonObject.getString("status").equals("0")) {
                                    JSONObject resultJson = jsonObject.getJSONObject("result");
                                    final String user_password = resultJson.getString("user_password");
                                    final String full_user_id = resultJson.getString("full_user_id");

                                    MyActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            HttpRequest.saveKandy(MyActivity.this, bean.getId(), full_user_id, user_password, new HttpRequest.MyCallBack() {
                                                @Override
                                                public void ok(String json) {
//                                                    ToastUtil.show("注册成功！");
                                                    num++;
                                                    MLog.e("kandy==ssaveKandy==ok", "ok=="+num);
                                                }
                                            });
                                        }
                                    });
                                } else {
                                    MyActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtil.show("注册失败！");
                                            MLog.e("通讯注册失败=>", "id:" + bean.getId() + "--phone:" + bean.getLeader_phone());
                                            MLog.e("失败原因=>", json);
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


            }
        });
    }


    /**
     * 登录
     *
     * @param phone
     * @param
     */
    private void login(final String phone, final String year) {

        HttpRequest.loginToggle(this, phone, year, new HttpRequest.MyCallBack() {
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
                        kandyRegister(phone + SPUtil.getString("id"), "android" + SPUtil.getString("password"));
                    } else {
                        kandyLogin(JsonUtil.getString(json, "kandy_user"), JsonUtil.getString(json, "kandy_pwd"));
                    }
                    tvYear.setText(SPUtil.getString("year"));
                } else {
                    //贫困户
                    SPUtil.saveString("id", JsonUtil.getString(json, "id"));
                    SPUtil.saveString("name", JsonUtil.getString(json, "fname"));
                    SPUtil.saveString("phone", JsonUtil.getString(json, "fphone"));
                    SPUtil.saveString("password", JsonUtil.getString(json, "password"));
                    SPUtil.saveString("photo", JsonUtil.getString(json, "basic_photo"));
                    SPUtil.saveString("identity", identity);

                    setJpushAlias(SPUtil.getString("phone"));
                }
            }

            @Override
            public void no(String msg) {
                super.no(msg);
                tvYear.setText(SPUtil.getString("year"));
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
                MLog.e("kandy==MyActivity=onSuccess:" + TxtKandy.getDataMpvConnnect().getCurrentLoginUser());
            }

            @Override
            public void onFail(int i, String s) {
                DialogUIUtils.dismssTie();
                try {
                    MLog.e("kandy=MyActivity=onFail:" + TxtKandy.getDataMpvConnnect().getCurrentLoginUser());
                } catch (Exception e) {
                    MLog.e("kandy=MyActivity=onFail:", e.getMessage());
                }
            }
        });
    }

    // 通讯注册
    public void kandyRegister(String phone, String pwd) {
        DialogUIUtils.showTie(this, "通讯注册...");
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
                        TxtKandy.getAccessKandy().mDomainName = SPUtil.getString("name");
                        final String user_password = resultJson.getString("user_password");
                        final String full_user_id = resultJson.getString("full_user_id");
                        HttpRequest.saveKandy(MyActivity.this, SPUtil.getString("id"), full_user_id, user_password, new HttpRequest.MyCallBack() {
                            @Override
                            public void ok(String json) {
                                MLog.e("kandy==ssaveKandy==ok", "ok");
                                SPUtil.saveString("kandy_user", full_user_id);
                                SPUtil.saveString("kandy_pwd", user_password);
                                kandyRegisterAll();
                            }
                        });
                    } else {
                        MyActivity.this.runOnUiThread(new Runnable() {
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
     * 弹出退出登录对话框
     */
    public void exitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("退出登录");
        builder.setMessage("确定退出当前账号？");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                kandyExit();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * kandy通讯退出
     */
    public void kandyExit() {
        if (TxtKandy.getAccessKandy().userIsLogin()) {
            TxtKandy.getAccessKandy().logout(new AccessKandy.LogoutRequestCallBack() {
                @Override
                public void onSuccess() {
                    MLog.e("kandy==logout==onSuccess");

                    //停止捕获log信息
                    SystemLogHelper.getmInstance().stop();
                    //删除极光推送别名
                    delJpushAlias();

                    //清空登录信息，销毁首页，跳转登录页，关闭当前页
                    clearInfo();
                    AppManager.getAppManager().finishActivity(HomeActivity.class);
                    LoginActivity.startIntent(MyActivity.this);
                    finish();
                }

                @Override
                public void onFail(int i, String s) {
                    MLog.e("kandy==logout==onfail" + s);

                }
            });
        } else {
            MLog.e("kandy==logout==else");
            //清空登录信息，销毁首页，跳转登录页，关闭当前页
            clearInfo();
            AppManager.getAppManager().finishActivity(HomeActivity.class);
            LoginActivity.startIntent(MyActivity.this);
            finish();
        }
    }


    /**
     * 取消极光推送别名
     */
    public void delJpushAlias() {
        JPushInterface.setAlias(this, "", null);
        MLog.e("JPush==delJpushAlias==setAlias");
    }

    /**
     * 清空登录信息
     */
    public void clearInfo() {

        SPUtil.removeValue("id");
        SPUtil.removeValue("name");
        SPUtil.removeValue("unit");
        SPUtil.removeValue("duty");
        SPUtil.removeValue("town");
        SPUtil.removeValue("phone");
        SPUtil.removeValue("photo");
        SPUtil.removeValue("password");
        SPUtil.removeValue("identity");
        SPUtil.removeValue("kandy_user");
        SPUtil.removeValue("kandy_pwd");

    }


}
