package com.xcb.commonlibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

/**
 * @author xcb
 * date：2019-12-25 18:20
 * description:
 */
public class CustomDrawerLayout extends DrawerLayout {
    private boolean is_click = false;

    public CustomDrawerLayout(@NonNull Context context) {
        super(context);
    }

    public CustomDrawerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomDrawerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("TEDrawerLayout", " dispatchTouchEvent   " + MotionEventType.getEnventTypeMsg(ev));
        boolean isDispatch = super.dispatchTouchEvent(ev);
        Log.e("TEDrawerLayout", "  dispatchTouchEvent   " + MotionEventType.getEnventTypeMsg(ev) + "   " + isDispatch);
        return isDispatch;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("TEDrawerLayout ", " onTouchEvent   " + MotionEventType.getEnventTypeMsg(event));
        boolean isTouch = super.onTouchEvent(event);
        Log.e("TEDrawerLayout", "  onTouchEvent   " + MotionEventType.getEnventTypeMsg(event) + "   " + isTouch);
        return isTouch;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("TEDrawerLayout", "  onInterceptTouchEvent   " + MotionEventType.getEnventTypeMsg(ev));
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = ev.getX();
                float y = ev.getY();
                View touchesView = findTopChildUnder((int) x, (int) y);
                if (touchesView != null && isContentView(touchesView) && this.isDrawerOpen(GravityCompat.START)) {
                    Log.e("TEDrawerLayout", "  onInterceptTouchEvent   " + MotionEventType.getEnventTypeMsg(ev) + "     手动判定不拦截");
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                return false;
            default:
                break;
        }
        boolean isIntercept = super.onInterceptTouchEvent(ev);
        Log.e("TEDrawerLayout", "  onInterceptTouchEvent   " + MotionEventType.getEnventTypeMsg(ev) + "   " + isIntercept);
        return isIntercept;
    }

    /**
     * @param x 触摸点X坐标
     * @param y 触摸点Y坐标
     * @return 返回触摸点所在的子View区域
     */
    public ViewGroup findTopChildUnder(int x, int y) {
        final int childCount = this.getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            ViewGroup child = (ViewGroup) this.getChildAt(i);
            if (x >= child.getLeft() && x < child.getRight() && y >= child.getTop() && y < child.getBottom()) {
                return child;
            }
        }
        return null;
    }


    /**
     * @param childView 子View
     * @return 该子View是否是主界面
     */
    public boolean isContentView(View childView) {
        return ((LayoutParams) childView.getLayoutParams()).gravity == Gravity.NO_GRAVITY;
    }
}
