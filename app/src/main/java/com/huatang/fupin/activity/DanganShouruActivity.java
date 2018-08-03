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
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lv_revenue)
    ListView lvRevenue;

    /*
             * @ forever 在 17/5/17 下午2:28 创建
             *
             * 描述：跳转到登录页面
             *
             */
    public static void startIntent(Activity activity, String basic_id) {
        Intent it = new Intent(activity, DanganShouruActivity.class);
        it.putExtra("basic_id", basic_id);
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

        lvRevenue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if("收入年份(年)".equals(keys.get(position))){
                    String[] words2 = new String[5];
                    words2[0] = "2016";
                    words2[1] = "2017";
                    words2[2] = "2018";
                    words2[3] = "2019";
                    words2[4] = "2020";

                    DialogUIUtils.showSingleChoose(DanganShouruActivity.this, "请选择年度", -1, words2, new DialogUIItemListener() {
                        @Override
                        public void onItemClick(CharSequence text, int position) {
                            if (TextUtils.isEmpty(text)) {
                                return;
                            }
                            year=text.toString();
                            getData(year);
                        }
                    }).show();
                }
            }
        });

        getData(year);

    }

    String year= SPUtil.getString("year");
    private void getData(String year) {
        values.clear();
        MLog.d("getBasicRevenue==", getIntent().getStringExtra("basic_id"));
        HttpRequest.getBasicRevenue(this, getIntent().getStringExtra("basic_id"),year, new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                Revenue bean = JsonUtil.json2Bean(json, Revenue.class);
                if(bean==null){
                    ToastUtil.show("当前年度未录入数据");
                    return;
                }
                values.add(bean.getRe_year()+"  >");
                values.add(bean.getIncome_wage());
                values.add(bean.getProduction_income());
                values.add(bean.getProperty_income());
                values.add(bean.getOther_transfer_income());
                values.add(bean.getTransfer_income());
                values.add(bean.getAllyear_income());
                values.add(bean.getNet_income());
                values.add(bean.getPer_capita());
                values.add(bean.getProduction_costs());
                values.add(bean.getFamily_planning());
                values.add(bean.getLow_income());
                values.add(bean.getSupporting_gold());
                values.add(bean.getOld_age_income());
                values.add(bean.getEcology_income());

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
        });
    }

    @OnClick(R.id.left_menu)
    public void onViewClicked() {
        finish();
    }
}
