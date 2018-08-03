package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.example.library.control.TxtKandy;
import com.example.library.http.HttpRequestClient;
import com.example.library.login.AccessKandy;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.bean.Cloumn;
import com.huatang.fupin.bean.Home;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.update.AppDownloadUtils;
import com.huatang.fupin.utils.AppManager;
import com.huatang.fupin.utils.GlideImageLoader;
import com.huatang.fupin.utils.GlideUtils;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.MLog;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.SystemLogHelper;
import com.huatang.fupin.utils.ToastUtil;
import com.huatang.fupin.zxing.activity.CaptureActivity;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * app首页
 *
 * @date 创建时间：17/7/13 下午7:57
 * @author: forever
 * @description:
 */
public class HomeActivity extends BaseActivity {

    @BindView(R.id.rightmenu)
    ImageView rightmenu;
    @BindView(R.id.gridview)
    GridView gridview;
    @BindView(R.id.tv_identity)
    TextView tvIdentity;

    @BindView(R.id.banner_main)
    Banner banner;

    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, HomeActivity.class);
        activity.startActivity(it);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        tvIdentity.setText("当前用户：【" + SPUtil.getString("name") + "," + SPUtil.getString("unit") + SPUtil.getString("duty") + "】");
        initView();
        initBanner();
        initGridview();
        initKandy();

        // 判断是否需要更新版本
        AppDownloadUtils.getApkInfo(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (!TxtKandy.getAccessKandy().userIsLogin()) {
//            MLog.e("kandy=HomeActivity=onStart=" + TxtKandy.getDataMpvConnnect().getCurrentLoginUser());
//        } else {
//            MLog.e("kandy=HomeActivity=else-->kandyLogin" );
//            kandyLogin();
//        }
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }

    }


    public void initView() {
        RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setPrimaryColors(getResources().getColor(R.color.colorPrimary));
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(true));
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout freshlayout) {
                freshlayout.finishRefresh(true);

                initBanner();
                initGridview();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (greadViews.get(position).getTitle()) {
                    case "帮扶日志":
                        SPUtil.saveString("fanwei", greadViews.get(position).getUrl());
                        BangFuListActivity.startIntent(HomeActivity.this);
                        break;

                    case "消息通知":
                        MsgActivity.startIntent(HomeActivity.this);
                        break;

                    case "档案管理":
                        if ("2".equals(SPUtil.getString("identity"))) {
//                            DanganXinxiActivity.startIntent(HomeActivity.this,);
                        }else{
                            DanganListActivity.startIntent(HomeActivity.this);
                        }


                        break;

                    case "扫一扫":
                        startCaptureActivityForResult();
                        break;

                    case "图表统计":
                        WebActivity.startIntent(HomeActivity.this, greadViews.get(position).getUrl());
                        break;

                    default:
                        ZhuanlanActivity.startIntent(HomeActivity.this, greadViews.get(position).getTitle());
                        break;
//                    case "公告公示":
//
//                        break;
//
//                    case "扶贫政策":
//                        ZhengceActivity.startIntent(HomeActivity.this);
//                        break;
//
//                    case "党建动态":
//                        DangJianActivity.startIntent(HomeActivity.this);
//                        break;
//
//                    case "扶贫培训":
//                        PeiXuneActivity.startIntent(HomeActivity.this);
//                        break;

                }
            }
        });
    }


    public void initKandy() {

        MLog.e("kandy=HomeActivity: kandyLogin=" + SPUtil.getString("kandy_user")+"##"+SPUtil.getString("kandy_pwd"));
        if (TextUtils.isEmpty(SPUtil.getString("kandy_user"))) {
            kandyRegister(SPUtil.getString("phone") + SPUtil.getString("id"), "android" + SPUtil.getString("password"));
        } else {
            kandyLogin(SPUtil.getString("kandy_user"), SPUtil.getString("kandy_pwd"));
        }
    }

    //   通讯登录
    public void kandyLogin(String kandy_user, String kandy_pwd) {
        DialogUIUtils.showTie(this, "加载中...");
        TxtKandy.getAccessKandy().userLogin(kandy_user, kandy_pwd, new AccessKandy.LoginRequestCallBack() {
            @Override
            public void onSuccess() {
                DialogUIUtils.dismssTie();
                MLog.e("kandy==HomeActivity=onSuccess:" + TxtKandy.getDataMpvConnnect().getCurrentLoginUser());
            }

            @Override
            public void onFail(int i, String s) {
                DialogUIUtils.dismssTie();
                try {
                    MLog.e("kandy=HomeActivity=onFail:" + TxtKandy.getDataMpvConnnect().getCurrentLoginUser());
                } catch (Exception e) {
                    MLog.e("kandy=HomeActivity=onFail:", e.getMessage());
                }
            }

        });
    }

    // 通讯注册
    public void kandyRegister(String phone, String pwd) {
        DialogUIUtils.showTie(this, "加载中...");
        Map<String, String> values = new HashMap<String, String>();
        values.put("user_id", phone);
        values.put("user_email", "email@sdkdemo.txtechnology.com.cn");
        values.put("user_password", pwd);
        TxtKandy.getAccessKandy().registerKandyUser(values, new HttpRequestClient.RequestHttpCallBack() {
            @Override
            public void onSuccess(String json) {
                DialogUIUtils.dismssTie();
                MLog.e("kandy==registerKandyUser=onSuccess:" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);

                    if (jsonObject.getString("status").equals("0")) {
                        JSONObject resultJson = jsonObject.getJSONObject("result");

//                        String user_id = resultJson.getString("user_id");
//                        String domain_name = resultJson.getString("domain_name");
//                        String user_api_key = resultJson.getString("user_api_key");
//                        String user_api_secret = resultJson.getString("user_api_secret");

                        TxtKandy.getAccessKandy().mDomainName = SPUtil.getString("name");
                        final String user_password = resultJson.getString("user_password");
                        final String full_user_id = resultJson.getString("full_user_id");

                        HttpRequest.saveKandy(HomeActivity.this, SPUtil.getString("id"), full_user_id, user_password, new HttpRequest.MyCallBack() {
                            @Override
                            public void ok(String json) {
                                MLog.e("kandy==ssaveKandy==ok", "ok");
                                SPUtil.saveString("kandy_user", full_user_id);
                                SPUtil.saveString("kandy_pwd", user_password);
                            }
                        });

                    } else {
                        HomeActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show("注册失败！");

                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String err, int code) {
                DialogUIUtils.dismssTie();
                MLog.d("kandy==onFail=registerKandyUser:" + TxtKandy.getDataMpvConnnect().getCurrentLoginUser());
            }
        });
    }

    /**
     * 头部轮播图
     *
     * @param
     */
    List<Cloumn> beans = new ArrayList<>();

    public void initBanner() {
        beans.clear();
        HttpRequest.getBanner(this, new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                beans = JsonUtil.toList(json, Cloumn.class);
                List<String> images = new ArrayList<>();
                List<String> titles = new ArrayList<>();
                for (Cloumn bean : beans) {
                    images.add(BaseConfig.zhuanUrl + bean.getClo_avtar());
                    titles.add(bean.getClo_title());

//                    MLog.e("banner==url" + BaseConfig.zhuanUrl + bean.getClo_avtar());
//                    MLog.e("banner==title" + bean.getClo_title());
                }

                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
                banner.setBannerTitles(titles);
                banner.setImages(images).setImageLoader(new GlideImageLoader()).setDelayTime(3000).start();

                banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        if (beans.get(position) != null) {
//                            ZhuanlanDetailsActivity.startIntent(HomeActivity.this, beans.get(position));
                            WebActivity.startIntent(HomeActivity.this, "http://61.138.108.34:8088/index/message.cloumn/appdetails?id=" + beans.get(position).getId());
                        }
                    }
                });
            }
        });

    }

    List<Home> greadViews = new ArrayList<>();
    HomeAdapter adapter;

    public void initGridview() {
        greadViews.clear();
        HttpRequest.getHome(this, new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                MLog.e("getHome",json);
                List<Home> list = JsonUtil.toList(json, Home.class);
                for (int i = 0; i < list.size(); i++) {
                    if (SPUtil.getString("identity").equals(list.get(i).getIdentity())) {
                        greadViews.add(list.get(i));
                    }
                }
                adapter = new HomeAdapter();
                gridview.setAdapter(adapter);
            }
        });
    }

    class HomeAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return greadViews.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(HomeActivity.this, R.layout.item_home, null);
            }
            ImageView item_img = (ImageView) convertView.findViewById(R.id.item_img);
            TextView item_title = (TextView) convertView.findViewById(R.id.item_title);

            if ("消息通知".equals(greadViews.get(position).getTitle()) && SPUtil.getInt("push", 0) > 0) {
                item_title.setText(greadViews.get(position).getTitle());
                GlideUtils.displayNative(item_img, R.mipmap.img_xiaoxi_red);
            } else {
                item_title.setText(greadViews.get(position).getTitle());
                GlideUtils.displayHome(item_img, BaseConfig.apiUrl + greadViews.get(position).getImage());
            }
            return convertView;
        }
    }


