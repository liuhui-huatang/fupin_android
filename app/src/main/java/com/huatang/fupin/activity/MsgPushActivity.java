package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.bean.SystemMsg;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.utils.DateUtil;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.SPUtil;
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
 * 系统消息列表页面
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class MsgPushActivity extends BaseActivity {


    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.right_menu)
    TextView rightMenu;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    /*
         * @ forever 在 17/5/17 下午2:28 创建
         *
         * 描述：跳转到登录页面
         *
         */
    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, MsgPushActivity.class);
        activity.startActivity(it);
    }

    /*
    * @ forever 在 17/5/17 下午2:28 创建
    *
    * 描述：页面创建时调用
    *
    */
    Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msginfo);
        ButterKnife.bind(this);

        SPUtil.saveInt("push",0);

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
                getData();

            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(1000);
            }
        });

        adapter=new Adapter(MsgPushActivity.this);
        listview.setAdapter(adapter);
        getData();
        //条目点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.show("点击了一下");
            }
        });
    }

    List<SystemMsg> list = new ArrayList<>();

    public void getData() {
        HttpRequest.getSystemMsg(this, SPUtil.getString("phone"), new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                list = JsonUtil.toList(json, SystemMsg.class);
                if (list.size() > 0) {
                    listview.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                } else {
                    listview.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                }

            }
        });
    }


    class Adapter extends BaseAdapter {

        private Context mContext;

        public Adapter(Context context) {
            mContext = context;
        }

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
                convertView = View.inflate(mContext, R.layout.item_msg_info, null);
            }
            TextView tv_title = ViewHolderUtil.get(convertView, R.id.tv_title);
            TextView tv_time = ViewHolderUtil.get(convertView, R.id.tv_time);
            TextView tv_text = ViewHolderUtil.get(convertView, R.id.tv_text);

            SystemMsg bean = list.get(position);
            tv_title.setText(bean.getPush_title());
            tv_time.setText(DateUtil.getStandardTime(Long.parseLong(bean.getPush_time())));
            tv_text.setText(bean.getPush_content());
            return convertView;
        }
    }

    @OnClick({R.id.left_menu, R.id.right_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;

        }
    }


}
