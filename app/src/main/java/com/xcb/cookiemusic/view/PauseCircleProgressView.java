package com.xcb.cookiemusic.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import androidx.annotation.Nullable;

import com.xcb.cookiemusic.R;
import com.xcb.cookiemusic.utils.CountDownUtils;
import com.xcb.cookiemusic.utils.ImageUtils;

import java.math.BigDecimal;

/**
 * @author xcb
 * date：2019-12-26 13:40
 * description:带进度条的暂停按钮
 */
public class PauseCircleProgressView extends View {
    /**
     * 默认控件大小
     */
    private static final int DEFAULT_SIZE = 120;
    /**
     * 画圆弧启示位置
     */
    private static final int DRAW_ARC_START_ANGEL = 270;
    //默认的颜色
    private int defaultColor;
    private int progressColor;
    private int radius;
    private int width, height, min;
    private int ringWidth;
    private Paint mPaint;
    private Paint arcPaint;//圆弧
    private Bitmap mBitmapStop;
    private Bitmap mBitmapStart;
    private Rect rect;
    private Rect rect1;
    private boolean isStart = false;
    private int currentProgress;
    private int maxProgress;
    //画圆弧RectF
    private RectF rectF;
    //抗锯齿
    private PaintFlagsDrawFilter pfd;

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
        pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        mBitmapStop = ImageUtils.drawableToBitmap(getResources().getDrawable(R.drawable.ic_pause));
        mBitmapStart = ImageUtils.drawableToBitmap(getResources().getDrawable(R.drawable.ic_start));
        rect = new Rect(0, 0, mBitmapStop.getWidth(), mBitmapStop.getHeight());
        mPaint = new Paint();
        mPaint.setColor(defaultColor);
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setStyle(Paint.Style.STROKE);//画笔属性是空心圆
        mPaint.setStrokeWidth(ringWidth);//设置画笔粗细

        arcPaint = new Paint();
        arcPaint.setColor(progressColor);
        arcPaint.setAntiAlias(true);//抗锯齿
        arcPaint.setStyle(Paint.Style.STROKE);//画笔属性是空心圆
        arcPaint.setStrokeWidth(ringWidth);//设置画笔粗细

        rectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMySize(widthMeasureSpec);
        height = getMySize(heightMeasureSpec);
        min = Math.min(width, height);
        //保证控件为正方形
        setMeasuredDimension(min, min);

        rectF.left = ringWidth;
        rectF.top = ringWidth;
        rectF.bottom = min - ringWidth;
        rectF.right = min - ringWidth;
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
        canvas.setDrawFilter(pfd);
        drawCircle(canvas);
        if (isStart) {
            drawTriangle(canvas);
        } else {
            drawPauseLine(canvas);
        }
        drawArc(canvas);
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
        Rect rect1 = new Rect(min / 4, min / 4, min / 4 * 3, min / 4 * 3);
        canvas.drawBitmap(mBitmapStop, rect, rect1, mPaint);
    }

    /**
     * 画暂停竖线
     */
    private void drawPauseLine(Canvas canvas) {
        Rect rect1 = new Rect(min / 4, min / 5, min / 4 * 3, min / 5 * 4);
        canvas.drawBitmap(mBitmapStart, rect, rect1, mPaint);
    }

    /**
     * 画圆弧
     */
    private void drawArc(Canvas canvas) {
        //比例
        float scale = (float) new BigDecimal((float) currentProgress / maxProgress).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        canvas.drawArc(rectF, DRAW_ARC_START_ANGEL, scale * 360, false, arcPaint);
    }

    /**
     * 开始
     */
    public void pause() {
        isStart = false;
        invalidate();
    }

    /**
     * 暂停
     */
    public void start() {
        isStart = true;
        invalidate();
    }

    /**
     * 设置进度(0-100)
     */
    public void setProgress(int progress) {
        this.currentProgress = progress;
        invalidate();
    }

    /**
     * 设置最大进度
     */
    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }
}
