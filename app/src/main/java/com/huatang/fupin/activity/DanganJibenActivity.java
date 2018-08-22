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
    private NewPoor poor;
    private NewBasic basic;

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
        basic = archive.getBasic()!= null ? archive.getBasic() : new NewBasic();
        tvTitle.setText("基本信息");
        initData();
    }


    public void initData() {

        if (poor != null && poor.getFname()!= null) {
            tvName.setText(poor.getFname());
        }

        if (poor != null && poor.getSex() != null) {

            tvSex.setText("1".equals(poor.getSex()) ? "男" : "女");
        }

        if (poor != null && poor.getAge() != null) {

            tvAge.setText(poor.getAge());
        }

        if (poor != null && poor.getFcard() != null) {

            String str = poor.getFcard().replace(poor.getFcard().substring(4, 14), "**********");
            tvCard.setText(str);
        }

        if (poor != null && poor.getFphone() != null) {

            tvPhone.setText(poor.getFphone());
        }

        if (poor != null && basic.getFamily_num() != null) {

            tvFamily.setText(basic.getFamily_num() + "口人");
        }

        if (poor != null && poor.getTown_name() != null) {

            tvAddress.setText(poor.getTown_name() + poor.getVillage_name());
        }

        if (basic != null && basic.getMain_pcause() != null) {
            //主要致贫原因 1.因病 2.因残 3.因学 4.因灾 5.缺土地 6.缺水 7.缺技术 8.缺劳动力 9.缺资金 10.交通条件落后 11.自身发展动力不足
            if (TextUtils.isEmpty(basic.getMain_pcause())) {
                tvCuoshi.setText("致贫原因");//主要致贫原因: 1.因病 2.因残 3.因学 4.因灾 5.缺土地 6.缺水 7.缺技术 8.缺劳动力 9.缺资金 10.交通条件落后 11.自身发展动力不足',
            } else {
                switch (basic.getMain_pcause()) {
                    case "0":
                        tvCuoshi.setText("致贫原因");
                        break;
                    case "1":
                        tvCuoshi.setText("因病");
                        break;
                    case "2":
                        tvCuoshi.setText("因残");
                        break;
                    case "3":
                        tvCuoshi.setText("因学");
                        break;
                    case "4":
                        tvCuoshi.setText("因灾");
                        break;
                    case "5":
                        tvCuoshi.setText("缺土地");
                        break;
                    case "6":
                        tvCuoshi.setText("缺水");
                        break;
                    case "7":
                        tvCuoshi.setText("缺技术");
                        break;
                    case "8":
                        tvCuoshi.setText("缺劳动力");
                        break;
                    case "9":
                        tvCuoshi.setText("缺资金");
                        break;
                    case "10":
                        tvCuoshi.setText("交通条件落后");
                        break;
                    case "11":
                        tvCuoshi.setText("自身发展动力不足");
                        break;
                }
            }

        }


        if (basic != null && basic.getYear() != null) {
            tvYear.setText(basic.getYear());
        }

        if (basic != null && basic.getMain_pcause_info() != null) {
            tvMain.setText(basic.getMain_pcause_info());
        }

        if (basic != null && basic.getSecondary_pcause_info() != null) {
            tvOuth.setText(basic.getSecondary_pcause_info());
        }


        //贫困户属性: 1一般贫困户 2.低保兜底户 3.五保户 4.一般农户
        if (TextUtils.isEmpty(basic.getFamily_state())) {
            //tvShuxing.setText("一般贫困户");
        } else {
            switch (basic.getFamily_state()) {
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


        if (TextUtils.isEmpty(basic.getOut_poor_state())) {
            tvTuopin.setText("脱贫状态");//1贫困户 2正常脱贫户  3返贫户  4:新识别贫困户  5:稳定脱贫户 6清退户',
        } else {
            switch (basic.getOut_poor_state()) {
                case "0":
                    tvTuopin.setText("脱贫状态：无");
                    break;
                case "1":
                    tvTuopin.setText("脱贫状态：贫困户");
                    break;
                case "2":
                    tvTuopin.setText("脱贫状态：正常脱贫户");
                    break;
                case "3":
                    tvTuopin.setText("脱贫状态：返贫户");
                    break;
                case "4":
                    tvTuopin.setText("脱贫状态：新识别贫困户");
                    break;
                case "5":
                    tvTuopin.setText("脱贫状态：稳定脱贫户");
                case "6":
                    tvTuopin.setText("脱贫状态：清退户");
                    break;
            }
        }

        //主要致贫原因 1.因病 2.因残 3.因学 4.因灾 5.缺土地 6.缺水 7.缺技术 8.缺劳动力 9.缺资金 10.交通条件落后 11.自身发展动力不足
        if (TextUtils.isEmpty(basic.getMain_pcause())) {
//            tvZhipin.setText("致贫原因");
        } else {
            switch (basic.getMain_pcause()) {
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
                //DanganJibenImageActivity.startIntent(DanganJibenActivity.this, poor);
                break;
        }
    }
}
