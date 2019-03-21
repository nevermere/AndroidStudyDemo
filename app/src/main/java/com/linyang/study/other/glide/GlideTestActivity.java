package com.linyang.study.other.glide;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.linyang.study.R;
import com.linyang.study.app.BaseActivity;

import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述:
 * Created by fzJiang on 2019/02/19 13:29 星期二
 */
public class GlideTestActivity extends BaseActivity {

    private static final String URL_IMAGE = "http://cn.bing.com/az/hprichbg/rb/TOAD_ZH-CN7336795473_1920x1080.jpg";
    private static final String URL_GIF = "http://guolin.tech/test.gif";

    @BindView(R.id.bt_load)
    AppCompatButton btLoad;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.bt_cache_load)
    AppCompatButton btCacheLoad;

    private RequestOptions mRequestOptions;

    @Override
    public int getLayoutId() {
        return R.layout.activity_glide_test;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mRequestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)// 加载策略
                .placeholder(R.drawable.music)// 占位符
                .error(R.drawable.empty_photo);// 加载出错显示
    }

    @OnClick({R.id.bt_load, R.id.bt_cache_load})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_load:
                Glide.with(ivImage.getContext())
                        .setDefaultRequestOptions(mRequestOptions)
                        .load(URL_GIF)
                        .listener(new RequestListener<Drawable>() {

                            @Override
                            public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(ivImage);
                break;

            case R.id.bt_cache_load:
                System.gc();
                break;
        }
    }
}
