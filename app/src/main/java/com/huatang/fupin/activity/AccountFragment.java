package com.huatang.fupin.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.bean.NewFuzeren;
import com.huatang.fupin.bean.NewLeader;
import com.huatang.fupin.bean.NewPoor;
import com.huatang.fupin.bean.YouKe;
import com.huatang.fupin.utils.GlideUtils;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.SkinUtil;
import com.huatang.fupin.utils.ToastUtil;
import com.huatang.fupin.zxing.activity.CaptureActivity;

import static android.app.Activity.RESULT_OK;

public class AccountFragment extends Fragment implements View.OnClickListener {

    private Button logout_btn;
    private LinearLayout register_layout;
    private ImageView leftMenu;
    private ImageView rightMenu;
    private TextView titleText;
    private LinearLayout rl_gengxin;
    private LinearLayout rl_mima;
    private LinearLayout rl_xinxi;
    private TextView nameView;
    private TextView zhiWuView;
    private ImageView iv_photo;
    private TextView tv_phone;
    private TextView tv_town;
    private TextView tv_naimanqi;
    private LinearLayout ll_town;
    private View line_view;
    private Button change_skill;
    private RelativeLayout title_bar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_account, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        if (TextUtils.isEmpty(SPUtil.getString(Config.PHONE))) {
            tv_phone.setVisibility(View.INVISIBLE);
        } else {
            tv_phone.setText(SPUtil.getString(Config.PHONE));
        }

