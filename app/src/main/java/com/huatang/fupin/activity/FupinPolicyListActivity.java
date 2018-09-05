package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.huatang.fupin.bean.NewColumn;
import com.huatang.fupin.bean.PolicyNews;
import com.huatang.fupin.http.NewHttpRequest;
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

import static com.huatang.fupin.app.Config.typeMap;

public class FupinPolicyListActivity extends BaseActivity {

    private final static String TYPE = "type";

    @BindView(R.id.left_menu)
    ImageView lefeMenu;
    @BindView(R.id.title_tx)
    TextView tvTitle;
    @BindView(R.id.gg_listview)
    ListView ggListview;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    private List<PolicyNews> list;
    private MyAdapter adapter;
    private String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gonggao);
        ButterKnife.bind(this);
        type = getIntent().getStringExtra(TYPE);
        tvTitle.setText("政策保障");
        list = new ArrayList<>();
        initView();
    }

    private void initView() {
        //条目点击事件
        ggListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PolicyNews policyNews = list.get(position);
                String title = policyNews.getTitle();
                String url = BaseConfig.apiUrl +"news/getSafeguradInfoWithId?id="+policyNews.getNid();
                WebActivity.startIntent(FupinPolicyListActivity.this,url,title);
            }
        });


        RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setPrimaryColors(getResources().getColor(R.color.dodgerblue));
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
                getCloumn();
            }
        });
        adapter = new MyAdapter();
        ggListview.setAdapter(adapter);
        getCloumn();
    }

    @OnClick(R.id.left_menu)
    public void onViewClicked() {
        finish();
    }

    int load = 1;

    public void getCloumn() {
        if (load == -1) {
            ToastUtil.show("没有更多数据了");
            return;
        }
        NewHttpRequest.getSafeguradList(this, type, String.valueOf(load), new NewHttpRequest.MyCallBack(this) {

            @Override
            public void ok(String json) {
                List<PolicyNews> policyNewsList = JsonUtil.toList(json, PolicyNews.class);
                if (policyNewsList.size() == 0 && load != 1) {
                    ToastUtil.show("没有更多数据了");
                    load = -1;
                    return;
                }
                list.addAll(policyNewsList);
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

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);
            }
        });


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
                convertView = View.inflate(FupinPolicyListActivity.this, R.layout.item_fragment, null);
            }

            ImageView iv_item_photo = ViewHolderUtil.get(convertView, R.id.iv_item_photo);
            TextView tv_item_title = ViewHolderUtil.get(convertView, R.id.tv_item_title);
            TextView tv_item_time = ViewHolderUtil.get(convertView, R.id.tv_item_time);

            PolicyNews policyNews = list.get(position);
            tv_item_title.setText(policyNews.getTitle());
            tv_item_time.setText("  " + policyNews.getPush_time());
            return convertView;
        }
    }

    public static void startIntent(Activity activity, String type) {
        Intent it = new Intent(activity, FupinPolicyListActivity.class);
        it.putExtra(TYPE, type);
        activity.startActivity(it);
    }
}
