package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.bean.NewSign;
import com.huatang.fupin.bean.Sign;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.utils.DateUtil;
import com.huatang.fupin.utils.GlideUtils;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.ToastUtil;
import com.huatang.fupin.utils.ViewHolderUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 帮扶日志详情页面
 * Created by forever on 2016/12/7.
 */

public class BangFuInfoActivity extends BaseActivity {


    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.title_tx)
    TextView pageTitle;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(R.id.iv_01)
    ImageView iv01;
    @BindView(R.id.iv_02)
    ImageView iv02;
    @BindView(R.id.iv_03)
    ImageView iv03;
    @BindView(R.id.iv_04)
    ImageView iv04;
    @BindView(R.id.iv_05)
    ImageView iv05;
    @BindView(R.id.iv_06)
    ImageView iv06;
    @BindView(R.id.iv_07)
    ImageView iv07;
    @BindView(R.id.iv_08)
    ImageView iv08;
    @BindView(R.id.layout_images)
    LinearLayout layoutImages;


    /*
       * @ forever 在 17/5/17 下午2:28 创建
       *
       * 描述：跳转到登录页面
       *
       */
    public static void startIntent(Activity activity, NewSign bean) {
        Intent it = new Intent(activity, BangFuInfoActivity.class);
        it.putExtra("sign", bean);
        activity.startActivity(it);
    }

    NewSign bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bangfuinfo);
        ButterKnife.bind(this);
        bean = (NewSign) getIntent().getSerializableExtra("sign");
        initView();
    }



    public void initView() {
        pageTitle.setText("日志详情");
        List<ImageView> imageViewList = new ArrayList<>();
        imageViewList.add(iv01);
        imageViewList.add(iv02);
        imageViewList.add(iv03);
        imageViewList.add(iv04);
        imageViewList.add(iv05);
        imageViewList.add(iv06);
        imageViewList.add(iv07);
        imageViewList.add(iv08);
        tvTitle.setText(bean.getSign_title());
        tvText.setText(bean.getSign_content());
        tvLocation.setText(bean.getSign_address());
        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HttpRequest.updateSignLocation(BangFuInfoActivity.this, bean.getId(),bean.getSign_town_id(),bean.getSign_town(),bean.getSign_village_id(),bean.getSign_village(), new HttpRequest.MyCallBack() {
                    @Override
                    public void ok(String json) {
                        ToastUtil.show("位置更正成功");
                    }
                });
            }
        });
        long time = Long.parseLong(bean.getSign_time());
        tvTime.setText(DateUtil.getStandardTime(time));

        String images = bean.getSign_imgs();
        if (!TextUtils.isEmpty(images)) {
            String[] strings = images.split("##");
            for (int i = 0; i < 8; i++) {
                if (i < strings.length) {
                    if (!TextUtils.isEmpty(strings[i])) {
                        imageViewList.get(i).setVisibility(View.VISIBLE);
                        GlideUtils.displayHome(imageViewList.get(i), BaseConfig.ImageUrl + strings[i]);
                    }
                } else {
                    imageViewList.get(i).setVisibility(View.GONE);
                }
            }
        } else {
            for (int i = 0; i < 8; i++) {
                imageViewList.get(i).setVisibility(View.GONE);
            }
            layoutImages.setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.left_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;

        }
    }


}
