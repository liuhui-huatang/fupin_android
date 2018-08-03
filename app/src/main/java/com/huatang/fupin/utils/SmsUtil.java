package com.huatang.fupin.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.List;

/**
 * 功能：
 * <p>
 * 创建： forever
 * 时间： 17/4/19
 */

public class SmsUtil {

    /**
     * 直接调用短信接口发短信
     *
     * @param phoneNumber
     * @param message
     */
    public static void sendSMS(final Context context, String phoneNumber, String message) {


        /**
         * 处理返回的发送状态
         */
        String SENT_SMS_ACTION = "SENT_SMS_ACTION";
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sentIntent,
                0);
        // register the Broadcast Receivers
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context _context, Intent _intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context,
                                "短信发送成功", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:    //普通错误
                        Toast.makeText(context,
                                "RESULT_ERROR_GENERIC_FAILURE", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:         //无线广播被明确地关闭
                        Toast.makeText(context,
                                "RESULT_ERROR_RADIO_OFF", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:          //没有提供pdu
                        Toast.makeText(context,
                                "RESULT_ERROR_NULL_PDU", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:         //服务当前不可用
                        Toast.makeText(context,
                                "RESULT_ERROR_NO_SERVICE", Toast.LENGTH_SHORT)
                                .show();
                        break;
                }
            }
        }, new IntentFilter(SENT_SMS_ACTION));


        /**
         * 处理返回的接收状态
         */
        String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
        // create the deilverIntent parameter
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        PendingIntent deliverPI = PendingIntent.getBroadcast(context, 0,
                deliverIntent, 0);
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context _context, Intent _intent) {
                Toast.makeText(context,
                        "短信成功接收", Toast.LENGTH_SHORT)
                        .show();
            }
        }, new IntentFilter(DELIVERED_SMS_ACTION));


        //获取短信管理器
        SmsManager smsManager = SmsManager.getDefault();
        //拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(message);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, text, sentPI, deliverPI);
        }
    }


}
