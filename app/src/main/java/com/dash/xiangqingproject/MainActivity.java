package com.dash.xiangqingproject;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);

        Map<String, String> params = new HashMap<>();
        params.put("pid","47");

        OkHttp3Util_03.doPost("https://www.zhaoapi.cn/product/getProductDetail", params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){

                    String json = response.body().string();
                    final DetailBean detailBean = new Gson().fromJson(json,DetailBean.class);
                    if ("0".equals(detailBean.getCode())){

                        //展示轮播图...主线程
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final ArrayList<String> imageUrls = new ArrayList<>();
                                String[] split = detailBean.getData().getImages().split("\\|");
                                for (int i =0;i<split.length;i++){
                                    imageUrls.add(split[i]);
                                }

                                viewPager.setAdapter(new PagerAdapter() {
                                    @Override
                                    public Object instantiateItem(ViewGroup container, int position) {
                                        ImageView imageView = new ImageView(MainActivity.this);
                                        //加载图片显示
                                        Glide.with(MainActivity.this).load(imageUrls.get(position)).into(imageView);

                                        //添加
                                        container.addView(imageView);

                                        imageView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                //跳转到下一个放大图片的页面,,,并传值过去
                                                Intent intent = new Intent(MainActivity.this,PicActivity.class);

                                                //直接传递string类型的arrayList
                                                intent.putStringArrayListExtra("list",imageUrls);
                                                startActivity(intent);
                                            }
                                        });
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
                        });

                    }

                }
            }
        });

    }
}
