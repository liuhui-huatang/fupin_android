package com.huatang.fupin.update;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.dou361.dialogui.DialogUIUtils;
import com.huatang.fupin.activity.AppUpdateActivity;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.MLog;
import com.huatang.fupin.utils.SPUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import okhttp3.Request;
import okhttp3.Response;


/**
 * app下载类
 */
public class AppDownloadUtils {


    static TextView mtvProgress;
    static ProgressBar mprogressBar;
    static LinearLayout mrlUpdateLayout;

    public static void init(TextView tvProgress, ProgressBar progressBar, LinearLayout rlUpdateLayout) {
        mtvProgress = tvProgress;
        mprogressBar = progressBar;
        mrlUpdateLayout = rlUpdateLayout;
    }


    //    获取线上版本信息
    public static void getApkInfo(final Activity context) {
        DialogUIUtils.showTie(context, "加载中...");
        OkHttpUtils.get(BaseConfig.downloadApp)
                .tag(context)
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(boolean isFromCache, String json, Request request, @Nullable Response response) {
                        MLog.e("app==", json);
                        DialogUIUtils.dismssTie();
                        int code = JsonUtil.getInt(json, "code");
                        String name = JsonUtil.getString(json, "name");
                        String url = JsonUtil.getString(json, "url");
                        if (getApkNumber(context) < code) {
                            updateDialog(context);
                        }
                    }
                });

    }

    /**
     * 弹出版本更新对话框
     */
    public static void updateDialog(final Activity context) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("版本更新提示");
        builder.setMessage("最新版本上线了，快点更新吧！");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载新版本
//                downloadFile(context, downApkUrl, apkName, apkCode);
                AppUpdateActivity.startIntent(context);
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


    //    下载最新版apk
    public static void downloadFile(final Activity context, final String downloadUrl, String fileName, int apkNum) {
        MLog.d("downloadFile=============================");
        final File saveFile = createFile(fileName, apkNum);
        if (saveFile != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    try {
                    URL url = null;
                    try {
                        url = new URL(downloadUrl);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    HttpURLConnection conn = null;
                    try {
                        conn = (HttpURLConnection) url.openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (null != conn) {
                        conn.setReadTimeout(10000);
                        try {
                            conn.setRequestMethod("GET");
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        }
                        conn.setDoInput(true);
                        try {
                            conn.connect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                final int fileSize = conn.getContentLength();
                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mprogressBar.setMax(fileSize);
                                    }
                                });
                                InputStream is = conn.getInputStream();
                                FileOutputStream fos = new FileOutputStream(saveFile);
                                byte[] buffer = new byte[1024];
                                int i = 0;
                                int downFileSize = 0;
                                while ((i = is.read(buffer)) != -1) {
                                    fos.write(buffer, 0, i);
                                    downFileSize = downFileSize + i;
                                    final int finalDownFileSize = downFileSize;
                                    context.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mtvProgress.setText(finalDownFileSize * 100 / fileSize + "%");
                                            mprogressBar.setProgress(finalDownFileSize);
                                        }
                                    });
                                }
                                fos.flush();
                                fos.close();
                                is.close();

                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mrlUpdateLayout.setVisibility(View.GONE);
                                        installApk(context, saveFile.getPath());
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
//                    } catch (Exception e) {
//                        Log.e("TAG", "downloadFile catch Exception:", e);
//                    }
                }
            }).start();
        }
    }


    //    根据名称版本号，创建文件
    public static File createFile(String fileName, int apkNum) {
        try {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                File sdcardDir = Environment.getExternalStorageDirectory();
                File saveFile = new File(sdcardDir, fileName + "_" + apkNum + ".apk");
                if (saveFile.exists()) {
                    saveFile.createNewFile();
                }
                return saveFile;
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e("TAG", "AppFileDownUtils catch Exception:", e);
            return null;
        }
    }


    //    获取当前应用版本号
    public static int getApkNumber(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo.versionCode;
    }

    /**
     * 安装apk
     */
    public static void installApk(Context context, String path) {
        File apkfile = new File(path);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        context.startActivity(i);
    }


    /**
     * 判断是否需要更新版本
     *
     * @param context
     */
    public static void isApkUpdate(Activity context) {
        //判断当前应用版本是否大于本地版本
        if (getApkNumber(context) > SPUtil.getInt("ApkNum", 0)) {
            //  当前版本大于本地版本，获取线上版本与本地版本对比,线上版本大于本地版本则下载更新
            getApkInfo(context);
//            ToastUtil.show("去下载");
        } else {
            // 当前版本小于本地版本，则提示安装本地版本
//            updateDialog(context);
        }
    }

}