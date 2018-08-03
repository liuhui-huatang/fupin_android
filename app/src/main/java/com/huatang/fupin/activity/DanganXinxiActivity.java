package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.bean.Basic;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 贫困户档案列表
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class DanganXinxiActivity extends BaseActivity {


    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.rl_xinxi)
    RelativeLayout rlXinxi;
    @BindView(R.id.rl_shouru)
    RelativeLayout rlShouru;
    @BindView(R.id.rl_jihua)
    RelativeLayout rlJihua;
    @BindView(R.id.rl_cuoshi)
    RelativeLayout rlCuoshi;
    @BindView(R.id.rl_taizhang)
    RelativeLayout rlTaizhang;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_jiating)
    RelativeLayout rlJiating;
    @BindView(R.id.rl_ganbu)
    RelativeLayout rlGanbu;

    /*
         * @ forever 在 17/5/17 下午2:28 创建
         *
         * 描述：跳转到登录页面
         *
         */
    public static void startIntent(Activity activity, Basic bean) {
        Intent it = new Intent(activity, DanganXinxiActivity.class);
        it.putExtra("basic", bean);
        activity.startActivity(it);
    }


    /*
    * @ forever 在 17/5/17 下午2:28 创建
    *
    * 描述：页面创建时调用
    *
    */
    Basic bean ;
    String basic_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danganinfo);
        bean = (Basic) getIntent().getSerializableExtra("basic");
        basic_id=bean.getId();
        ButterKnife.bind(this);

        if(bean!=null&&bean.getFname()!=null){
            tvTitle.setText(bean.getFname());
        }
    }


    @OnClick({R.id.left_menu, R.id.rl_xinxi, R.id.rl_shouru, R.id.rl_jihua, R.id.rl_cuoshi, R.id.rl_taizhang, R.id.rl_jiating, R.id.rl_ganbu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.rl_xinxi:
                DanganJibenActivity.startIntent(this,bean);
                break;
            case R.id.rl_jiating:
                DanganJiatingActivity.startIntent(this, basic_id);
                break;
            case R.id.rl_ganbu:
                DanganGanbuActivity.startIntent(this, basic_id);
                break;
            case R.id.rl_shouru:
                DanganShouruActivity.startIntent(this,basic_id);
                break;
//            case R.id.rl_jihua:
//                DanganJihuaActivity.startIntent(this,basic_id);
//                break;
//            case R.id.rl_cuoshi:
//                DanganCuoshiActivity.startIntent(this);
//                break;
//            case R.id.rl_taizhang:
//                DanganTaizhangActivity.startIntent(this);
//                break;
        }
    }


}
