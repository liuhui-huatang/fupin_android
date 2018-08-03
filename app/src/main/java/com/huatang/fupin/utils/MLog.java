package com.huatang.fupin.utils;

import android.app.Activity;
import android.util.Log;

import com.huatang.fupin.R;
import com.huatang.fupin.app.MyApplication;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by author_dang on 16/8/15.
 */
public class MLog {

    private static final String TAG_ = MyApplication.getContext().getResources().getString(R.string.app_name);

    static {
        Logger.init(TAG_);
    }

    enum LogMode {
        NORMAL, // As usual.
        NONE, // Don't show any log at all.
        ALL // Always show logs.
    }

    public static LogMode logMode = LogMode.NORMAL;

    public static void v(String tag, String message) {
        if (interceptLog(tag, message)) {
            return;
        }
        printLog(Log.VERBOSE, tag, message);
    }

    public static void i(String tag, String message) {
        if (interceptLog(tag, message)) {
            return;
        }
        printLog(Log.INFO, tag, message);
    }

    public static void d(String message) {
        d("", message);
    }

    public static void d(String tag, String message) {
        if (interceptLog(tag, message)) {
            return;
        }
        printLog(Log.DEBUG, tag, message);
    }

    public static void w(String message) {
        w("", message);
    }

    public static void w(String tag, String message) {
        if (interceptLog(tag, message)) {
            return;
        }
        printLog(Log.WARN, tag, message);
    }

    public static void e(String message) {

        e("", message);
    }

    public static void e(String tag, String message) {
        if (interceptLog(tag, message)) {
            return;
        }
        printLog(Log.ERROR, tag, message);
    }
    public static void e(Activity context, String message) {
        if (interceptLog("", message)) {
            return;
        }
        printLog(Log.ERROR, context.getLocalClassName(), message);
    }

    private static boolean interceptLog(String tag, String message) {
        boolean isIntercepted = false;
        switch (logMode) {
            case NORMAL:
                // isIntercepted = false;
                break;
            case ALL:
                // Set priority WARN to show logs always even in release mode.
                printLog(Log.WARN, tag, message);
            case NONE:
                isIntercepted = true;
                break;
            default:
                break;
        }
        return isIntercepted;
    }

    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    private static void printLog(int priority, String tag, String message) {
        try {
            switch (priority) {
                case Log.VERBOSE:
                    Logger.t(tag).v(message);
                    break;
                case Log.INFO:
                    Logger.t(tag).i(message);
                    break;
                case Log.DEBUG:
                    if (isJSONValid(message)) {
                        Logger.t(tag).json(message);
                    } else {
                        Logger.t(tag).d(message);
                    }
                    break;
                case Log.WARN:
                    Logger.t(tag).w(message);
                    break;
                case Log.ERROR:
                    Logger.t(tag).e(message);
                    break;
                default:
                    Logger.t(tag).d(message);
                    break;
            }
            return;
        } catch (Exception exception) {
            exception.printStackTrace();
            Logger.t(tag).w(exception.toString());
        }
    }
}
