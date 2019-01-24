package com.linyang.study.primary.custom_view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.linyang.study.app.util.DisplayUtil;

import java.util.Calendar;

import androidx.annotation.NonNull;

/**
 * 描述:
 * Created by fzJiang on 2018/11/22 上午 9:39  星期四
 */
public class ClockDrawableView extends Drawable {

    private Paint mPaint;
    private final static int C_default = Color.WHITE;
    private final static String tag = "ClockDrawable";
    private Context mContext;
    private int padding = 2;

    public ClockDrawableView(Context context) {
        this.mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(C_default);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(DisplayUtil.dip2px(mContext, 2));
        mPaint.setTextSize(DisplayUtil.dip2px(mContext, 12));
        padding = DisplayUtil.dip2px(mContext, padding);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        final Rect rect = getBounds();
        //画圆
        drawCircle(rect, canvas);
        //画竖线
        drawVerLine(rect, canvas);
        //画数字
        drawNum(rect, canvas);
        //画时针和分针
        drawHourMinuteHand(rect, canvas);
    }

    private void drawCircle(Rect rect, Canvas canvas) {
        float cx = rect.exactCenterX();
        float cy = rect.exactCenterY();
        float radius = Math.min(cx, cy) - padding;
        canvas.drawCircle(cx, cy, radius, mPaint);
        canvas.drawCircle(cx, cy, DisplayUtil.dip2px(mContext, 2), mPaint);
    }

    private void drawVerLine(Rect rect, Canvas canvas) {
        float cx = rect.exactCenterX();
        float cy = rect.exactCenterY();
        for (int i = 0; i < 12; i++) {
            canvas.save();
            canvas.rotate(30 * i, cx, cy);
            canvas.drawLine(cx, padding, cx, padding + DisplayUtil.dip2px(mContext, 5), mPaint);
            canvas.restore();
        }
    }

    private void drawNum(Rect rect, Canvas canvas) {
        float cx = rect.exactCenterX();
        float cy = rect.exactCenterY();
        for (int i = 0; i < 12; i++) {
            canvas.save();

            String txt = "" + i;
            if (i == 0) {
                txt = "12";
            }
            Rect tempRect = new Rect();
            mPaint.getTextBounds(txt, 0, txt.length(), tempRect);

            if (i == 0 || i == 1 || i == 2 || i == 10 || i == 11) {
                canvas.rotate(30 * i, cx, cy);
                float x = cx - tempRect.width() / 2.0f;
                float y = padding + DisplayUtil.dip2px(mContext, 5) + tempRect.height();
                canvas.drawText(txt, x, y, mPaint);
            } else if (i == 3) {
                float x = rect.width() - padding - tempRect.width() - DisplayUtil.dip2px(mContext, 5);
                float y = rect.height() / 2.0f + tempRect.height() / 2.0f;
                canvas.drawText(txt, x, y, mPaint);
            } else if (i == 9) {
                float x = padding + DisplayUtil.dip2px(mContext, 5);
                float y = rect.height() / 2.0f + tempRect.height() / 2.0f;
                canvas.drawText(txt, x, y, mPaint);
            } else {
                canvas.rotate(30 * (i - 6), cx, cy);
                float x = cx - tempRect.width() / 2.0f;
                float y = rect.height() - padding - DisplayUtil.dip2px(mContext, 5);
                canvas.drawText(txt, x, y, mPaint);
            }
            canvas.restore();
        }
    }

    private void drawHourMinuteHand(Rect rect, Canvas canvas) {
        float cx = rect.exactCenterX();
        float cy = rect.exactCenterY();
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        int rotate = (int) (360f * (hour * 60 * 60 + minutes * 60 + second) / (12 * 60.0f * 60));

        canvas.save();
        canvas.rotate(rotate, cx, cy);
        canvas.drawLine(cx, cy - rect.height() / 6.0f, cx, cy + rect.height() / 10.0f, mPaint);
        canvas.restore();

        canvas.save();
        canvas.rotate(360 * (minutes * 60 + second) / (60 * 60.0f), cx, cy);
        canvas.drawLine(cx, cy - rect.height() / 4.0f, cx, cy + rect.height() / 10.0f, mPaint);
        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}

