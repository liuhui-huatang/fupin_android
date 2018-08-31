package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.bean.Basics;
import com.huatang.fupin.bean.Leaders;
import com.huatang.fupin.bean.NewLeader;
import com.huatang.fupin.bean.NewPoor;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.http.NewHttpRequest;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.ToastUtil;
import com.huatang.fupin.utils.ViewHolderUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发布消息搜索发布对象页面
 *
 * @author forever
 * created at 2017/1/9 11:16
 */

public class MsgSendSearchActivity extends BaseActivity {

    public final static String TAG = "tag";
    public final static int LEADER_RESULT_CODE = 100;
    public final static int POOR_RESULT_CODE = 200;
    @BindView(R.id.left_menu)
    ImageView leftMenu;

    @BindView(R.id.title_tx)
    TextView tvTitle;

    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    @BindView(R.id.et_text)
    EditText etText;
    @BindView(R.id.bt_send)
    Button btSend;
    @BindView(R.id.listview)
    ListView listview;
    List<NewLeader> leader_list = new ArrayList<>();
    List<NewPoor> poorList = new ArrayList<>();
    private NewLeader leader;

    /*
     * @ forever 在 17/5/17 下午2:28 创建
     *
     * 描述：跳转到登录页面
     *
     */
    public static void startIntent(Activity activity, String tag) {
        Intent it = new Intent(activity, MsgSendSearchActivity.class);
        it.putExtra("tag", tag);
        activity.startActivity(it);
    }

    /*
     * @ forever 在 17/5/17 下午2:28 创建
     *
     * 描述：页面创建时调用
     *
     */
    String tag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        tag = getIntent().getStringExtra(TAG);
        initHeadView();
        leader = SPUtil.getLeaderFromSharePref();


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tag.equals(Config.GANBU_KEY)) {
                    setResult(LEADER_RESULT_CODE, new Intent().putExtra(Config.GANBU_KEY, leader_list.get(position)));
                } else {
                    setResult(POOR_RESULT_CODE, new Intent().putExtra(Config.PENKUNHU_KEY, poorList.get(position)));
                }
                finish();
            }
        });
    }

    private void initHeadView() {
        tvTitle.setText(tag.equals(Config.GANBU_KEY) ? "选择干部" : "选择贫困户");
    }

    @OnClick({R.id.left_menu, R.id.bt_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;

            case R.id.bt_send:
                String text = etText.getText().toString().trim();
                if (TextUtils.isEmpty(text)) {
                    ToastUtil.show("请输入查询关键字");
                    return;
                }
                if (tag.equals(Config.GANBU_KEY)) {
                    getLeaderList(text);
                } else if (tag.equals(Config.PENKUNHU_KEY)) {
                    getPoorList();
                }

                break;
        }
    }


    public void getLeaderList(String text) {
        NewHttpRequest.searchGanbuList(this, text, new NewHttpRequest.MyCallBack(this) {

            @Override
            public void ok(String json) {
                leader_list = JsonUtil.toList(json, NewLeader.class);
                if (leader_list.size() > 0) {
                    tvEmpty.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                    listview.setAdapter(new BaseAdapter() {
                        @Override
                        public int getCount() {
                            return leader_list.size();
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
                                convertView = View.inflate(MsgSendSearchActivity.this, R.layout.item_list, null);
                            }
                            TextView item_name = ViewHolderUtil.get(convertView, R.id.item_name);
                            TextView item_phone = ViewHolderUtil.get(convertView, R.id.item_phone);
                            RadioButton item_radio = ViewHolderUtil.get(convertView, R.id.item_radio);
                            item_radio.setVisibility(View.GONE);

                            item_name.setText(leader_list.get(position).getLeader_name());
                            item_phone.setText(leader_list.get(position).getLeader_phone());

                            return convertView;
                        }
                    });
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


    public void getPoorList() {
        NewHttpRequest.searchPoorList(this, leader.getId(), new NewHttpRequest.MyCallBack(this) {
            @Override
            public void ok(String json) {
                poorList = JsonUtil.toList(json, NewPoor.class);
                if (poorList.size() > 0) {
                    tvEmpty.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                    listview.setAdapter(new BaseAdapter() {
                        @Override
                        public int getCount() {
                            return poorList.size();
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
                                convertView = View.inflate(MsgSendSearchActivity.this, R.layout.item_list, null);
                            }
                            TextView item_name = ViewHolderUtil.get(convertView, R.id.item_name);
                            TextView item_phone = ViewHolderUtil.get(convertView, R.id.item_phone);
                            RadioButton item_radio = ViewHolderUtil.get(convertView, R.id.item_radio);
                            item_radio.setVisibility(View.GONE);

                            item_name.setText(poorList.get(position).getFname());
                            item_phone.setText(poorList.get(position).getFphone());

                            return convertView;
                        }
                    });


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


}
