package com.linyang.study.primary.animation.widget;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 描述:
 * Created by fzJiang on 2018/11/23 下午 4:21  星期五
 */
public class Rotate3dAnimation extends Animation {

    private float mFromDegrees;
    private float mToDegrees;
    private float mCenterX;
    private float mCenterY;
    private float mDepthZ;

    private boolean mReverse;
    private Camera mCamera;

    public Rotate3dAnimation(float fromDegrees, float toDegrees, float centerX, float centerY, float depthZ, boolean reverse) {
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
        mCenterX = centerX;
        mCenterY = centerY;
        mDepthZ = depthZ;
        mReverse = reverse;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        final float fromDegrees = mFromDegrees;
        float degrees = fromDegrees + ((mToDegrees - mFromDegrees) * interpolatedTime);
        final float centerX = mCenterX;
        final float centerY = mCenterY;
        final Camera camera = mCamera;
        final Matrix matrix = t.getMatrix();

        camera.save();

        if (mReverse) {
            camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
        } else {
            camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
        }
        camera.rotateY(degrees);
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-centerX, centerY);
        matrix.postTranslate(centerX, centerY);
    }
}
