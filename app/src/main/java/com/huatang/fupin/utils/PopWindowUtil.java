package com.huatang.fupin.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.huatang.fupin.R;


/**
 * Created by forever on 2016/11/28.
 * 资源文件：
 * <p>
 * layout:
 * popup_window_button.xml
 * popup_window_photo.xml
 * <p>
 * anim:
 * push_bottom_in.xml
 * push_bottom_out.xml
 * <p>
 * style:
 * <style name="AnimBottom" parent="@android:style/Animation">
 * <item name="android:windowEnterAnimation">@anim/push_bottom_in</item>
 * <item name="android:windowExitAnimation">@anim/push_bottom_out</item>
 * </style>
 * <p>
 * 用法：
 * final String[] btString=new String[]{"回复", "查看对话", "删除"};
 * PopWindowUtil.init().show(MyDynamicActivity.this, R.id.rl_my_dynamic_activity,btString , new PopWindowUtil.OnPopupWindowClickLinstener() {
 *
 * @Override public void popClick(Button popButton) {
 * String btStr=popButton.getText().toString();
 * if(btStr.equals(btString[0])){
 * }else if(btStr.equals(btString[1])){
 * }else if(btStr.equals(btString[2])){
 * }
 * }
 * });
 */

public class PopWindowUtil {

    public static PopWindowUtil mPopWindowUtil;

    public static PopWindowUtil init() {

        if (mPopWindowUtil == null) {
            mPopWindowUtil = new PopWindowUtil();
        }
        return mPopWindowUtil;
    }


    public void show(final Activity activity, int id, String[] btnString, OnPopupWindowClickLinstener onClickListener) {
        PhotoPopupWindow menuWindow = new PhotoPopupWindow(activity, btnString, onClickListener);
        menuWindow.showAtLocation(activity.findViewById(id),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
        setWindowBackground(activity, 0.5f);
        menuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowBackground(activity, 1f);
            }
        });


    }


    public interface OnPopupWindowClickLinstener {
        void popClick(Button popButton);
    }


    private void setWindowBackground(Activity activity, Float alpha) {
        WindowManager.LayoutParams lp2 = activity.getWindow().getAttributes();
        if (alpha == 0.5f) {
            alpha = 0.3f;
        }
        lp2.alpha = alpha;
        activity.getWindow().setAttributes(lp2);
    }


    class PhotoPopupWindow extends PopupWindow {
        private LinearLayout mLayout;
        private Button btn_cancel;
        private View mMenuView;

        public PhotoPopupWindow(Activity context, String[] btnString, final OnPopupWindowClickLinstener clickListener) {
            super(context);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mMenuView = inflater.inflate(R.layout.popup_window_photo, null);
            mLayout = (LinearLayout) mMenuView.findViewById(R.id.layout_popup_window_button);
            btn_cancel = (Button) mMenuView.findViewById(R.id.btn_popup_window_cancel);


            for (int i = 0; i < btnString.length; i++) {
                View btnView = View.inflate(context, R.layout.popup_window_button, null);
                View lineView = btnView.findViewById(R.id.line_popup_window);
                Button button = (Button) btnView.findViewById(R.id.bt_popup_window);
                button.setText(btnString[i]);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 销毁弹出框
                        dismiss();
                        clickListener.popClick((Button) v);

                    }
                });
                if (i == 0) {
                    lineView.setVisibility(View.GONE);
                }
                mLayout.addView(btnView);
            }

            // 取消按钮
            btn_cancel.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // 销毁弹出框
                    dismiss();
                }
            });
            // 设置按钮监听
            // 设置SelectPicPopupWindow的View
            this.setContentView(mMenuView);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            // 设置SelectPicPopupWindow弹出窗体可点击
            this.setFocusable(true);
            // 设置SelectPicPopupWindow弹出窗体动画效果
            this.setAnimationStyle(R.style.AnimBottom);
            // 实例化一个ColorDrawable颜色为透明
            ColorDrawable dw = new ColorDrawable(0x00000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
            mMenuView.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {

                    int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y < height) {
                            dismiss();
                        }
                    }
                    return true;
                }
            });

        }
    }

}
