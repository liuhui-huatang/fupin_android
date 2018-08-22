package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.MyApplication;
import com.huatang.fupin.bean.Area;
import com.huatang.fupin.bean.Basic;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.MLog;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的位置显示页面
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class MyLocationActivity extends BaseActivity {


    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.title_tx)
    TextView tvTitle;
    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(R.id.rl_cun)
    RelativeLayout rlCun;


    /*
         * @ forever 在 17/5/17 下午2:28 创建
         *
         * 描述：跳转到登录页面
         *
         */
    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, MyLocationActivity.class);
        activity.startActivity(it);
    }

    /*
    * @ forever 在 17/5/17 下午2:28 创建
    *
    * 描述：页面创建时调用
    *
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfankui);
        ButterKnife.bind(this);
        tvTitle.setText("我的位置");
        initLocation();
    }

    @OnClick({R.id.left_menu, R.id.rl_cun})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.rl_cun:
                selectBasic();
                break;

        }
    }

    List<Area> list;

    public void selectBasic() {
        HttpRequest.getArea(this, new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                list = JsonUtil.toList(json, Area.class);
                if (list == null || list.size() == 0) {
                    ToastUtil.show("没有查到村");
                    return;
                }
                String[] words2 = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    words2[i] =list.get(i).getName()+":"+list.get(i).getArea_name();
                }
                DialogUIUtils.showSingleChoose(MyLocationActivity.this, "请选择当前所在的村", 0, words2, new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
                        if (TextUtils.isEmpty(text)) {
                            return;
                        }
                        double mi = getDistance(longitude, latitude, Double.parseDouble(list.get(position).getLongitude()), Double.parseDouble(list.get(position).getDimension()));
                        MLog.e("longitude1==" + longitude);
                        MLog.e("latitude1==" + latitude);
                        MLog.e("longitude2==" + list.get(position).getLongitude());
                        MLog.e("latitude2==" + list.get(position).getDimension());
                        MLog.e("距离==" + mi);

                        tvText.setText("当前位置："+mlocation
                                +"\n当前经度："+longitude+"\n当前纬度："+latitude

                                +"\n\n所在村庄："+ text
                                +"\n村庄经度："+ list.get(position).getLongitude()+"\n村庄纬度："+list.get(position).getDimension()

                                +"\n\n"+"距离偏差:"+mi+"米");
                    }
                }).show();
            }
        });
    }

    double latitude = 0;
    double longitude = 0;
    LocationClient mLocationClient;
    String mlocation = "";

    public void initLocation() {
        mLocationClient = new LocationClient(MyApplication.getContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                //获取定位结果
//                bdLocation.getTime();    //获取定位时间
//                bdLocation.getLocationID();    //获取定位唯一ID，v7.2版本新增，用于排查定位问题
//                bdLocation.getLocType();    //获取定位类型
                latitude = bdLocation.getLatitude();    //获取纬度信息
                longitude = bdLocation.getLongitude();    //获取经度信息
//                bdLocation.getRadius();    //获取定位精准度
//                bdLocation.getAddrStr();    //获取地址信息
//                bdLocation.getCountry();    //获取国家信息
//                bdLocation.getCity();    //获取城市信息
//                bdLocation.getDistrict();    //获取区县信息
//                bdLocation.getStreet();    //获取街道信息
//                bdLocation.getLocationDescribe();    //获取当前位置描述信息
//                bdLocation.getBuildingID();    //室内精准定位下，获取楼宇ID
//                bdLocation.getBuildingName();    //室内精准定位下，获取楼宇名称
//                bdLocation.getFloor();    //室内精准定位下，获取当前位置所处的楼层信息
//                list=bdLocation.getPoiList();    //获取当前位置周边POI信息

//                MLog.e("location==bdLocation.getCity()", bdLocation.getCity());
//                MLog.e("location==bdLocation.getDistrict()", bdLocation.getDistrict());
//                MLog.e("location==bdLocation.getStreet()", bdLocation.getStreet());
//                MLog.e("location==bdLocation.getBuildingName()", bdLocation.getBuildingName());
//                MLog.e("location==bdLocation.getAddrStr()", bdLocation.getAddrStr());
//                MLog.e("location==bdLocation.getAddrStr()", bdLocation.getLocationDescribe());
                mlocation = bdLocation.getAddrStr()+bdLocation.getLocationDescribe();
                if (TextUtils.isEmpty(mlocation) || "nullnull".equals(mlocation)) {
//                    tvLocation.setText("定位失败，请检查网络和定位权限是否开启");
                    mlocation = "定位失败";
                    tvText.setText("当前位置："+mlocation);
                } else {
                    tvText.setText("当前位置："+mlocation);
                    mLocationClient.stop();
                }
            }
        });
        //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span = 5000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        option.setIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
//        option.setWifiValidTime(5 * 60 * 1000);
        //可选，7.2版本新增能力，如果您设置了这个接口，首次启动定位时，会先判断当前WiFi是否超出有效期，超出有效期的话，会先重新扫描WiFi，然后再定位
        mLocationClient.setLocOption(option);
        //    第四步，开始定位
        mLocationClient.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtil.saveString("location", "");
        mLocationClient.stop();
    }


    // 返回单位是米
    public double getDistance(double longitude1, double latitude1,
                              double longitude2, double latitude2) {
        double EARTH_RADIUS = 6378137.0;
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private double rad(double d) {
        return d * Math.PI / 180.0;
    }


}
