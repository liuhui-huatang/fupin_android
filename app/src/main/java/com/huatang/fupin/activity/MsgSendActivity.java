package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.bean.Basic;
import com.huatang.fupin.bean.Basics;
import com.huatang.fupin.bean.Leaders;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.utils.DateUtil;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.MLog;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.ToastUtil;
import com.huatang.fupin.utils.ViewHolderUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发布消息页面
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class MsgSendActivity extends BaseActivity {


    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.right_menu)
    TextView rightMenu;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_text)
    EditText etText;
    @BindView(R.id.rl_leader)
    RelativeLayout rlLeader;
    @BindView(R.id.rl_hu)
    RelativeLayout rlHu;
    @BindView(R.id.lv_send)
    ListView lvSend;

    int num = 1;

    /*
         * @ forever 在 17/5/17 下午2:28 创建
         *
         * 描述：跳转到登录页面
         *
         */
    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, MsgSendActivity.class);
        activity.startActivity(it);
    }

    /*
    * @ forever 在 17/5/17 下午2:28 创建
    *
    * 描述：页面创建时调用
    *
    */
    LeaderAdapter leaderAdapter;
    BasicAdapter basicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        ButterKnife.bind(this);

        rlLeader.setVisibility(View.VISIBLE);
        rlHu.setVisibility(View.GONE);
        leaderAdapter = new LeaderAdapter(MsgSendActivity.this);
        lvSend.setAdapter(leaderAdapter);

//        if ("4".equals(SPUtil.getString("identity"))) {
//            rlLeader.setVisibility(View.VISIBLE);
//            rlHu.setVisibility(View.GONE);
//            leaderAdapter = new LeaderAdapter(MsgSendActivity.this);
//            lvSend.setAdapter(leaderAdapter);
//
//        } else {
//            rlLeader.setVisibility(View.GONE);
//            rlHu.setVisibility(View.VISIBLE);
//            basicAdapter = new BasicAdapter(MsgSendActivity.this);
//            lvSend.setAdapter(basicAdapter);
//        }
    }

    @OnClick({R.id.left_menu, R.id.right_menu, R.id.rl_leader, R.id.rl_hu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.right_menu:
                String title = etTitle.getText().toString().trim();
                String text = etText.getText().toString().trim();
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(text)) {
                    ToastUtil.show("请输入标题或内容");
                    return;
                }
                if (TextUtils.isEmpty(phones)) {
                    ToastUtil.show("请选择消息发布对象");
                    return;
                }
                createGroup(title, text, num + "", ids, phones);
                break;
            case R.id.rl_leader:
                Intent intent = new Intent(this, MsgSendSearchActivity.class);
                intent.putExtra("tag", "leader");
                startActivityForResult(intent, 100);
                break;
            case R.id.rl_hu:
//                Intent intent1 = new Intent(this, MsgSendSearchActivity.class);
//                intent1.putExtra("tag", "basic");
//                startActivityForResult(intent1, 200);

                selectBasic();
                break;
        }
    }

    List<Basics> basiclist;
    int mPosition;

    public void selectBasic() {
        HttpRequest.getBasicAll(this, SPUtil.getString("phone"), new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                basiclist = JsonUtil.toList(json, Basics.class);
                if (basiclist == null || basiclist.size() == 0) {
                    ToastUtil.show("没有帮扶户");
                } else {
                    String[] words2 = new String[basiclist.size()];
                    for (int i = 0; i < basiclist.size(); i++) {
                        words2[i] = basiclist.get(i).getFname() == null ? " " : basiclist.get(i).getFname();
                    }
                    DialogUIUtils.showSingleChoose(MsgSendActivity.this, "请选择帮扶户", -1, words2, new DialogUIItemListener() {
                        @Override
                        public void onItemClick(CharSequence text, int position) {
                            mPosition = position;
                            if (TextUtils.isEmpty(text)) {
                                return;
                            }
                            if (!TextUtils.isEmpty(phones) && phones.contains(basiclist.get(position).getFphone())) {
                                ToastUtil.show("当前用户已选择");
                                return;
                            }
                            Basics bean = basiclist.get(position);
                            basics.add(basiclist.get(position));
                            if (TextUtils.isEmpty(ids)) {
//                                ids = SPUtil.getString("id") + "#" + bean.getId();
//                                phones = SPUtil.getString("phone ") + "#" + bean.getFphone();
                                ids = bean.getId();
                                phones = bean.getFphone();
                            } else {
                                ids = ids + "#" + bean.getId();
                                phones = phones + "#" + bean.getFphone();
                            }
                            basicAdapter.notifyDataSetChanged();
                            num++;
                        }
                    }).show();
                }

            }
        });
    }

    /**
     * 创建群聊
     */
    public void createGroup(String title, String msg, String num, String ids, String phones) {
        HttpRequest.createGroup(this, title, msg, SPUtil.getString("id"), num, String.valueOf(DateUtil.getMillis() / 1000), SPUtil.getString("name"), SPUtil.getString("photo"), SPUtil.getString("phone"), ids, phones, new HttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                MLog.e("createGroup", json);
                finish();
            }
        });
    }

    String ids = "";
    String phones = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (resultCode == 100) {
            Leaders bean = (Leaders) data.getSerializableExtra("leaders");

            if (!TextUtils.isEmpty(phones) && phones.contains(bean.getLeader_phone())) {
                ToastUtil.show("当前用户已选择");
                return;
            }
            leaders.add(bean);
            if (TextUtils.isEmpty(ids)) {
//                ids =SPUtil.getString("id")+"#"+ bean.getId();
//                phones = SPUtil.getString("phone")+"#"+bean.getLeader_phone();
                ids = bean.getId();
                phones = bean.getLeader_phone();
            } else {
                ids = ids + "#" + bean.getId();
                phones = phones + "#" + bean.getLeader_phone();
            }
            leaderAdapter.notifyDataSetChanged();
        }
    }


    List<Leaders> leaders = new ArrayList<>();
    List<Basics> basics = new ArrayList<>();

    class LeaderAdapter extends BaseAdapter {
        private Context mContext;

        public LeaderAdapter(Context context) {
            mContext = context;
        }

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
                convertView = View.inflate(mContext, R.layout.item_list, null);
            }
            TextView item_name = ViewHolderUtil.get(convertView, R.id.item_name);
            TextView item_phone = ViewHolderUtil.get(convertView, R.id.item_phone);
            RadioButton item_radio = ViewHolderUtil.get(convertView, R.id.item_radio);
            item_radio.setChecked(true);
            item_name.setText(leaders.get(position).getLeader_name());
            item_phone.setText(leaders.get(position).getLeader_phone());
            return convertView;
        }
    }

    class BasicAdapter extends BaseAdapter {
        private Context mContext;

        public BasicAdapter(Context context) {
            mContext = context;
        }

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
                convertView = View.inflate(mContext, R.layout.item_list, null);
            }
            TextView item_name = ViewHolderUtil.get(convertView, R.id.item_name);
            TextView item_phone = ViewHolderUtil.get(convertView, R.id.item_phone);
            RadioButton item_radio = ViewHolderUtil.get(convertView, R.id.item_radio);
            item_radio.setChecked(true);
            item_name.setText(basics.get(position).getFname());
            item_phone.setText(basics.get(position).getFphone());
            return convertView;
        }
    }
}
