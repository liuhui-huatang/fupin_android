package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.bean.Archive;
import com.huatang.fupin.bean.Info;
import com.huatang.fupin.bean.NewBasic;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DanganInfoActivity extends BaseActivity {
    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.title_tx)
    TextView tvTitle;
    @BindView(R.id.tv_zhufang)
    TextView tv_zhufang;
    @BindView(R.id.tv_jiegou)
    TextView tv_jiegou;
    @BindView(R.id.tv_mianji)
    TextView tv_mianji;
    @BindView(R.id.tv_anquan)
    TextView tv_anquan;
    @BindView(R.id.tv_house_photo)
    TextView tv_house_photo;
    @BindView(R.id.tv_youxiao_guangai)
    TextView tv_youxiao_guangai;
    @BindView(R.id.tv_gendi_mianji)
    TextView tv_gendi_mianji;
    @BindView(R.id.tv_lindi_mianji)
    TextView tv_lindi_mianji;
    @BindView(R.id.tv_tuigen_huanlin)
    TextView tv_tuigen_huanlin;
    @BindView(R.id.tv_linguo_mianji)
    TextView tv_linguo_mianji;
    @BindView(R.id.tv_mucao_mianji)
    TextView tv_mucao_mianji;
    @BindView(R.id.tv_shuimian_mianji)
    TextView tv_shuimian_mianji;
    @BindView(R.id.tv_ziyuan_photo)
    TextView tv_ziyuan_photo;
    @BindView(R.id.tv_yangzhi)
    TextView tv_yangzhi;
    @BindView(R.id.tv_yangzhi_photo)
    TextView tv_yangzhi_photo;
    @BindView(R.id.tv_yunshu_car)
    TextView tv_yunshu_car;
    @BindView(R.id.tv_yunshu_car_photo)
    TextView tv_yunshu_car_photo;
    @BindView(R.id.tv_jiaotong)
    TextView tv_jiaotong;
    @BindView(R.id.tv_jiaotong_photo)
    TextView tv_jiaotong_photo;
    @BindView(R.id.tv_jiayong)
    TextView tv_jiayong;
    @BindView(R.id.tv_jiayong_photo)
    TextView tv_jiayong_photo;







    private Archive archive;
    private Info info;
    private String fcard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangan_info);
        ButterKnife.bind(this);
        archive = (Archive) getIntent().getSerializableExtra("archive");
        if(archive == null){
            return;
        }
        fcard = archive.getPoor().getFcard();
        info = archive.getInfo();
        tvTitle.setText("资源信息");
        initData();
    }
    private void initData() {
        if(info == null || !info.isIshave()){
            return;
        }
        tv_zhufang.setText(info.getIs_have_house());
        tv_jiegou.setText(info.getHouse_structure());
        tv_mianji.setText(info.getHouse_area());
        tv_anquan.setText(info.getHouse_security());
        tv_house_photo.setText(info.getHouse_photo()!= null && info.getHouse_photo().size() >0 ? "查看>>" : "未录入");
        tv_youxiao_guangai.setText(info.getWater_surface_area());
        tv_gendi_mianji.setText(info.getPloughing_area());
        tv_lindi_mianji.setText(info.getWoodland_area());
        tv_tuigen_huanlin.setText(info.getReforestation_area());
        tv_linguo_mianji.setText(info.getFruit_area());
        tv_mucao_mianji.setText(info.getGrass_area());
        tv_shuimian_mianji.setText(info.getWater_surface_area());
        tv_ziyuan_photo.setText(info.getResource_photo()!= null && info.getResource_photo().size() >0 ? "查看>>" : "未录入");
        tv_yangzhi.setText(info.getBreeding());
        tv_yangzhi_photo.setText(info.getBreeding_photo()!= null && info.getBreeding_photo().size() >0 ? "查看>>" : "未录入");
        tv_yunshu_car.setText(info.getTransport());
        tv_yunshu_car_photo.setText(info.getTransport_photo().size()>0 ? "查看>>" :"未录入");
        tv_jiaotong.setText(info.getVehicle());
        tv_jiaotong_photo.setText(info.getVehicle_photo().size()>0 ? "查看>>" :"未录入");
        tv_jiayong.setText(info.getElectric());
        tv_jiayong_photo.setText(info.getElectric_photo().size()>0 ? "查看>>" :"未录入");
    }
    @OnClick({R.id.left_menu, R.id.tv_house_photo,R.id.tv_ziyuan_photo,R.id.tv_yangzhi_photo,R.id.tv_yunshu_car_photo,R.id.tv_jiaotong_photo,R.id.tv_jiayong_photo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.tv_house_photo:
                List<String> house_photo = info.getHouse_photo();
                if(house_photo!=null && house_photo.size()>0){
                    ImageViewPageActivity.startIntent(this,house_photo, Config.DANGAN_HOUSE,fcard);
                }
                break;
            case R.id.tv_ziyuan_photo:
                List<String> ziyuan_photo = info.getResource_photo();
                if(ziyuan_photo!=null && ziyuan_photo.size()>0){
                    ImageViewPageActivity.startIntent(this,ziyuan_photo,Config.DANGAN_RESOUCE,fcard);
                }
                break;
            case R.id.tv_yangzhi_photo:
                List<String> breed_photo = info.getBreeding_photo();
                if(breed_photo!=null && breed_photo.size()>0){
                    ImageViewPageActivity.startIntent(this,breed_photo,Config.DANGAN_BLEED,fcard);
                }
                break;
            case R.id.tv_yunshu_car_photo:
                List<String> transport_photo =  info.getTransport_photo();
                if(transport_photo!=null && transport_photo.size()>0){
                    ImageViewPageActivity.startIntent(this,transport_photo,Config.DANGAN_TRANSPORT,fcard);
                }
                break;
            case R.id.tv_jiaotong_photo:
                List<String> vehicle_photo = info.getVehicle_photo();
                if(vehicle_photo!=null && vehicle_photo.size()>0){
                    ImageViewPageActivity.startIntent(this,vehicle_photo,Config.DANGAN_VERICH,fcard);
                }
                break;
            case R.id.tv_jiayong_photo:
                List<String> elect_photo = info.getElectric_photo();
                if(elect_photo!=null && elect_photo.size()>0){
                    ImageViewPageActivity.startIntent(this,elect_photo,Config.DANGAN_VERICH,fcard);
                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            List<String> photolist = (List<String>) data.getSerializableExtra("photoList");
            switch (requestCode){
                case  Config.DANGAN_HOUSE:
                    info.setHouse_photo(photolist);
                    break;
                case Config.DANGAN_RESOUCE:
                    info.setResource_photo(photolist);
                    break;
                case Config.DANGAN_BLEED:
                    info.setBreeding_photo(photolist);
                    break;
                case Config.DANGAN_ELECTOR:
                    info.setElectric_photo(photolist);
                    break;
                case Config.DANGAN_TRANSPORT:
                    info.setTransport_photo(photolist);
                    break;
                case Config.DANGAN_VERICH:
                    info.setVehicle_photo(photolist);
                    break;
            }

        }

    }

    public static void startIntent(Activity activity, Archive archive) {
        Intent it = new Intent(activity, DanganInfoActivity.class);
        it.putExtra("archive", archive);
        activity.startActivity(it);
    }
}
