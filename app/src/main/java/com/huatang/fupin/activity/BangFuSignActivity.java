package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.app.MyApplication;
import com.huatang.fupin.bean.Area;
import com.huatang.fupin.bean.Basic;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.utils.DateUtil;
import com.huatang.fupin.utils.GlideUtils;
import com.huatang.fupin.utils.ImageUtil;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.MLog;
import com.huatang.fupin.utils.PopWindowUtil;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 帮扶日志添加页面
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class BangFuSignActivity extends BaseActivity {


    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.right_menu)
    TextView rightMenu;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_text)
    EditText etText;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.iv_sign_1)
    ImageView ivSign1;
    @BindView(R.id.iv_sign_2)
    ImageView ivSign2;
    @BindView(R.id.iv_sign_3)
    ImageView ivSign3;
    @BindView(R.id.iv_sign_4)
    ImageView ivSign4;
    @BindView(R.id.iv_sign_add1)
    ImageView ivSignAdd1;
    @BindView(R.id.iv_sign_6)
    ImageView ivSign6;
    @BindView(R.id.iv_sign_7)
    ImageView ivSign7;
    @BindView(R.id.iv_sign_8)
    ImageView ivSign8;
    @BindView(R.id.iv_sign_9)
    ImageView ivSign9;
    @BindView(R.id.iv_sign_add_2)
    ImageView ivSignAdd2;
    @BindView(R.id.rl_location)
    RelativeLayout rlLocation;
    @BindView(R.id.tv_basic)
    TextView tvBasic;
    @BindView(R.id.rl_basic)
    RelativeLayout rlBasic;
    @BindView(R.id.ll_sign)
    LinearLayout llSign;

    /*
         * @ forever 在 17/5/17 下午2:28 创建
         *
         * 描述：跳转到登录页面
         *
         */
    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, BangFuSignActivity.class);
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
        setContentView(R.layout.activity_sign);
        ButterKnife.bind(this);
        initImageView();
        initLocation();
    }


    @OnClick({R.id.left_menu, R.id.right_menu, R.id.tv_location, R.id.iv_sign_add1, R.id.iv_sign_add_2, R.id.rl_location, R.id.rl_basic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.rl_location:

                break;
            case R.id.rl_basic:
                selectBasic();
                break;
            case R.id.right_menu:
                rightMenu.setClickable(false);
                String title = etTitle.getText().toString().trim();
                String text = etText.getText().toString().trim();
                String location = tvLocation.getText().toString().trim();
                String basic = tvBasic.getText().toString().trim();
                if (TextUtils.isEmpty(title)) {
                    ToastUtil.show("标题未填写");
                    rightMenu.setClickable(true);
                    return;
                }
                if (title.length() < 2) {
                    ToastUtil.show("标题不得少于2个字");
                    rightMenu.setClickable(true);
                    return;
                }

                if (TextUtils.isEmpty(text)) {
                    ToastUtil.show("内容暂未填写");
                    rightMenu.setClickable(true);
                    return;
                }
                if (text.length() < 5) {
                    ToastUtil.show("内容不得少于5个字");
                    rightMenu.setClickable(true);
                    return;
                }

                if (TextUtils.isEmpty(location)) {
                    ToastUtil.show("定位信息不全");
                    rightMenu.setClickable(true);
                    return;
                }
                if (imagePathList.size() == 0) {
                    ToastUtil.show("请先选择图片");
                    rightMenu.setClickable(true);
                    return;
                }
                if (TextUtils.isEmpty(basic)) {
                    ToastUtil.show("请选择帮扶户");
                    rightMenu.setClickable(true);
                    return;
                }

                uploadSign(title, text, location, latitude + "", longitude + "", basic);
                break;

            case R.id.iv_sign_add1:
            case R.id.iv_sign_add_2:
                hideKeyboard();
                showSelectPicture();
                break;
        }
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && this.getCurrentFocus() != null) {
            if (this.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    List<Basic> basiclist;
    int mPosition=-1;

    public void selectBasic() {
        HttpRequest.getBasic(this, SPUtil.getString("id"),SPUtil.getString("year"), new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                basiclist = JsonUtil.toList(json, Basic.class);
                if (basiclist == null || basiclist.size() == 0) {
                    ToastUtil.show("没有帮扶户");
                    selectArea();
                } else {
                    String[] words2 = new String[basiclist.size()];
                    for (int i = 0; i < basiclist.size(); i++) {
                        words2[i] = basiclist.get(i).getFname() == null ? " " : basiclist.get(i).getFname();
                    }
                    DialogUIUtils.showSingleChoose(BangFuSignActivity.this, "请选择帮扶户", mPosition, words2, new DialogUIItemListener() {
                        @Override
                        public void onItemClick(CharSequence text, int position) {
                            mPosition = position;
                            if (TextUtils.isEmpty(text)) {
                                return;
                            }


                            double mi = getDistance(longitude, latitude, Double.parseDouble(basiclist.get(mPosition).getLongitude()), Double.parseDouble(basiclist.get(mPosition).getDimension()));
                            MLog.e("longitude1==" + longitude);
                            MLog.e("latitude1==" + latitude);
                            MLog.e("longitude2==" + basiclist.get(mPosition).getLongitude());
                            MLog.e("latitude2==" + basiclist.get(mPosition).getDimension());
                            MLog.e("距离==" + mi);

                            if (mi > Double.parseDouble(SPUtil.getString("fanwei"))) {
                                //签到范围外
                                ToastUtil.show("超出限定范围：" + mi + "米");
                            } else {
                                basicId = basiclist.get(mPosition).getId();
                                basicFcard = basiclist.get(mPosition).getFcard();
                                basicVillage = basiclist.get(mPosition).getVillage_name();
                                basicVillageId = basiclist.get(mPosition).getVillage();
                                year = basiclist.get(mPosition).getYear();

                                tvBasic.setText(text);
                                tvLocation.setText(mlocation + basiclist.get(mPosition).getTown_name() + basiclist.get(mPosition).getVillage_name());
                            }

                        }
                    }).show();
                }

            }
        });
    }

    List<Area> arealist;

    public void selectArea() {
        HttpRequest.getAreaById(this, SPUtil.getString("town_id"), new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                arealist = JsonUtil.toList(json, Area.class);
                if (arealist == null || arealist.size() == 0) {
                    ToastUtil.show("没有查到村");
                    return;
                }
                String[] words2 = new String[arealist.size()];
                for (int i = 0; i < arealist.size(); i++) {
                    words2[i] = arealist.get(i).getArea_name();
                }

                DialogUIUtils.showSingleChoose(BangFuSignActivity.this, "请选择当前所在的村", mPosition, words2, new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
                        mPosition = position;
                        if (TextUtils.isEmpty(text)) {
                            return;
                        }

                        double mi = getDistance(longitude, latitude, Double.parseDouble(arealist.get(mPosition).getLongitude()), Double.parseDouble(arealist.get(mPosition).getDimension()));
                        MLog.e("longitude1==" + longitude);
                        MLog.e("latitude1==" + latitude);
                        MLog.e("longitude2==" + arealist.get(mPosition).getLongitude());
                        MLog.e("latitude2==" + arealist.get(mPosition).getDimension());
                        MLog.e("距离==" + mi);

                        if (mi > Double.parseDouble(SPUtil.getString("fanwei"))) {
                            //签到范围外
                            ToastUtil.show("超出限定范围：" + mi + "米");
                        } else {
                            basicVillage = arealist.get(mPosition).getArea_name();
                            basicVillageId = arealist.get(mPosition).getId();
                            year = SPUtil.getString("year");
                            tvBasic.setText(text);
                            tvLocation.setText(mlocation + arealist.get(mPosition).getArea_name());
                        }
                    }
                }).show();
            }
        });
    }

    String img = "";
    String basicId = "";
    String basicFcard = "";
    String basicVillage = "";
    String basicVillageId = "";
    String year = "";

    public void uploadSign(final String title, final String text, final String location, final String latitude, final String longitude, final String basic_name) {
        HttpRequest.addSign(BangFuSignActivity.this, SPUtil.getString("id"), title, text, String.valueOf(DateUtil.getMillis() / 1000), location, img, latitude, longitude, basic_name, basicId, basicFcard, basicVillage, basicVillageId, year, new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                img = "";
                imagePathList.clear();
                ToastUtil.show("签到成功");
                finish();
            }
        });
        rightMenu.setClickable(true);
    }


    /*
        * @ forever 在 17/5/17 下午5:20 创建
        *
        * 描述：初始话界面数据
        *
        */
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
                mlocation = bdLocation.getAddrStr();
                if (TextUtils.isEmpty(mlocation) || "nullnull".equals(mlocation)) {
                    tvLocation.setText("定位失败，请检查网络和定位权限是否开启");
                } else {
                    tvLocation.setText(mlocation);
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


    List<ImageView> imageViewList = new ArrayList<>();
    List<String> imagePathList = new ArrayList<>();

    /*
     * @ forever 在 17/5/17 下午5:20 创建
     *
     * 描述：初始化界面布局
     *
     */
    public void initImageView() {
        imageViewList.add(ivSign1);
        imageViewList.add(ivSign2);
        imageViewList.add(ivSign3);
        imageViewList.add(ivSign4);
        imageViewList.add(ivSign6);
        imageViewList.add(ivSign7);
        imageViewList.add(ivSign8);
        imageViewList.add(ivSign9);
    }


    // －－－－－－－－－－－－－－－－－－－－－－－－－－－拍照相关的方法
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;

    /*
     * @ forever 在 17/5/17 下午5:01 创建
     *
     * 描述：打开选择照片框
     *
     */
    public void showSelectPicture() {
        final FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setMutiSelectMaxSize(6)
                .build();
        final String[] btString = new String[]{"拍照", "相册"};
        PopWindowUtil.init().show(this, R.id.ll_sign, btString, new PopWindowUtil.OnPopupWindowClickLinstener() {
            @Override
            public void popClick(Button popButton) {
                String btStr = popButton.getText().toString();
                if (btStr.equals(btString[0])) {
                    GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);
                } else if (btStr.equals(btString[1])) {
                    //相册单选
                    GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
                }
            }
        });
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                String imagePath = resultList.get(0).getPhotoPath();
                imagePath = ImageUtil.getCompressedImgPath(imagePath);
                MLog.e("onHanlderSuccess", imagePath);
                upLoadImg(imagePath);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(BangFuSignActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    public void upLoadImg(String path) {
        HttpRequest.upLoadImg(this, path, new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {

                if (TextUtils.isEmpty(img)) {
                    img = json;
                } else {
                    img = img + "##" + json;
                }
                imagePathList.add(BaseConfig.apiUrl + json);
                setImageShow();
            }

            @Override
            public void no(String msg) {
                super.no(msg);
                rightMenu.setClickable(true);
            }
        });
    }

    /*
   * @ forever 在 17/5/17 下午5:19 创建
   *
   * 描述：图片选择之后显示到界面
   *
   */
    public void setImageShow() {
        if (imagePathList.size() >= 4) {
            ivSignAdd1.setVisibility(View.GONE);
            ivSignAdd2.setVisibility(View.VISIBLE);
        }
        if (imagePathList.size() == 8) {
            ivSignAdd1.setVisibility(View.GONE);
            ivSignAdd2.setVisibility(View.GONE);
        }
        for (int i = 0; i < imagePathList.size(); i++) {

            GlideUtils.displayHome(imageViewList.get(i), imagePathList.get(i));
            imageViewList.get(i).setVisibility(View.VISIBLE);
        }
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
