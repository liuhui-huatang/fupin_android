package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.bean.Cloumn;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 专栏信息详情
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class ZhuanlanDetailsActivity extends BaseActivity {



    @BindView(R.id.right_tx_menu)
    TextView rightMenu;
    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_item_title)
    TextView tvItemTitle;

    @BindView(R.id.tv_item_time)
    TextView tvItemTime;
//    @BindView(R.id.iv_item_photo)
//    ImageView ivItemPhoto;
//    @BindView(R.id.tv_item_text)
//    TextView tvItemText;
    @BindView(R.id.webview)
    WebView webview;


    /*
     * @ forever 在 17/5/17 下午2:28 创建
     *
     * 描述：跳转到登录页面
     *
     */
    public static void startIntent(Activity activity, Cloumn bean) {
        Intent it = new Intent(activity, ZhuanlanDetailsActivity.class);
        it.putExtra("cloumn", bean);
        activity.startActivity(it);
    }

    /*
    * @ forever 在 17/5/17 下午2:28 创建
    *
    * 描述：页面创建时调用
    *
    */
    Cloumn bean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        bean= (Cloumn) getIntent().getSerializableExtra("cloumn");
        initView();
    }

    public void initView() {
//        GlideUtils.displayHome(ivItemPhoto, bean.getClo_avtar());
//        tvItemText.setText("      "+bean.getClo_content());

        tvItemTitle.setText(bean.getClo_title());
        tvItemTime.setText("  " + bean.getCreate_time());

        webview.loadDataWithBaseURL(null,bean.getClo_content(),"text/html","utf-8",null);
    }


    @OnClick({R.id.left_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
        }
    }




}
