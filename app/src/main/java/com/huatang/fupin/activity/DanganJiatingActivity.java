package com.huatang.fupin.activity;

import android.app.Activity;
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
import com.huatang.fupin.bean.Family;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.ViewHolderUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 贫困户家庭成员
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class DanganJiatingActivity extends BaseActivity {


    @BindView(R.id.lv_family)
    ListView lvFamily;
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
    public static void startIntent(Activity activity, String basic_id) {
        Intent it = new Intent(activity, DanganJiatingActivity.class);
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
        setContentView(R.layout.activity_danganjiating);
        ButterKnife.bind(this);


        getData(getIntent().getStringExtra("basic_id"));
    }

    List<Family> list = new ArrayList<>();

    public void getData(String basic_id) {
        HttpRequest.getBasicFamily(this, basic_id, new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                list = JsonUtil.toList(json, Family.class);

                if (list.size() > 0) {
                    tvEmpty.setVisibility(View.GONE);
                    lvFamily.setVisibility(View.VISIBLE);
                    initData();
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                    lvFamily.setVisibility(View.GONE);
                }
            }
        });
    }

    public void initData() {
        lvFamily.setAdapter(new BaseAdapter() {
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
                    convertView = View.inflate(DanganJiatingActivity.this, R.layout.item_family, null);
                }
                TextView tv_name = ViewHolderUtil.get(convertView, R.id.tv_name);
                TextView tv_card = ViewHolderUtil.get(convertView, R.id.tv_card);
                TextView tv_relation = ViewHolderUtil.get(convertView, R.id.tv_relation);
                TextView tv_low = ViewHolderUtil.get(convertView, R.id.tv_low);
                TextView tv_disability = ViewHolderUtil.get(convertView, R.id.tv_disability);

                Family bean = list.get(position);
                tv_name.setText(bean.getMname());
                tv_card.setText(bean.getMidcard());

//                        与户主关系：1.户主 2.配偶 3.之子 4.之女 5.之儿媳 6.之女婿 7.之孙子 8.之孙女 9.之外孙子 10.之外孙女 11.之父 12.之母 13.之岳父 14.之岳母 15.之公公 16.之婆婆 17.之祖父 18.之祖母 19.之外祖父 20.之外祖母 21.其他
                switch (bean.getMrelationship()) {
                    case "1":
                        tv_relation.setText("户主");
                        break;
                    case "2":
                        tv_relation.setText("配偶");
                        break;
                    case "3":
                        tv_relation.setText("之子");
                        break;
                    case "4":
                        tv_relation.setText("之女");
                        break;
                    case "5":
                        tv_relation.setText("之儿媳");
                        break;
                    case "6":
                        tv_relation.setText("之女婿");
                        break;
                    case "7":
                        tv_relation.setText("之孙子");
                        break;
                    case "8":
                        tv_relation.setText("之孙女");
                        break;
                    case "9":
                        tv_relation.setText("之外孙子");
                        break;
                    case "10":
                        tv_relation.setText("之外孙女");
                        break;
                    case "11":
                        tv_relation.setText("之父");
                        break;
                    case "12":
                        tv_relation.setText("之母");
                        break;
                    case "13":
                        tv_relation.setText("之岳父");
                        break;
                    case "14":
                        tv_relation.setText("之岳母");
                        break;
                    case "15":
                        tv_relation.setText("之公公");
                        break;
                    case "16":
                        tv_relation.setText("之婆婆");
                        break;
                    case "17":
                        tv_relation.setText("之祖父");
                        break;
                    case "18":
                        tv_relation.setText("之祖母");
                        break;
                    case "19":
                        tv_relation.setText("之外祖父");
                        break;
                    case "20":
                        tv_relation.setText("之外祖母");
                        break;
                    case "21":
                        tv_relation.setText("其他");
                        break;
                }

//                        劳动能力: 1.普通劳动力 2.丧失劳动力 3.无劳动力 4.技能劳动力

                switch (bean.getLabor_state()) {
                    case "1":
                        tv_low.setText("普通劳动力");
                        break;
                    case "2":
                        tv_low.setText("丧失劳动力");
                        break;
                    case "3":
                        tv_low.setText("无劳动力");
                        break;
                    case "4":
                        tv_low.setText("技能劳动力");
                        break;
                }

//                        健康状况: 1.健康 2.常见慢性病 3.特殊慢性病 4.重大疾病 5.残疾
                switch (bean.getHealth_state()) {
                    case "1":
                        tv_disability.setText("健康");
                        break;
                    case "2":
                        tv_disability.setText("常见慢性病");
                        break;
                    case "3":
                        tv_disability.setText("特殊慢性病");
                        break;
                    case "4":
                        tv_disability.setText("重大疾病");
                        break;
                    case "5":
                        tv_disability.setText("残疾");
                        break;
                }
                return convertView;
            }
        });

    }


    @OnClick(R.id.left_menu)
    public void onViewClicked() {
        finish();
    }
}
