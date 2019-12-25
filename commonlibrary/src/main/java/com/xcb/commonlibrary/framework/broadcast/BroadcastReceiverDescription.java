package com.xcb.commonlibrary.framework.broadcast;

import com.xcb.commonlibrary.framework.MicroDescription;

/**
 * @author jinpingchen
 */
public class BroadcastReceiverDescription extends MicroDescription {

    //广播的action集合
    private String[] msgCode;

    public String[] getMsgCode() {
        return msgCode;
    }

    public BroadcastReceiverDescription setMsgCode(String[] msgCode) {
        this.msgCode = msgCode;
        return this;
    }
}
