package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.bean.Archive;
import com.huatang.fupin.bean.NewLeader;
import com.huatang.fupin.bean.NewPoor;
import com.huatang.fupin.http.NewHttpRequest;
import com.huatang.fupin.utils.GlideUtils;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.techery.properratingbar.ProperRatingBar;
import io.techery.properratingbar.RatingListener;

public class PingJiaActivity extends BaseActivity {


    @BindView(R.id.left_menu)
    ImageView leftMenu;

    @BindView(R.id.title_tx)
    TextView tvTitle;

    @BindView(R.id.bt_submit)
    ImageView bt_submit;

    @BindView(R.id.tv_poor_num)
    TextView tv_poor_num;

    @BindView(R.id.ll_poor_num)
    LinearLayout ll_poor_num;

    @BindView(R.id.tv_zhiwu)
    TextView tv_zhiwu;

    @BindView(R.id.tv_town)
    TextView tv_town;

    @BindView(R.id.tv_phone)
    TextView tv_phone;

    @BindView(R.id.pingjia_et)
    EditText pingjia_et;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;

    @BindView(R.id.lowerRatingBar)
    ProperRatingBar lowerRatingBar;

    private NewLeader leader;
    private NewPoor poor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pingjia);
        ButterKnife.bind(this);
        initHeadView();
        initView();
        lowerRatingBar.setListener(ratingListener);
    }

    private int score;
    private RatingListener ratingListener = new RatingListener() {
        @Override
        public void onRatePicked(ProperRatingBar ratingBar) {
            score = 6 - ratingBar.getRating();
        }
    };
    private void initView() {
        Intent intent = getIntent();
        leader = (NewLeader) intent.getSerializableExtra("leader");
        if (leader == null) {
            return;
        }
        tvName.setText(leader.getLeader_name());
        tv_zhiwu.setText(leader.getLeader_duty());
        tv_phone.setText(leader.getLeader_phone());
        tv_town.setText(TextUtils.isEmpty(leader.getHelp_town()) ? "" : "包连乡镇:" + leader.getHelp_town());
        //tv_poor_num.setText(leader.get);
        GlideUtils.LoadCircleImageWithoutBorderColor(this, BaseConfig.ImageUrl + leader.getLeader_phone(), ivPhoto);
        poor = (NewPoor) SPUtil.getObject(Config.PENKUNHU_KEY);
        if (poor == null) {
            ToastUtil.show("身份失效，请重新登录");
            return;
        }

    }

    private void initHeadView() {
        tvTitle.setText("评价帮扶干部");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.left_menu, R.id.bt_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.bt_submit:
                String content = pingjia_et.getText().toString().trim();
                if(TextUtils.isEmpty(content)){
                    ToastUtil.show("评价内容不能为空");
                    return;
                }
                saveContent(content,score);
                break;
        }


    }

    private void saveContent(String content,int score) {
        NewHttpRequest.leaderEvaluation(this, String.valueOf(score), content, poor, leader, new NewHttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                ToastUtil.show("评价成功");
                finish();
            }

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);

            }
        });

    }

    public static void startIntent(Activity activity, NewLeader leader) {
        Intent it = new Intent(activity, PingJiaActivity.class);
        it.putExtra("leader", leader);
        activity.startActivity(it);
    }
}
