package com.huatang.fupin.utils;

import android.app.Activity;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.Toast;

import com.huatang.fupin.app.MyApplication;


/**
 * Created by author_dang on 16/8/15.
 */
public class ToastUtil {

    private static final int SPACE_SIZE = 3;
    private static final long TOAST_CD = 3 * 1000;

    private static String lastToast;
    private static boolean inCounting;

    private static int space_count = SPACE_SIZE;


    public static void show(String message) {
        if (TextUtils.isEmpty(message) || message.equals(lastToast) || !hasToastSpace()) {
            return;
        }
        lastToast = message;

        Toast.makeText(MyApplication.getContext(), message, Toast.LENGTH_SHORT)
                .show();
    }

    public static void showUI(final Activity activity, final String message) {
        if (TextUtils.isEmpty(message) || message.equals(lastToast) || !hasToastSpace()) {
            return;
        }
        lastToast = message;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT)
                        .show();
            }
        });

    }

    private static CountDownTimer toastCountDown = new CountDownTimer(TOAST_CD, TOAST_CD) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            resetCD();
        }
    };

    private static boolean hasToastSpace() {
        if (!inCounting) {
            inCounting = true;
            toastCountDown.start();
        }
        return space_count-- > 0;
    }

    private static void resetCD() {
        lastToast = "";
        space_count = SPACE_SIZE;
        inCounting = false;
    }
}
