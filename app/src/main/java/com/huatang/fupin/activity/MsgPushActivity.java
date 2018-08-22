package com.huatang.fupin.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.bean.NewLeader;
import com.huatang.fupin.bean.NewPushMsg;
import com.huatang.fupin.http.NewHttpRequest;
import com.huatang.fupin.utils.CustomDialog;
import com.huatang.fupin.utils.DateUtil;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.SkinUtil;
import com.huatang.fupin.utils.ToastUtil;
import com.huatang.fupin.utils.ViewHolderUtil;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 系统消息列表页面
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class MsgPushActivity extends BaseActivity {


    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.right_menu)
    ImageView rightMenu;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    NewLeader admin ;
    /*
         * @ forever 在 17/5/17 下午2:28 创建
         *
         * 描述：跳转到登录页面
         *
         */
    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, MsgPushActivity.class);
        activity.startActivity(it);
    }

    /*
    * @ forever 在 17/5/17 下午2:28 创建
    *
    * 描述：页面创建时调用
    *
    */
    Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msginfo);
        ButterKnife.bind(this);
        initHeadView();
        SPUtil.saveInt("push",0);

        RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setPrimaryColors(getResources().getColor(R.color.colorPrimary));
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(true));
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout freshlayout) {
                freshlayout.finishRefresh(true);
                getData();

            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(1000);
            }
        });

        adapter=new Adapter(MsgPushActivity.this);
        listview.setAdapter(adapter);
        getData();
        //条目点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.show("点击了一下");
            }
        });
    }
    private void initHeadView() {
        ((TextView)findViewById(R.id.title_tx)).setText("系统消息");
        leftMenu.setImageResource(SkinUtil.getResouceId(R.mipmap.icon_commen_break));
        rightMenu.setImageResource(SkinUtil.getResouceId(R.mipmap.icon_main_select));
        rightMenu.setVisibility(View.INVISIBLE);
        String type = SPUtil.getString(Config.Type);
        if(type.equals(Config.ADMIN_TYPE)){
            rightMenu.setVisibility(View.VISIBLE);
            admin = (NewLeader) SPUtil.getObject(Config.ADMIN_KEY);
            //admin = (NewLeader) SPUtil.getObject(Config.GANBU_KEY);

        }
    }
    List<NewPushMsg> list = new ArrayList<>();

    public void getData() {

        NewHttpRequest.getSystemMsg(this, new NewHttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                list = JsonUtil.toList(json, NewPushMsg.class);
                if (list.size() > 0) {
                    listview.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                } else {
                    listview.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void no(String msg){
                ToastUtil.show(msg);

            }
        });
    }


    class Adapter extends BaseAdapter {

        private Context mContext;

        public Adapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_msg_info, null);
            }
            TextView tv_title = ViewHolderUtil.get(convertView, R.id.tv_title);
            TextView tv_time = ViewHolderUtil.get(convertView, R.id.tv_time);
            TextView tv_text = ViewHolderUtil.get(convertView, R.id.tv_text);

            NewPushMsg bean = list.get(position);
            tv_title.setText(bean.getTitle());
            tv_time.setText(DateUtil.getStandardTime(Long.parseLong(bean.getCreate_time())));
            tv_text.setText(bean.getContent());
            return convertView;
        }
    }

    @OnClick({R.id.left_menu, R.id.right_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.right_menu:
                showSendMessageDialog();
                break;

        }
    }

    private void showSendMessageDialog() {

        final CustomDialog dialog=new CustomDialog(this, R.style.myDialog);
        final View view = View.inflate(this, R.layout.dialog_layout,null);
        dialog.setView(view);
      //  dialog.setProperty(0,0, 600, 400);//设置坐标和宽高
        dialog.setCanceledOnTouchOutside(true);
        final EditText title_v = (EditText)view.findViewById(R.id.msg_title);
        final EditText content_v = (EditText)view.findViewById(R.id.msg_content);
        final TextView error_v = (TextView)view.findViewById(R.id.error);
        Button dialogCancel= (Button) view.findViewById(R.id.dialog_cancel);

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button dialogConfirm= (Button) view.findViewById(R.id.dialog_confirm);
        dialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_v.setVisibility(View.INVISIBLE);
                String title = title_v.getText().toString().trim();
                String content = content_v.getText().toString().trim();

                if( TextUtils.isEmpty(title)){
                    error_v.setText("标题不能为空");
                    error_v.setVisibility(View.VISIBLE);
                    return;

                }
                if( TextUtils.isEmpty(content)){
                    error_v.setText("内容不能为空");
                    error_v.setVisibility(View.VISIBLE);
                    return;

                }
                NewHttpRequest.sendPushMsg(MsgPushActivity.this,content,title,admin.getId(),admin.getLeader_name(),new NewHttpRequest.MyCallBack(){


                    @Override
                    public void ok(String json) {
                        ToastUtil.show("发送成功！");
                        dialog.dismiss();
                        list.clear();
                        getData();

                    }

                    @Override
                    public void no(String msg) {
                        ToastUtil.show(msg);
                        error_v.setText(msg);
                        error_v.setVisibility(View.VISIBLE);

                    }
                } );





            }
        });
        dialog.show();

    }



}
