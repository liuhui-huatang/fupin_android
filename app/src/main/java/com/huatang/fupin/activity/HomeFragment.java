package com.huatang.fupin.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.bean.NewBanner.BannerColumn;
import com.huatang.fupin.bean.Home;
import com.huatang.fupin.bean.NewBanner;
import com.huatang.fupin.bean.NewPoor;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.http.NewHttpRequest;
import com.huatang.fupin.utils.GlideImageLoader;
import com.huatang.fupin.utils.GlideUtils;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.MLog;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.SkinUtil;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {




    GridView gridview;
    Banner banner;
    private RelativeLayout title_bar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_home,container,false);
        initView(view);
        initHeadView(view);
        return view;

    }

    private void initHeadView(View view) {
        ImageView lefeMenu = view.findViewById(R.id.left_menu);
        lefeMenu.setVisibility(View.VISIBLE);
        lefeMenu.setImageResource(SkinUtil.getResouceId(R.mipmap.img_sao));
        lefeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCaptureActivityForResult();
            }
        });
        ImageView rightMenu = (ImageView) view.findViewById(R.id.right_menu);
        rightMenu.setVisibility(View.VISIBLE);
        rightMenu.setImageResource(SkinUtil.getResouceId(R.mipmap.img_message));
        rightMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewMsgActivity.startIntent(getActivity());
            }
        });

    }
    private void startCaptureActivityForResult() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(CaptureActivity.KEY_NEED_BEEP, CaptureActivity.VALUE_BEEP);
        bundle.putBoolean(CaptureActivity.KEY_NEED_VIBRATION, CaptureActivity.VALUE_VIBRATION);
        bundle.putBoolean(CaptureActivity.KEY_NEED_EXPOSURE, CaptureActivity.VALUE_NO_EXPOSURE);
        bundle.putByte(CaptureActivity.KEY_FLASHLIGHT_MODE, CaptureActivity.VALUE_FLASHLIGHT_OFF);
        bundle.putByte(CaptureActivity.KEY_ORIENTATION_MODE, CaptureActivity.VALUE_ORIENTATION_AUTO);
        bundle.putBoolean(CaptureActivity.KEY_SCAN_AREA_FULL_SCREEN, CaptureActivity.VALUE_SCAN_AREA_FULL_SCREEN);
        bundle.putBoolean(CaptureActivity.KEY_NEED_SCAN_HINT_TEXT, CaptureActivity.VALUE_SCAN_HINT_TEXT);
        startActivityForResult(intent, CaptureActivity.REQ_CODE);
    }
    private void initView(View view) {
        title_bar = (RelativeLayout)view.findViewById(R.id.title_bar);
        //title_bar.setBackgroundResource(SkinUtil.getResouceId(R.mipmap.header));
        banner = (Banner) view.findViewById(R.id.banner_main);
        gridview = (GridView) view.findViewById(R.id.gridview);

        RefreshLayout refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setPrimaryColors(getResources().getColor(R.color.dodgerblue));
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new MaterialHeader(this.getContext()).setShowBezierWave(true));
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(this.getContext()).setSpinnerStyle(SpinnerStyle.Scale));

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout freshlayout) {
                freshlayout.finishRefresh(true);

                initBanner();

            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);
            }
        });
        refreshLayout.setEnableFooterTranslationContent(false);
        initBanner();

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (greadViews.get(position)) {
                        case Config.bangfuriji://帮扶日志
                            BangFuListActivity.startIntent(getActivity());
                            break;
                        case Config.xiaoxitongzhi://消息通知
                            NewMsgActivity.startIntent(getActivity());
                            break;
                        case  Config.danganguanli://档案管理
                            switch (SPUtil.getString(Config.Type)){//1: 游客2: 贫困户3: 帮扶干部4: 管理员5: 村负责人
                                case Config.YOUKU_TYPE:
                                    break;
                                case Config.PENKUNHU_TYPE:

                                    DanganDetailActivity.startIntent(getActivity());
                                    break;
                                case Config.GANBU_TYPE:
                                case Config.ADMIN_TYPE:
                                    DanganListNewActivity.startIntent(getActivity());
                                    break;
                                case Config.FUZEREN_TYPE:
                                    break;

                            }
                            break;
                        case Config.fupenyaowen://扶贫要闻
                            UniteNewsActivity.startIntent(getActivity(),Config.fupenyaowen_type);
                            break;
                        case Config.gongshigonggao://公告
                            UniteNewsActivity.startIntent(getActivity(),Config.gongshigonggao_type);
                            break;
                        case Config.dianxingyinlu://典型引路
                            UniteNewsActivity.startIntent(getActivity(),Config.dianxingyinlu_type);
                            break;
                        case Config.fupenxingdong://扶贫行动
                            UniteNewsActivity.startIntent(getActivity(),Config.fupenxingdong_type);
                            break;
                        case Config.qunzonghudong://群众互动
                            UniteNewsActivity.startIntent(getActivity(),Config.qunzonghudong_type);
                            break;

                        case Config.fupenzhengce://扶贫政策
                            FupinPolicyActivity.startIntent(getActivity());
                            break;
                        case Config.zuzhilingdao://组织领导
                            break;
                        default:
                            break;
                    }
                }
            });
    }
    /**
     * 头部轮播图
     *
     * @param
     */
    NewBanner newBanner = new NewBanner();
    List<BannerColumn> bannerColumnList =new ArrayList<BannerColumn>();
    public void initBanner() {
        bannerColumnList.clear();
        NewHttpRequest.getHome(getActivity(), SPUtil.getString(Config.Type),new NewHttpRequest.MyCallBack(getActivity()) {
            @Override
            public void ok(String json) {
                newBanner = JsonUtil.json2Bean(json, NewBanner.class);
                bannerColumnList = newBanner.getShuffling_img();
                List<String> images = new ArrayList<>();
                List<String> titles = new ArrayList<>();
                for (BannerColumn banner : bannerColumnList) {
                    images.add(BaseConfig.ImageUrl + banner.getImg());
                    titles.add(banner.getTitle());
                }

                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
                banner.setBannerTitles(titles);
                banner.setImages(images).setImageLoader(new GlideImageLoader()).setDelayTime(3000).start();

                banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        if (bannerColumnList.get(position) != null) {
                            WebActivity.startIntent(getActivity(), BaseConfig.banner_url+"?id=" + bannerColumnList.get(position).getId());
                        }
                    }
                });
                initGridview();
            }

            @Override
            public void no(String msg) {

                ToastUtil.show(msg);


            }
        });

    }

    List<String> greadViews = new ArrayList<>();
    HomeAdapter adapter;

    public void initGridview() {

        greadViews.clear();
        for(String menuTitle : newBanner.getModel()){
            if(Config.menuMap.containsKey(menuTitle)){
                greadViews.add(menuTitle);

            }

        }
        adapter = new HomeAdapter();
        gridview.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

    class HomeAdapter extends BaseAdapter {
        public HomeAdapter(){

        }
        @Override
        public int getCount() {
            return greadViews.size();
        }

        @Override
        public Object getItem(int position) {
            return greadViews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.item_home, null);
            }
            ImageView item_img = (ImageView) convertView.findViewById(R.id.item_img);
            TextView item_title = (TextView) convertView.findViewById(R.id.item_title);
            String title = greadViews.get(position);
            if("消息通知".equals(greadViews.get(position)) && SPUtil.getInt("push", 0) > 0){
                item_title.setText(greadViews.get(position));
                item_img.setImageResource(SkinUtil.getResouceId(R.mipmap.img_xiaoxi_red));
            }else{
                item_title.setText(title);
                Integer resouceId = (Integer)Config.menuMap.get(title);
                item_img.setImageResource(Config.menuMap.get(title));
            }
            return convertView;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
