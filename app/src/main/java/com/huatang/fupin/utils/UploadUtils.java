package com.huatang.fupin.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.huatang.fupin.R;
import com.huatang.fupin.activity.CreateMessageBoardActivity;
import com.huatang.fupin.activity.MsgCreateChatActivity;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.bean.Archive;
import com.huatang.fupin.http.NewHttpRequest;

import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

public class UploadUtils {

    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private static UploadUtils mInstance;
    private Context mcontext;
    private  Integer layout;
    private MyCallBack myCallBack;
    public UploadUtils(){

    }
    public static UploadUtils getmInstance(){
        if (mInstance==null){
            mInstance=new UploadUtils();
        }
        return mInstance;
    }
    public void start(Context context, Integer layout,MyCallBack myCallBack){
        this.mcontext = context;
        this.layout = layout;
        this.myCallBack = myCallBack;
        showSelectPicture();
    }


    public void showSelectPicture() {

        final FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setMutiSelectMaxSize(6)
                .build();
        final String[] btString = new String[]{"拍照", "相册"};
        PopWindowUtil.init().show((Activity) mcontext, layout, btString, new PopWindowUtil.OnPopupWindowClickLinstener() {

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
            Toast.makeText(mcontext, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };
    public void imagesUpload(String filePath) {
        /**
         * 图片上传服务器
         */
        NewHttpRequest.uploadImage((Activity) mcontext, filePath, new NewHttpRequest.UploadCallBack() {
            @Override
            public void callback(String json) {
                ToastUtil.show("图片上传成功");
                String photoUrl = JsonUtil.getStringFromArray(json, "url");
                //调用一个更新到数据库的接口
                myCallBack.success(photoUrl);


            }
        });
    }
    public interface MyCallBack {
        public void success(String url);
    }
}
