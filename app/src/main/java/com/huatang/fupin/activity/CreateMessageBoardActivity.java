package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.bean.NewPoor;
import com.huatang.fupin.http.NewHttpRequest;
import com.huatang.fupin.utils.GlideUtils;
import com.huatang.fupin.utils.ImageUtil;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.MLog;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.StringUtil;
import com.huatang.fupin.utils.ToastUtil;
import com.huatang.fupin.utils.UploadUtils;
import com.huatang.fupin.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

public class CreateMessageBoardActivity extends BaseActivity {
    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.title_tx)
    TextView tvTitle;
    @BindView(R.id.right_menu)
    ImageView rightMenu;
    @BindView(R.id.right_tx_menu)
    TextView right_tx_menu;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_text)
    EditText etText;
    @BindView(R.id.iv_sign_1)
    ImageView ivSign1;
    @BindView(R.id.iv_sign_2)
    ImageView ivSign2;
    @BindView(R.id.iv_sign_3)
    ImageView ivSign3;
    @BindView(R.id.iv_sign_4)
    ImageView ivSign4;
    @BindView(R.id.iv_sign_add1)
    ImageView ivSignAdd1;
    @BindView(R.id.iv_sign_6)
    ImageView ivSign6;
    @BindView(R.id.iv_sign_7)
    ImageView ivSign7;
    @BindView(R.id.iv_sign_8)
    ImageView ivSign8;
    @BindView(R.id.iv_sign_9)
    ImageView ivSign9;
    @BindView(R.id.iv_sign_add_2)
    ImageView ivSignAdd2;
    private List <ImageView>imageViewList;
    private List<String> imagePathList;
    private NewPoor poor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message_borad);
        ButterKnife.bind(this);
        imageViewList = new ArrayList<>();
        imagePathList = new ArrayList<>();
        initView();

        poor = (NewPoor)SPUtil.getObject(Config.PENKUNHU_KEY);
        if(poor == null){
            return;
        }

    }
    private void initView(){
        initHeadView();
        initImageView();


    }

    private void initHeadView() {
        right_tx_menu.setText("提交");
        right_tx_menu.setVisibility(View.VISIBLE);
        tvTitle.setText("提交留言");
    }
    public void initImageView() {
        imageViewList.add(ivSign1);
        imageViewList.add(ivSign2);
        imageViewList.add(ivSign3);
        imageViewList.add(ivSign4);
        imageViewList.add(ivSign6);
        imageViewList.add(ivSign7);
        imageViewList.add(ivSign8);
        imageViewList.add(ivSign9);
    }
    @OnClick({R.id.left_menu, R.id.right_tx_menu,R.id.iv_sign_add1, R.id.iv_sign_add_2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;

            case R.id.right_tx_menu:
                right_tx_menu.setClickable(false);
                String title = etTitle.getText().toString().trim();
                String content = etText.getText().toString().trim();
                if (TextUtils.isEmpty(title)) {
                    ToastUtil.show("标题未填写");
                    right_tx_menu.setClickable(true);
                    return;
                }
                if (title.length() < 2) {
                    ToastUtil.show("标题不得少于2个字");
                    right_tx_menu.setClickable(true);
                    return;
                }

                if (TextUtils.isEmpty(content)) {
                    ToastUtil.show("内容暂未填写");
                    right_tx_menu.setClickable(true);
                    return;
                }
                if (content.length() < 5) {
                    ToastUtil.show("内容不得少于5个字");
                    right_tx_menu.setClickable(true);
                    return;
                }
                createMessageBoard(title,content);


                break;

            case R.id.iv_sign_add1:
            case R.id.iv_sign_add_2:
                Utils.hideKeyboard(this);
                showSelectPicture();
                break;
        }
    }

    private void createMessageBoard(String title,String content) {

        NewHttpRequest.createLeaveMsg(this, title, content, StringUtil.listToString(imagePathList, StringUtil.separator), poor, new NewHttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                ToastUtil.show("留言成功");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);

            }
        });
    }

    private void showSelectPicture() {
        UploadUtils.getmInstance().start(this, R.id.ll_message_board, new UploadUtils.MyCallBack() {
            @Override
            public void success(String url) {
                imagePathList.add(url);
                setImageShow();
            }
        });
    }


    public void setImageShow() {
        if (imagePathList.size() >= 4) {
            ivSignAdd1.setVisibility(View.GONE);
            ivSignAdd2.setVisibility(View.VISIBLE);
        }
        if (imagePathList.size() == 8) {
            ivSignAdd1.setVisibility(View.GONE);
            ivSignAdd2.setVisibility(View.GONE);
        }
        for (int i = 0; i < imagePathList.size(); i++) {

            GlideUtils.displayHome(imageViewList.get(i), BaseConfig.ImageUrl+imagePathList.get(i));
            imageViewList.get(i).setVisibility(View.VISIBLE);
        }
    }

    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, CreateMessageBoardActivity.class);
        activity.startActivity(it);
    }
}
