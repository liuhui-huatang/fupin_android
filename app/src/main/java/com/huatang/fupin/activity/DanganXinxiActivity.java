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
import com.huatang.fupin.app.Config;
import com.huatang.fupin.bean.Archive;
import com.huatang.fupin.bean.Basic;
import com.huatang.fupin.bean.NewFamily;
import com.huatang.fupin.bean.NewPoor;
import com.huatang.fupin.http.NewHttpRequest;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.ToastUtil;

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

    private final static String DATA_KEY_ARCHIVE=  "archive";
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
    @BindView(R.id.title_tx)
    TextView tvTitle;
    @BindView(R.id.rl_jiating)
    RelativeLayout rlJiating;
    @BindView(R.id.rl_ganbu)
    RelativeLayout rlGanbu;
    private  Archive archive ;
    private String type;


    /*
         * @ forever 在 17/5/17 下午2:28 创建
         *
         * 描述：跳转到登录页面
         *
         */
    public static void startIntent(Activity activity,Archive archive) {
        Intent it = new Intent(activity, DanganXinxiActivity.class);
        it.putExtra(DATA_KEY_ARCHIVE,archive);
        activity.startActivity(it);
    }

    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, DanganXinxiActivity.class);
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
        setContentView(R.layout.activity_danganinfo);
        ButterKnife.bind(this);
        type = SPUtil.getString(Config.Type);
        archive = new Archive();

        if(type.equals(Config.PENKUNHU_TYPE)){
            getArchive();
            rlGanbu.setVisibility(View.VISIBLE);
        }else{
            archive = (Archive) getIntent().getSerializableExtra(DATA_KEY_ARCHIVE);
            rlGanbu.setVisibility(View.GONE);
        }
        if(archive!= null){
            tvTitle.setText(archive.getPoor().getFname());

        }
    }
    private void getArchive(){
        String year = SPUtil.getString(Config.YEAR);
        NewPoor poor = (NewPoor) SPUtil.getObject(Config.PENKUNHU_KEY);
        if(poor != null ){
            NewHttpRequest.getArchivesWithFcard(this,poor.getFcard(),year,new NewHttpRequest.MyCallBack(){

                @Override
                public void ok(String json) {
                    archive = JsonUtil.json2Bean(json,Archive.class);
                }

                @Override
                public void no(String msg) {
                    ToastUtil.show(msg);

                }
            });
        }else{
            ToastUtil.show("系统出错了，请重新登录");
        }

    }


    @OnClick({R.id.left_menu, R.id.rl_xinxi, R.id.rl_shouru, R.id.rl_jihua, R.id.rl_cuoshi, R.id.rl_taizhang, R.id.rl_jiating, R.id.rl_ganbu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.rl_xinxi://基本信息
                DanganJibenActivity.startIntent(this,archive);
                break;
            case R.id.rl_jiating://家庭
                DanganJiatingActivity.startIntent(this, archive);
                break;
            case R.id.rl_ganbu://帮扶干部
                DanganGanbuActivity.startIntent(this, archive.getPoor());
                break;
            case R.id.rl_shouru://shouru
                DanganShouruActivity.startIntent(this,archive);
                break;


        }
    }


}
