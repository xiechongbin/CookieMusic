package com.xcb.commonlibrary.framework;

/**
 * @author jinpingchen
 */
public abstract class MicroDescription {

    /**
     * app id
     */
    private String mAppId;

    /**
     * 服务名字
     */
    private String mName;
    /**
     * 服务实现类名
     */
    private String mClassName;

    public String getAppId() {
        return mAppId;
    }

    public MicroDescription setAppId(String mAppId) {
        this.mAppId = mAppId;
        return this;
    }

    public String getName() {
        return mName;
    }

    public MicroDescription setName(String name) {
        mName = name;
        return this;
    }

    public String getClassName() {
        return mClassName;
    }

    public MicroDescription setClassName(String className) {
        mClassName = className;
        return this;
    }

}
