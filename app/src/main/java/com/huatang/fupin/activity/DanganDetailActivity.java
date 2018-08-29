package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.bean.Archive;
import com.huatang.fupin.bean.Fund;
import com.huatang.fupin.bean.Info;
import com.huatang.fupin.bean.NewBasic;
import com.huatang.fupin.bean.NewFamily;
import com.huatang.fupin.bean.NewPoor;
import com.huatang.fupin.bean.NewRevenue;
import com.huatang.fupin.bean.Policy;
import com.huatang.fupin.http.NewHttpRequest;
import com.huatang.fupin.utils.GlideUtils;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.ToastUtil;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DanganDetailActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.left_menu)
    ImageView leftMenu;

    @BindView(R.id.title_tx)
    TextView tvTitle;
    @BindView(R.id.dangan_info_layout)
    RelativeLayout dangan_info_layout;

    @BindView(R.id.dangan_detail_layout)
    LinearLayout dangan_detail_layout;

    @BindView(R.id.jiben_layout)
    RelativeLayout jiben_layout;

    @BindView(R.id.family_layout)
    RelativeLayout family_layout;

    @BindView(R.id.shenghuo_layout)
    RelativeLayout shenghuo_layout;

    @BindView(R.id.shouru_layout)
    RelativeLayout shouru_layout;

    @BindView(R.id.ziyuan_layout)
    RelativeLayout ziyuan_layout;

    @BindView(R.id.cuoshi_layout)
    RelativeLayout cuoshi_layout;

    @BindView(R.id.zijin_layout)
    RelativeLayout zijin_layout;
    @BindView(R.id.poor_dangan_address)
    TextView  poor_dangan_address;
    @BindView(R.id.poor_dangan_fcard)
    TextView  poor_dangan_fcard;
    @BindView(R.id.poor_dangan_name)
    TextView  poor_dangan_name;
    @BindView(R.id.poor_dangan_year)
    TextView  poor_dangan_year;
    @BindView(R.id.iv_photo)
    ImageView iv_photo;
    @BindView(R.id.pingjia_layout)
    RelativeLayout pingjia_layout;
    @BindView(R.id.right_tx_menu)
    TextView rightMenu;
    private Archive archive;
    private NewPoor poor;
    private String year;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangan_detail);
        ButterKnife.bind(this);
        initHeadView();
        initData();
    }

    private void initData() {
        year = TextUtils.isEmpty(SPUtil.getString(Config.YEAR))?   String.valueOf(Calendar.getInstance().get(Calendar.YEAR)) : SPUtil.getString(Config.YEAR);
        getArchive();
    }

    private void initHeadView() {
        tvTitle.setText("贫困户信息");
        dangan_info_layout.setVisibility(View.GONE);
        dangan_detail_layout.setVisibility(View.VISIBLE);
        leftMenu.setOnClickListener(this);
        rightMenu.setText("切换年限");
        rightMenu.setVisibility(View.VISIBLE);
        rightMenu.setOnClickListener(this);
    }
    private void getArchive(){
        NewPoor poor = (NewPoor) SPUtil.getObject(Config.PENKUNHU_KEY);
        if(poor != null ){
            DialogUIUtils.showTie(this, "加载中...");
            NewHttpRequest.getArchivesWithFcard(this,poor.getFcard(),year,new NewHttpRequest.MyCallBack(){
                @Override
                public void ok(String json) {

                    archive = JsonUtil.json2Bean(json,Archive.class);
                    DialogUIUtils.dismssTie();
                    if(archive == null){
                        return;
                    }
                    initView();
                }
                @Override
                public void no(String msg) {
                    DialogUIUtils.dismssTie();
                    ToastUtil.show(msg);
                }
            });
        }else{
            ToastUtil.show("系统出错了，请重新登录");
        }
    }

    private void initView() {
        poor = archive.getPoor();
        pingjia_layout.setVisibility(View.VISIBLE);
        pingjia_layout.setOnClickListener(this);
        GlideUtils.LoadCircleImageWithoutBorderColor(this, poor.getPhoto(),iv_photo);
        poor_dangan_fcard.setText(poor.getFcard());
        poor_dangan_name.setText(poor.getFname());
        poor_dangan_address.setText(poor.getCity() +poor.getVillage_name());
        if(archive.getBasic() != null && archive.getBasic().isIshave()){
            poor_dangan_year.setText(archive.getBasic().getYear());
        }
        jiben_layout.setOnClickListener(this);
        List<NewFamily> families = archive.getFamily();
        if(families != null && families.size()>0){
            family_layout.setVisibility(View.VISIBLE);
        }else{
            family_layout.setVisibility(View.GONE);
        }
        family_layout.setOnClickListener(this);
        NewBasic newBasic = archive.getBasic();
        if(newBasic.isIshave()){
            shenghuo_layout.setVisibility(View.VISIBLE);
        }else{
            shenghuo_layout.setVisibility(View.GONE);
        }
        shenghuo_layout.setOnClickListener(this);
        NewRevenue revenue = archive.getReve();
        if(revenue.isIshave()){
            shouru_layout.setVisibility(View.VISIBLE);

        }else{
            shouru_layout.setVisibility(View.GONE);
        }
        shouru_layout.setOnClickListener(this);
        Info info = archive.getInfo();
        if(info.isIshave()){
            ziyuan_layout.setVisibility(View.VISIBLE);

        }else{
            ziyuan_layout.setVisibility(View.GONE);
        }
        ziyuan_layout.setOnClickListener(this);
        List<Policy> policy = archive.getPolicy();
        if(policy != null && policy.size() >0){
            cuoshi_layout.setVisibility(View.VISIBLE);
        }else{
            cuoshi_layout.setVisibility(View.GONE);
        }
        cuoshi_layout.setOnClickListener(this);
        List<Fund> fund = archive.getFunds();
        if(fund != null && fund.size() >0 ){
            zijin_layout.setVisibility(View.VISIBLE);
        }else{
            zijin_layout.setVisibility(View.GONE);
        }
        zijin_layout.setOnClickListener(this);


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
        switch (v.getId()){
            case R.id.left_menu:
                finish();
                break;
            case R.id.right_tx_menu:
                String[] words2 = new String[5];
                words2[0] = "2016";
                words2[1] = "2017";
                words2[2] = "2018";
                words2[3] = "2019";
                words2[4] = "2020";

                DialogUIUtils.showSingleChoose(DanganDetailActivity.this, "请选择年度", -1, words2, new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {

                        if (TextUtils.isEmpty(text)) {
                            return;
                        }
                        year = text.toString();
                        SPUtil.saveString("year",year);
                        getArchive();

                    }
                }).show();
                break;
            case R.id.jiben_layout:
                DanganJibenActivity.startIntent(this,archive);
                break;
            case R.id.family_layout:
                DanganJiatingActivity.startIntent(this,archive);
                break;
            case R.id.shenghuo_layout:
                DanganBasciActivity.startIntent(this,archive);
                break;
            case R.id.shouru_layout:
                DanganShouruActivity.startIntent(this,archive);
                break;
            case R.id.ziyuan_layout:
                DanganInfoActivity.startIntent(this,archive);
                break;
            case R.id.cuoshi_layout:
                DanganPolicyActivity.startIntent(this,archive);
                break;
            case R.id.zijin_layout:
                DanganFundActivity.startIntent(this,archive);
                break;
            case R.id.pingjia_layout:
                DanganGanbuActivity.startIntent(this, archive);
                break;
        }
    }
    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, DanganDetailActivity.class);
        activity.startActivity(it);
    }
}