        iv_photo.setImageResource(SkinUtil.getResouceId(R.mipmap.header_default));
        logout_btn.setBackgroundResource(SkinUtil.getResouceId(R.mipmap.logout_btn));
        switch (SPUtil.getString(Config.Type)) {
            case Config.YOUKU_TYPE:

                String phone = SPUtil.getString(Config.PHONE);//phone代表已经注册的游客
                if (TextUtils.isEmpty(phone)) {
                    nameView.setVisibility(View.INVISIBLE);
                    register_layout.setVisibility(View.VISIBLE);
                    line_view.setVisibility(View.VISIBLE);
                    tv_phone.setVisibility(View.INVISIBLE);
                    // GlideUtils.LoadCircleImageWithoutBorderColor(getActivity(),BaseConfig.ImageUrl+SPUtil.getString(Config.HEAD_PHOTO),iv_photo);
                } else {//phone代表已经注册的游客
                    nameView.setVisibility(View.VISIBLE);
                    tv_phone.setVisibility(View.VISIBLE);
                    YouKe youKe = (YouKe) SPUtil.getObject(Config.YOUKE);
                    String youke_photo = TextUtils.isEmpty(youKe.getPhoto()) ? SPUtil.getString(Config.HEAD_PHOTO) : BaseConfig.ImageUrl + youKe.getPhoto();
                    GlideUtils.LoadCircleImageWithoutBorderColor(getActivity(), youke_photo, iv_photo);
                    SPUtil.saveString(Config.NAME, youKe.getName());
                    if (!TextUtils.isEmpty(youke_photo)) {
                        SPUtil.saveString(Config.HEAD_PHOTO, youke_photo);
                    }
                    nameView.setText(TextUtils.isEmpty(youKe.getName()) ? SPUtil.getString(Config.NAME) : youKe.getName());
                }
                ll_town.setVisibility(View.INVISIBLE);
                zhiWuView.setVisibility(View.INVISIBLE);
                tv_naimanqi.setVisibility(View.INVISIBLE);


                break;
            case Config.ADMIN_KEY:
                NewLeader admin = (NewLeader) SPUtil.getObject(Config.ADMIN_KEY);
                nameView.setText(admin.leader_name);
                String admin_photo = TextUtils.isEmpty(admin.getLeader_photo()) ? SPUtil.getString(Config.HEAD_PHOTO) : BaseConfig.ImageUrl + admin.getLeader_photo();
                SPUtil.saveString(Config.NAME, admin.getLeader_name());
                if (!TextUtils.isEmpty(admin_photo)) {
                    SPUtil.saveString(Config.HEAD_PHOTO, admin_photo);
                }
                GlideUtils.LoadCircleImageWithoutBorderColor(getActivity(), admin_photo, iv_photo);
                if (TextUtils.isEmpty(admin.getLeader_duty())) {
                    zhiWuView.setVisibility(View.INVISIBLE);
                } else {
                    zhiWuView.setText(admin.getLeader_duty());
                }
                if (TextUtils.isEmpty(admin.getLeader_unit())) {

                    tv_naimanqi.setVisibility(View.INVISIBLE);
                } else {
                    tv_naimanqi.setText(admin.getLeader_unit());
                }
                if (TextUtils.isEmpty(admin.getHelp_town())) {
                    ll_town.setVisibility(View.INVISIBLE);
                } else {
                    tv_town.setText(admin.getHelp_town());
                }


                break;
            case Config.GANBU_TYPE:
                NewLeader leader = (NewLeader) SPUtil.getObject(Config.GANBU_KEY);
                nameView.setText(leader.leader_name);
                String leader_photo = TextUtils.isEmpty(leader.getLeader_photo()) ? SPUtil.getString(Config.HEAD_PHOTO) : BaseConfig.ImageUrl + leader.getLeader_photo();
                if (!TextUtils.isEmpty(leader_photo)) {
                    SPUtil.saveString(Config.HEAD_PHOTO, leader_photo);
                }
                GlideUtils.LoadCircleImageWithoutBorderColor(getActivity(), leader_photo, iv_photo);
                if (TextUtils.isEmpty(leader.getLeader_duty())) {
                    zhiWuView.setVisibility(View.INVISIBLE);
                } else {
                    zhiWuView.setText(leader.getLeader_duty());
                }
                if (TextUtils.isEmpty(leader.getLeader_unit())) {

                    tv_naimanqi.setVisibility(View.INVISIBLE);
                } else {
                    tv_naimanqi.setText(leader.getLeader_unit());
                }
                if (TextUtils.isEmpty(leader.getHelp_town())) {
                    tv_town.setText("无");
                } else {
                    tv_town.setText(leader.getHelp_town());
                }

                SPUtil.saveString(Config.NAME, leader.getLeader_name());
                break;
            case Config.PENKUNHU_TYPE:
                NewPoor poor = (NewPoor) SPUtil.getObject(Config.PENKUNHU_KEY);
                nameView.setText(poor.getFname());
                SPUtil.saveString(Config.NAME, poor.getFname());
                String poor_photo = TextUtils.isEmpty(poor.getPhoto()) ? SPUtil.getString(Config.HEAD_PHOTO) : BaseConfig.ImageUrl + poor.getPhoto();
                GlideUtils.LoadCircleImageWithoutBorderColor(getActivity(), poor_photo, iv_photo);
                if (TextUtils.isEmpty(poor_photo)) {
                    SPUtil.saveString(Config.HEAD_PHOTO, poor_photo);
                }
                ll_town.setVisibility(View.INVISIBLE);
                zhiWuView.setVisibility(View.INVISIBLE);
                tv_naimanqi.setVisibility(View.INVISIBLE);
                break;
            case Config.FUZEREN_TYPE:
                NewFuzeren fuzeren = (NewFuzeren) SPUtil.getObject(Config.FUZEREN_KEY);
                nameView.setText(fuzeren.getVillage_chief());
                String chief_photo = TextUtils.isEmpty(fuzeren.getPhoto()) ? SPUtil.getString(Config.HEAD_PHOTO) : fuzeren.getPhoto();
                GlideUtils.LoadCircleImageWithoutBorderColor(getActivity(), chief_photo, iv_photo);
                if (TextUtils.isEmpty(chief_photo)) {
                    SPUtil.saveString(Config.HEAD_PHOTO, chief_photo);
                }
                zhiWuView.setText(fuzeren.getChief_duty());
                tv_town.setText(fuzeren.getVillage());
                SPUtil.saveString(Config.NAME, fuzeren.getVillage_chief());
                break;
        }
    }

    private void initView(View view) {
        initHeadView(view);
        logout_btn = (Button) view.findViewById(R.id.logout_btn);
        register_layout = (LinearLayout) view.findViewById(R.id.youke_register_layout);
        logout_btn.setOnClickListener(this);
        register_layout.setOnClickListener(this);
        rl_gengxin = (LinearLayout) view.findViewById(R.id.rl_gengxin);
        rl_gengxin.setOnClickListener(this);
        rl_mima = (LinearLayout) view.findViewById(R.id.rl_mima);
        rl_mima.setOnClickListener(this);
        rl_xinxi = (LinearLayout) view.findViewById(R.id.rl_xinxi);
        rl_xinxi.setOnClickListener(this);
        nameView = (TextView) view.findViewById(R.id.tv_name);
        iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
        zhiWuView = (TextView) view.findViewById(R.id.tv_zhiwu);
        tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        tv_town = (TextView) view.findViewById(R.id.tv_town);
        tv_naimanqi = (TextView) view.findViewById(R.id.tv_naimanqi);
        ll_town = (LinearLayout) view.findViewById(R.id.ll_town);
        line_view = (View) view.findViewById(R.id.line_view);
        change_skill = (Button) view.findViewById(R.id.change_skill);
        change_skill.setOnClickListener(this);
    }

    private void initHeadView(View view) {
        leftMenu = view.findViewById(R.id.left_menu);
        leftMenu.setVisibility(View.VISIBLE);
        leftMenu.setImageResource(SkinUtil.getResouceId(R.mipmap.img_sao));
        leftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCaptureActivityForResult();
            }
        });
        rightMenu = (ImageView) view.findViewById(R.id.right_menu);
        rightMenu.setVisibility(View.VISIBLE);
        rightMenu.setImageResource(SkinUtil.getResouceId(R.mipmap.img_message));
        rightMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewMsgActivity.startIntent(getActivity());
            }
        });
        titleText = (TextView) view.findViewById(R.id.title_tx);
        titleText.setText("个人中心");
        rightMenu.setVisibility(View.VISIBLE);
        title_bar = (RelativeLayout) view.findViewById(R.id.title_bar);
        //title_bar.setBackgroundResource(SkinUtil.getResouceId(R.mipmap.header));
    }

    private void startCaptureActivityForResult() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(CaptureActivity.KEY_NEED_BEEP, CaptureActivity.VALUE_BEEP);
        bundle.putBoolean(CaptureActivity.KEY_NEED_VIBRATION, CaptureActivity.VALUE_VIBRATION);
        bundle.putBoolean(CaptureActivity.KEY_NEED_EXPOSURE, CaptureActivity.VALUE_NO_EXPOSURE);
        bundle.putByte(CaptureActivity.KEY_FLASHLIGHT_MODE, CaptureActivity.VALUE_FLASHLIGHT_OFF);
        bundle.putByte(CaptureActivity.KEY_ORIENTATION_MODE, CaptureActivity.VALUE_ORIENTATION_AUTO);
        bundle.putBoolean(CaptureActivity.KEY_SCAN_AREA_FULL_SCREEN, CaptureActivity.VALUE_SCAN_AREA_FULL_SCREEN);
        bundle.putBoolean(CaptureActivity.KEY_NEED_SCAN_HINT_TEXT, CaptureActivity.VALUE_SCAN_HINT_TEXT);
        startActivityForResult(intent, CaptureActivity.REQ_CODE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout_btn:

                exitDialog();


                break;
            case R.id.youke_register_layout:
                youkeRegister();
                break;
            case R.id.rl_mima:
                if (SPUtil.getString(Config.Type).equals(Config.YOUKU_TYPE) && TextUtils.isEmpty(SPUtil.getString(Config.PHONE))) {
                    ToastUtil.show("请先完成注册");
                } else {
                    PwdUpdateActivity.startIntent(getActivity());
                }

                break;
            case R.id.rl_gengxin:
                AppUpdateActivity.startIntent(getActivity());
                break;
            case R.id.rl_xinxi:
                if (SPUtil.getString(Config.Type).equals(Config.YOUKU_TYPE) && TextUtils.isEmpty(SPUtil.getString(Config.PHONE))) {
                    ToastUtil.show("请先完成注册");
                } else {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), MyInfoActivity.class);
                    startActivityForResult(intent, MyInfoActivity.UPDATE_HEAD_PHOTO);
                }

                break;
            case R.id.change_skill:
                SPUtil.saveString(SkinUtil.CURSKINTYPEKEY, SkinUtil.skin_type_blue);

                break;

        }

    }

    /**
     * 弹出退出登录对话框
     */
    public void exitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("退出登录");
        builder.setMessage("确定退出当前账号？");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SPUtil.removeValue(Config.TOKEN);
                SPUtil.removeValue(Config.Type);
                SPUtil.removeValue(Config.PHONE);
                SPUtil.removeValue(Config.NAME);
                SPUtil.removeValue(Config.PASSWORD);
                SPUtil.removeValue(Config.HEAD_PHOTO);
                ToastUtil.show("退出成功");
                getActivity().finish();

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

    private void youkeRegister() {
        Intent intent = new Intent();//register
        intent.setClass(getActivity(), ToursitsRegisterActivity.class);
        startActivityForResult(intent, ToursitsRegisterActivity.REGISTER_SUCCESS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ToursitsRegisterActivity.REGISTER_SUCCESS) {
                nameView.setVisibility(View.VISIBLE);
                tv_phone.setVisibility(View.VISIBLE);
                tv_phone.setText(SPUtil.getString(Config.PHONE));
                nameView.setText(SPUtil.getString(Config.NAME));
            } else if (requestCode == MyInfoActivity.UPDATE_HEAD_PHOTO) {
                GlideUtils.LoadCircleImageWithoutBorderColor(getActivity(), SPUtil.getString(Config.HEAD_PHOTO), iv_photo);
            }

        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();

        } else {  // 在最前端显示 相当于调用了onResume();
            // initData();

        }
    }
}
