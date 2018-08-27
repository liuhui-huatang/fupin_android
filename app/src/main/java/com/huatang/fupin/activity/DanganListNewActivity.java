package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.bean.Archive;
import com.huatang.fupin.bean.Fund;
import com.huatang.fupin.bean.Info;
import com.huatang.fupin.bean.NewBasic;
import com.huatang.fupin.bean.NewFamily;
import com.huatang.fupin.bean.NewLeader;
import com.huatang.fupin.bean.NewPoor;
import com.huatang.fupin.bean.NewRevenue;
import com.huatang.fupin.bean.Policy;
import com.huatang.fupin.http.NewHttpRequest;
import com.huatang.fupin.utils.GlideUtils;
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

public class DanganListNewActivity extends BaseActivity {


    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.title_tx)
    TextView tvTitle;
    @BindView(R.id.right_tx_menu)
    TextView rightMenu;
    private NewLeader leader;
    private String year;
    private int curentPosition = 0;

    /*
     * @ forever 在 17/5/17 下午2:28 创建
     *
     * 描述：跳转到登录页面
     *
     */
    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, DanganListNewActivity.class);
        activity.startActivity(it);
    }

    /*
     * @ forever 在 17/5/17 下午2:28 创建
     *
     * 描述：页面创建时调用
     *
     */
    private Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangan);
        ButterKnife.bind(this);
        initHeadView();
        getLeaderInfo();
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
                list.clear();
                getData();

            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(1000);
            }
        });

        adapter= new Adapter(this);
        listview.setAdapter(adapter);


        //条目点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // DanganXinxiActivity.startIntent(DanganListNewActivity.this, list.get(position));
                view.findViewById(R.id.dangan_detail_layout).setVisibility(View.VISIBLE);
                curentPosition = position;
            }
        });

        tvEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivity.startIntent(DanganListNewActivity.this);
            }
        });
        getData();

    }

    private void getLeaderInfo() {
        leader = new NewLeader();
        if(SPUtil.getString(Config.Type).equals(Config.GANBU_TYPE)){
            leader= (NewLeader)SPUtil.getObject(Config.GANBU_KEY);
        }else if(SPUtil.getString(Config.Type).equals(Config.GANBU_TYPE)){
            leader = (NewLeader)SPUtil.getObject(Config.ADMIN_KEY);
        }
    }

    private void initHeadView() {

        tvTitle.setText("档案信息");
        rightMenu.setText("切换年限");
        rightMenu.setVisibility(View.VISIBLE);
    }


    List<Archive> list = new ArrayList<>();
    public void getData() {
        NewHttpRequest.getArchivesWithLeader(this,String.valueOf(leader.getId()),year, new NewHttpRequest.MyCallBack(){

            @Override
            public void ok(String json) {
                list = JsonUtil.toList(json, Archive.class);
                if (list.size() > 0) {
                    tvEmpty.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
                }

            }

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);

            }
        });
    }

    @OnClick({R.id.left_menu, R.id.right_tx_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.right_tx_menu:
                String[] words2 = new String[5];
                words2[0] = "2016";
                words2[1] = "2017";
                words2[2] = "2018";
                words2[3] = "2019";
                words2[4] = "2020";

                DialogUIUtils.showSingleChoose(DanganListNewActivity.this, "请选择年度", -1, words2, new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {

                        if (TextUtils.isEmpty(text)) {
                            return;
                        }
                        year = text.toString();
                        getData();

                    }
                }).show();
                break;
        }
    }


    class Adapter extends BaseAdapter implements View.OnClickListener {

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
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.item_new_dangan, null);
                holder.iv_photo = convertView.findViewById(R.id.iv_photo);
                holder.tv_address = convertView.findViewById(R.id.tv_address);
                holder.tv_name = convertView.findViewById(R.id.tv_name);
                holder.tv_phone = convertView.findViewById(R.id.tv_phone);
                holder.iv_show_detail = convertView.findViewById(R.id.iv_show_detail);
                holder.dangan_detail_layout = convertView.findViewById(R.id.dangan_detail_layout);
                holder.dangan_info_layout = convertView.findViewById(R.id.dangan_info_layout);

                holder.jiben_layout = convertView.findViewById(R.id.jiben_layout);
                holder.family_layout = convertView.findViewById(R.id.family_layout);
                holder.shenghuo_layout = convertView.findViewById(R.id.shenghuo_layout);
                holder.shouru_layout = convertView.findViewById(R.id.shouru_layout);
                holder.ziyuan_layout = convertView.findViewById(R.id.ziyuan_layout);
                holder.cuoshi_layout = convertView.findViewById(R.id.cuoshi_layout);
                holder.zijin_layout = convertView.findViewById(R.id.zijin_layout);
                holder.pingjia_layout = convertView.findViewById(R.id.pingjia_layout);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            Archive archive = list.get(position);
            if(archive !=null ){
                NewPoor poor = archive.getPoor();
                GlideUtils.LoadCircleImageWithoutBorderColor((Activity)mContext, poor.getPhoto(),holder.iv_photo);
                holder.tv_name.setText(  poor.getFname());
                holder.tv_address.setText(  poor.getVillage_name());
                holder.tv_phone.setText(poor.getFphone());
                holder.dangan_info_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if( holder.dangan_detail_layout.getVisibility() == View.VISIBLE){
                            holder.dangan_detail_layout.setVisibility(View.GONE);
                            holder.iv_show_detail.setImageResource(R.mipmap.icon_common_right);
                        }else{
                            holder.dangan_detail_layout.setVisibility(View.VISIBLE);
                            holder.iv_show_detail.setImageResource(R.mipmap.icon_common_down);
                        }
                    }
                });
                holder.jiben_layout.setOnClickListener(this);
                List<NewFamily> families = archive.getFamily();
                if(families != null && families.size()>0){
                    holder.family_layout.setVisibility(View.VISIBLE);
                }else{
                    holder.family_layout.setVisibility(View.GONE);
                }
                holder.family_layout.setOnClickListener(this);
                NewBasic newBasic = archive.getBasic();
                if(newBasic.isIshave()){
                    holder.shenghuo_layout.setVisibility(View.VISIBLE);
                }else{
                    holder.shenghuo_layout.setVisibility(View.GONE);
                }
                holder.shenghuo_layout.setOnClickListener(this);
                NewRevenue revenue = archive.getReve();
                if(revenue.isIshave()){
                    holder.shouru_layout.setVisibility(View.VISIBLE);

                }else{
                    holder.shouru_layout.setVisibility(View.GONE);
                }
                holder.shouru_layout.setOnClickListener(this);
                Info info = archive.getInfo();
                if(info.isIshave()){
                    holder.ziyuan_layout.setVisibility(View.VISIBLE);

                }else{
                    holder.ziyuan_layout.setVisibility(View.GONE);
                }
                holder.ziyuan_layout.setOnClickListener(this);
                List<Policy> policy = archive.getPolicy();
                if(policy != null && policy.size() >0){
                    holder.cuoshi_layout.setVisibility(View.VISIBLE);
                }else{
                    holder.cuoshi_layout.setVisibility(View.GONE);
                }
                holder.cuoshi_layout.setOnClickListener(this);
                List<Fund> fund = archive.getFunds();
                if(fund != null && fund.size() >0 ){
                    holder.zijin_layout.setVisibility(View.VISIBLE);
                }else{
                    holder.zijin_layout.setVisibility(View.GONE);
                }
                holder.zijin_layout.setOnClickListener(this);
            }
            return convertView;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.jiben_layout:
                    DanganJibenActivity.startIntent((Activity)mContext,list.get(curentPosition));
                    break;
                case R.id.family_layout:
                    DanganJiatingActivity.startIntent((Activity)mContext,list.get(curentPosition));
                    break;
                case R.id.shenghuo_layout:
                    DanganBasciActivity.startIntent((Activity)mContext,list.get(curentPosition));
                    break;
                case R.id.shouru_layout:
                    DanganShouruActivity.startIntent((Activity)mContext,list.get(curentPosition));
                    break;
                case R.id.ziyuan_layout:
                    DanganInfoActivity.startIntent((Activity)mContext,list.get(curentPosition));
                    break;
                case R.id.cuoshi_layout:
                    DanganPolicyActivity.startIntent((Activity)mContext,list.get(curentPosition));
                    break;
                case R.id.zijin_layout:
                    DanganFundActivity.startIntent((Activity)mContext,list.get(curentPosition));
                    break;
            }
        }
    }
    private class ViewHolder {
        ImageView iv_photo;
        TextView tv_name;
        TextView tv_address;
        TextView tv_phone;
        ImageView iv_show_detail;
        LinearLayout dangan_detail_layout;
        RelativeLayout dangan_info_layout;
        RelativeLayout jiben_layout;
        RelativeLayout family_layout;
        RelativeLayout shenghuo_layout;
        RelativeLayout shouru_layout;
        RelativeLayout ziyuan_layout;
        RelativeLayout cuoshi_layout;
        RelativeLayout zijin_layout;
        RelativeLayout pingjia_layout;
    }
}