//    如果你需要考虑更好的体验，可以这么操作
//    @Override
//    public void onStart() {
//        super.onStart();
//        //开始轮播
//        banner.startAutoPlay();
//    }
//
//    @Override
//    public void onStop() {
//
//        super.onStop();
//        //结束轮播
//        banner.stopAutoPlay();
//    }


    /**
     * 物理返回键监听，双击退出程序
     *
     * @param keyCode
     * @param event
     * @return
     */
    Long exitTime = (long) 1000;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) { // System.currentTimeMillis()无论何时调用，肯定大于2000
                Toast.makeText(this, "再次点击退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                //程序退出时的处理
                SystemLogHelper.getmInstance().stop();
                AppManager.getAppManager().finishAllActivity();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick({R.id.rightmenu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rightmenu:
                MyActivity.startIntent(this);
                break;
        }
    }


    private void startCaptureActivityForResult() {
        Intent intent = new Intent(this, CaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(CaptureActivity.KEY_NEED_BEEP, CaptureActivity.VALUE_BEEP);
        bundle.putBoolean(CaptureActivity.KEY_NEED_VIBRATION, CaptureActivity.VALUE_VIBRATION);
        bundle.putBoolean(CaptureActivity.KEY_NEED_EXPOSURE, CaptureActivity.VALUE_NO_EXPOSURE);
        bundle.putByte(CaptureActivity.KEY_FLASHLIGHT_MODE, CaptureActivity.VALUE_FLASHLIGHT_OFF);
        bundle.putByte(CaptureActivity.KEY_ORIENTATION_MODE, CaptureActivity.VALUE_ORIENTATION_AUTO);
        bundle.putBoolean(CaptureActivity.KEY_SCAN_AREA_FULL_SCREEN, CaptureActivity.VALUE_SCAN_AREA_FULL_SCREEN);
        bundle.putBoolean(CaptureActivity.KEY_NEED_SCAN_HINT_TEXT, CaptureActivity.VALUE_SCAN_HINT_TEXT);
//        intent.putExtra(CaptureActivity.EXTRA_SETTING_BUNDLE, bundle);
        startActivityForResult(intent, CaptureActivity.REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CaptureActivity.REQ_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        ToastUtil.show(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        break;
                    case RESULT_CANCELED:
                        if (data != null) {
                            // for some reason camera is not working correctly
                            ToastUtil.show(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        }
                        break;
                }
                break;
        }
    }

}
