package com.huatang.fupin.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.Toast;

import com.huatang.fupin.R;
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
    private Context mcontext;
    private  Integer layout;
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback;
    public UploadUtils(Context context, Integer layout,GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback){
        this.mcontext = context;
        this.layout = layout;
        this.mOnHanlderResultCallback = mOnHanlderResultCallback;
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


}
