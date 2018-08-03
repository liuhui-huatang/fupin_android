package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.bean.Family;
import com.huatang.fupin.bean.Leader;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.ViewHolderUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 贫困户帮扶干部列表页面
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class DanganGanbuActivity extends BaseActivity {

    @BindView(R.id.lv_leader)
    ListView lvLeader;
    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    /*
         * @ forever 在 17/5/17 下午2:28 创建
         *
         * 描述：跳转到登录页面
         *
         */
    public static void startIntent(Activity activity,String basic_id) {
        Intent it = new Intent(activity, DanganGanbuActivity.class);
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
        setContentView(R.layout.activity_danganganbu);
        ButterKnife.bind(this);

        getData(getIntent().getStringExtra("basic_id"));
    }

    List<Leader> list = new ArrayList<>();

    public void getData(String basic_id) {
        HttpRequest.getBasicLeader(this, basic_id, new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                list = JsonUtil.toList(json, Leader.class);

                if (list.size() > 0) {
                    tvEmpty.setVisibility(View.GONE);
                    lvLeader.setVisibility(View.VISIBLE);
                    initData();
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                    lvLeader.setVisibility(View.GONE);
                }

            }
        });
    }

    public void initData(){
        lvLeader.setAdapter(new BaseAdapter() {
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
                    convertView = View.inflate(DanganGanbuActivity.this, R.layout.item_leader, null);
                }
                TextView tv_name = ViewHolderUtil.get(convertView, R.id.tv_name);
                TextView tv_danwei = ViewHolderUtil.get(convertView, R.id.tv_danwei);
                TextView tv_zhiwu = ViewHolderUtil.get(convertView, R.id.tv_zhiwu);
                TextView tv_phone = ViewHolderUtil.get(convertView, R.id.tv_phone);

                Leader bean = list.get(position);
                tv_name.setText(bean.getLeader_name());
                tv_danwei.setText(bean.getLeader_duty());
                tv_zhiwu.setText(bean.getLeader_unit());
                tv_phone.setText(bean.getLeader_phone());

                return convertView;
            }
        });
    }


    @OnClick(R.id.left_menu)
    public void onViewClicked() {
        finish();
    }
}
