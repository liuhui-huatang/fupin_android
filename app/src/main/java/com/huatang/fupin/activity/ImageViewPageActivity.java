package com.huatang.fupin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.huatang.fupin.utils.ToastUtil;
import com.huatang.fupin.utils.UploadUtils;
import com.huatang.fupin.view.PhotoViewPager;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.io.Serializable;
import java.util.ArrayList;
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
    private List photoList;
    private int currentPosition = 0;
    private int size =0;
    private MyImageAdapter myImageAdapter;
    private String from;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewpage);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        photoList = (List<String>)intent.getSerializableExtra("photos");
        initHeadView();
        size = photoList.size();
        myImageAdapter = new MyImageAdapter(photoList,this);
        photoViewPager.setAdapter(myImageAdapter);
        photoViewPager.setCurrentItem(currentPosition, false);
        mTvImageCount.setText(currentPosition+1 + "/" + size);
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
        if(!TextUtils.isEmpty(from) && from.equals("BangFuInfoActivity")){//帮扶日子详情页面的图片浏览
            rightMenu.setVisibility(View.INVISIBLE);
            delete_photo.setVisibility(View.INVISIBLE);
        }else{
            if(type.equals(Config.GANBU_TYPE) || type.equals(Config.ADMIN_TYPE)){
                rightMenu.setText("添加");
                rightMenu.setVisibility(View.VISIBLE);
                delete_photo.setVisibility(View.VISIBLE);
            }
        }
    }
    @OnClick({R.id.left_menu,R.id.right_tx_menu,R.id.delete_photo})
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
                myImageAdapter.notifyDataSetChanged();
                size = photoList.size();
                mTvImageCount.setText(currentPosition+1 + "/" + size);
                break;
        }
    }

    private void showUploadDialog() {
        UploadUtils uploadUtils = new UploadUtils(this,R.id.image_viewpage_layout,mOnHanlderResultCallback);
        uploadUtils.showSelectPicture();


    }
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                String imagePath = resultList.get(0).getPhotoPath();
                imagePath = ImageUtil.getCompressedImgPath(imagePath);
                MLog.e("onHanlderSuccess", imagePath);
                imagesUpload(imagePath);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(ImageViewPageActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };


    public void imagesUpload(String filePath) {
        /**
         * 图片上传服务器
         */
        NewHttpRequest.uploadImage(this, filePath, new NewHttpRequest.UploadCallBack() {
            @Override
            public void callback(String json) {
                ToastUtil.show("修改成功");
                String photoUrl = BaseConfig.ImageUrl + JsonUtil.getStringFromArray(json,"url");
                //调用一个更新到数据库的接口
                photoList.add(photoUrl);
                myImageAdapter.notifyDataSetChanged();
                size = photoList.size();
                mTvImageCount.setText(currentPosition+1 + "/" + size);

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
        private List<View>viewList;

        public MyImageAdapter(List<String> imageUrls, AppCompatActivity activity) {
            this.imageUrls = imageUrls;
            this.activity = activity;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String url = imageUrls.get(position);
            PhotoView photoView = new PhotoView(activity);
            GlideUtils.displayUrl(photoView,url,R.mipmap.news_default_img);
            container.addView(photoView);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.finish();
                }
            });
            viewList = new ArrayList<>() ;
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
            if (viewList.contains((View) object)) {
                // 如果当前 item 未被 remove，则返回 item 的真实 position
                Log.e("view is contains", true + "");
                return viewList.indexOf((View) object);
            } else {
                // 否则返回状态值 POSITION_NONE
                Log.e("view is contains", false + "");
                return POSITION_NONE;
            }
        }
    }
    public static void startIntent(Activity activity,List<String> photoList,String from) {
        Intent it = new Intent(activity, ImageViewPageActivity.class);
        it.putExtra("from",from);
        //it.putStringArrayListExtra("photos",  (ArrayList<String>) photoList);
        it.putExtra("photos", (Serializable) photoList);
        activity.startActivity(it);
    }
}
