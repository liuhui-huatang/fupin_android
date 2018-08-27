package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.bean.Archive;
import com.huatang.fupin.bean.NewBasic;
import com.huatang.fupin.bean.NewPoor;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DanganBasciActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.title_tx)
    TextView tvTitle;

    @BindView(R.id.tv_shuxing)
    TextView tvShuxing;
    @BindView(R.id.tv_tuopin)
    TextView tvTuopin;
    @BindView(R.id.tv_year)
    TextView    tvYear;
    @BindView(R.id.tv_water_safe)
    TextView    tv_water_safe;
    @BindView(R.id.tv_water_diff)
    TextView    tv_water_diff;
    @BindView(R.id.tv_has_elect)
    TextView    tv_has_elect;
    @BindView(R.id.tv_shengchan_elect)
    TextView	tv_shengchan_elect;
    @BindView(R.id.tv_ranliao_type )
    TextView	tv_ranliao_type ;
    @BindView(R.id.tv_juli_cun )
    TextView	tv_juli_cun ;
    @BindView(R.id.tv_banqian )
    TextView	tv_banqian ;
    @BindView(R.id.tv_banqian_way)
    TextView	tv_banqian_way;
    @BindView(R.id.tv_anzhi_way )
    TextView	tv_anzhi_way ;
    @BindView(R.id.tv_anzhi_place)
    TextView	tv_anzhi_place ;
    @BindView(R.id.tv_join_hezuoshe)
    TextView	tv_join_hezuoshe ;
    @BindView(R.id.tv_chuoxue )
    TextView	tv_chuoxue ;
    @BindView(R.id.tv_banqian_question )
    TextView	tv_banqian_question ;
    @BindView(R.id.tv_family_num )
    TextView	tv_family_num ;
    @BindView(R.id.tv_family_cunzai )
    TextView	tv_family_cunzai ;
    @BindView(R.id.tv_water_way )
    TextView	tv_water_way ;
    @BindView(R.id.tv_water_photo  )
    TextView	tv_water_photo ;
    @BindView(R.id.tv_main_info )
    TextView	tv_main_info ;
    @BindView(R.id.tv_zhipin_imgs )
    TextView	tv_zhipin_imgs ;
    @BindView(R.id.tv_main )
    TextView	tv_main ;
    @BindView(R.id.tv_other)
    TextView	tv_other ;
    private Archive archive;
    private NewBasic basic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangan_basic);
        ButterKnife.bind(this);
        archive = (Archive) getIntent().getSerializableExtra("archive");
        basic = archive.getBasic()!= null ? archive.getBasic() : new NewBasic();
        tvTitle.setText("基本信息");
        initData();
    }
    public static void startIntent(Activity activity, Archive archive) {
        Intent it = new Intent(activity, DanganBasciActivity.class);
        it.putExtra("archive", archive);
        activity.startActivity(it);
    }
    private void initData() {
        if(basic == null || !basic.isIshave()){
            return;
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
        if (basic != null && basic.getYear() != null) {
            tvYear.setText(basic.getYear());
        }
        tv_water_safe.setText(basic.getIs_water_security());
        tv_water_diff.setText(basic.getIs_water_difficulty());
        tv_has_elect.setText(basic.getIs_life_electricity());
        tv_shengchan_elect.setText(basic.getIs_produced_electricity());
        tv_ranliao_type.setText(basic.getFuel_type());
        tv_juli_cun.setText(basic.getDistance());
        tv_banqian.setText(basic.getIs_relocation());
        tv_banqian_way.setText(basic.getOut_way());
        tv_anzhi_way.setText(basic.getPlacement_state());
        tv_anzhi_place.setText(basic.getPlacement_state());
        tv_join_hezuoshe.setText(basic.getIs_poor_cooperation());
        tv_chuoxue.setText(basic.getIs_outschool());
       // tv_banqian_question.setText(basic.getMine_difficulty());
        tv_family_num.setText(basic.getFamily_num());
        tv_family_cunzai.setText(basic.getFamily_state());
        tv_water_way.setText(basic.getWay_water());
        //tv_water_photo.setText(basic.getWay_water_file().size() >0 ? "查看>>" :"未录入");
        tv_water_photo.setOnClickListener(this);
        tv_main_info.setText(basic.getMain_pcause_info());
        tv_zhipin_imgs .setText(basic.getMain_path().size() > 0 ? "查看>>" :"未录入");
        tv_main_info.setOnClickListener(this);
        tv_other.setText(basic.getSecondary_pcause_info());


        if (basic != null && basic.getMain_pcause() != null) {
            //主要致贫原因 1.因病 2.因残 3.因学 4.因灾 5.缺土地 6.缺水 7.缺技术 8.缺劳动力 9.缺资金 10.交通条件落后 11.自身发展动力不足
            if (TextUtils.isEmpty(basic.getMain_pcause())) {
                tv_main.setText("致贫原因");//主要致贫原因: 1.因病 2.因残 3.因学 4.因灾 5.缺土地 6.缺水 7.缺技术 8.缺劳动力 9.缺资金 10.交通条件落后 11.自身发展动力不足',
            } else {
                switch (basic.getMain_pcause()) {
                    case "0":
                        tv_main.setText("致贫原因");
                        break;
                    case "1":
                        tv_main.setText("因病");
                        break;
                    case "2":
                        tv_main.setText("因残");
                        break;
                    case "3":
                        tv_main.setText("因学");
                        break;
                    case "4":
                        tv_main.setText("因灾");
                        break;
                    case "5":
                        tv_main.setText("缺土地");
                        break;
                    case "6":
                        tv_main.setText("缺水");
                        break;
                    case "7":
                        tv_main.setText("缺技术");
                        break;
                    case "8":
                        tv_main.setText("缺劳动力");
                        break;
                    case "9":
                        tv_main.setText("缺资金");
                        break;
                    case "10":
                        tv_main.setText("交通条件落后");
                        break;
                    case "11":
                        tv_main.setText("自身发展动力不足");
                        break;
                }
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


    }
    @OnClick({R.id.left_menu, R.id.tv_water_photo,R.id.tv_zhipin_imgs})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.tv_water_photo:
               // List photos = basic.getWay_water_file();
                break;
            case R.id.tv_zhipin_imgs:
                List<String> imgs = basic.getMain_path();
                if(imgs!=null && imgs.size()>0){
                    ImageViewPageActivity.startIntent(this,imgs,"");
                }
                break;

        }
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
    public void onClick(View v) {

    }
}
