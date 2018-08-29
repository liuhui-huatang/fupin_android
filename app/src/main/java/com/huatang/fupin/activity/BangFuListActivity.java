package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.bean.NewLeader;
import com.huatang.fupin.bean.NewSign;
import com.huatang.fupin.bean.Sign;
import com.huatang.fupin.http.HttpRequest;
import com.huatang.fupin.http.NewHttpRequest;
import com.huatang.fupin.utils.DateUtil;
import com.huatang.fupin.utils.GlideUtils;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.SkinUtil;
import com.huatang.fupin.utils.StringUtil;
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
 * 帮扶日志列表页面
 *
 * @author forever
 *         created at 2017/1/9 11:16
 */

public class BangFuListActivity extends BaseActivity {


    @BindView(R.id.bf_listview)
    ListView bfListview;
    @BindView(R.id.left_menu)
    ImageView leftMenu;
    @BindView(R.id.right_menu)
    ImageView rightMenu;
    @BindView(R.id.title_tx)
    TextView tvTitle;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    private int pageNo = 1;



    /*
         * @ forever 在 17/5/17 下午2:28 创建
         *
         * 描述：跳转到登录页面
         *
         */
    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, BangFuListActivity.class);
        activity.startActivity(it);
    }

    /*
    * @ forever 在 17/5/17 下午2:28 创建
    *
    * 描述：页面创建时调用
    *
    */
    Adapter adapter;
    NewLeader leader ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bangfu);
        ButterKnife.bind(this);
        initHeadView();
        leader = SPUtil.getLeaderFromSharePref();
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
                pageNo = 1 ;
                getData();


            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(1000);
                list.clear();
                pageNo ++ ;
                getData();
            }
        });

        adapter = new Adapter(BangFuListActivity.this);
        bfListview.setAdapter(adapter);
        //条目点击事件
        bfListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BangFuInfoActivity.startIntent(BangFuListActivity.this, list.get(position));
            }
        });
        getData();

    }

    private void initHeadView() {
        rightMenu.setImageResource(SkinUtil.getResouceId(R.mipmap.img_add));
        rightMenu.setVisibility(View.VISIBLE);
        tvTitle.setText("帮扶日志");
    }


    List<NewSign> list = new ArrayList<>();

    public void getData() {

        DialogUIUtils.showTie(this, "加载中...");
        NewHttpRequest.getSignList(this,leader.getId(),String.valueOf(pageNo),new NewHttpRequest.MyCallBack(){
            @Override
            public void ok(String json) {
                List<NewSign> signList = JsonUtil.toList(json, NewSign.class);
                list.addAll(signList);
                DialogUIUtils.dismssTie();
                if (list.size() > 0) {

                    tvEmpty.setVisibility(View.GONE);
                    bfListview.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                    bfListview.setVisibility(View.GONE);
                }
            }

            @Override
            public void no(String msg) {
                DialogUIUtils.dismssTie();
                ToastUtil.show(msg);

            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        list.clear();
        pageNo = 1;
        getData();
    }

    @OnClick({R.id.left_menu, R.id.right_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.right_menu:
                BangFuSignActivity.startIntent(this);
                break;
        }
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
                convertView = View.inflate(mContext, R.layout.item_bangfu, null);
            }

            LinearLayout layout_images = ViewHolderUtil.get(convertView, R.id.layout_images);
            ImageView iv_photo = ViewHolderUtil.get(convertView, R.id.iv_photo);
            TextView tv_title = ViewHolderUtil.get(convertView, R.id.tv_title);
            TextView tv_text = ViewHolderUtil.get(convertView, R.id.tv_text);
            TextView tv_location = ViewHolderUtil.get(convertView, R.id.tv_location);
            TextView tv_time = ViewHolderUtil.get(convertView, R.id.tv_time);


            List<ImageView> imageViewList = new ArrayList<>();
            imageViewList.add((ImageView) ViewHolderUtil.get(convertView, R.id.iv_01));
            imageViewList.add((ImageView) ViewHolderUtil.get(convertView, R.id.iv_02));
            imageViewList.add((ImageView) ViewHolderUtil.get(convertView, R.id.iv_03));
            imageViewList.add((ImageView) ViewHolderUtil.get(convertView, R.id.iv_04));
            imageViewList.add((ImageView) ViewHolderUtil.get(convertView, R.id.iv_05));
            imageViewList.add((ImageView) ViewHolderUtil.get(convertView, R.id.iv_06));
            imageViewList.add((ImageView) ViewHolderUtil.get(convertView, R.id.iv_07));
            imageViewList.add((ImageView) ViewHolderUtil.get(convertView, R.id.iv_08));


            if (!TextUtils.isEmpty(SPUtil.getString(Config.HEAD_PHOTO))) {
               GlideUtils.LoadCircleImageWithoutBorderColor((Activity)mContext, SPUtil.getString(Config.HEAD_PHOTO),iv_photo);
            }else{
                GlideUtils.LoadCircleImageWithoutBorderColor((Activity)mContext, leader.getLeader_photo(),iv_photo);
                SPUtil.saveString(Config.HEAD_PHOTO,leader.getLeader_photo());
            }

            tv_title.setText(list.get(position).getSign_title());
            tv_text.setText(list.get(position).getSign_content());
            tv_location.setText(list.get(position).getSign_address());
            long time = Long.parseLong(list.get(position).getSign_time());
            tv_time.setText(DateUtil.getStandardTime(time));

            String images = list.get(position).getSign_imgs();
            if (!TextUtils.isEmpty(images)) {
                String[] strings = images.split(StringUtil.separator);
                for (int i = 0; i < 8; i++) {
                    if (i < strings.length) {
                        if (!TextUtils.isEmpty(strings[i])) {
                            imageViewList.get(i).setVisibility(View.VISIBLE);

                            GlideUtils.displayHome(imageViewList.get(i), BaseConfig.ImageUrl + strings[i]);
                        }
                    } else {
                        imageViewList.get(i).setVisibility(View.GONE);
                    }
                }
            } else {
                for (int i = 0; i < 8; i++) {
                    imageViewList.get(i).setVisibility(View.GONE);
                }
                layout_images.setVisibility(View.GONE);
            }
            return convertView;
        }

    }


}
