package com.huatang.fupin.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import com.huatang.fupin.app.MyApplication;

import java.io.File;

/**
 * 说明：手机信息相关工具类
 * <p/>
 * 作者：forever
 * <p/>
 * 时间：2015/10/26 23:57
 * <p/>
 * 版本：verson 1.0
 */

public final class AndroidInfoUtils {

    private AndroidInfoUtils() {
    }

    /**
     * 说明：获取手机IMEI号码
     *
     * @return 返回手机IMEI号码
     */
    public static String getImeiCode() {
        String result = "";
        try {
            final TelephonyManager tm = (TelephonyManager) MyApplication.getContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            result = tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 说明：获取手机IMSI号码
     *
     * @return 返回手机IMEI号码
     */
    public static String getImsiCode() {
        String result = "";
        try {
            final TelephonyManager tm = (TelephonyManager) MyApplication.getContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            result = tm.getSubscriberId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取手机Android_ID
     *
     * @return MacAddress String
     */
    public static String getAndroidId() {
        String androidId = Secure.getString(MyApplication.getContext().getContentResolver(),
                Secure.ANDROID_ID);
        return androidId;
    }

    /**
     * 说明：获取本机手机号码
     *
     * @return 返回本机手机号码
     */
    public static String getMobilNumber() {
        String result = "";
        try {
            final TelephonyManager tm = (TelephonyManager) MyApplication.getContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            result = tm.getLine1Number();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 说明：myPid
     *
     * @return
     */
    public static int myPid() {
        return android.os.Process.myPid();
    }

    /**
     * 说明：获取系统信息
     *
     * @return
     */
    public static String getOs() {
        return Build.VERSION.RELEASE;
    }


    /**
     * 说明：获取当前应用程序的VersionName
     * <p>
     * 当前上下文环境
     *
     * @return 返回当前应用的版本号
     */
    public static String versionName() {
        try {
            PackageInfo info = MyApplication.getContext().getPackageManager().getPackageInfo(
                    MyApplication.getContext().getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 说明：获取当前应用程序的VersionCode
     *
     * @return 返回当前应用的版本号
     */
    public static int versionCode() {
        try {
            PackageInfo info = MyApplication.getContext().getPackageManager().getPackageInfo(
                    MyApplication.getContext().getPackageName(), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 说明：检测手机空间可用大小 get the space is left over on phone self
     */
    public static long getRealSizeOnPhone() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        @SuppressWarnings("deprecation")
        long blockSize = stat.getBlockSize();
        @SuppressWarnings("deprecation")
        long availableBlocks = stat.getAvailableBlocks();
        long realSize = blockSize * availableBlocks;
        return realSize;
    }


    /**
     * 获取手机系统SDK版本
     *
     * @return 如API 17 则返回 17
     */
    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取设备的可用内存大小
     *
     * @return 当前内存大小
     */
    public static int getDeviceUsableMemory() {
        ActivityManager am = (ActivityManager) MyApplication.getContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        // 返回当前系统的可用内存
        return (int) (mi.availMem / (1024 * 1024));
    }

    /**
     * 说明：获取当前线程名称
     *
     * @return
     */
    public static String getCurProcessName() {
        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) MyApplication.getContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /** 获取单个App的签名 **/
    public static String getAppSignature(String packageName) throws NameNotFoundException {
        PackageInfo packageInfo = MyApplication.getContext().getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
        String allSignature = packageInfo.signatures[0].toCharsString();
        return allSignature;
    }

    /** 获取单个App名称 **/
    public static String getAppName() {
        ApplicationInfo appInfo = null;
        try {
            appInfo = MyApplication.getContext().getPackageManager().getApplicationInfo( MyApplication.getContext().getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        String appName = MyApplication.getContext().getPackageManager().getApplicationLabel(appInfo).toString();
        return appName;
    }
}
