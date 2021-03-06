package com.example.library.control;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.example.library.call.DoCallActivity;
import com.example.library.mpv.MpvListActivity;
import com.example.library.util.LogUtil;

/**
 * Created by DELL on 2017/7/24.
 */

public class ConnectCall {
    private final static String TAG=ConnectCall.class.getSimpleName();
    public static final String ISVIDEO="isvideo";
    public static final String NUMBER="number";
    public static final String ISPSTN="ispstn";

    public static final String ISGOING="isgoging";
    public ConnectCall(){

    }

    public void skipDoCall(Context context,boolean isVideo,boolean isPstn,boolean isGoing,String number){
        LogUtil.d(TAG, "skipDoCall: isVideo"+isVideo);
        Intent intent=new Intent(context, DoCallActivity.class);
        intent.putExtra(ISVIDEO,isVideo);
        intent.putExtra(NUMBER,number);
        intent.putExtra(ISPSTN,isPstn);
        intent.putExtra(ISGOING,isGoing);
        context.startActivity(intent);
    }

    public void skipIncommingDoCall(Context context){
        LogUtil.d(TAG, "skipIncommingDoCall: ");
        Intent intent=new Intent(context, DoCallActivity.class);
        intent.putExtra(ISVIDEO, TxtKandy.getKandyCall().mCurrentCall.canReceiveVideo());
        intent.putExtra(NUMBER,TxtKandy.getKandyCall().mCurrentCall.getCallee().getUserName());
        intent.putExtra(ISPSTN,false);
        intent.putExtra(ISGOING,false);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        TxtKandy.getMediaConnnect().playIncommingMp3(context);
    }

    public void  skipDoCallMpv(Context context){
        Intent intent=new Intent(context, MpvListActivity.class);
        context.startActivity(intent);
    }

    public void skipMpvCall(Context context){
        TxtKandy.getMpvCall().callRoomNumber(context, TxtKandy.getMpvCall().mRoomNum,"");
    }




}
