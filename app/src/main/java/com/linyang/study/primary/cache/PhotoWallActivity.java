package com.linyang.study.primary.cache;

import android.os.Bundle;
import android.widget.GridView;

import com.linyang.study.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * Created by fzJiang on 2018/12/29 9:12 星期六
 */
public class PhotoWallActivity extends AppCompatActivity {

    @BindView(R.id.gv_photo_wall)
    GridView gvPhotoWall;

    private PhotoWallAdapter mPhotoWallAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_wall);
        ButterKnife.bind(this);
        initGridView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPhotoWallAdapter.flushCache();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPhotoWallAdapter.cancelAllTasks();
    }

    private void initGridView() {
        mPhotoWallAdapter = new PhotoWallAdapter(this, 0, Images.IMAGE_THUMB_URLS, gvPhotoWall);
        gvPhotoWall.setAdapter(mPhotoWallAdapter);
//        gvPhotoWall.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//            @Override
//            public void onGlobalLayout() {
//                final int numColumns = (int) Math.floor(gvPhotoWall.getWidth() / (50 + 5));
//                if (numColumns > 0) {
//                    int columnHeight = (gvPhotoWall.getWidth() / numColumns) - 5;
//                    mPhotoWallAdapter.setItemHeight(columnHeight);
//                    gvPhotoWall.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                }
//            }
//        });
    }

}
