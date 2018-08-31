package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huatang.fupin.R;
import com.huatang.fupin.update.AppDownloadUtils;
import com.huatang.fupin.utils.AppManager;
import com.huatang.fupin.utils.SkinUtil;
import com.huatang.fupin.zxing.activity.CaptureActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

public class NewHomeActivity extends FragmentActivity implements View.OnClickListener {


    private LinearLayout mTabHome;
    private LinearLayout mTabAccount;
    private ImageView mHomeImg;
    private ImageView mAccountImg;
    private HomeFragment homeFragment;
    private AccountFragment accountFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_home);

        initView();
        initEvent();
        setSelect(0);  //显示第一个Tab

        // 判断是否需要更新版本
        // AppDownloadUtils.getApkInfo(this);
    }

    private void initEvent() {
        mTabHome.setOnClickListener(this);
        mTabAccount.setOnClickListener(this);
        mHomeImg.setOnClickListener(this);
        mAccountImg.setOnClickListener(this);
    }

    private void initView() {

        mTabHome = (LinearLayout) findViewById(R.id.tab_home);
        mTabAccount = (LinearLayout) findViewById(R.id.tab_account);


        mHomeImg = (ImageView) findViewById(R.id.tab_home_img);
        mAccountImg = (ImageView) findViewById(R.id.tab_account_img);

    }

    public static void startIntent(Activity activity) {
        activity.startActivity(new Intent(activity, NewHomeActivity.class));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tab_home:
                setSelect(0);
                break;
            case R.id.tab_account:
                setSelect(1);
                break;
            case R.id.tab_account_img:
                setSelect(1);
                break;
            case R.id.tab_home_img:
                setSelect(0);
                break;
        }
    }

    /**
     * 显示指定Tab，并将对应的图片设置为亮色
     *
     * @param i
     */
    private void setSelect(int i) {
        resetImg();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.content, homeFragment);
                }
                //将图片设置为亮色
                mHomeImg.setImageResource(SkinUtil.getResouceId(R.mipmap.img_home_light));
                //显示指定Fragment
                transaction.show(homeFragment);
                transaction.commit();
                break;
            case 1:
                if (accountFragment == null) {
                    accountFragment = new AccountFragment();
                    transaction.add(R.id.content, accountFragment);

                }
                mAccountImg.setImageResource(SkinUtil.getResouceId(R.mipmap.img_account_tab_light));
                //显示指定Fragment
                transaction.show(accountFragment);
                transaction.commit();
                break;
        }

    }

    /**
     * 将所有Fragment隐藏起来
     *
     * @param transaction
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (accountFragment != null) {
            transaction.hide(accountFragment);
        }
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
    }

    /**
     * 将所有图片切换成暗色
     */
    private void resetImg() {
        // mHomeImg.setImageResource(SkinUtil.getResouceId(R.mipmap.img_home_light));
        mHomeImg.setImageResource(SkinUtil.getResouceId(R.mipmap.img_home_normal));
        //mAccountImg.setImageResource(SkinUtil.getResouceId(R.mipmap.img_account_light));
        mAccountImg.setImageResource(SkinUtil.getResouceId(R.mipmap.img_account_normal));
    }


    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
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
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
