package com.xcb.commonlibrary.view;

import android.view.MotionEvent;

/**
 * @author xcb
 * date：2019-12-25 19:30
 * description:
 */
public class MotionEventType {
    public static String getEnventTypeMsg(MotionEvent event) {
        String actionMsg = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                actionMsg = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_MOVE:
                actionMsg = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_CANCEL:
                actionMsg = "ACTION_CANCEL";
                break;
            case MotionEvent.ACTION_UP:
                actionMsg = "ACTION_UP";
                break;
            default:
                actionMsg = "";
                break;

        }
        return actionMsg;
    }
}
