package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.AsyncListUtil;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.huatang.fupin.R;
import com.huatang.fupin.app.BaseActivity;
import com.huatang.fupin.app.BaseConfig;
import com.huatang.fupin.app.Config;
import com.huatang.fupin.http.NewHttpRequest;
import com.huatang.fupin.utils.GlideImageLoader;
import com.huatang.fupin.utils.GlideUtils;
import com.huatang.fupin.utils.ImageUtil;
import com.huatang.fupin.utils.JsonUtil;
import com.huatang.fupin.utils.MLog;
import com.huatang.fupin.utils.SPUtil;
import com.huatang.fupin.utils.StringUtil;
import com.huatang.fupin.utils.ToastUtil;
import com.huatang.fupin.utils.UploadUtils;
import com.huatang.fupin.view.PhotoViewPager;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.widget.zoonview.PhotoView;

public class ImageViewPageActivity extends BaseActivity {

    @BindView(R.id.photoViewPager)
    PhotoViewPager photoViewPager;
    @BindView(R.id.mTvImageCount)
    TextView mTvImageCount;

    @BindView(R.id.delete_photo)
    ImageView delete_photo;
    @BindView(R.id.left_menu)
    ImageView leftMenu;

    @BindView(R.id.title_tx)
    TextView tvTitle;
    @BindView(R.id.right_tx_menu)
    TextView rightMenu;
    private List<String> photoList;
    private int currentPosition = 0;
    private int size = 0;
    private MyImageAdapter myImageAdapter;
    private String from;
    private String fcard;
    private String year;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewpage);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        fcard = intent.getStringExtra("fcard");
        year = TextUtils.isEmpty(SPUtil.getString(Config.YEAR)) ? String.valueOf(Calendar.getInstance().get(Calendar.YEAR)) : SPUtil.getString(Config.YEAR);
        photoList = (List<String>) intent.getSerializableExtra("photos");
        initHeadView();
        size = photoList.size();
        myImageAdapter = new MyImageAdapter(photoList, this);
        photoViewPager.setAdapter(myImageAdapter);
        photoViewPager.setCurrentItem(currentPosition, false);
        mTvImageCount.setText(currentPosition + 1 + "/" + size);
        photoViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
                mTvImageCount.setText(currentPosition + 1 + "/" + size);
            }
        });
    }

    private void initHeadView() {
        tvTitle.setText("图片浏览");
        String type = SPUtil.getString(Config.Type);
        if(type.equals(Config.GANBU_TYPE) || type.equals(Config.ADMIN_TYPE)){
            if(!TextUtils.isEmpty(from)){
                rightMenu.setText("添加");
                rightMenu.setVisibility(View.VISIBLE);
                delete_photo.setVisibility(View.VISIBLE);
            }

        }else{
            rightMenu.setVisibility(View.INVISIBLE);
            delete_photo.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick({R.id.left_menu, R.id.right_tx_menu, R.id.delete_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_menu:
                finish();
                break;
            case R.id.right_tx_menu:
                showUploadDialog();
                break;
            case R.id.delete_photo:
                photoList.remove(currentPosition);
                saveImage();
                break;
        }
    }

    private void showUploadDialog() {

        UploadUtils.getmInstance().start(this,R.id.image_viewpage_layout, new UploadUtils.MyCallBack() {
            @Override
            public void success(String url) {
                String photoUrl = BaseConfig.ImageUrl + url;
                //调用一个更新到数据库的接口
                photoList.add(url);
                saveImage();
            }
        });

    }



    public void saveImage() {
        myImageAdapter.notifyDataSetChanged();
        size = photoList.size();
        mTvImageCount.setText(currentPosition + 1 + "/" + size);
        NewHttpRequest.editPoorPhotoWithFcard(this, fcard, year, from, StringUtil.listToString(photoList, "###"), new NewHttpRequest.MyCallBack(this) {
            @Override
            public void ok(String json) {
                ToastUtil.show("图片编辑成功");
                Intent intent = new Intent();
                intent.putExtra("photoList", (Serializable) photoList);

                setResult(RESULT_OK, intent);
            }

            @Override
            public void no(String msg) {
                ToastUtil.show(msg);

            }
        });
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

    public class MyImageAdapter extends PagerAdapter {
        private List<String> imageUrls;
        private AppCompatActivity activity;
        private List<View> viewList;

        public MyImageAdapter(List<String> imageUrls, AppCompatActivity activity) {
            this.imageUrls = imageUrls;
            this.activity = activity;

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String url = BaseConfig.ImageUrl + imageUrls.get(position);
            PhotoView photoView = new PhotoView(activity);
            GlideUtils.displayUrl(photoView, url, R.mipmap.news_default_img);
            container.addView(photoView);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.finish();
                }
            });
            if (viewList == null) {
                viewList = new ArrayList<>();
            }
            //
            viewList.add(photoView);
            return photoView;
        }

        @Override
        public int getCount() {
            return imageUrls != null ? imageUrls.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {

            return POSITION_NONE;

        }
    }

    public static void startIntent(Activity activity, List<String> photoList, int from, String fcard) {
        Intent it = new Intent(activity, ImageViewPageActivity.class);
        it.putExtra("from", String.valueOf(from));
        it.putExtra("photos", (Serializable) photoList);
        it.putExtra("fcard", fcard);
        activity.startActivityForResult(it, Integer.valueOf(from));
    }
    public static void startIntent(Activity activity, List<String> photoList){
        Intent it = new Intent(activity, ImageViewPageActivity.class);
        it.putExtra("photos", (Serializable) photoList );
        activity.startActivity(it);
    }
}
