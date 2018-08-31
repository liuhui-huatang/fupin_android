package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.bean.Cloumn;
import com.huatang.fupin.bean.NewColumn;
import com.huatang.fupin.http.NewHttpRequest;
import com.huatang.fupin.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UnitNewsInfoActivity extends BaseActivity {

    public final static String COLUMN = "column";

    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.title_tx)
    TextView tvTitle;


    @BindView(R.id.webview)
    WebView webview;
    private NewColumn column;


    /*
     * @ forever 在 17/5/17 下午2:28 创建
     *
     * 描述：跳转到登录页面
     *
     */
    public static void startIntent(Activity activity, NewColumn column) {
        Intent it = new Intent(activity, UnitNewsInfoActivity.class);
        it.putExtra(COLUMN, column);
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
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        column = (NewColumn) getIntent().getSerializableExtra(COLUMN);
        initView();
        initData();
    }

    private void initData() {
        NewHttpRequest.getNewsInfoWithId(this, column.getId(), new NewHttpRequest.HtmlCallBack() {

            @Override
            public void callback(String json) {
                webview.loadDataWithBaseURL(null, json, "text/html", "utf-8", null);
            }
        });
    }

    public void initView() {
        tvTitle.setText("新闻详情");

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
