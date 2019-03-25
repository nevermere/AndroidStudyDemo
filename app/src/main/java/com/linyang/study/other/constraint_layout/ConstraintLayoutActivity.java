package com.linyang.study.other.constraint_layout;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.linyang.study.R;
import com.linyang.study.app.BaseActivity;
import com.linyang.study.app.util.DisplayUtil;
import com.linyang.study.app.util.ToastUtil;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述:
 * Created by fzJiang on 2019/03/21 9:31 星期四
 */
public class ConstraintLayoutActivity extends BaseActivity {

    @BindView(R.id.group_like)
    Group groupLike;
    @BindView(R.id.group_edit)
    Group groupEdit;
    @BindView(R.id.group_upload)
    Group groupUpload;

    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;
    @BindView(R.id.fab_like)
    FloatingActionButton fabLike;
    @BindView(R.id.fab_edit)
    FloatingActionButton fabEdit;
    @BindView(R.id.fab_upload)
    FloatingActionButton fabUpload;

    private AnimatorSet mAnimatorSet;// 动画集合,用来控制动画的有序播放
    private int mRadius; // 圆的半径
    private int mWidth; // FloatingActionButton宽度和高度，宽高一样


    @Override
    public int getLayoutId() {
        return R.layout.activity_constraint_layout;
    }

    @Override
    public void initView() {
        // 动态获取FloatingActionButton的宽
        fabAdd.post(() -> mWidth = fabAdd.getMeasuredWidth());
        // 在xml文件里设置的半径
        mRadius = DisplayUtil.dip2px(this, 80);
        // 将三个弹出的FloatingActionButton隐藏
        setViewVisible(false);
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.fab_add, R.id.fab_like, R.id.fab_edit, R.id.fab_upload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_add:
                // 播放动画的时候不可以点击
                if (mAnimatorSet != null && mAnimatorSet.isRunning()) {
                    return;
                }
                // 设置动画
                mAnimatorSet = new AnimatorSet();
                if (View.VISIBLE != groupLike.getVisibility()) {
                    ValueAnimator animatorLike = getValueAnimator(fabLike, false, groupLike, 0);
                    ValueAnimator animatorEdit = getValueAnimator(fabEdit, false, groupEdit, 45);
                    ValueAnimator animatorUpload = getValueAnimator(fabUpload, false, groupUpload, 90);
                    mAnimatorSet.playSequentially(animatorLike, animatorEdit, animatorUpload);
                } else {
                    ValueAnimator animatorLike = getValueAnimator(fabLike, true, groupLike, 0);
                    ValueAnimator animatorEdit = getValueAnimator(fabEdit, true, groupEdit, 45);
                    ValueAnimator animatorUpload = getValueAnimator(fabUpload, true, groupUpload, 90);
                    mAnimatorSet.playSequentially(animatorUpload, animatorEdit, animatorLike);
                }
                mAnimatorSet.start();
                break;

            case R.id.fab_like:
                if (View.VISIBLE == groupLike.getVisibility()) {
                    ToastUtil.showToast(getApplicationContext(), "Like Click");
                }
                break;

            case R.id.fab_edit:
                if (View.VISIBLE == groupEdit.getVisibility()) {
                    ToastUtil.showToast(getApplicationContext(), "Edit Click");
                }
                break;

            case R.id.fab_upload:
                if (View.VISIBLE == groupUpload.getVisibility()) {
                    ToastUtil.showToast(getApplicationContext(), "Upload Click");
                }
                break;
        }
    }

    /**
     * 设置界面显示/隐藏
     *
     * @param visible true:显示/false:隐藏
     */
    private void setViewVisible(boolean visible) {
        groupLike.setVisibility(visible ? View.VISIBLE : View.GONE);
        groupEdit.setVisibility(visible ? View.VISIBLE : View.GONE);
        groupUpload.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 获取ValueAnimator
     *
     * @param button  FloatingActionButton
     * @param reverse 开始还是隐藏
     * @param group   Group
     * @param angle   angle 转动的角度
     * @return ValueAnimator
     */
    private ValueAnimator getValueAnimator(final FloatingActionButton button, final boolean reverse, final Group group, final int angle) {
        ValueAnimator valueAnimator;
        if (reverse) {
            valueAnimator = ValueAnimator.ofFloat(1, 0);
        } else {
            valueAnimator = ValueAnimator.ofFloat(0, 1);
        }
        valueAnimator.setDuration(300);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        // 设置播放进度监听，实时更新按钮位置
        valueAnimator.addUpdateListener(animation -> {
            // 获取动画进度
            float value = (float) animation.getAnimatedValue();
            // 更新位置
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) button.getLayoutParams();
            layoutParams.circleRadius = (int) (mRadius * value);
            layoutParams.circleAngle = 270f + angle * value;
            button.setLayoutParams(layoutParams);
        });
        // 设置动画播放状态监听，播放完毕设置界面显示/隐藏
        valueAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation, boolean isReverse) {

            }

            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {

            }

            @Override
            public void onAnimationStart(Animator animation) {
                // 动画开始时，显示
                group.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束，设置隐藏
                if (group == groupLike && reverse) {
                    setViewVisible(false);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        return valueAnimator;
    }
}
