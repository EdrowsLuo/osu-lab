package com.edplan.framework.timing;

/**
 * 记载处理帧时间的类
 */
public abstract class FrameClock {

    /**
     * 缓存的帧时间
     * @return
     */
    public abstract double getFrameTime();

    /**
     * 当前的Clock是否是有效更新的
     * @return
     */
    public abstract boolean isRunninng();

    /**
     * 更新Clock的缓存时间
     */
    public abstract void update();

    /**
     * 让暂停的Clock运行
     */
    public abstract void run();

    /**
     * 暂停Clock，暂停之后调用update时不再更新时间
     */
    public abstract void pause();
}
