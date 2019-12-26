package com.xcb.cookiemusic.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.xcb.cookiemusic.R;

/**
 * @author xcb
 * date：2019-12-26 13:40
 * description:带进度条的暂停按钮
 */
public class PauseCircleProgressView extends View {
    private static final int DEFAULT_SIZE = 120;
    //默认的颜色
    private int defaultColor;
    private int progressColor;
    private int radius;
    private int width, height, min;
    private int ringWidth;
    private Paint mPaint;

    public PauseCircleProgressView(Context context) {
        super(context);
        initTypedArray(context, null);
    }

    public PauseCircleProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initTypedArray(context, attrs);
    }

    public PauseCircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypedArray(context, attrs);
    }

    private void initTypedArray(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PauseCircleProgressView);
        defaultColor = typedArray.getColor(R.styleable.PauseCircleProgressView_defaultColor, context.getResources().getColor(R.color.color_313131));
        progressColor = typedArray.getColor(R.styleable.PauseCircleProgressView_progressColor, context.getResources().getColor(R.color.red));
        radius = typedArray.getDimensionPixelOffset(R.styleable.PauseCircleProgressView_radius, 0);
        ringWidth = typedArray.getDimensionPixelOffset(R.styleable.PauseCircleProgressView_ringWidth, 6);
        typedArray.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(defaultColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);//画笔属性是空心圆
        mPaint.setStrokeWidth(ringWidth);//设置画笔粗细
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMySize(widthMeasureSpec);
        height = getMySize(heightMeasureSpec);
        min = Math.min(width, height);
        //保证控件为正方形
        setMeasuredDimension(min, min);
    }

    /**
     * 获取测量大小
     */
    private int getMySize(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;//确切大小,所以将得到的尺寸给view
        } else if (specMode == MeasureSpec.AT_MOST) {
            //默认值为120dp,此处要结合父控件给子控件的最多大小(要不然会填充父控件),所以采用最小值
            result = Math.min(DEFAULT_SIZE, specSize);
        } else {
            result = DEFAULT_SIZE;
        }
        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
        // drawTriangle(canvas);
        drawLine(canvas);
    }

    /**
     * 画圆
     */
    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(min / 2, min / 2, min / 2 - ringWidth, mPaint);
    }

    /**
     * 画三角形
     */
    private void drawTriangle(Canvas canvas) {
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_san)).getBitmap();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Rect rect1 = new Rect(min / 4, min / 4, min / 4 * 3, min / 4 * 3);
        canvas.drawBitmap(bitmap, rect, rect1, mPaint);
    }

    /**
     * 画暂停竖线
     */
    private void drawLine(Canvas canvas) {
        canvas.drawLine(min / 8 * 3, min / 4, min / 8 * 3, min / 4 * 3, mPaint);
        canvas.drawLine(min / 8 * 5, min / 4, min / 8 * 5, min / 4 * 3, mPaint);
    }
}
