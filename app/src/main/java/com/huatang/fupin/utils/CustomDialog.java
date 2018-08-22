package com.huatang.fupin.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class CustomDialog extends Dialog {

        private Window window = null;

        public CustomDialog(Context context, boolean cancelable,
                        OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        public CustomDialog(Context context, int theme) {
            super(context, theme);
        }

        public CustomDialog(Context context) {
            super(context);
        }

        public void setView(View view) {
            setContentView(view);
        }

        public void setView(int id) {
            setContentView(id);
        }

        public void setProperty(int x, int y, int w, int h) {
            window = getWindow();//得到对话框的窗口．
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.x = x;//设置对话框的位置．0为中间
            wl.y = y;
            wl.width = w;
            wl.height = h;
            wl.alpha = 1f;// 设置对话框的透明度,1f不透明
            wl.gravity = Gravity.CENTER;//设置显示在中间
            window.setAttributes(wl);
        }
}
