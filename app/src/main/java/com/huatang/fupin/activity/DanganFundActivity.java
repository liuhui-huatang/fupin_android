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
import com.huatang.fupin.bean.NewFamily;
import com.huatang.fupin.utils.ViewHolderUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DanganFundActivity extends BaseActivity {

    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.title_tx)
    TextView tvTitle;
    @BindView(R.id.lv_fund)
    ListView lv_fund;
    private List<Fund> fundList;



    private Archive archive;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangan_fund);
        ButterKnife.bind(this);
        archive = (Archive) getIntent().getSerializableExtra("archive");
        tvTitle.setText("帮扶资金信息");
        fundList = archive.getFunds()==null ? new ArrayList<Fund>() :  archive.getFunds();
        initData();
    }
    private void initData() {
        lv_fund.setAdapter(new FundAdapter(this,fundList));
    }
   class FundAdapter extends BaseAdapter{

        private Context mcontext;
        private List<Fund> mfundList ;
       public FundAdapter(Context context,List<Fund> fundList){
           this.mcontext = context;
           this.mfundList = fundList;
       }

        @Override
        public int getCount() {
            return mfundList.size();
        }

        @Override
        public Object getItem(int position) {
            return mfundList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate((Activity)mcontext, R.layout.item_fund, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_year = convertView.findViewById(R.id.tv_year);
                viewHolder.tv_fcard = convertView.findViewById(R.id.tv_fcard);
                viewHolder.tv_town_name = convertView.findViewById(R.id.tv_town_name);
                viewHolder.tv_village_name = convertView.findViewById(R.id.tv_village_name);
                viewHolder.tv_big_cateid = convertView.findViewById(R.id.tv_big_cateid);
                viewHolder.tv_second_cateid = convertView.findViewById(R.id.tv_second_cateid);
                viewHolder.tv_smail_cateid = convertView.findViewById(R.id.tv_smail_cateid);
                viewHolder.tv_money = convertView.findViewById(R.id.tv_money);
                viewHolder.tv_fund_time = convertView.findViewById(R.id.tv_fund_time);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Fund fund = mfundList.get(position);
            viewHolder.tv_year.setText(fund.getYear());
            viewHolder.tv_fcard.setText(fund.getFcard());
            viewHolder.tv_town_name .setText(fund.getTown_name());
            viewHolder.tv_village_name .setText(fund.getVillage_name());
            viewHolder.tv_big_cateid.setText(fund.getBig_cateid());
            viewHolder.tv_second_cateid .setText(fund.getSecond_cateid());
            viewHolder.tv_smail_cateid.setText(fund.getSmail_cateid());
            viewHolder.tv_money .setText(fund.getMoney());
            viewHolder.tv_fund_time .setText(fund.getFund_time());
            return convertView;
        }
    }
    class ViewHolder{

        private TextView tv_year;
        private TextView tv_fcard;
        private TextView tv_town_name;
        private TextView tv_village_name;
        private TextView tv_big_cateid;
        private TextView tv_second_cateid;
        private TextView tv_smail_cateid;
        private TextView tv_money;
        private TextView tv_fund_time;
    }



    @OnClick(R.id.left_menu)
    public void onViewClicked() {
        finish();
    }
    public static void startIntent(Activity activity, Archive archive) {
        Intent it = new Intent(activity, DanganFundActivity.class);
        it.putExtra("archive", archive);
        activity.startActivity(it);
    }
}
