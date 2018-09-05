package com.huatang.fupin.activity;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.bean.NewColumn;
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

public class TabContentFragment extends Fragment {
    private static final String TAB_TYPE = "type";
    private String type;
    private ArrayList<NewColumn> list = new ArrayList<>();
    private MyAdapter adapter;
    private ListView listview;
    TextView tvEmpty;

    public static Fragment newInstance(String tabType) {
        Bundle args = new Bundle();
        args.putString(TAB_TYPE, tabType);
        TabContentFragment myFragment = new TabContentFragment();
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString(TAB_TYPE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_tab_content, container, false);

        listview = view.findViewById(R.id.listview);
        tvEmpty = view.findViewById(R.id.tv_empty);

        adapter = new MyAdapter(getActivity(), list);
        listview.setAdapter(adapter);
        //条目点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UnitNewsInfoActivity.startIntent(getActivity(), list.get(position).getId());
            }
        });
        initView(view);
        getCloumn();
        return view;
    }

    private void initView(View view) {

        RefreshLayout refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setPrimaryColors(getResources().getColor(R.color.dodgerblue));
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new MaterialHeader(getActivity()).setShowBezierWave(true));
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));

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
    }

    int load = 1;

    private void getCloumn() {
        if (load == -1) {
            ToastUtil.show("没有更多数据了");
            return;
        }
        NewHttpRequest.getNewsWithType(getActivity(), type, String.valueOf(load), new NewHttpRequest.MyCallBack(getActivity()) {

            @Override
            public void ok(String json) {
                List<NewColumn> cloumns = JsonUtil.toList(json, NewColumn.class);
                if (cloumns.size() == 0 && load != 1) {
                    ToastUtil.show("没有更多数据了");
                    load = -1;
                    return;
                }
                list.addAll(cloumns);
                if (list.size() > 0) {
                    tvEmpty.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
                }

                load++;
            }

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);

            }
        });
    }

    class MyAdapter extends BaseAdapter {
        private Context context;
        private List<NewColumn> columnList;

        public MyAdapter(Context context, List<NewColumn> columnList) {
            this.context = context;
            this.columnList = columnList;
        }

        @Override
        public int getCount() {
            return columnList.size();
        }

        @Override
        public Object getItem(int position) {
            return columnList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_fragment, null);
            }

            ImageView iv_item_photo = ViewHolderUtil.get(convertView, R.id.iv_item_photo);
            TextView tv_item_title = ViewHolderUtil.get(convertView, R.id.tv_item_title);
            TextView tv_item_time = ViewHolderUtil.get(convertView, R.id.tv_item_time);

            NewColumn bean = list.get(position);
            if (!TextUtils.isEmpty(bean.getImg())) {
                GlideUtils.displayHomeUrl(iv_item_photo, BaseConfig.ImageUrl + bean.getImg(), R.mipmap.news_default_img);
            }
            tv_item_title.setText(bean.getTitle());
            tv_item_time.setText("  " + bean.getUpdate_time());
            return convertView;
        }
    }
}
