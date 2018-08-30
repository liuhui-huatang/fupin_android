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

import com.dou361.dialogui.DialogUIUtils;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.bean.Archive;
import com.huatang.fupin.bean.Family;
import com.huatang.fupin.bean.Leader;
import com.huatang.fupin.bean.NewLeader;
import com.huatang.fupin.bean.NewPoor;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.http.NewHttpRequest;
import com.huatang.fupin.utils.GlideUtils;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.ToastUtil;
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
    @BindView(R.id.title_tx)
    TextView tvTitle;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    private NewPoor poor;

    /*
         * @ forever 在 17/5/17 下午2:28 创建
         *
         * 描述：跳转到登录页面
         *
         */
    public static void startIntent(Activity activity,Archive archive) {
        Intent it = new Intent(activity, DanganGanbuActivity.class);
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
        setContentView(R.layout.activity_danganganbu);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Archive archive = (Archive)intent.getSerializableExtra("archive");
        if(archive == null){
            return;
        }
        poor = archive == null ? (NewPoor) SPUtil.getObject(Config.PENKUNHU_KEY): archive.getPoor();
        initHeadView();
        getData();
    }

    private void initHeadView() {
        tvTitle.setText("帮扶干部详情");
    }


    List<NewLeader> list = new ArrayList<>();

    public void getData() {
        NewHttpRequest.getLeaderByPoorFcard(this,poor.getFcard(),new NewHttpRequest.MyCallBack(this){
            @Override
            public void ok(String json) {
                list = JsonUtil.toList(json, NewLeader.class);
                if (list.size() > 0) {
                    tvEmpty.setVisibility(View.GONE);
                    lvLeader.setVisibility(View.VISIBLE);
                    initData();
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                    lvLeader.setVisibility(View.GONE);
                }
            }

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);

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
                ImageView iv_photo =  ViewHolderUtil.get(convertView, R.id.iv_photo);
                TextView tv_name = ViewHolderUtil.get(convertView, R.id.tv_name);
                TextView tv_zhiwu = ViewHolderUtil.get(convertView, R.id.tv_zhiwu);
                TextView tv_phone = ViewHolderUtil.get(convertView, R.id.tv_phone);
                TextView tv_pingjia = ViewHolderUtil.get(convertView,R.id.pingjia_btn);

                final NewLeader leader = list.get(position);
                GlideUtils.LoadCircleImageWithoutBorderColor(DanganGanbuActivity.this, BaseConfig.ImageUrl+leader.getLeader_photo(),iv_photo);
                tv_name.setText(leader.getLeader_name());

                tv_zhiwu.setText(leader.getLeader_unit());
                tv_phone.setText(leader.getLeader_phone());
                tv_pingjia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PingJiaActivity.startIntent(DanganGanbuActivity.this,leader);
                    }
                });

                return convertView;
            }
        });
        lvLeader.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PingJiaActivity.startIntent(DanganGanbuActivity.this,list.get(position));
            }
        });
    }


    @OnClick(R.id.left_menu)
    public void onViewClicked() {
        finish();
    }
}
