package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.text.TextUtils;
import android.view.Gravity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.bean.MessageBoard;
import com.huatang.fupin.bean.NewLeader;
import com.huatang.fupin.bean.NewPoor;
import com.huatang.fupin.bean.Reply;
import com.huatang.fupin.http.NewHttpRequest;
import com.huatang.fupin.utils.GlideUtils;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageBoradDetailActivity extends BaseActivity {


    private ListView listview;
    private MyAdapter mAdapter;
    private List<Reply> list = new ArrayList<>();
    private String type;
    private MessageBoard messageBoard;
    private TextView tv_empty;
    @BindView(R.id.et_reply)
    EditText et_reply;
    @BindView(R.id.send_reply)
    TextView send_reply;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);
        ButterKnife.bind(this);
        type = SPUtil.getString(Config.Type);
        if (TextUtils.isEmpty(type)) {
            ToastUtil.show("身份失效，请重新登录");
            return;
        }
        messageBoard = (MessageBoard)getIntent().getSerializableExtra("messageBoard");
        listview = (ListView) findViewById(R.id.listview);
        mAdapter = new MyAdapter(this, list);
        listview.setAdapter(mAdapter);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        getData();
    }

    public static void startIntent(Activity activity, MessageBoard messageBoard) {
        Intent it = new Intent(activity, MessageBoradDetailActivity.class);
        it.putExtra("messageBoard", (Serializable) messageBoard);
        activity.startActivity(it);
    }

    private void getData() {
        NewHttpRequest.getReplyMsgInfo(this, messageBoard.getId(), new NewHttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                list = JsonUtil.toList(json, Reply.class);
                if (list == null || list.size() == 0) {
                    listview.setVisibility(View.GONE);
                    tv_empty.setVisibility(View.VISIBLE);

                } else {
                    listview.setVisibility(View.VISIBLE);
                    tv_empty.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                    listview.setSelection(ListView.FOCUS_DOWN);//刷新到底部
                }

            }

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);

            }
        });
    }

    @OnClick({R.id.left_menu,R.id.send_reply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.send_reply:
                String content = et_reply.getText().toString().trim();
                if(TextUtils.isEmpty(content)){
                    ToastUtil.show("请输入内容");
                    return;
                }
                if(content.length() < 5){
                    ToastUtil.show("输入内容太短。");
                    return;
                }
                sendReplyMessage(content);
                break;
        }
    }
    public class MyAdapter extends BaseAdapter  {
        private Context mcontext;



        public MyAdapter(Context context ,List<Reply> list) {
            this.mcontext = context;

        }

        @Override
        public int getItemViewType(int position) {
            return Integer.valueOf(list.get(position).getIs_leader());
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolderRight vhRight = null;
            ViewHolderLeft vhLeft = null;
            Reply reply =  list.get(position);
            if(getItemViewType(position) == 1){
                if(convertView == null){
                    convertView = View.inflate(mcontext, R.layout.activity_board_detail_right, null);
                    vhRight = new ViewHolderRight(convertView);
                    convertView.setTag(vhRight);
                }else{
                    if(convertView.getTag() instanceof ViewHolderRight){
                        vhRight = (ViewHolderRight) convertView.getTag();
                    }else{
                        convertView = View.inflate(mcontext, R.layout.activity_board_detail_right, null);
                        vhRight = new ViewHolderRight(convertView);
                        convertView.setTag(vhRight);
                    }

                }

            }else{
                if(convertView == null){
                    convertView = View.inflate(mcontext, R.layout.activity_board_detail_left, null);
                    vhLeft = new ViewHolderLeft(convertView);
                    convertView.setTag(vhLeft);
                }else{
                    if(convertView.getTag() instanceof ViewHolderLeft){
                        vhLeft = (ViewHolderLeft) convertView.getTag();
                    }else{
                        convertView = View.inflate(mcontext, R.layout.activity_board_detail_left, null);
                        vhLeft = new ViewHolderLeft(convertView);
                        convertView.setTag(vhLeft);
                    }

                }

            }


            if(reply.isLeader()){
                vhRight.author.setText(reply.getName()+":");
                vhRight.author_content.setText(TextUtils.isEmpty(reply.getContent()) ? reply.getReply_content() : reply.getContent());
            }else{
                vhLeft.author.setText(reply.getName()+":");
                vhLeft.author_content.setText(TextUtils.isEmpty(reply.getContent()) ? reply.getReply_content() : reply.getContent());
                List<String> imgsList = reply.getImgs();
                if(position == 0 && !reply.isLeader() &&  imgsList != null && imgsList.size() > 0){
                    List<ImageView> imageViewList = new ArrayList<>();
                    imageViewList.add(vhLeft.iv_01);
                    imageViewList.add(vhLeft.iv_02);
                    imageViewList.add(vhLeft.iv_03);
                    imageViewList.add(vhLeft.iv_04);
                    imageViewList.add(vhLeft.iv_05);
                    imageViewList.add(vhLeft.iv_06);
                    imageViewList.add(vhLeft.iv_07);
                    imageViewList.add(vhLeft.iv_08);
                    for(int i = 0 ; i < imgsList.size();i++){
                        if(i < imageViewList.size()){
                            imageViewList.get(i).setVisibility(View.VISIBLE);
                            GlideUtils.displayHomeUrl(imageViewList.get(i), BaseConfig.ImageUrl + imgsList.get(i),R.mipmap.news_default_img);
                        }else {
                            imageViewList.get(i).setVisibility(View.GONE);
                        }
                    }
                    vhLeft.layout_images.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ImageViewPageActivity.startIntent((Activity) mcontext,list.get(position).getImgs());
                        }
                    });
                }
            }
            return convertView;
        }



        class ViewHolderRight  {
            @BindView(R.id.author)
            TextView author;
            @BindView(R.id.author_content)
            TextView author_content;
            ViewHolderRight( View view) {
                ButterKnife.bind(this, view);
            }
        }
        class ViewHolderLeft{
            @BindView(R.id.author)
            TextView author;
            @BindView(R.id.author_content)
            TextView author_content;
            @BindView(R.id.layout_images)
            LinearLayout layout_images;
            @BindView(R.id.iv_01)
            ImageView iv_01;
            @BindView(R.id.iv_02)
            ImageView iv_02;
            @BindView(R.id.iv_03)
            ImageView iv_03;
            @BindView(R.id.iv_04)
            ImageView iv_04;
            @BindView(R.id.iv_05)
            ImageView iv_05;
            @BindView(R.id.iv_06)
            ImageView iv_06;
            @BindView(R.id.iv_07)
            ImageView iv_07;
            @BindView(R.id.iv_08)
            ImageView iv_08;

            ViewHolderLeft( View view) {
                ButterKnife.bind(this, view);
            }
        }

    }

    private void sendReplyMessage(String reply) {
        switch (type){
            case Config.PENKUNHU_TYPE:
                NewPoor poor = (NewPoor) SPUtil.getObject(Config.PENKUNHU_KEY);
                poorReplyMsg(reply,poor);
                break;
            case Config.GANBU_TYPE:
                NewLeader leader = (NewLeader) SPUtil.getObject(Config.GANBU_KEY);
                leaderReplyMsg(reply,leader);
                break;
            case Config.ADMIN_TYPE:
                leader = (NewLeader) SPUtil.getObject(Config.ADMIN_KEY);
                leaderReplyMsg(reply,leader);
                break;
        }



    }

    private void poorReplyMsg(String reply, NewPoor poor) {
        NewHttpRequest.poorReplyMsg(this, reply, messageBoard, poor, new NewHttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                ToastUtil.show("回复成功");
                //list.clear();
                //getData();
                list.clear();
                et_reply.setText("");
                list = JsonUtil.toList(json,Reply.class);
                mAdapter.notifyDataSetChanged();
                listview.setSelection(ListView.FOCUS_DOWN);//刷新到底部
            }

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);
            }
        });
    }

    private void leaderReplyMsg(String reply,NewLeader leader){
        NewHttpRequest.leaderReplyMsg(this, reply, messageBoard, leader, new NewHttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                ToastUtil.show("回复成功");
                list.clear();
                et_reply.setText("");
                list = JsonUtil.toList(json,Reply.class);
                mAdapter.notifyDataSetChanged();
                listview.setSelection(ListView.FOCUS_DOWN);//刷新到底部
            }

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);

            }
        });
    }
}
