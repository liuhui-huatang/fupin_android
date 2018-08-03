package com.huatang.fupin.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {
    public static final String PIC_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/huatangImage/";

    public static String getCompressedImgPath(String sourceImgPath) {
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();

            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(sourceImgPath, opts);

            int w = opts.outWidth;
            int h = opts.outHeight;
            float standardW = 480f;
            float standardH = 800f;

            int zoomRatio = 1;
            if (w > h && w > standardW) {
                zoomRatio = (int) (w / standardW);
            } else if (w < h && h > standardH) {
                zoomRatio = (int) (h / standardH);
            }
            if (zoomRatio <= 0)
                zoomRatio = 1;
            opts.inSampleSize = zoomRatio;

            opts.inJustDecodeBounds = false;
            Bitmap bmp = BitmapFactory.decodeFile(sourceImgPath, opts);

            File compressedImg = new File(PIC_DIR);
            if (!compressedImg.exists()) {
                compressedImg.mkdirs();
            }
            compressedImg = new File(PIC_DIR,"android_"+System.currentTimeMillis() +"_"+SPUtil.getString("id")+ ".jpg");
            if (!compressedImg.exists()) {
                compressedImg.createNewFile();
            }


            FileOutputStream fos = new FileOutputStream(compressedImg);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();

            return compressedImg.getAbsolutePath();

        } catch (FileNotFoundException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            MLog.e("catch (FileNotFoundException e) ", e.getMessage());
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            MLog.e(" catch (IOException e)", e.getMessage());
        }
        return null;
    }

}
