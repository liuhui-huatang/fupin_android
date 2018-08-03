package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.bean.Basic;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.utils.DateUtil;
import com.huatang.fupin.utils.GlideUtils;
import com.huatang.fupin.utils.ImageUtil;
import com.huatang.fupin.utils.MLog;
import com.huatang.fupin.utils.PopWindowUtil;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 贫困户致贫原因图片页面
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class DanganJibenImageActivity extends BaseActivity {


    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.right_menu)
    TextView rightMenu;
    @BindView(R.id.iv_01)
    ImageView iv01;
    @BindView(R.id.iv_02)
    ImageView iv02;
    @BindView(R.id.iv_03)
    ImageView iv03;
    @BindView(R.id.iv_04)
    ImageView iv04;
    @BindView(R.id.iv_05)
    ImageView iv05;
    @BindView(R.id.iv_06)
    ImageView iv06;
    @BindView(R.id.iv_07)
    ImageView iv07;
    @BindView(R.id.iv_08)
    ImageView iv08;
    @BindView(R.id.layout_images)
    LinearLayout layoutImages;
    @BindView(R.id.iv_add)
    TextView ivAdd;

    List<ImageView> imageViewList = new ArrayList<>();
    List<String> imagePathList = new ArrayList<>();

    /*
     * @ forever 在 17/5/17 下午2:28 创建
     *
     * 描述：跳转到登录页面
     *
     */
    public static void startIntent(Activity activity, Basic bean) {
        Intent it = new Intent(activity, DanganJibenImageActivity.class);
        it.putExtra("basic", bean);
        activity.startActivity(it);
    }

    /*
    * @ forever 在 17/5/17 下午2:28 创建
    *
    * 描述：页面创建时调用
    *
    */
    Basic bean;
    String imgPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        bean = (Basic) getIntent().getSerializableExtra("basic");

        initView();
        getDate();
    }

    public void initView() {

        iv01.setOnClickListener(imageOnClick);
        iv02.setOnClickListener(imageOnClick);
        iv03.setOnClickListener(imageOnClick);
        iv04.setOnClickListener(imageOnClick);
        iv05.setOnClickListener(imageOnClick);
        iv06.setOnClickListener(imageOnClick);
        iv07.setOnClickListener(imageOnClick);
        iv08.setOnClickListener(imageOnClick);

        imageViewList.add(iv01);
        imageViewList.add(iv02);
        imageViewList.add(iv03);
        imageViewList.add(iv04);
        imageViewList.add(iv05);
        imageViewList.add(iv06);
        imageViewList.add(iv07);
        imageViewList.add(iv08);


    }

    public void getDate(){
        HttpRequest.getMainPathImg(this, bean.getId(), new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                    MLog.e("main_path==>" + json);
                    if(!TextUtils.isEmpty(json)){
                        String[] paths = json.split("###");
                        for (int i = 0; i < paths.length; i++) {
                            if(!TextUtils.isEmpty(paths[i])) {
                                MLog.e("path" + i + "==>:" + paths[i]);
                                imagePathList.add(paths[i]);
                            }
                        }
                        setImageShow();
                    }

            }
        });
    }


    View.OnClickListener imageOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index=0;
            switch (v.getId()){
                case R.id.iv_01:
                    index=0;
                    break;
                case R.id.iv_02:
                    index=1;
                    break;
                case R.id.iv_03:
                    index=2;
                    break;
                case R.id.iv_04:
                    index=3;
                    break;
                case R.id.iv_05:
                    index=4;
                    break;
                case R.id.iv_06:
                    index=5;
                    break;
                case R.id.iv_07:
                    index=6;
                    break;
                case R.id.iv_08:
                    index=7;
                    break;
            }
            showDelDialog(index);
        }
    };

    /**
     * 弹出删除图片对话框
     */
    public void showDelDialog(final int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确定删除");
        builder.setMessage("确定删除当前点击的图片");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delImg(index);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    public void delImg(final int index){
        HttpRequest.delMainPathImg(this, bean.getId(), imagePathList.get(index), new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                ToastUtil.show("删除成功");
                imagePathList.remove(index);
                setImageShow();
            }
        });

    }


    @OnClick({R.id.left_menu, R.id.right_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.right_menu:
//                imagesUpload();
                break;
        }
    }

    @OnClick(R.id.iv_add)
    public void onViewClicked() {
        showSelectPicture();
    }

    public void imagesUploadOne(String path) {
        /**
         * 图片上传服务器
         */
        HttpRequest.upLoadImg(this, path, new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                HttpRequest.uploadFupinImg(DanganJibenImageActivity.this, bean.getId(), BaseConfig.apiUrl + json, new HttpRequest.MyCallBack() {
                    @Override
                    public void ok(String json) {
                        ToastUtil.show("上传成功");
                        MLog.e("上传成功：" + json);
                        imagePathList.add(json);
                        setImageShow();

                    }
                });
            }

            @Override
            public void no(String msg) {
                super.no(msg);
            }
        });
    }


    /*
    * @ forever 在 17/5/17 下午5:19 创建
    *
    * 描述：图片选择之后显示到界面
    *
    */
    public void setImageShow() {
        if (imagePathList.size() >= 8) {
            ivAdd.setVisibility(View.GONE);
        }
        for (int i = 0; i < 8; i++) {
            if(i<imagePathList.size()){
                GlideUtils.displayHome(imageViewList.get(i), imagePathList.get(i));
                imageViewList.get(i).setVisibility(View.VISIBLE);
            }else{
                imageViewList.get(i).setVisibility(View.GONE);
            }

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
        PopWindowUtil.init().show(this, R.id.activity_image, btString, new PopWindowUtil.OnPopupWindowClickLinstener() {
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
//            rightMenu.setVisibility(View.VISIBLE);
            if (resultList != null) {
                String imagePath = resultList.get(0).getPhotoPath();
                imagePath = ImageUtil.getCompressedImgPath(imagePath);
                MLog.e("onHanlderSuccess", imagePath);
                imagesUploadOne(imagePath);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(DanganJibenImageActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };


}
