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
import com.huatang.fupin.app.Config;
import com.huatang.fupin.app.MyApplication;
import com.huatang.fupin.bean.Area;
import com.huatang.fupin.bean.Basic;
import com.huatang.fupin.bean.NewArea;
import com.huatang.fupin.bean.NewLeader;
import com.huatang.fupin.bean.NewPoor;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.http.NewHttpRequest;
import com.huatang.fupin.utils.DateUtil;
import com.huatang.fupin.utils.GlideUtils;
import com.huatang.fupin.utils.ImageUtil;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.MLog;
import com.huatang.fupin.utils.PopWindowUtil;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.StringUtil;
import com.huatang.fupin.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

import static com.huatang.fupin.app.Config.IS_OPEN_TEST_DISTANCE;

/**
 * 帮扶日志添加页面
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class BangFuSignActivity extends BaseActivity {


    public static final int REQUEST_CODE = 1000;
    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.title_tx)
    TextView tvTitle;
    @BindView(R.id.right_menu)
    ImageView rightMenu;
    @BindView(R.id.right_tx_menu)
    TextView right_tx_menu;
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
    private NewLeader leader;
    private String signAddress;
    private String signType;
    private String signVillage;
    private String signVillageId;
    private String signTown;
    private String signTownId;
    private String img;
    private NewPoor poor = new NewPoor();
    private List<NewArea> arealist;
    List<String> imagePathList = new ArrayList<>();
    private List<NewPoor> poorList;

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
    public static void startIntentForResult(Activity activity,int requestCode) {
        Intent it = new Intent(activity, BangFuSignActivity.class);
        activity.startActivityForResult(it,requestCode);
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
        initHeadView();
        initImageView();
        initLocation();
        leader = SPUtil.getLeaderFromSharePref();
        if(!leader.isHasbasic()){
            tvBasic.setText("选择贫困村");
        }
    }

    private void initHeadView() {
        right_tx_menu.setText("提交");
        right_tx_menu.setVisibility(View.VISIBLE);
        tvTitle.setText("工作日志");

    }



    @OnClick({R.id.left_menu, R.id.right_tx_menu, R.id.tv_location, R.id.iv_sign_add1, R.id.iv_sign_add_2, R.id.rl_location, R.id.rl_basic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.rl_location:

                break;
            case R.id.rl_basic:
                selectPoor();
                break;
            case R.id.right_tx_menu:
                right_tx_menu.setClickable(false);
                String title = etTitle.getText().toString().trim();
                String text = etText.getText().toString().trim();
                String location = tvLocation.getText().toString().trim();
                String basic = tvBasic.getText().toString().trim();
                if (TextUtils.isEmpty(title)) {
                    ToastUtil.show("标题未填写");
                    right_tx_menu.setClickable(true);
                    return;
                }
                if (title.length() < 2) {
                    ToastUtil.show("标题不得少于2个字");
                    right_tx_menu.setClickable(true);
                    return;
                }

                if (TextUtils.isEmpty(text)) {
                    ToastUtil.show("内容暂未填写");
                    right_tx_menu.setClickable(true);
                    return;
                }
                if (text.length() < 5) {
                    ToastUtil.show("内容不得少于5个字");
                    right_tx_menu.setClickable(true);
                    return;
                }

                if (TextUtils.isEmpty(location)) {
                    ToastUtil.show("定位信息不全");
                    right_tx_menu.setClickable(true);
                    return;
                }
                if (imagePathList.size() == 0) {
                    ToastUtil.show("请先选择图片");
                    right_tx_menu.setClickable(true);
                    return;
                }
                if (TextUtils.isEmpty(basic)) {
                    ToastUtil.show("请选择帮扶户");
                    right_tx_menu.setClickable(true);
                    return;
                }
                signAddress = location;
                uploadSign(title,text);
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


    int mPosition=-1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_OK){
            if(resultCode == MsgSendSearchActivity.POOR_RESULT_CODE){
                NewPoor newPoor = (NewPoor)data.getSerializableExtra(Config.PENKUNHU_KEY);
                if(poor.equals(newPoor)){
                    ToastUtil.show("当前用户已选择");
                    return;
                }
                double mi = getDistance(longitude,latitude,Double.parseDouble(newPoor.getLongitude()),Double.parseDouble(newPoor.getDimension()));
                if(mi > Double.parseDouble(Config.BAI_DU_MAP_DISTANCE)){
                    //签到范围外
                    ToastUtil.show("超出限定范围：" + mi + "米");
                }else{
                    poor = newPoor;
                    signVillage = poor.getVillage_name();
                    signVillageId = poor.getVillage();
                    signTown = poor.getTown_name();
                    signTownId = poor.getTown();
                    tvBasic.setText(newPoor.getFname());
                    tvLocation.setText(mlocation + newPoor.getTown_name() + newPoor.getVillage_name());
                }

            }

        }
    }

    public void selectPoor() {
        if(leader.isHasbasic()){
            selectBasic();
        }else{
            ToastUtil.show("没有帮扶户");
             selectArea();
        }

    }
    public void selectBasic() {

        NewHttpRequest.searchPoorList(this, leader.getId(), new NewHttpRequest.MyCallBack(this) {
            @Override
            public void ok(String json) {
                poorList = JsonUtil.toList(json,NewPoor.class);
                if (poorList == null || poorList.size() == 0) {
                    ToastUtil.show("没有帮扶户");
                    selectArea();
                } else {
                    String[] words2 = new String[poorList.size()];
                    for (int i = 0; i < poorList.size(); i++) {
                        words2[i] = poorList.get(i).getFname() == null ? " " : poorList.get(i).getFname();
                    }
                    DialogUIUtils.showSingleChoose(BangFuSignActivity.this, "请选择帮扶户", mPosition, words2, new DialogUIItemListener() {
                        @Override
                        public void onItemClick(CharSequence text, int position) {
                            mPosition = position;
                            if (TextUtils.isEmpty(text)) {
                                return;
                            }


                            double mi = getDistance(longitude, latitude, Double.parseDouble(poorList.get(mPosition).getLongitude()), Double.parseDouble(poorList.get(mPosition).getDimension()));
                            MLog.e("longitude1==" + longitude);
                            MLog.e("latitude1==" + latitude);
                            MLog.e("longitude2==" + poorList.get(mPosition).getLongitude());
                            MLog.e("latitude2==" + poorList.get(mPosition).getDimension());
                            MLog.e("距离==" + mi);
                            //SPUtil.getString("fanwei")
                            if (mi > Double.parseDouble(Config.BAI_DU_MAP_DISTANCE) && Config.IS_OPEN_TEST_DISTANCE) {
                                //签到范围外
                                ToastUtil.show("超出限定范围：" + mi + "米");
                            } else {
                                poor =  poorList.get(mPosition);
                                signVillage = poor.getVillage_name();
                                signVillageId = poor.getVillage();
                                signTown = poor.getTown_name();
                                signTownId = poor.getTown();
                                tvBasic.setText(poor.getFname());
                                tvLocation.setText(mlocation + poor.getTown_name() + poor.getVillage_name());
                            }

                        }
                    }).show();
                }

            }

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);
            }
        });
    }


    public void selectArea() {
        NewHttpRequest.getVillageList(this, leader.getHelp_town_id(), new NewHttpRequest.MyCallBack(this) {
            @Override
            public void ok(String json) {
                arealist = JsonUtil.toList(json, NewArea.class);
                if (arealist == null || arealist.size() == 0) {
                    ToastUtil.show("没有查到村");
                    return;
                }
                String[] words2 = new String[arealist.size()];
                for (int i = 0; i < arealist.size(); i++) {
                    words2[i] = arealist.get(i).getVillage_name();
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

                        if (mi > Double.parseDouble(Config.BAI_DU_MAP_DISTANCE)  && Config.IS_OPEN_TEST_DISTANCE) {
                            //签到范围外
                            ToastUtil.show("超出限定范围：" + mi + "米");
                        } else {
                            signVillage = arealist.get(mPosition).getVillage_name();
                            signVillageId = arealist.get(mPosition).getVillage();

                           // year = SPUtil.getString("year");
                            tvBasic.setText(text);
                            tvLocation.setText(mlocation + arealist.get(mPosition).getVillage_name());
                        }
                    }
                }).show();
            }

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);
            }
        });
    }


    public void uploadSign(String title,String content) {
        NewHttpRequest.addSign(this, leader, title, content, signType, signAddress, signVillage, signVillageId, signTown, signTownId, StringUtil.listToString(imagePathList,StringUtil.separator),  String.valueOf(longitude), String.valueOf(latitude),poor, new NewHttpRequest.MyCallBack(this) {

            @Override
            public void ok(String json) {
                img = "";
                imagePathList.clear();
                ToastUtil.show("签到成功");
                setResult(RESULT_OK);
                finish();

            }

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);
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
//               signAddress =bdLocation.getAddrStr();    //获取地址信息
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
                imagesUpload(imagePath);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(BangFuSignActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };


    public void imagesUpload(String filePath) {
        /**
         * 图片上传服务器
         */
        NewHttpRequest.uploadImage(this, filePath, new NewHttpRequest.UploadCallBack() {
            @Override
            public void callback(String json) {
                ToastUtil.show("上传成功");
                String url = JsonUtil.getStringFromArray(json,"url");
                String photoUrl = BaseConfig.ImageUrl + url;
                imagePathList.add(url);
                setImageShow();

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

            GlideUtils.displayHome(imageViewList.get(i), BaseConfig.ImageUrl+imagePathList.get(i));
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
