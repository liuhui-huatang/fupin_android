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
import com.huatang.fupin.bean.Basic;

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
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_card)
    TextView tvCard;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_family)
    TextView tvFamily;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_cuoshi)
    TextView tvCuoshi;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.tv_main)
    TextView tvMain;
    @BindView(R.id.tv_outh)
    TextView tvOuth;
    @BindView(R.id.tv_tuopin)
    TextView tvTuopin;
    @BindView(R.id.tv_zhipin)
    TextView tvZhipin;
    @BindView(R.id.tv_shuxing)
    TextView tvShuxing;
    @BindView(R.id.tv_open)
    TextView tvOpen;

    /*
             * @ forever 在 17/5/17 下午2:28 创建
             *
             * 描述：跳转到登录页面
             *
             */
    public static void startIntent(Activity activity, Basic bean) {
        Intent it = new Intent(activity, DanganJibenActivity.class);
        it.putExtra("basic", bean);
        activity.startActivity(it);
    }

    /*
    * @ forever 在 17/5/17 下午2:28 创建
    *
    * 描述：页面创建时调用
    *
    */
    Basic bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danganxinxi);
        ButterKnife.bind(this);
        bean = (Basic) getIntent().getSerializableExtra("basic");
        initData();
    }

    public void initData() {

        if (bean != null && bean.getFname() != null) {
            tvName.setText(bean.getFname());
        }

        if (bean != null && bean.getSex() != null) {

            tvSex.setText("1".equals(bean.getSex()) ? "男" : "女");
        }

        if (bean != null && bean.getAge() != null) {

            tvAge.setText(bean.getAge());
        }

        if (bean != null && bean.getFcard() != null) {

            String str = bean.getFcard().replace(bean.getFcard().substring(4, 14), "**********");
            tvCard.setText(str);
        }

        if (bean != null && bean.getFphone() != null) {

            tvPhone.setText(bean.getFphone());
        }

        if (bean != null && bean.getFamily_num() != null) {

            tvFamily.setText(bean.getFamily_num() + "口人");
        }

        if (bean != null && bean.getTown_name() != null) {

            tvAddress.setText(bean.getTown_name() + bean.getVillage_name());
        }

        if (bean != null && bean.getHelp_measures() != null) {

            switch (bean.getHelp_measures()) {
                case "1":
                    tvCuoshi.setText("大病救治");
                    break;
                case "2":
                    tvCuoshi.setText("社会保障兜底");
                    break;
                case "3":
                    tvCuoshi.setText("产业发展转移就业");
                    break;
                case "4":
                    tvCuoshi.setText("易地扶贫搬迁");
                    break;
                case "5":
                    tvCuoshi.setText("生态补偿");
                    break;
                case "6":
                    tvCuoshi.setText("教育扶贫");
                    break;
                default:
                    tvCuoshi.setText("未录入");
                    break;
            }
        }


        if (bean != null && bean.getYear() != null) {
            tvYear.setText(bean.getYear());
        }

        if (bean != null && bean.getMain_pcause_pcause_info() != null) {
            tvMain.setText(bean.getMain_pcause_pcause_info());
        }

        if (bean != null && bean.getSecondary_pcause_info() != null) {
            tvOuth.setText(bean.getSecondary_pcause_info());
        }


        //贫困户属性: 1一般贫困户 2.低保兜底户 3.五保户 4.一般农户
        if (TextUtils.isEmpty(bean.getFamily_state())) {
            tvShuxing.setText("一般贫困户");
        } else {
            switch (bean.getFamily_state()) {
                case "0":
                    tvShuxing.setText("一般贫困户");
                    break;
                case "1":
                    tvShuxing.setText("一般贫困户");
                    break;
                case "2":
                    tvShuxing.setText("低保兜底户");
                    break;
                case "3":
                    tvShuxing.setText("五保户");
                    break;
                case "4":
                    tvShuxing.setText("一般农户");
                    break;
            }
        }

        // 0:未脱贫 1:预脱贫 2:已脱贫 3:注销 4:返贫
        if (TextUtils.isEmpty(bean.getOut_poor_state())) {
            tvTuopin.setText("未脱贫");
        } else {
            switch (bean.getOut_poor_state()) {
                case "0":
                    tvTuopin.setText("未脱贫");
                    break;
                case "1":
                    tvTuopin.setText("预脱贫");
                    break;
                case "2":
                    tvTuopin.setText("已脱贫");
                    break;
                case "3":
                    tvTuopin.setText("注销");
                    break;
                case "4":
                    tvTuopin.setText("返贫");
                    break;
            }
        }


        //主要致贫原因 1.因病 2.因残 3.因学 4.因灾 5.缺土地 6.缺水 7.缺技术 8.缺劳动力 9.缺资金 10.交通条件落后 11.自身发展动力不足
        if (TextUtils.isEmpty(bean.getMain_pcause())) {
//            tvZhipin.setText("致贫原因");
        } else {
            switch (bean.getMain_pcause()) {
                case "0":
//                    tvZhipin.setText("致贫原因");
                    break;
                case "1":
                    tvZhipin.setText("因病");
                    break;
                case "2":
                    tvZhipin.setText("因残");
                    break;
                case "3":
                    tvZhipin.setText("因学");
                    break;
                case "4":
                    tvZhipin.setText("因灾");
                    break;
                case "5":
                    tvZhipin.setText("缺土地");
                    break;
                case "6":
                    tvZhipin.setText("缺水");
                    break;
                case "7":
                    tvZhipin.setText("缺技术");
                    break;
                case "8":
                    tvZhipin.setText("缺劳动力");
                    break;
                case "9":
                    tvZhipin.setText("缺资金");
                    break;
                case "10":
                    tvZhipin.setText("交通条件落后");
                    break;
                case "11":
                    tvZhipin.setText("自身发展动力不足");
                    break;
            }
        }

    }


    @OnClick({R.id.left_menu, R.id.tv_open})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.tv_open:
                DanganJibenImageActivity.startIntent(DanganJibenActivity.this, bean);
                break;
        }
    }
}
