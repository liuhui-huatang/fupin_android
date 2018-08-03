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

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.bean.Basics;
import com.huatang.fupin.bean.Leaders;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.utils.JsonUtil;
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
 *         created at 2017/1/9 11:16
 */

public class MsgSendSearchActivity extends BaseActivity {


    @BindView(R.id.left_menu)
    ImageView leftMenu;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.right_menu)
    TextView rightMenu;
    @BindView(R.id.et_text)
    EditText etText;
    @BindView(R.id.bt_send)
    Button btSend;
    @BindView(R.id.listview)
    ListView listview;

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
        tag = getIntent().getStringExtra("tag");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tag.equals("leader")) {
                    setResult(100, new Intent().putExtra("leaders", leaders.get(position)));
                } else {
                    setResult(200, new Intent().putExtra("basics", basics.get(position)));
                }
                finish();
            }
        });
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
                if (tag.equals("leader")) {
                    getLeaders(text);
                } else {
                    getBasics(text);
                }

                break;
        }
    }

    List<Leaders> leaders = new ArrayList<>();

    public void getLeaders(String text) {
        HttpRequest.getLeaders(this, text, new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                leaders = JsonUtil.toList(json, Leaders.class);
                if (leaders.size() > 0) {
                    tvEmpty.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                    listview.setAdapter(new BaseAdapter() {
                        @Override
                        public int getCount() {
                            return leaders.size();
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

                            item_name.setText(leaders.get(position).getLeader_name());
                            item_phone.setText(leaders.get(position).getLeader_phone());

                            return convertView;
                        }
                    });
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
                }
            }
        });
    }

    List<Basics> basics = new ArrayList<>();
    public void getBasics(String text) {
        HttpRequest.getBasics(this, text, new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                basics = JsonUtil.toList(json, Basics.class);
                if (basics.size() > 0) {
                    tvEmpty.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                    listview.setAdapter(new BaseAdapter() {
                        @Override
                        public int getCount() {
                            return basics.size();
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

                            item_name.setText(basics.get(position).getFname());
                            item_phone.setText(basics.get(position).getFphone());

                            return convertView;
                        }
                    });

                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
                }
            }
        });
    }


}
