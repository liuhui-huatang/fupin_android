package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.utils.AndroidInfoUtils;
import com.huatang.fupin.update.AppDownloadUtils;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.MLog;
import com.huatang.fupin.utils.ToastUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;
import okhttp3.Response;

/**
 * app更新页面
 * Created by forever on 2016/12/7.
 */

public class AppUpdateActivity extends BaseActivity {


    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.bt_update)
    Button btUpdate;
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.rl_update_layout)
    LinearLayout rlUpdateLayout;

    /*
       * @ forever 在 17/5/17 下午2:28 创建
       *
       * 描述：跳转到登录页面
       *
       */
    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, AppUpdateActivity.class);
        activity.startActivity(it);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appupdate);
        ButterKnife.bind(this);

        ((TextView) findViewById(R.id.tv_code)).setText("V " + AndroidInfoUtils.versionName());

        initUpdata();

    }

    String apkUrl = "";
    String apkName = "";
    int apkCode = 0;

    private void initUpdata() {
        DialogUIUtils.showTie(this, "加载中...");
        OkHttpUtils.get(BaseConfig.downloadApp)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String json, Request request, @Nullable Response response) {
                        MLog.e("app==", json);
                        DialogUIUtils.dismssTie();
                        apkCode = JsonUtil.getInt(json, "code");
                        apkName = JsonUtil.getString(json, "name");
                        apkUrl = JsonUtil.getString(json, "url");
                        if (getApkNumber(AppUpdateActivity.this) < apkCode) {
                            btUpdate.setVisibility(View.VISIBLE);
                            rlUpdateLayout.setVisibility(View.GONE);
                        } else {
                            ToastUtil.show("当前版本已是最新版本！");
                        }
//                        else {
//                            btUpdate.setVisibility(View.GONE);
//                            rlUpdateLayout.setVisibility(View.VISIBLE);
//                        }
                    }
                });
    }

    public static int getApkNumber(Context context) {

        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo.versionCode;
    }


    @OnClick({R.id.left_menu, R.id.bt_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.bt_update:
                ToastUtil.show("开始更新");
                btUpdate.setVisibility(View.GONE);
                rlUpdateLayout.setVisibility(View.VISIBLE);
                appUpload();
                break;
        }
    }

    public void appUpload() {
        AppDownloadUtils.init(tvProgress, progressBar, rlUpdateLayout);
        AppDownloadUtils.downloadFile(this, apkUrl, apkName, apkCode);
    }
}
