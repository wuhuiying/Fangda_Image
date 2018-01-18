package com.dash.xiangqingproject;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PicActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ArrayList<String> imageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);

        viewPager = findViewById(R.id.view_pager);

        //获取导数据
        imageUrls = getIntent().getStringArrayListExtra("list");

        if (imageUrls != null){
            viewPager.setAdapter(new PagerAdapter() {
                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    ZoomImageView imageView = new ZoomImageView(PicActivity.this);
                    //加载图片显示
                    Glide.with(PicActivity.this).load(imageUrls.get(position)).into(imageView);

                    //添加
                    container.addView(imageView);
                    //返回
                    return imageView;
                }

                @Override
                public void destroyItem(ViewGroup container, int position,
                                        Object object) {
                    container.removeView((View) object);
                }

                @Override
                public boolean isViewFromObject(View arg0, Object arg1) {
                    return arg0 == arg1;
                }

                @Override
                public int getCount() {
                    return imageUrls.size();
                }
            });
        }

    }
}
