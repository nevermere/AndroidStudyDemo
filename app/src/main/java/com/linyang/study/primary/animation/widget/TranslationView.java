package com.linyang.study.primary.animation.widget;

import android.content.Context;
import android.graphics.Outline;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.linyang.study.R;
import com.linyang.study.app.util.DisplayUtil;

import androidx.annotation.RequiresApi;

/**
 * 描述:
 * Created by fzJiang on 2018/12/19 13:41 星期三
 */
public class TranslationView extends RelativeLayout implements View.OnClickListener {

    private ImageView mImageView;

    private int mTranslationStateCount = Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP ? 6 : 4;
    private int mTranslationState = 0;

    public TranslationView(Context context) {
        super(context);
    }

    public TranslationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mImageView = findViewById(R.id.iv_image);

        Button startBtn = findViewById(R.id.bt_start);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startBtn.setOutlineProvider(new MusicOutlineProvider());
        }
        startBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (mTranslationState) {
            case 0:// x轴 0 -> 100
                mImageView.animate().translationX(DisplayUtil.dip2px(getContext(), 100));
                break;

            case 1:// x轴 100 -> 0
                mImageView.animate().translationX(0);
                break;

            case 2:// y轴 0 -> 100
                mImageView.animate().translationY(DisplayUtil.dip2px(getContext(), 100));
                break;

            case 3:// y轴 100 -> 0
                mImageView.animate().translationY(0);
                break;

            case 4:// z轴 0 -> 100
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mImageView.animate().translationZ(DisplayUtil.dip2px(getContext(), 100));
                }
                break;

            case 5:// z轴 100 -> 0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mImageView.animate().translationZ(0);
                }
                break;
        }

        mTranslationState++;
        if (mTranslationState == mTranslationStateCount) {
            mTranslationState = 0;
        }
    }

    /**
     * 自定义外阴影效果
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class MusicOutlineProvider extends ViewOutlineProvider {

        Path mPath = new Path();

        {
            mPath.moveTo(0, DisplayUtil.dip2px(getContext(), 10));
            mPath.lineTo(DisplayUtil.dip2px(getContext(), 7), DisplayUtil.dip2px(getContext(), 2));
            mPath.lineTo(DisplayUtil.dip2px(getContext(), 116), DisplayUtil.dip2px(getContext(), 58));
            mPath.lineTo(DisplayUtil.dip2px(getContext(), 116), DisplayUtil.dip2px(getContext(), 70));
            mPath.lineTo(DisplayUtil.dip2px(getContext(), 7), DisplayUtil.dip2px(getContext(), 128));
            mPath.lineTo(0, DisplayUtil.dip2px(getContext(), 120));
            mPath.close();
        }

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setConvexPath(mPath);
        }
    }
}
