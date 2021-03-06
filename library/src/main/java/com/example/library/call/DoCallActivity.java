package com.example.library.call;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library.R;
import com.example.library.activity.BaseActivity;
import com.example.library.control.ConnectCall;
import com.example.library.control.TxtKandy;
import com.example.library.util.AudioModeManger;
import com.example.library.util.LogUtil;
import com.genband.kandy.api.services.calls.IKandyCall;
import com.genband.kandy.api.services.calls.KandyCallResponseListener;
import com.genband.kandy.api.services.calls.KandyView;
import com.genband.kandy.api.services.common.KandyResponseListener;

import java.util.Locale;

/**
 * Created by DELL on 2017/7/24.
 * 语音、视频拨打和接听显示界面
 */

public class DoCallActivity extends BaseActivity implements KandyCall.KandyCallListener{
    private static final String TAG=DoCallActivity.class.getSimpleName();
    private boolean mIsVideoCall=false;
    private boolean mIsPstn=false;
    private boolean mIsGoing=false;
    //video
    private KandyView mRemoteView;
    private KandyView mLoacalView;
    private RelativeLayout mVideoLayout;

    private TextView mUserVideoName;
    private TextView mCallVideoStatue;
    private TextView mCallTime;
    private LinearLayout mHangUplayout;
    private LinearLayout mButtonItem;
    private LinearLayout mHanguoItemButton;
    private LinearLayout mCamerOffOrOpen;
    private ImageView mCamerabutton;
    private LinearLayout mVideoMute;
    private ImageView mVideoMutebutton;
    private LinearLayout mCameraChange;
    //audio
    private RelativeLayout mAudioLayout;
    private TextView mAudioName;
    private TextView mAudioStatus;
    private TextView mAudioTime;

    private LinearLayout mAudioMute;
    private ImageView mAudioMute_img;
    private LinearLayout mSpeakLayout;
    private ImageView mSpeakLaout_img;
    private LinearLayout mAudioHangUp;
    private boolean mIsFront=true;
    private boolean mIsMute=false;
    private boolean mIsCameraOpen=true;
    private String mUsenamePhone;
    private LinearLayout mAudioItemLayout;
    //incoming

    private RelativeLayout mInComingLayout;
    private TextView InCommingName;
    private TextView mInComingType;
    private LinearLayout mAcceptButton;
    private LinearLayout mRejectButton;
    private RelativeLayout mGoingLayout;
    private AudioModeManger mModelManager;
    private boolean isSpeaker=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        TxtKandy.getKandyCall().setKandyCallListener(this);
        mUsenamePhone=getIntent().getStringExtra(ConnectCall.NUMBER);
        mIsVideoCall=getIntent().getBooleanExtra(ConnectCall.ISVIDEO,false);
        mIsPstn=getIntent().getBooleanExtra(ConnectCall.ISPSTN,false);
        mIsGoing=getIntent().getBooleanExtra(ConnectCall.ISGOING,true);
        initView();
        mModelManager=new AudioModeManger(this);
        mModelManager.register();
        mModelManager.setOnSpeakerListener(new AudioModeManger.onSpeakerListener() {
            @Override
            public void onSpeakerChanged(boolean isSpeakerOn) {
                //9mModelManager.setSpeakerPhoneOn(isSpeakerOn);
            }
        });

