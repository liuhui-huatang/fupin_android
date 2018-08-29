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

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.bean.Archive;
import com.huatang.fupin.bean.NewRevenue;
import com.huatang.fupin.bean.Revenue;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.MLog;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.ToastUtil;
import com.huatang.fupin.utils.ViewHolderUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 贫困户收入页面
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class DanganShouruActivity extends BaseActivity {


    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.title_tx)
    TextView tvTitle;
    @BindView(R.id.lv_revenue)
    ListView lvRevenue;
    private Archive archive;

    /*
             * @ forever 在 17/5/17 下午2:28 创建
             *
             * 描述：跳转到登录页面
             *
             */
    public static void startIntent(Activity activity, Archive archive) {
        Intent it = new Intent(activity, DanganShouruActivity.class);
        it.putExtra("archive", archive);
        activity.startActivity(it);
    }

    /*
    * @ forever 在 17/5/17 下午2:28 创建
    *
    * 描述：页面创建时调用
    *
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangansouru);
        ButterKnife.bind(this);
        archive = (Archive)getIntent().getSerializableExtra("archive");
        if(archive == null){
            return;
        }
        tvTitle.setText("收支情况");
        initData();
    }

    List<String> keys = new ArrayList<>();
    List<String> values = new ArrayList<>();

    public void initData() {
        keys.add("收入年份(年)");
        keys.add("工资性收入(元)");
        keys.add("生产经营性收入(元)");
        keys.add("财产收入(元)");
        keys.add("其他转移性收入(元)");
        keys.add("转移性收入(元)");
        keys.add("年收入(元)");
        keys.add("纯收入(元)");
        keys.add("人均收入(元)");
        keys.add("生产经营性支出(元)");
        keys.add("计划生育金(元)");
        keys.add("低保金(元)");
        keys.add("五保金(元)");
        keys.add("养老保险金(元)");
        keys.add("生态补偿金(元)");


        getData(year);

    }

    String year= SPUtil.getString(Config.YEAR);
    private void getData(String year) {
        values.clear();
//        MLog.d("getBasicRevenue==", getIntent().getStringExtra("basic_id"));

        NewRevenue revenue = archive.getReve();
                if(revenue==null){
                    ToastUtil.show("当前年度未录入数据");
                    return;
                }
                values.add(revenue.getYear()+"  >");
                values.add(revenue.getIncome_wage());
                values.add(revenue.getProduction_income());
                values.add(revenue.getProperty_income());
                values.add(revenue.getOther_transfer_income());
                values.add(revenue.getTransfer_income());
                values.add(revenue.getAllyear_income());
                values.add(revenue.getNet_income());
                values.add(revenue.getPer_capita());
                values.add(revenue.getProduction_costs());
                values.add(revenue.getFamily_planning());
                values.add(revenue.getLow_income());
                values.add(revenue.getSupporting_gold());
                values.add(revenue.getOld_age_income());
                values.add(revenue.getEcology_income());

                lvRevenue.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return keys.size();
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
                            convertView = View.inflate(DanganShouruActivity.this, R.layout.item_layout, null);
                        }
                        TextView tv_name = ViewHolderUtil.get(convertView, R.id.tv_name);
                        TextView tv_value = ViewHolderUtil.get(convertView, R.id.tv_value);

                        tv_name.setText(keys.get(position));
                        tv_value.setText(values.get(position));

                        return convertView;
                    }
                });


    }

    @OnClick(R.id.left_menu)
    public void onViewClicked() {
        finish();
    }
}
