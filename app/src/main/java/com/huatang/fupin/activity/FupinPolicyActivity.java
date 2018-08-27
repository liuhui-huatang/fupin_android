package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.utils.SkinUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FupinPolicyActivity extends BaseActivity {
    @BindView(R.id.left_menu)
    ImageView leftmenu;
    @BindView(R.id.title_tx)
    TextView tvTitle;
    @BindView(R.id.center_policy_item)
    RelativeLayout center_policy_item;
    @BindView(R.id.zizhiqu_policy_item)
    RelativeLayout zizhiqu_policy_item;
    @BindView(R.id.city_policy_item)
    RelativeLayout city_policy_item;
    @BindView(R.id.naiman_policy_item)
    RelativeLayout naiman_policy_item;
    @BindView(R.id.center_policy_layout)
    LinearLayout center_policy_layout;
    @BindView(R.id.zizhiqu_policy_layout)
    LinearLayout zizhiqu_policy_layout;
    @BindView(R.id.city_policy_layout)
    LinearLayout city_policy_layout;
    @BindView(R.id.naiman_policy_layout)
    LinearLayout naiman_policy_layout;
    @BindView(R.id.center_policy_arrow)
    ImageView center_policy_arrow;
    @BindView(R.id.zizhiqu_policy_arrow)
    ImageView zizhiqu_policy_arrow;
    @BindView(R.id.city_policy_arrow)
    ImageView city_policy_arrow;
    @BindView(R.id.naiman_policy_arrow)
    ImageView naiman_policy_arrow;
    @BindView(R.id.center_xidada_policy)
    RelativeLayout center_xidada_policy;
    @BindView(R.id.center_gelei_policy)
    RelativeLayout center_gelei_policy;
    @BindView(R.id.center_file_policy)
    RelativeLayout center_file_policy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fupin_policy);
        ButterKnife.bind(this);
        initHeadView();
        initView();
    }
    private void initHeadView(){
        tvTitle.setText("政策保障");
    }

    private void initView() {


    }
    @OnClick({R.id.left_menu,
            R.id.center_policy_item,R.id.zizhiqu_policy_item,R.id.city_policy_item,R.id.naiman_policy_item,
            R.id.center_xidada_policy,R.id.center_gelei_policy,R.id.center_file_policy,
            R.id.zizhiqu_gelei_policy,R.id.zizhiqu_file_policy,
            R.id.city_policy_layout,R.id.city_file_policy,
            R.id.naiman_tuopin_policy,R.id.naiman_sannian_policy,R.id.naiman_niandu_policy,R.id.naiman_file_policy})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.left_menu:
                finish();
                break;
            case R.id.center_policy_item:
                if(center_policy_layout.getVisibility() == View.VISIBLE){
                    center_policy_layout.setVisibility(View.GONE);
                    center_policy_arrow.setImageResource(SkinUtil.getResouceId(R.mipmap.icon_common_right));
                }else{
                    center_policy_layout.setVisibility(View.VISIBLE);
                    center_policy_arrow.setImageResource(SkinUtil.getResouceId(R.mipmap.icon_common_down));
                }

                break;
            case R.id.zizhiqu_policy_item:
                if(zizhiqu_policy_layout.getVisibility() == View.VISIBLE){
                    zizhiqu_policy_layout.setVisibility(View.GONE);
                    zizhiqu_policy_arrow.setImageResource(SkinUtil.getResouceId(R.mipmap.icon_common_right));
                }else{
                    zizhiqu_policy_layout.setVisibility(View.VISIBLE);
                    zizhiqu_policy_arrow.setImageResource(SkinUtil.getResouceId(R.mipmap.icon_common_down));
                }

                break;
            case R.id.city_policy_item:
                if(city_policy_layout.getVisibility() == View.VISIBLE){
                    city_policy_layout.setVisibility(View.GONE);
                    city_policy_arrow.setImageResource(SkinUtil.getResouceId(R.mipmap.icon_common_right));
                }else{
                    city_policy_layout.setVisibility(View.VISIBLE);
                    city_policy_arrow.setImageResource(SkinUtil.getResouceId(R.mipmap.icon_common_down));
                }

                break;
            case R.id.naiman_policy_item:
                if(naiman_policy_layout.getVisibility() == View.VISIBLE){
                    naiman_policy_layout.setVisibility(View.GONE);
                    naiman_policy_arrow.setImageResource(SkinUtil.getResouceId(R.mipmap.icon_common_right));
                }else{
                    naiman_policy_layout.setVisibility(View.VISIBLE);
                    naiman_policy_arrow.setImageResource(SkinUtil.getResouceId(R.mipmap.icon_common_down));
                }

                break;
            case R.id.center_xidada_policy:
                break;
            case R.id.center_gelei_policy:
                break;
            case R.id.center_file_policy:
                break;
            case R.id.zizhiqu_gelei_policy:
                break;
            case R.id.zizhiqu_file_policy:
                break;
            case R.id.city_gelei_policy:
                break;
            case R.id.city_file_policy:
                break;
            case R.id.naiman_file_policy:
                break;
            case R.id.naiman_niandu_policy:
                break;
            case R.id.naiman_tuopin_policy://
                FupinPolicyTabActivity.startIntent(this);
                break;
            case R.id.naiman_sannian_policy:
                break;

        }


    }
    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, FupinPolicyActivity.class);
        activity.startActivity(it);
    }
}
