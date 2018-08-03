package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * web view页面
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class WebActivity extends BaseActivity {


    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    /*
     * @ forever 在 17/5/17 下午2:28 创建
     *
     * 描述：跳转到登录页面
     *
     */
    public static void startIntent(Activity activity, String url) {
        Intent it = new Intent(activity, WebActivity.class);
        it.putExtra("url", url);
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
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void  onPageStarted(WebView view, String url, Bitmap favicon) {
                //设定加载开始的操作
                DialogUIUtils.showTie(WebActivity.this, "加载中...");
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                //设定加载结束的操作
                DialogUIUtils.dismssTie();
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //handler.cancel(); // Android默认的处理方式
                handler.proceed();  // 接受所有网站的证书
                //handleMessage(Message msg); // 进行其他处理
            }
        });
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDefaultTextEncodingName("utf-8");//设置编码格式
        //设置自适应屏幕，两者合用
        webview.getSettings().setUseWideViewPort(true); //将图片调整到适合webview的大小
        webview.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webview.getSettings().setLoadsImagesAutomatically(true); //支持自动加载图片

        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setTextSize(WebSettings.TextSize.LARGEST);


        webview.loadUrl(getIntent().getStringExtra("url"));
    }


    @OnClick(R.id.left_menu)
    public void onViewClicked() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            finish();
        }
    }
}
