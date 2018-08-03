package com.huatang.fupin.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by forever on 16/12/15.
 */

public class TimerUtil {
    public static CountDownTimer timer;

    public static void start(final TextView textView) {

        timer = new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                textView.setText((millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                end(textView);
            }
        };

        timer.start();
        textView.setEnabled(false);
    }

    public static void end(final TextView textView) {
        textView.setEnabled(true);
        textView.setText("获取验证码");
        timer.cancel();
        timer = null;
    }

    public static void cancel() {
        timer.cancel();
        timer = null;
    }
}
