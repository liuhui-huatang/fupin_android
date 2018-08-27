package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.bean.Archive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FupinPolicyTabActivity extends BaseActivity {



    @BindView(R.id.left_menu)
    ImageView leftmenu;
    @BindView(R.id.title_tx)
    TextView tvTitle;
    private TabLayout mTabTl;
    private ViewPager mContentVp;

    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    private ContentPagerAdapter contentAdapter;
    private final static Map<String,String> titleMap = new HashMap<String,String>(){
       {
           put("1","分类扶持抓巩固");
           put("2","整体推进抓提升");
           put("3","深化东西部协作");

       }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);
        ButterKnife.bind(this);
        initHeadView();
        mTabTl = (TabLayout) findViewById(R.id.tl_tab);
        mContentVp = (ViewPager) findViewById(R.id.vp_content);
        tabFragments = new ArrayList<>() ;
        tabIndicators = new ArrayList<String>();
        for(String tabType : titleMap.keySet()){
            tabIndicators.add(titleMap.get(tabType));
            tabFragments.add(TabContentFragment.newInstance(tabType));
        }
        initContent();
        initTab();
    }

    private void initHeadView() {
        tvTitle.setText("政策保障");
        leftmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initTab(){
        mTabTl.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabTl.setTabTextColors(ContextCompat.getColor(this, R.color.black), ContextCompat.getColor(this, R.color.color_blue));
        mTabTl.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.color_blue));
        ViewCompat.setElevation(mTabTl, 10);
        mTabTl.setupWithViewPager(mContentVp);

    }

    private void initContent(){

        contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        mContentVp.setAdapter(contentAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_tab_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


        }
        return super.onOptionsItemSelected(item);
    }

    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getCount() {
            return tabIndicators.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabIndicators.get(position);
        }
    }

    public static void startIntent(Activity activity) {
        Intent it = new Intent(activity, FupinPolicyTabActivity.class);
        activity.startActivity(it);
    }
}
