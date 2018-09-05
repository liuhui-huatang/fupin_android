package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.bean.MessageBoard;
import com.huatang.fupin.bean.NewLeader;
import com.huatang.fupin.bean.NewPoor;
import com.huatang.fupin.http.NewHttpRequest;
import com.huatang.fupin.utils.GlideUtils;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.SkinUtil;
import com.huatang.fupin.utils.ToastUtil;
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

public class QunZhongHuDongActivity extends BaseActivity {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.right_menu)
    ImageView rightMenu;
    @BindView(R.id.title_tx)
    TextView tvTitle;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    private List<MessageBoard> list ;
    private MyAdapter adapter;
    private String type;
    private int load = 1;
    private NewLeader leader;
    private NewPoor poor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hudong);
        ButterKnife.bind(this);
        initView();
        getData();
    }

    private void initView() {
        tvTitle.setText("群众互动");
        type = SPUtil.getString(Config.Type);
        if(type.equals(Config.PENKUNHU_TYPE)){
            rightMenu.setImageResource(SkinUtil.getResouceId(R.mipmap.img_add));
            rightMenu.setVisibility(View.VISIBLE);
        }

        list = new ArrayList<MessageBoard>();
        RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setPrimaryColors(getResources().getColor(R.color.dodgerblue));
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(true));
        //设置 Footer 为 球脉冲
        refreshLayout.setEnableAutoLoadmore(false);
        refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout freshlayout) {
                freshlayout.finishRefresh(true);
                load = 1 ;
                getData();


            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(1000);
                list.clear();
                getData();
            }
        });

        adapter = new MyAdapter(this);
        listview.setAdapter(adapter);
        //条目点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageBoradDetailActivity.startIntent(QunZhongHuDongActivity.this,list.get(position));

            }
        });
    }
    private void getData(){
        //网络请求数据
        switch (type){
            case Config.PENKUNHU_TYPE:
                poor = (NewPoor)SPUtil.getObject(Config.PENKUNHU_KEY);
                poorGetLeaveMsgList();
                break;
            case Config.ADMIN_TYPE:
                leader = (NewLeader) SPUtil.getObject(Config.ADMIN_KEY);
                getLeaveMsgListWithTown(leader);
                break;
            case Config.GANBU_TYPE:
                leader = (NewLeader) SPUtil.getObject(Config.GANBU_KEY);
                getLeaveMsgListWithTown(leader);
                break;
        }

    }

    private void getLeaveMsgListWithTown(NewLeader leader) {
        if(leader == null ){
            ToastUtil.show("系统出错，请重新登录");
            return;
        }
        if (load == -1) {
            ToastUtil.show("没有更多数据了");
            return;
        }

        NewHttpRequest.getLeaveMsgListWithTown(this,leader.getHelp_town_id(),String.valueOf(load), new NewHttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                List<MessageBoard> messageBoardList = JsonUtil.toList(json,MessageBoard.class);
                if (messageBoardList.size() == 0 && load != 1) {
                    ToastUtil.show("没有更多数据了");
                    load = -1;
                    return;
                }
                list.addAll(messageBoardList);
                if (list.size() > 0) {
                    tvEmpty.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
                }
                load++;
            }

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);

            }
        });
    }

    private void poorGetLeaveMsgList() {
        if(poor == null ){
            ToastUtil.show("系统出错，请重新登录");
            return;
        }
        if (load == -1) {
            ToastUtil.show("没有更多数据了");
            return;
        }
        poor = (NewPoor)SPUtil.getObject(Config.PENKUNHU_KEY);
        NewHttpRequest.poorGetLeaveMsgList(this, poor.getFcard(),String.valueOf(load), new NewHttpRequest.MyCallBack() {
            @Override
            public void ok(String json) {
                List<MessageBoard> messageBoardList = JsonUtil.toList(json,MessageBoard.class);
                if (messageBoardList.size() == 0 && load != 1) {
                    ToastUtil.show("没有更多数据了");
                    load = -1;
                    return;
                }
                list.addAll(messageBoardList);
                if (list.size() > 0) {
                    tvEmpty.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
                }
                load++;
            }

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);

            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        list.clear();
        load = 1;
        getData();
    }

    @OnClick({R.id.left_menu,R.id.right_menu})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.left_menu:
                finish();
                break;
            case R.id.right_menu:
                CreateMessageBoardActivity.startIntent(this);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    class MyAdapter extends BaseAdapter{
        private Context mcontext;
        public MyAdapter(Context context ){
            this.mcontext = context;

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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null ;
            if (convertView == null) {
                convertView = View.inflate(mcontext, R.layout.item_message_board, null);
                viewHolder = new ViewHolder();
                viewHolder.message_board_layout = convertView.findViewById(R.id.message_board_layout);
                viewHolder.iv_photo = convertView.findViewById(R.id.iv_photo);
                viewHolder.author = convertView.findViewById(R.id.author);
                viewHolder.title = convertView.findViewById(R.id.title);
                viewHolder.item_createTime = convertView.findViewById(R.id.item_createTime);
                viewHolder.author_ll = convertView.findViewById(R.id.author_ll);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            MessageBoard  messageBoard = list.get(position);
            viewHolder.title.setText(messageBoard.getTitle());
            viewHolder.item_createTime.setText(messageBoard.getCreate_time());
            if(leader != null ){
                viewHolder.author.setText(messageBoard.getName());
                viewHolder.iv_photo.setVisibility(View.VISIBLE);
                viewHolder.author_ll.setVisibility(View.VISIBLE);
                GlideUtils.LoadCircleImageWithoutBorderColor((Activity)mcontext, BaseConfig.ImageUrl+leader.getLeader_photo(),viewHolder.iv_photo);
            }else if(poor != null){
                viewHolder.iv_photo.setVisibility(View.GONE);
                viewHolder.author_ll.setVisibility(View.GONE);
            }
            return convertView;
        }
    }
    class ViewHolder{
        private RelativeLayout message_board_layout;
        private ImageView iv_photo;
        private TextView title;
        private TextView item_createTime;
        private TextView author;
        private LinearLayout author_ll;

    }
    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, QunZhongHuDongActivity.class);
        activity.startActivity(it);
    }
}
