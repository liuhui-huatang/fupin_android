package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的信息编辑页面
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class MyEditActivity extends BaseActivity {


    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.right_menu)
    TextView rightMenu;
    @BindView(R.id.et_edit)
    EditText etEdit;

    /*
     * @ forever 在 17/5/17 下午2:28 创建
     *
     * 描述：跳转到登录页面
     *
     */
    public static void startIntent(Activity activity, String key, String value) {
        Intent it = new Intent(activity, MyEditActivity.class);
        it.putExtra("key", key);
        it.putExtra("value", value);
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
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        String key = getIntent().getStringExtra("key");
        String value = getIntent().getStringExtra("value");
        tvTitle.setText("编辑" + key);
        etEdit.setText(value);
    }


    @OnClick({R.id.left_menu, R.id.right_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.right_menu:
                save();
                break;
        }
    }

    public void save() {
        finish();



    }
}
