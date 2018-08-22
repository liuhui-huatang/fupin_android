package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.bean.NewFuzeren;
import com.huatang.fupin.bean.NewLeader;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.http.NewHttpRequest;
import com.huatang.fupin.utils.GlideUtils;
import com.huatang.fupin.utils.ImageUtil;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.MLog;
import com.huatang.fupin.utils.PopWindowUtil;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 我的信息展示页面
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class MyInfoActivity extends BaseActivity {


    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rl_name)
    RelativeLayout rlName;
    @BindView(R.id.tv_zhiwu)
    TextView tvZhiwu;
    @BindView(R.id.rl_zhiwu)
    RelativeLayout rlZhiwu;
    @BindView(R.id.tv_danwei)
    TextView tvDanwei;
    @BindView(R.id.rl_danwei)
    RelativeLayout rlDanwei;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.rl_phone)
    RelativeLayout rlPhone;
    @BindView(R.id.tv_xiangzhen)
    TextView tvXiangzhen;
    @BindView(R.id.rl_xiangzhen)
    RelativeLayout rlXiangzhen;

    /*
             * @ forever 在 17/5/17 下午2:28 创建
             *
             * 描述：跳转到登录页面
             *
             */
    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, MyInfoActivity.class);
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
        setContentView(R.layout.activity_myinfo);
        ButterKnife.bind(this);

        initView();
    }

    public void initView() {
        tvName.setText(SPUtil.getString(Config.NAME));
        tvPhone.setText(SPUtil.getString(Config.PHONE));
        switch (SPUtil.getString(Config.Type)){

            case Config.PENKUNHU_TYPE:
                if(!TextUtils.isEmpty(SPUtil.getString(Config.HEAD_PHOTO))){
                    GlideUtils.LoadCircleImageWithoutBorderColor(this,SPUtil.getString(Config.HEAD_PHOTO),ivPhoto);
                }
                rlZhiwu.setVisibility(View.GONE);
                rlDanwei.setVisibility(View.GONE);
                rlXiangzhen.setVisibility(View.GONE);
                break;
            case Config.ADMIN_TYPE:
                NewLeader admin = (NewLeader) SPUtil.getObject(Config.ADMIN_KEY);
                if(!TextUtils.isEmpty(SPUtil.getString(Config.HEAD_PHOTO))){
                    GlideUtils.LoadCircleImageWithoutBorderColor(this,SPUtil.getString(Config.HEAD_PHOTO),ivPhoto);
                }else{
                    GlideUtils.LoadCircleImageWithoutBorderColor(this,admin.getLeader_photo(),ivPhoto);
                }
                tvName.setText(admin.getLeader_name());
                tvZhiwu.setText(admin.getLeader_duty());
                tvDanwei.setText(admin.getLeader_unit());
                tvPhone.setText(admin.getLeader_phone());
                tvXiangzhen.setText(admin.getHelp_town());
            case Config.GANBU_TYPE:
                NewLeader leader = (NewLeader) SPUtil.getLeaderFromSharePref();
                if(!TextUtils.isEmpty(SPUtil.getString(Config.HEAD_PHOTO))){
                    GlideUtils.LoadCircleImageWithoutBorderColor(this,SPUtil.getString(Config.HEAD_PHOTO),ivPhoto);
                }else{
                    GlideUtils.LoadCircleImageWithoutBorderColor(this,leader.getLeader_photo(),ivPhoto);
                }
                tvName.setText(leader.getLeader_name());
                tvZhiwu.setText(leader.getLeader_unit());
                tvDanwei.setText(leader.getLeader_andscape());
                tvPhone.setText(leader.getLeader_phone());
                tvXiangzhen.setText(leader.getHelp_town());
                break;
            case Config.FUZEREN_TYPE:
                NewFuzeren fuzeren = (NewFuzeren)SPUtil.getObject(Config.FUZEREN_KEY);
                if(!TextUtils.isEmpty(SPUtil.getString(Config.HEAD_PHOTO))){
                    GlideUtils.LoadCircleImageWithoutBorderColor(this,SPUtil.getString(Config.HEAD_PHOTO),ivPhoto);
                }
                tvName.setText(fuzeren.getVillage_name());
                tvZhiwu.setText(fuzeren.getVillage_chief());
                tvDanwei.setText(fuzeren.getChief_duty());
                tvPhone.setText(fuzeren.getChief_phone());
                tvXiangzhen.setVisibility(View.INVISIBLE);
                break;
            case Config.YOUKU_TYPE:
                if(!TextUtils.isEmpty(SPUtil.getString(Config.PHONE))){
                    GlideUtils.LoadCircleImageWithoutBorderColor(this,SPUtil.getString(Config.HEAD_PHOTO),ivPhoto);
                }
                rlZhiwu.setVisibility(View.GONE);
                rlDanwei.setVisibility(View.GONE);
                rlXiangzhen.setVisibility(View.GONE);
                break;
        }



    }

    @OnClick({R.id.left_menu, R.id.iv_photo, R.id.rl_name, R.id.rl_zhiwu, R.id.rl_danwei, R.id.rl_phone, R.id.rl_xiangzhen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.iv_photo:
                showSelectPicture();
                break;
            case R.id.rl_name:
//                MyEditActivity.startIntent(this, "名字", tvName.getText().toString().trim());
                break;
            case R.id.rl_zhiwu:
//                MyEditActivity.startIntent(this, "职务", tvZhiwu.getText().toString().trim());
                break;
            case R.id.rl_danwei:
//                MyEditActivity.startIntent(this, "单位", tvDanwei.getText().toString().trim());
                break;
            case R.id.rl_phone:
//                MyEditActivity.startIntent(this, "手机", tvPhone.getText().toString().trim());
                break;
            case R.id.rl_xiangzhen:
//                MyEditActivity.startIntent(this, "帮扶乡镇", tvXiangzhen.getText().toString().trim());
                break;
        }
    }


    // －－－－－－－－－－－－－－－－－－－－－－－－－－－拍照相关的方法
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;

    /*
     * @ forever 在 17/5/17 下午5:01 创建
     *
     * 描述：打开选择照片框
     *
     */
    public void showSelectPicture() {
        final FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setMutiSelectMaxSize(6)
                .build();
        final String[] btString = new String[]{"拍照", "相册"};
        PopWindowUtil.init().show(this, R.id.rl_myinfo, btString, new PopWindowUtil.OnPopupWindowClickLinstener() {

            @Override
            public void popClick(Button popButton) {
                String btStr = popButton.getText().toString();
                if (btStr.equals(btString[0])) {
                    GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);
                } else if (btStr.equals(btString[1])) {
                    //相册单选
                    GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
                }
            }
        });
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                String imagePath = resultList.get(0).getPhotoPath();
                imagePath = ImageUtil.getCompressedImgPath(imagePath);
                MLog.e("onHanlderSuccess", imagePath);
                imagesUpload(imagePath);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(MyInfoActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };


    public void imagesUpload(String filePath) {
        /**
         * 图片上传服务器
         */

        NewHttpRequest.uploadImage(this, filePath, new NewHttpRequest.UploadCallBack() {
            @Override
            public void callback(String json) {
                ToastUtil.show("修改成功");
                String photoUrl = BaseConfig.ImageUrl + JsonUtil.getStringFromArray(json,"url");
                SPUtil.saveString(Config.HEAD_PHOTO,photoUrl);
                GlideUtils.LoadCircleImageWithoutBorderColor(MyInfoActivity.this,photoUrl ,ivPhoto);

            }
        });


    }


}
