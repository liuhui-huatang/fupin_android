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
    @BindView(R.id.naiman_fupin_yaodian)
    RelativeLayout naiman_fupin_yaodian;


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
        //习总书记关于脱贫攻坚的讲话  5
        //中央政策 各类政策         6
        //中央政策 相关文件         7
        //自治区政策- 各类政策      8
        //自治区政策  相关文件      9
        //通辽市政策  各类政策      10
        //通辽市政策 相关文件       11
        //奈曼旗政策 35610脱贫机制 12
        //奈曼旗政策 年度脱贫方案   14
        //奈曼旗政策 扶贫政策要点   15
        //奈曼旗政策 下发的其他相关文件 16

    }
    @OnClick({R.id.left_menu,
            R.id.center_policy_item,R.id.zizhiqu_policy_item,R.id.city_policy_item,R.id.naiman_policy_item,
            R.id.center_xidada_policy,R.id.center_gelei_policy,R.id.center_file_policy,
            R.id.zizhiqu_gelei_policy,R.id.zizhiqu_file_policy,
            R.id.city_policy_layout,R.id.city_file_policy,R.id.city_gelei_policy,
            R.id.naiman_tuopin_policy,R.id.naiman_sannian_policy,R.id.naiman_niandu_policy,R.id.naiman_file_policy,R.id.naiman_fupin_yaodian})
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
                FupinPolicyListActivity.startIntent(this,"5");
                break;
            case R.id.center_gelei_policy:
                FupinPolicyListActivity.startIntent(this,"6");
                break;
            case R.id.center_file_policy:
                FupinPolicyListActivity.startIntent(this,"7");
                break;
            case R.id.zizhiqu_gelei_policy:
                FupinPolicyListActivity.startIntent(this,"8");
                break;
            case R.id.zizhiqu_file_policy:
                FupinPolicyListActivity.startIntent(this,"9");
                break;
            case R.id.city_gelei_policy:
                FupinPolicyListActivity.startIntent(this,"10");
                break;
            case R.id.city_file_policy:
                FupinPolicyListActivity.startIntent(this,"11");
                break;
            case R.id.naiman_file_policy:
                FupinPolicyListActivity.startIntent(this,"16");
                break;
            case R.id.naiman_niandu_policy:
                FupinPolicyListActivity.startIntent(this,"14");
                break;
            case R.id.naiman_tuopin_policy://
                FupinPolicyListActivity.startIntent(this,"12");
                break;
            case R.id.naiman_fupin_yaodian:
                FupinPolicyListActivity.startIntent(this,"15");
                break;

        }


    }
    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, FupinPolicyActivity.class);
        activity.startActivity(it);
    }
}
