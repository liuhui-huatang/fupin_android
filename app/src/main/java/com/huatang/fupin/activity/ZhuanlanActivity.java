package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.bean.Cloumn;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.utils.GlideUtils;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.ToastUtil;
import com.huatang.fupin.utils.ViewHolderUtil;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 专栏信息列表
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class ZhuanlanActivity extends BaseActivity {


    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.gg_listview)
    ListView ggListview;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    /*
         * @ forever 在 17/5/17 下午2:28 创建
         *
         * 描述：跳转到登录页面
         *
         */
    public static void startIntent(Activity activity, String title) {
        Intent it = new Intent(activity, ZhuanlanActivity.class);
        it.putExtra("title", title);
        activity.startActivity(it);
    }

    /*
    * @ forever 在 17/5/17 下午2:28 创建
    *
    * 描述：页面创建时调用
    *
    */
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gonggao);
        ButterKnife.bind(this);
        title = getIntent().getStringExtra("title");
        tvTitle.setText(title);
        initView();
    }


    public void initView() {

        //条目点击事件
        ggListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ZhuanlanDetailsActivity.startIntent(ZhuanlanActivity.this, list.get(position));
                WebActivity.startIntent(ZhuanlanActivity.this, "http://61.138.108.34:8088/index/message.cloumn/appdetails?id=" + list.get(position).getId());
            }
        });


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
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(1000);
//                refreshlayout.finishRefresh(true);
                getCloumn(title);
            }
        });
         adapter= new MyAdapter();
        ggListview.setAdapter(adapter);
        getCloumn(title);

    }
    MyAdapter adapter;
    @OnClick(R.id.left_menu)
    public void onViewClicked() {
        finish();
    }


    List<Cloumn> list = new ArrayList<>();
    int load = 1;

    public void getCloumn(String title) {
        if(load==-1){
            ToastUtil.show("别拉了，我是有底线的");
            return;
        }

        HttpRequest.getCloumn(this, title,load, new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                List<Cloumn> cloumns = JsonUtil.toList(json, Cloumn.class);
                if (cloumns.size() == 0 && load != 1) {
                    ToastUtil.show("没有更多数据了");
                    load=-1;
                    return;
                }
                list.addAll(cloumns);
                if (list.size() > 0) {
                    tvEmpty.setVisibility(View.GONE);
                    ggListview.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                    ggListview.setVisibility(View.GONE);
                }

                load++;

            }
        });
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(ZhuanlanActivity.this, R.layout.item_fragment, null);
            }

            ImageView iv_item_photo = ViewHolderUtil.get(convertView, R.id.iv_item_photo);
            TextView tv_item_title = ViewHolderUtil.get(convertView, R.id.tv_item_title);
            TextView tv_item_text = ViewHolderUtil.get(convertView, R.id.tv_item_text);
            TextView tv_item_ren = ViewHolderUtil.get(convertView, R.id.tv_item_ren);
            TextView tv_item_time = ViewHolderUtil.get(convertView, R.id.tv_item_time);

            Cloumn bean = list.get(position);
            if (!TextUtils.isEmpty(bean.getClo_avtar())) {
                GlideUtils.displayHome(iv_item_photo, BaseConfig.zhuanUrl + bean.getClo_avtar());
            }
            tv_item_title.setText(bean.getClo_title());
//                           tv_item_text.setText(bean.getClo_content());
            tv_item_ren.setText("  " + bean.getPub_person());
            tv_item_time.setText("  " + bean.getUpdate_time());
            return convertView;
        }
    }
}
