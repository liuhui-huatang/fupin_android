package com.huatang.fupin.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.utils.ToastUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class ImageUploadActivity extends BaseActivity implements View.OnClickListener {
    public static String PHOTO_URL = "url";


    private ImageView image;
    private Button downLoadBtn;
    private String url;
    private String imagePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_down_load);
        image = (ImageView)findViewById(R.id.image);
        image.setOnClickListener(this);
        downLoadBtn = (Button)findViewById(R.id.downLoadBtn);
        url = getIntent().getStringExtra(PHOTO_URL);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }
        Glide.with(this).load(url).into(image);
        downLoadBtn.setOnClickListener(this);

    }


    /**
     * Glide 获得图片缓存路径
     */
    private String getImagePath(String imgUrl) {
        String path = null;
        FutureTarget<File> future = Glide.with(this)
                .load(imgUrl)
                .downloadOnly(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL);
        try {
            File cacheFile = future.get();
            path = cacheFile.getAbsolutePath();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return path;
    }

    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image:
                this.finish();
                break;
            case R.id.downLoadBtn:
                 save();
                 ToastUtil.show("保存成功");
                 finish();

                break;
        }


    }

    private void save() {
        new Thread(new Runnable() {


            @Override
            public void run() {

                String path =   getImagePath(url);
                SimpleDateFormat timesdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time =timesdf.format(new Date()).toString();//获取系统时间
                imagePath += time+".jpg" ;
                copyFile(path, imagePath);
                Intent intentBroadcast = new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File file = new File(imagePath);
                intentBroadcast.setData(Uri.fromFile(file));
                sendBroadcast(intentBroadcast);

            }
        }).start();


            }


}
