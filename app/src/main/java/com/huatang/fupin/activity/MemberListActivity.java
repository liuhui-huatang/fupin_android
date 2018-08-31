package com.huatang.fupin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.bean.NewChat;
import com.huatang.fupin.bean.NewLeader;
import com.huatang.fupin.http.NewHttpRequest;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.ToastUtil;
import com.huatang.fupin.utils.ViewHolderUtil;

import java.util.ArrayList;
import java.util.List;

public class MemberListActivity extends BaseActivity implements View.OnClickListener {
    public static String CHAT = "chat";
    public static final int REQUEST_CODE_ADD_LEADER = 10001;
    private ImageView leftMenu;
    private TextView rightMenu;
    private TextView title;
    private NewChat chat;
    private String phones = "";
    private List<NewLeader> leader_list;
    private ListView listview;
    private Adapter leaderAdapter;
    private TextView tvEmpty;
    private RelativeLayout rl_leader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
        initView();
        initData();
    }

    private void initView() {
        initHeadView();
    }

    private void initHeadView() {
        leftMenu = (ImageView) findViewById(R.id.left_menu);
        leftMenu.setOnClickListener(this);
        rightMenu = (TextView) findViewById(R.id.right_tx_menu);
        rightMenu.setText("添加成员");
        rightMenu.setVisibility(View.VISIBLE);
        rightMenu.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title_tx);
        title.setText("成员列表");
        listview = (ListView) findViewById(R.id.listview);
        tvEmpty = (TextView) findViewById(R.id.tv_empty);
        rl_leader = (RelativeLayout) findViewById(R.id.rl_leader);
        rl_leader.setOnClickListener(this);

    }

    private void initData() {
        Intent intent = getIntent();
        chat = (NewChat) intent.getSerializableExtra(CHAT);
        leader_list = new ArrayList<>();
        leaderAdapter = new Adapter(this);
        listview.setAdapter(leaderAdapter);
        NewHttpRequest.getChatMember(this, chat.getId(), new NewHttpRequest.MyCallBack(this) {
            @Override
            public void ok(String json) {
                leader_list = JsonUtil.toList(json, NewLeader.class);
                if (leader_list.size() > 0) {
                    tvEmpty.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                    leaderAdapter.notifyDataSetChanged();
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
                }

            }

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.rl_leader:
                Intent intent = new Intent(this, MsgSendSearchActivity.class);
                intent.putExtra(MsgSendSearchActivity.TAG, Config.GANBU_KEY);
                startActivityForResult(intent, MsgSendSearchActivity.LEADER_RESULT_CODE);
                break;
            case R.id.right_tx_menu:
                //添加
                if (TextUtils.isEmpty(phones)) {
                    ToastUtil.show("请选择成员");
                    return;

                }
                updateChatMember();
                break;

        }

    }

    private void updateChatMember() {
        NewHttpRequest.addChatMember(this, chat.getId(), phones, new NewHttpRequest.MyCallBack(this) {
            @Override
            public void ok(String json) {
                ToastUtil.show("添加成功");
                finish();
                Intent intent = new Intent();
                intent.putExtra("num", leader_list.size());
                setResult(RESULT_OK, intent);

            }

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == MsgSendSearchActivity.LEADER_RESULT_CODE) {
            NewLeader leader = (NewLeader) data.getSerializableExtra(Config.GANBU_KEY);
            if (!TextUtils.isEmpty(phones) && phones.contains(leader.getLeader_phone())) {
                ToastUtil.show("当前用户已选择");
                return;
            }
            leader_list.add(leader);
            phones += leader.getLeader_phone() + ",";
            leaderAdapter.notifyDataSetChanged();
        }

    }

    class Adapter extends BaseAdapter {
        private Context mContext;

        public Adapter(Context context) {
            this.mContext = context;
        }


        @Override
        public int getCount() {
            return leader_list.size();
        }

        @Override
        public Object getItem(int position) {
            return leader_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_list, null);
            }
            TextView item_name = ViewHolderUtil.get(convertView, R.id.item_name);
            TextView item_phone = ViewHolderUtil.get(convertView, R.id.item_phone);
            RadioButton item_radio = ViewHolderUtil.get(convertView, R.id.item_radio);
            item_radio.setChecked(true);
            item_name.setText(leader_list.get(position).getLeader_name());
            item_phone.setText(leader_list.get(position).getLeader_phone());
            return convertView;
        }
    }
}
