package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Context;
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

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.bean.Basic;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.MLog;
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
 * 干部的帮扶户列表页面
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class DanganListActivity extends BaseActivity {


    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.right_menu)
    TextView rightMenu;

    /*
     * @ forever 在 17/5/17 下午2:28 创建
     *
     * 描述：跳转到登录页面
     *
     */
    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, DanganListActivity.class);
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
        setContentView(R.layout.activity_dangan);
        ButterKnife.bind(this);

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
                getData(SPUtil.getString("year"));

            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(1000);
            }
        });

        adapter= new Adapter(DanganListActivity.this);
        listview.setAdapter(adapter);


        //条目点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DanganXinxiActivity.startIntent(DanganListActivity.this, list.get(position));
            }
        });

        tvEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivity.startIntent(DanganListActivity.this);
            }
        });
        getData(SPUtil.getString("year"));

    }


    List<Basic> list = new ArrayList<>();
    public void getData(String year) {
        HttpRequest.getBasic(this, SPUtil.getString("id"), year, new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                list = JsonUtil.toList(json, Basic.class);
                if (list.size() > 0) {
                    tvEmpty.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
                }

            }
        });
    }

    @OnClick({R.id.left_menu, R.id.right_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.right_menu:

                break;
        }
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
                convertView = View.inflate(mContext, R.layout.item_dangan, null);
            }
            ImageView iv_photo = ViewHolderUtil.get(convertView, R.id.iv_photo);
            TextView tv_name = ViewHolderUtil.get(convertView, R.id.tv_name);
            TextView tv_statu = ViewHolderUtil.get(convertView, R.id.tv_statu);
            TextView tv_lable1 = ViewHolderUtil.get(convertView, R.id.tv_lable1);
            TextView tv_lable2 = ViewHolderUtil.get(convertView, R.id.tv_lable2);
            TextView tv_lable3 = ViewHolderUtil.get(convertView, R.id.tv_lable3);
            TextView tv_lable4 = ViewHolderUtil.get(convertView, R.id.tv_lable4);
            TextView tv_address = ViewHolderUtil.get(convertView, R.id.tv_address);

            Basic bean = list.get(position);
//            GlideUtils.displayHome(iv_photo, bean.get);

            if (TextUtils.isEmpty(bean.getFname())) {
                tv_name.setText("户主：" + "");
            } else {
                tv_name.setText("户主：" + bean.getFname());
            }

            // 0:未脱贫 1:预脱贫 2:已脱贫 3:注销 4:返贫
            if (TextUtils.isEmpty(bean.getOut_poor_state())) {
                tv_statu.setText("脱贫状态");
            } else {
                switch (bean.getOut_poor_state()) {
                    case "0":
                        tv_statu.setText("脱贫状态：未脱贫");
                        break;
                    case "1":
                        tv_statu.setText("脱贫状态：预脱贫");
                        break;
                    case "2":
                        tv_statu.setText("脱贫状态：已脱贫");
                        break;
                    case "3":
                        tv_statu.setText("脱贫状态：注销");
                        break;
                    case "4":
                        tv_statu.setText("脱贫状态：返贫");
                        break;
                }
            }

            if (TextUtils.isEmpty(bean.getSex())) {
                tv_lable1.setText("性别：男");
            } else {
                switch (bean.getSex()) {
                    case "1":
                        tv_lable1.setText("性别：男");
                        break;
                    case "2":
                        tv_lable1.setText("性别：女");
                        break;
                }
            }

            if (TextUtils.isEmpty(bean.getFamily_num())) {
                tv_lable2.setText("0口人");
            } else {
                tv_lable2.setText(bean.getFamily_num() + "口人");
            }

            //主要致贫原因 1.因病 2.因残 3.因学 4.因灾 5.缺土地 6.缺水 7.缺技术 8.缺劳动力 9.缺资金 10.交通条件落后 11.自身发展动力不足
            if (TextUtils.isEmpty(bean.getMain_pcause())) {
                tv_lable3.setText("致贫原因");
            } else {
                switch (bean.getMain_pcause()) {
                    case "0":
                        tv_lable3.setText("致贫原因");
                        break;
                    case "1":
                        tv_lable3.setText("因病");
                        break;
                    case "2":
                        tv_lable3.setText("因残");
                        break;
                    case "3":
                        tv_lable3.setText("因学");
                        break;
                    case "4":
                        tv_lable3.setText("因灾");
                        break;
                    case "5":
                        tv_lable3.setText("缺土地");
                        break;
                    case "6":
                        tv_lable3.setText("缺水");
                        break;
                    case "7":
                        tv_lable3.setText("缺技术");
                        break;
                    case "8":
                        tv_lable3.setText("缺劳动力");
                        break;
                    case "9":
                        tv_lable3.setText("缺资金");
                        break;
                    case "10":
                        tv_lable3.setText("交通条件落后");
                        break;
                    case "11":
                        tv_lable3.setText("自身发展动力不足");
                        break;
                }
            }

            //贫困户属性: 1一般贫困户 2.低保兜底户 3.五保户 4.一般农户
            if (TextUtils.isEmpty(bean.getFamily_state())) {
                tv_lable4.setText("贫困户属性");
            } else {
                switch (bean.getFamily_state()) {
                    case "0":
                        tv_lable4.setText("贫困户属性");
                        break;
                    case "1":
                        tv_lable4.setText("贫困户属性：一般贫困户");
                        break;
                    case "2":
                        tv_lable4.setText("贫困户属性：低保兜底户");
                        break;
                    case "3":
                        tv_lable4.setText("贫困户属性：五保户");
                        break;
                    case "4":
                        tv_lable4.setText("贫困户属性：一般农户");
                        break;
                }
            }

            if (TextUtils.isEmpty(bean.getCity()) && TextUtils.isEmpty(bean.getTown_name()) && TextUtils.isEmpty(bean.getVillage_name())) {
                tv_address.setText("  住址：");
            } else {
                tv_address.setText("  住址：" + bean.getCity() + bean.getTown_name() + bean.getVillage_name());
            }


            return convertView;
        }
    }


}
