package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.bean.Archive;
import com.huatang.fupin.bean.Fund;
import com.huatang.fupin.bean.Policy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DanganPolicyActivity extends BaseActivity {


    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.title_tx)
    TextView tvTitle;
    @BindView(R.id.lv_policy)
    ListView lv_policy;
    private List<Policy> policyList;



    private Archive archive;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangan_policy);
        ButterKnife.bind(this);
        archive = (Archive) getIntent().getSerializableExtra("archive");
        if(archive == null){
            return;
        }
        tvTitle.setText("帮扶政策信息");
        policyList = archive.getPolicy()==null ? new ArrayList<Policy>() :  archive.getPolicy();
        initData();
    }
    private void initData() {
        lv_policy.setAdapter(new PolicyAdapter(this,policyList));
    }
    class PolicyAdapter extends BaseAdapter {

        private Context mcontext;
        private List<Policy> mpolicyList ;
        public PolicyAdapter(Context context,List<Policy> policyList){
            this.mcontext = context;
            this.mpolicyList = policyList;
        }

        @Override
        public int getCount() {
            return mpolicyList.size();
        }

        @Override
        public Object getItem(int position) {
            return mpolicyList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate((Activity)mcontext, R.layout.item_policy, null);
                viewHolder.tv_poorfcard = convertView.findViewById(R.id.tv_poorfcard);
                viewHolder.tv_town_name = convertView.findViewById(R.id.tv_town_name);
                viewHolder.tv_village_name = convertView.findViewById(R.id.tv_village_name);
                viewHolder.tv_big_cateid = convertView.findViewById(R.id.tv_big_cateid);
                viewHolder.tv_second_cateid = convertView.findViewById(R.id.tv_second_cateid);
                viewHolder.tv_smail_cateid = convertView.findViewById(R.id.tv_smail_cateid);
                viewHolder.tv_policy_time = convertView.findViewById(R.id.tv_policy_time);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Policy policy = mpolicyList.get(position);
            viewHolder.tv_poorfcard.setText(policy.getPoor_card());
            viewHolder.tv_town_name.setText(policy.getTown_name());
            viewHolder.tv_village_name.setText(policy.getVillage_name());
            viewHolder.tv_big_cateid.setText(policy.getMeasure_big_id());
            viewHolder.tv_second_cateid.setText(policy.getMeasure_mid_id());
            viewHolder.tv_smail_cateid.setText(policy.getMeasure_sml_id());
            viewHolder.tv_policy_time.setText(policy.getEnjoy_policy_time());
            return convertView;
        }
    }
    class ViewHolder{

        private TextView tv_poorfcard;
        private TextView tv_town_name;
        private TextView tv_village_name;
        private TextView tv_big_cateid;
        private TextView tv_second_cateid;
        private TextView tv_smail_cateid;
        private TextView tv_policy_time;
    }
    @OnClick(R.id.left_menu)
    public void onViewClicked() {
        finish();
    }

    public static void startIntent(Activity activity, Archive archive) {
        Intent it = new Intent(activity, DanganPolicyActivity.class);
        it.putExtra("archive", archive);
        activity.startActivity(it);
    }
}