        mModelManager.setOnSpeakIconChange(new AudioModeManger.OnSpeakInconChange() {
            @Override
            public void onSpeakIconChange(boolean isSpeakerOn) {
                if (isSpeakerOn){
                    mSpeakLaout_img.setBackgroundResource(R.drawable.speak_on);
                }else {
                    mSpeakLaout_img.setBackgroundResource(R.drawable.speak_off);
                }
            }
        });

    }


    private void initView() {
        String name=mUsenamePhone.split("@")[0];
        mGoingLayout= (RelativeLayout) findViewById(R.id.outgoing);
        mRemoteView= (KandyView) findViewById(R.id.remote);
        mLoacalView= (KandyView) findViewById(R.id.local);
        mVideoLayout= (RelativeLayout) findViewById(R.id.videolayout);
        mUserVideoName= (TextView) findViewById(R.id.name);
        mUserVideoName.setText(name);
        mCallVideoStatue= (TextView) findViewById(R.id.callstatus);
        mCallVideoStatue.setText("正在拨打视频，请稍后....");
        mCallTime= (TextView) findViewById(R.id.call_time);
        mHangUplayout= (LinearLayout) findViewById(R.id.hangulayout);
        mHangUplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoCallActivity.this.finish();
                TxtKandy.getKandyCall().hangUp(new KandyCall.CallRequestCallBack() {
                    @Override
                    public void onRequrestSuccess() {

                    }
                    @Override
                    public void onRequrestFailer() {

                    }
                });
            }
        });


        //下面Button组
        mButtonItem= (LinearLayout) findViewById(R.id.video_button_item);
        mButtonItem.setVisibility(View.GONE);

        mHanguoItemButton= (LinearLayout) findViewById(R.id.hangup_talk);

        mHanguoItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TxtKandy.getKandyCall().hangUp(new KandyCall.CallRequestCallBack() {
                    @Override
                    public void onRequrestSuccess() {
                        DoCallActivity.this.finish();
                    }

                    @Override
                    public void onRequrestFailer() {

                    }
                });
            }
        });
        mCamerOffOrOpen= (LinearLayout) findViewById(R.id.camera);
        mCamerabutton= (ImageView) findViewById(R.id.camerabutton);
        mCamerOffOrOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsCameraOpen){
                    TxtKandy.getKandyCall().cameraOpenOrOff(false, new KandyCallResponseListener() {
                        @Override
                        public void onRequestSucceeded(IKandyCall iKandyCall) {
                            DoCallActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LogUtil.d(TAG, "onClick: mCamerOffOrOpen"+false);
                                    mIsCameraOpen=false;
                                    mCamerabutton.setBackgroundResource(R.drawable.camera_open);
                                }
                            });
                        }

                        @Override
                        public void onRequestFailed(IKandyCall iKandyCall, int i, String s) {

                        }
                    });
                }else {
                    TxtKandy.getKandyCall().cameraOpenOrOff(true, new KandyCallResponseListener() {
                        @Override
                        public void onRequestSucceeded(IKandyCall iKandyCall) {

                            DoCallActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIsCameraOpen=true;
                                    LogUtil.d(TAG, "onClick: mCamerOffOrOpen2:true");
                                    mCamerabutton.setBackgroundResource(R.drawable.camera_off);
                                }
                            });
                        }

                        @Override
                        public void onRequestFailed(IKandyCall iKandyCall, int i, String s) {

                        }
                    });

                }

            }
        });


        mVideoMute= (LinearLayout) findViewById(R.id.video_mute);
        mVideoMutebutton= (ImageView) findViewById(R.id.mute_button);
        mVideoMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mIsMute){
                    TxtKandy.getKandyCall().doUnMute(new KandyCallResponseListener() {
                        @Override
                        public void onRequestSucceeded(IKandyCall iKandyCall) {
                            DoCallActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mVideoMutebutton.setBackgroundResource(R.drawable.mute);
                                }
                            });
                            mIsMute=false;

                        }

                        @Override
                        public void onRequestFailed(IKandyCall iKandyCall, int i, String s) {

                        }
                    });
                }else {
                    TxtKandy.getKandyCall().doMute(new KandyCallResponseListener() {
                        @Override
                        public void onRequestSucceeded(IKandyCall iKandyCall) {
                            mIsMute=true;
                            DoCallActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mVideoMutebutton.setBackgroundResource(R.drawable.unmute);
                                }
                            });

                        }

                        @Override
                        public void onRequestFailed(IKandyCall iKandyCall, int i, String s) {

                        }
                    });
                }

            }
        });
        mCameraChange= (LinearLayout) findViewById(R.id.camera_change);
        mCameraChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mIsFront){
                    TxtKandy.getKandyCall().switchCamer(false, new KandyResponseListener() {
                        @Override
                        public void onRequestSucceded() {
                            mIsFront=false;
                        }

                        @Override
                        public void onRequestFailed(int i, String s) {

                        }
                    });
                }else {
                    TxtKandy.getKandyCall().switchCamer(true, new KandyResponseListener() {
                        @Override
                        public void onRequestSucceded() {
                            mIsFront=true;
                        }

                        @Override
                        public void onRequestFailed(int i, String s) {

                        }
                    });

                }
            }
        });

        mSpeakLayout= (LinearLayout) findViewById(R.id.speak);
        mSpeakLaout_img= (ImageView) findViewById(R.id.speakbutton);
        mSpeakLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSpeaker){
                    isSpeaker=false;
                }else {
                    isSpeaker=true;
                }
                if (mModelManager!=null)
                    mModelManager.setSpeakerPhoneOn(isSpeaker);
            }
        });

        mAudioMute= (LinearLayout) findViewById(R.id.audio_mute);

        mAudioMute_img= (ImageView) findViewById(R.id.audio_mute_button);

        mAudioMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mIsMute){
                    TxtKandy.getKandyCall().doUnMute(new KandyCallResponseListener() {
                        @Override
                        public void onRequestSucceeded(IKandyCall iKandyCall) {
                            DoCallActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAudioMute_img.setBackgroundResource(R.drawable.mute);
                                }
                            });
                            mIsMute=false;
                        }

                        @Override
                        public void onRequestFailed(IKandyCall iKandyCall, int i, String s) {

                        }
                    });
                }else {
                    TxtKandy.getKandyCall().doMute(new KandyCallResponseListener() {
                        @Override
                        public void onRequestSucceeded(IKandyCall iKandyCall) {
                            mIsMute=true;
                            DoCallActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAudioMute_img.setBackgroundResource(R.drawable.unmute);
                                }
                            });

                        }

                        @Override
                        public void onRequestFailed(IKandyCall iKandyCall, int i, String s) {

                        }
                    });
                }
            }
        });

        mAudioHangUp= (LinearLayout) findViewById(R.id.auido_hang);
        mAudioHangUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TxtKandy.getKandyCall().hangUp(new KandyCall.CallRequestCallBack() {
                    @Override
                    public void onRequrestSuccess() {
                        DoCallActivity.this.finish();
                    }

                    @Override
                    public void onRequrestFailer() {

                    }
                });
            }
        });

        mAudioLayout= (RelativeLayout) findViewById(R.id.audiolayout);
        mAudioName= (TextView) findViewById(R.id.audio_name);
        mAudioName.setText(name);
        mAudioStatus= (TextView) findViewById(R.id.audio_status);
        mAudioTime= (TextView) findViewById(R.id.audio_time);
        mStartTime=System.currentTimeMillis();
        mTalkingRunnable=new Runnable() {
            @Override
            public void run() {
                long time=System.currentTimeMillis()-mStartTime;
                String talking_time=generateTime(time);
                if (mIsVideoCall){
                    mCallTime.setText(talking_time);
                }else {
                    mAudioTime.setText(talking_time);
                }
                mTalkHandler.postDelayed(mTalkingRunnable,1000);
            }
        };
        mTalkHandler=new Handler();

        if (mIsVideoCall){
            mVideoLayout.setVisibility(View.VISIBLE);
            mAudioLayout.setVisibility(View.GONE);
        }else {
            mVideoLayout.setVisibility(View.GONE);
            mAudioLayout.setVisibility(View.VISIBLE);
        }
        mAudioItemLayout= (LinearLayout) findViewById(R.id.audio_button_item);
        mAudioItemLayout.setVisibility(View.GONE);
        initCommngView();
    }

    private void initCommngView() {
        mInComingLayout= (RelativeLayout) findViewById(R.id.incomminglayout);
        mInComingType= (TextView) findViewById(R.id.incoming_state);
        mAcceptButton= (LinearLayout) findViewById(R.id.accept);
        InCommingName= (TextView) findViewById(R.id.incoming_name);

        //设置显示来电账号
//        InCommingName.setText(mUsenamePhone);
        InCommingName.setText("奈曼旗扶贫云指挥中心");

        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TxtKandy.getKandyCall().accep(new KandyCallResponseListener() {
                    @Override
                    public void onRequestSucceeded(IKandyCall iKandyCall) {

                    }

                    @Override
                    public void onRequestFailed(IKandyCall iKandyCall, int i, String s) {

                    }
                });
            }
        });
        mRejectButton= (LinearLayout) findViewById(R.id.reject);
        mRejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TxtKandy.getKandyCall().rejectIncomingCall();
            }
        });
        if (mIsVideoCall){
            mInComingType.setText("请求视频通话");
        }else {
            mInComingType.setText("请求语音通话");
        }
        if (mIsGoing){
            doCall();
            mGoingLayout.setVisibility(View.VISIBLE);
            mInComingLayout.setVisibility(View.GONE);
        }else {
            mGoingLayout.setVisibility(View.GONE);
            mInComingLayout.setVisibility(View.VISIBLE);
            TxtKandy.getKandyCall().mCurrentCall.setLocalVideoView(mLoacalView);
            TxtKandy.getKandyCall().mCurrentCall.setRemoteVideoView(mRemoteView);
        }
    }

    private void doCall() {
        TxtKandy.getKandyCall().doCall(mUsenamePhone, mIsVideoCall, mIsPstn, mRemoteView, mLoacalView, new KandyCall.CallRequestCallBack() {
            @Override
            public void onRequrestSuccess() {

            }

            @Override
            public void onRequrestFailer() {
                DoCallActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DoCallActivity.this,"创建拨打失败",Toast.LENGTH_SHORT).show();
                        DoCallActivity.this.finish();
                    }
                });
            }
        });
    }

    private Runnable mTalkingRunnable;
    private Handler mTalkHandler;
    private long mStartTime=0;


    private static String generateTime(long position) {
        int totalSeconds = (int) (position / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60);
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }

    @Override
    public void onTalking() {
        TxtKandy.getMediaConnnect().pauseAndStopPlay();
        mGoingLayout.setVisibility(View.VISIBLE);
        mInComingLayout.setVisibility(View.GONE);
        mTalkHandler.postDelayed(mTalkingRunnable,0);
        if (mIsVideoCall){
            if (mModelManager!=null){
                mModelManager.setSpeakerPhoneOn(true);
            }
            mCallTime.setVisibility(View.VISIBLE);
            mCallVideoStatue.setVisibility(View.GONE);
            mButtonItem.setVisibility(View.VISIBLE);

        }else {
            if (mModelManager!=null){
                mModelManager.setSpeakerPhoneOn(false);
            }
            mAudioTime.setVisibility(View.VISIBLE);
            mAudioStatus.setVisibility(View.GONE);
            mAudioItemLayout.setVisibility(View.VISIBLE);
        }
        mHangUplayout.setVisibility(View.GONE);
    }

    @Override
    public void onTenminaten(int code) {
        TxtKandy.getMediaConnnect().hangUp(DoCallActivity.this);
        TxtKandy.getMediaConnnect().stopMediaPlay();
        String msg=TxtKandy.getKandyCall().getTermaintedMsg(code);
        Toast.makeText(DoCallActivity.this,msg,Toast.LENGTH_SHORT).show();
        DoCallActivity.this.finish();
    }

    @Override
    public void onIncomingCall(boolean isVideo) {

    }
    @Override
    public void onDialing() {
        TxtKandy.getMediaConnnect().playConnetMp3(DoCallActivity.this);
        LogUtil.d(TAG, "onDialing: ");
        if (mIsVideoCall){
            mCallVideoStatue.setText("正在拨打视频，请稍后....");
        }else {
            mAudioStatus.setText("正在拨打音频，请稍后...");
        }
    }

    @Override
    public void onRinging() {
        LogUtil.d(TAG, "onRinging: ");
        TxtKandy.getMediaConnnect().playCallMp3(DoCallActivity.this);
        if (mIsVideoCall){
            mCallVideoStatue.setText("正在连通视频，请稍后...");
        }else {
            mAudioStatus.setText("正在连通音频，请稍后...");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mModelManager!=null){
            mModelManager.setSpeakerInit();
            //mModelManager.setSpeakerPhoneOn(true);
            mModelManager.unregister();
        }
    }

    @Override
    public void onBackPressed() {

    }
}
