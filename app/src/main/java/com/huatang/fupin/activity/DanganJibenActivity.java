package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.bean.Archive;
import com.huatang.fupin.bean.Basic;
import com.huatang.fupin.bean.NewBasic;
import com.huatang.fupin.bean.NewPoor;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 贫困户基本情况
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class DanganJibenActivity extends BaseActivity {


    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.title_tx)
    TextView tvTitle;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sex)
    TextView tvSex;

    @BindView(R.id.tv_card)
    TextView tvCard;
    @BindView(R.id.tv_phone)
    TextView tvPhone;


    @BindView(R.id.tv_jiashu)
    TextView tv_jiashu;
    @BindView(R.id.tv_hubianhao)
    TextView tv_hubianhao;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_group)
    TextView tv_group;
    @BindView(R.id.tv_bank_name)
    TextView tv_bank_name;
    @BindView(R.id.tv_bank_card)
    TextView tv_bank_card;
    @BindView(R.id.tv_bianqian)
    TextView tv_bianqian;
     private NewPoor poor;

    /*
             * @ forever 在 17/5/17 下午2:28 创建
             *
             * 描述：跳转到登录页面
             *
             */
    public static void startIntent(Activity activity, Archive archive) {
        Intent it = new Intent(activity, DanganJibenActivity.class);
        it.putExtra("archive", archive);
        activity.startActivity(it);
    }

    /*
    * @ forever 在 17/5/17 下午2:28 创建
    *
    * 描述：页面创建时调用
    *
    */
    Archive archive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danganxinxi);
        ButterKnife.bind(this);
        archive = (Archive) getIntent().getSerializableExtra("archive");
        poor = archive.getPoor();
        tvTitle.setText("基本信息");
        initData();
    }


    public void initData() {
        if(poor == null || !poor.isIshave()){
            return;
        }

        if (poor != null && poor.getFname()!= null) {
            tvName.setText(poor.getFname());
        }
        if (poor != null && poor.getSex() != null) {

            tvSex.setText("1".equals(poor.getSex()) ? "男" : "女");
        }



        if (poor != null && poor.getFcard() != null) {

            String str = poor.getFcard().replace(poor.getFcard().substring(4, 14), "**********");
            tvCard.setText(str);
        }

        if (poor != null && poor.getFphone() != null) {

            tvPhone.setText(poor.getFphone());
        }

        if (poor != null && poor.getTown_name() != null) {

            tvAddress.setText(poor.getTown_name() + poor.getVillage_name());
        }

        tv_jiashu.setText(poor.getIs_trong_family());
        tv_bank_card.setText(poor.getBank_car());
        tv_bank_name.setText(poor.getBank_name());
        tv_bianqian.setText(poor.getIs_mine());
        tv_group.setText(poor.getVillage_group());
        tv_hubianhao.setText(poor.getHome_number());
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
