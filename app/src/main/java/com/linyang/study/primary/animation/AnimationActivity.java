package com.linyang.study.primary.animation;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.linyang.study.R;
import com.linyang.study.primary.animation.widget.Rotate3dAnimation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * Created by fzJiang on 2018/11/26 上午 8:53 星期一
 */
public class AnimationActivity extends AppCompatActivity {

    @BindView(R.id.bt_rotate_3d_animation)
    AppCompatButton btRotate3dAnimation;
    @BindView(R.id.bt_frame_animation)
    AppCompatButton btFrameAnimation;
    @BindView(R.id.bt_value_animator)
    AppCompatButton btValueAnimator;
    @BindView(R.id.bt_object_animator)
    AppCompatButton btObjectAnimator;
    @BindView(R.id.bt_animator_set)
    AppCompatButton btAnimatorSet;
    @BindView(R.id.bt_animator_set_personal)
    AppCompatButton btAnimatorSetPersonal;
    @BindView(R.id.bt_animator_set_personal2)
    AppCompatButton btAnimatorSetPersonal2;
    @BindView(R.id.bt_android_interpolator)
    AppCompatButton btAndroidInterpolator;
    @BindView(R.id.bt_animation_advanced)
    AppCompatButton btAnimationAdvanced;
    @BindView(R.id.iv_heart)
    ImageView ivHeart;

    private IntEvaluator mIntEvaluator = new IntEvaluator();

    private boolean isStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        ButterKnife.bind(this);
    }

    @OnClick({
            R.id.bt_rotate_3d_animation,
            R.id.bt_frame_animation,
            R.id.bt_value_animator,
            R.id.bt_object_animator,
            R.id.bt_animator_set,
            R.id.bt_animator_set_personal,
            R.id.bt_animator_set_personal2,
            R.id.bt_android_interpolator,
            R.id.bt_animation_advanced,
//            R.id.iv_heart
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_rotate_3d_animation:
                Rotate3dAnimation rotate3dAnimation = new Rotate3dAnimation(0, 360, 300, 300, 50, true);
                rotate3dAnimation.setDuration(5000);
                btRotate3dAnimation.clearAnimation();
                btRotate3dAnimation.setAnimation(rotate3dAnimation);
                rotate3dAnimation.start();
                break;

            case R.id.bt_frame_animation:
                btFrameAnimation.clearAnimation();
                btFrameAnimation.setBackgroundResource(R.drawable.frame_animation);
                AnimationDrawable animationDrawable = (AnimationDrawable) btFrameAnimation.getBackground();
                animationDrawable.start();
                break;

            case R.id.bt_value_animator:
                btValueAnimator.clearAnimation();
                ValueAnimator colorAnimator = ObjectAnimator.ofInt(btValueAnimator, "backgroundColor", 0xFFFF8080, 0xFF8080FF);
                colorAnimator.setDuration(5000);
                colorAnimator.setEvaluator(new ArgbEvaluator());
                colorAnimator.setRepeatCount(ValueAnimator.INFINITE);
                colorAnimator.setRepeatMode(ValueAnimator.REVERSE);
                colorAnimator.start();
                break;

            case R.id.bt_object_animator:
                btObjectAnimator.clearAnimation();
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(btObjectAnimator, "translationX", 500);
                objectAnimator.setDuration(5000);
                objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
                objectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
                objectAnimator.start();
                break;

            case R.id.bt_animator_set:
                btAnimatorSet.clearAnimation();
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(
                        ObjectAnimator.ofFloat(btAnimatorSet, "rotationX", 0, 360),
                        ObjectAnimator.ofFloat(btAnimatorSet, "rotationY", 0, 180),
                        ObjectAnimator.ofFloat(btAnimatorSet, "rotation", 0, -90),
                        ObjectAnimator.ofFloat(btAnimatorSet, "translationX", 0, 90),
                        ObjectAnimator.ofFloat(btAnimatorSet, "translationY", 0, 90),
                        ObjectAnimator.ofFloat(btAnimatorSet, "scaleX", 1, 1.5f),
                        ObjectAnimator.ofFloat(btAnimatorSet, "scaleY", 1, 0.5f),
                        ObjectAnimator.ofFloat(btAnimatorSet, "alpha", 1, 0.25f, 1)
                );
                animatorSet.setDuration(5000);
                animatorSet.start();

//                // 插值器
//                LinearInterpolator linearInterpolator;
//                TimeInterpolator timeInterpolator;
//                AccelerateDecelerateInterpolator accelerateDecelerateInterpolator;
//                DecelerateInterpolator decelerateInterpolator;
//
//                // 估值器
//                IntEvaluator intEvaluator;
//                FloatEvaluator floatEvaluator;
//                ArgbEvaluator argbEvaluator;
                break;

            case R.id.bt_animator_set_personal:
                ViewWrapper viewWrapper = new ViewWrapper(btAnimatorSetPersonal);
                ObjectAnimator animator = ObjectAnimator.ofInt(viewWrapper, "width", 100, 800);
                animator.setDuration(5000);
                animator.setRepeatCount(ObjectAnimator.INFINITE);
                animator.setRepeatMode(ObjectAnimator.REVERSE);
                animator.start();
                break;

            case R.id.bt_animator_set_personal2:
                ValueAnimator valueAnimator = ValueAnimator.ofInt(100, 800);
                valueAnimator.addUpdateListener(valueAnimator1 -> {
                    // 获取当前动画的进度(0~100)
                    int currentValue = (int) valueAnimator1.getAnimatedValue();
                    // 获取当前进度比例
                    float fraction = valueAnimator1.getAnimatedFraction();
                    // 通过整型估值器，计算出宽度
                    btAnimatorSetPersonal2.getLayoutParams().width = mIntEvaluator.evaluate(fraction, 100, 800);
                    btAnimatorSetPersonal2.requestLayout();
                });
                valueAnimator.setDuration(5000);
                valueAnimator.setRepeatCount(ObjectAnimator.INFINITE);
                valueAnimator.setRepeatMode(ObjectAnimator.REVERSE);
                valueAnimator.start();
                break;

            case R.id.bt_android_interpolator:
                startActivity(new Intent(this, InterpolatorActivity.class));
                break;

            case R.id.bt_animation_advanced:
                startActivity(new Intent(this, AdvancedAnimationActivity.class));
                break;

            case R.id.iv_heart:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    AnimatedVectorDrawable emptyDrawable = (AnimatedVectorDrawable) getDrawable(R.drawable.avd_path_heart_empty);
                    AnimatedVectorDrawable fullDrawable = (AnimatedVectorDrawable) getDrawable(R.drawable.avd_path_heart_full);

                    AnimatedVectorDrawable drawable = isStart ? emptyDrawable : fullDrawable;
                    ivHeart.setImageDrawable(drawable);
                    if (drawable != null) {
                        drawable.start();
                        isStart = !isStart;
                    }
                }
                break;
        }
    }


    /**
     * 使用类包装原始对象,间接为其提供set、get方法
     */
    private class ViewWrapper {

        private View mView;

        ViewWrapper(View view) {
            mView = view;
        }

        public int getWidth() {
            return mView.getLayoutParams().width;
        }

        public void setWidth(int width) {
            mView.getLayoutParams().width = width;
            mView.requestLayout();
        }
    }
}
