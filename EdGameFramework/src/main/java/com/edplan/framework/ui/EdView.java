package com.edplan.framework.ui;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.main.MainCallBack;
import com.edplan.framework.math.RectF;
import com.edplan.framework.ui.additions.FrameListener;
import com.edplan.framework.ui.animation.AbstractAnimation;
import com.edplan.framework.ui.animation.AnimationHandler;
import com.edplan.framework.ui.animation.callback.OnFinishListener;
import com.edplan.framework.ui.animation.callback.OnProgressListener;
import com.edplan.framework.ui.drawable.ColorDrawable;
import com.edplan.framework.ui.drawable.EdDrawable;
import com.edplan.framework.ui.drawable.RoundedRectDrawable;
import com.edplan.framework.ui.inputs.EdMotionEvent;
import com.edplan.framework.ui.inputs.HoverEvent;
import com.edplan.framework.ui.inputs.ScrollEvent;
import com.edplan.framework.ui.layout.EdLayoutParam;
import com.edplan.framework.ui.layout.EdMeasureSpec;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.layout.LayoutException;
import com.edplan.framework.ui.layout.MarginLayoutParam;
import com.edplan.framework.utils.BitUtil;
import com.edplan.framework.timing.IRunnableHandler;

import java.lang.ref.WeakReference;


public class EdView implements IRunnableHandler, MainCallBack, FrameListener {
    protected static int CUSTOM_INDEX = 0;

    public static final int FLAG_INVALIDATE_MEASURE = Invalidate.FLAG_INVALIDATE_MEASURE;

    public static final int FLAG_INVALIDATE_LAYOUT = Invalidate.FLAG_INVALIDATE_LAYOUT;

    public static final int FLAG_INVALIDATE_DRAW = Invalidate.FLAG_INVALIDATE_DRAW;

    public static final int VISIBILITY_SHOW = 1;

    public static final int VISIBILITY_HIDDEN = 2;

    public static final int VISIBILITY_GONE = 3;

    //没有处理事件，事件下发
    public static final int EVENT_FLAG_PASS = 0;

    //处理了事件，但是继续下发，返回值变成true
    public static final int EVENT_FLAG_CHECKING = 1;

    //处理了事件，不继续下发，返回值变成true
    public static final int EVENT_FLAG_HANDLED = 2;

    //处理了事件，处理中产生了拦截
    public static final int EVENT_FLAG_CANCELED = 3;

    private WeakReference<EdAbstractViewGroup> parent;

    private String name;

    private MContext context;

    protected EdDrawable background;

    private int visiblility = VISIBILITY_SHOW;

    private EdLayoutParam layoutParam;

    private float measuredWidth, measuredHeight;

    private float minWidth, minHeight;

    private float leftToParent, rightToParent, topToParent, bottomToParent;

    private boolean hasCreated = false;

    private boolean pressed = false;

    private boolean focus = false;

    /**
     * 设置是否处理点击事件（事件开始结束均在view范围内，且事件时间不是太久)
     */
    private boolean clickable = false;

    /**
     * 设置是否处理长摁事件（事件开始结束均在view范围内，且事件时间比较久)
     */
    private boolean longclickable = false;

    /**
     * 是否接受滚动，包含了两个方向相关的信息。
     * 具体的取值在ScrollEvent.java
     */
    private int scrollableFlag;

    private int gravity = Gravity.Center;

    /**
     * 用于在完成layout后的动态位置变换
     */
    private float offsetX, offsetY;

    private AbstractAnimation animation;

    private boolean hasInitialLayouted = false;

    private boolean outsideTouchable = false;

    private int initialId;

    private boolean debug = false;

    private boolean masking = false;

    private boolean onClickWhenDown = false;

    public EdView(MContext context) {
        this.context = context;
        if (!checkCurrentThread()) {
            throw new RuntimeException("you can only create a view in main thread!");
        }
        initialName();
    }

    public void setOnClickWhenDown(boolean onClickWhenDown) {
        this.onClickWhenDown = onClickWhenDown;
    }

    public boolean isOnClickWhenDown() {
        return onClickWhenDown;
    }

    public void setMasking(boolean masking) {
        this.masking = masking;
    }

    public boolean isMasking() {
        return masking;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isDebug() {
        return debug;
    }

    @Override
    public void onFrameStart() {

    }

    public void setOutsideTouchable(boolean outsideTouchable) {
        this.outsideTouchable = outsideTouchable;
    }

    public boolean isOutsideTouchable() {
        return outsideTouchable;
    }

    public void setAnimation(AbstractAnimation animation) {
        this.animation = animation;
    }

    public AbstractAnimation getAnimation() {
        return animation;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
        invalidateDraw();
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
        invalidateDraw();
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void invalidate(int flag) {
        if (getParent() != null) {
            getParent().invalidate(flag);
        }
    }

    public void invalidate() {
        invalidate(Invalidate.FLAG_INVALIDATE_LAYOUT | Invalidate.FLAG_INVALIDATE_MEASURE);
    }

    public void invalidateDraw() {
        if (getParent() != null) {
            getParent().invalidateDraw();
        }
    }

    public float getMarginHorizonIfHas() {
        if (getLayoutParam() instanceof MarginLayoutParam) {
            return ((MarginLayoutParam) getLayoutParam()).getMarginHorizon();
        } else {
            return 0;
        }
    }

    public float getMarginVerticalIfHas() {
        if (getLayoutParam() instanceof MarginLayoutParam) {
            return ((MarginLayoutParam) getLayoutParam()).getMarginVertical();
        } else {
            return 0;
        }
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public void gravity(int gravity) {
        setGravity(gravity);
    }

    public int getGravity() {
        return gravity;
    }

    public boolean isStrictInvalidateLayout() {
        return true;
    }

    /**
     * 如果当前view在滚动，停止滚动
     */
    public void stopScrolling() {

    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public boolean isPressed() {
        return pressed;
    }

    public ViewRoot getViewRoot() {
        return getContext().getViewRoot();
    }

    protected void setScrollableFlag(int scrollableFlag) {
        this.scrollableFlag = scrollableFlag;
    }

    protected int getScrollableFlag() {
        return scrollableFlag;
    }

    public void setLongclickable(boolean longclickable) {
        this.longclickable = longclickable;
    }

    public boolean isLongclickable() {
        return longclickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public boolean isClickable() {
        return clickable;
    }

    public boolean hasCreated() {
        return hasCreated;
    }

    public float getTop() {
        return topToParent + offsetY;
    }

    public float getBottom() {
        return bottomToParent + offsetY;
    }

    public float getLeft() {
        return leftToParent + offsetX;
    }

    public float getRight() {
        return rightToParent + offsetX;
    }

    public float getWidth() {
        return getRight() - getLeft();
    }

    public float getHeight() {
        return getBottom() - getTop();
    }

    public float getPaddingLeft() {
        return 0;
    }

    public float getPaddingTop() {
        return 0;
    }

    public float getPaddingRight() {
        return 0;
    }

    public float getPaddingBottom() {
        return 0;
    }

    public float getPaddingHorizon() {
        return getPaddingLeft() + getPaddingRight();
    }

    public float getPaddingVertical() {
        return getPaddingTop() + getPaddingBottom();
    }

    public float getMeasuredWidth() {
        return measuredWidth;
    }

    public float getMeasuredHeight() {
        return measuredHeight;
    }

    public void setLayoutParam(EdLayoutParam layoutParam) {
        this.layoutParam = layoutParam;
    }

    public void layoutParam(EdLayoutParam layoutParam) {
        setLayoutParam(layoutParam);
    }

    public EdLayoutParam getLayoutParam() {
        return layoutParam;
    }

    public final boolean checkCurrentThread() {
        return Thread.currentThread() == getContext().getMainThread();
    }

    public void setParent(EdAbstractViewGroup parent) {
        this.parent = new WeakReference<EdAbstractViewGroup>(parent);
    }

    public EdAbstractViewGroup getParent() {
        return parent != null ? parent.get() : null;
    }

    public void setBackground(Color4 c) {
        ColorDrawable cd = new ColorDrawable(getContext());
        cd.setColor(c);
        setBackground(cd);
    }

    public void setBackgroundRoundedRect(Color4 c, float radius) {
        RoundedRectDrawable rd = new RoundedRectDrawable(getContext());
        rd.setColor(c);
        rd.setRadius(radius);
        setBackground(rd);
    }

    public void setBackground(EdDrawable background) {
        this.background = background;
    }

    public EdDrawable getBackground() {
        return background;
    }

    @Override
    public void post(Runnable r) {
        getContext().runOnUIThread(r);
    }

    @Override
    public void post(Runnable r, double delayMS) {
        getContext().runOnUIThread(r, delayMS);
    }

    public void setContext(MContext context) {
        this.context = context;
    }

    public MContext getContext() {
        return context;
    }

    private void initialName() {
        name = "@index/" + CUSTOM_INDEX;
        initialId = CUSTOM_INDEX;
        CUSTOM_INDEX++;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setVisiblility(int visiblility) {
        this.visiblility = visiblility;
        invalidate();
        invalidateDraw();
    }

    public int getVisiblility() {
        return visiblility;
    }

    public void onCreate() {
        hasCreated = true;
    }

    public void onRemoveAnimation(AbstractAnimation anim) {

    }

    public void performAnimation(double deltaTime) {
        if (animation != null) {
            if (AnimationHandler.handleSingleAnima(animation, deltaTime)) {
                onRemoveAnimation(animation);
                animation = null;
            }
        }
    }

    /**
     * 绘制操作，不可变的逻辑
     * @param canvas
     */
    public final void draw(BaseCanvas canvas) {
        onDrawBackgroundLayer(canvas);
        if (masking) {
            canvas.unprepare();
            BaseCanvas clip = canvas.requestClipCanvas(0, 0, Math.round(getWidth()), Math.round(getHeight()));
            if (clip == null) {
                throw new RuntimeException(String.format("%s do not support clipCanvas! masking is not supported!",canvas.getClass().getSimpleName()));
            }
            clip.prepare();
            onDraw(clip);
            clip.unprepare();
            canvas.prepare();
        } else {
            onDraw(canvas);
        }
        onDrawOverlayLayer(canvas);
    }

    /**
     * 在绘制开始前被调用，masking此时没有起效，这里的background和setBackground设置的背景无关
     * （词穷了不会起名x谅解一下 (qwq)）
     * @param canvas 画板
     */
    protected void onDrawBackgroundLayer(BaseCanvas canvas) {

    }

    /**
     * 在绘制开始后被调用，masking此时没有起效
     * @param canvas 画板
     */
    protected void onDrawOverlayLayer(BaseCanvas canvas) {

    }

    protected void onDraw(BaseCanvas canvas) {
        defaultDraw(canvas);
    }

    protected void defaultDraw(BaseCanvas canvas) {
        drawBackground(canvas);
    }

    protected void drawBackground(BaseCanvas canvas) {
        if (background != null) {
            int s = canvas.save();
            background.draw(canvas);
            canvas.restoreToCount(s);
        }
    }


    private boolean hasSetMeasureDimension = false;

    public final void measure(long widthSpec, long heightSpec) {
        hasSetMeasureDimension = false;
        onMeasure(widthSpec, heightSpec);
        if (!hasSetMeasureDimension) {
            throw new LayoutException("you must call setMeasureDimension in onMeasure Method, pleas check class : " + getClass().toString());
        }
    }

    protected void onMeasure(long widthSpec, long heightSpec) {
        setMeasuredDimensition(
                getDefaultSize(getSuggestedMinWidth(), widthSpec),
                getDefaultSize(getSuggestedMinHeight(), heightSpec));
    }

    public float getSuggestedMinWidth() {
        return (background == null) ? minWidth : Math.max(minWidth, background.getMinWidth());
    }

    public float getSuggestedMinHeight() {
        return (background == null) ? minHeight : Math.max(minHeight, background.getMinHeight());
    }

    public static float makeupMeasureSize(float size, long spec) {
        final int specMode = EdMeasureSpec.getMode(spec);
        switch (specMode) {
            case EdMeasureSpec.MODE_AT_MOST:
                return Math.min(size, EdMeasureSpec.getSize(spec));
            case EdMeasureSpec.MODE_DEFINEDED:
                return EdMeasureSpec.getSize(spec);
            case EdMeasureSpec.MODE_NONE:
            default:
                return size;
        }
    }

    public static float getDefaultSize(float size, long spec) {
        float r = size;
        int mode = EdMeasureSpec.getMode(spec);
        switch (mode) {
            case EdMeasureSpec.MODE_NONE:
                r = size;
                break;
            case EdMeasureSpec.MODE_DEFINEDED:
            case EdMeasureSpec.MODE_AT_MOST:
                r = EdMeasureSpec.getSize(spec);
                break;
        }
        return r;
    }

    protected final void setMeasuredDimensition(float w, float h) {
        measuredWidth = w;
        measuredHeight = h;
        hasSetMeasureDimension = true;
        //Log.v("setMeasureDimension","view:"+getName()+" "+w+","+h);
    }

    /**
     * 四个参数分别为子view到父view坐标系的距离
     */
    public void layout(float left, float top, float right, float bottom) {
        boolean hasChanged = setFrame(left, top, right, bottom);
        if (hasChanged) {
            onLayout(true, left, top, right, bottom);
        } else {
            if (isStrictInvalidateLayout()) {
                onLayout(false, left, top, right, bottom);
            }
        }
    }

    protected void onLayout(boolean changed, float left, float top, float right, float bottom) {

    }

    protected final boolean setFrame(float left, float top, float right, float bottom) {
        boolean hasChanged = false;
        if (leftToParent != left || topToParent != top || rightToParent != right || bottomToParent != bottom) {
            hasChanged = true;
            this.leftToParent = left;
            this.topToParent = top;
            this.rightToParent = right;
            this.bottomToParent = bottom;
            if (!hasInitialLayouted) {
                hasInitialLayouted = true;
                onInitialLayouted();
            }
        }
        if (hasChanged) invalidateDraw();
        return hasChanged;
    }

    public void onInitialLayouted() {

    }


    private ClickChecker clickChecker;

    protected int handleClick(EdMotionEvent event) {
        if (clickable) {
            if (clickChecker == null) clickChecker = new ClickChecker();
            if (!clickChecker.checkPointer(event)) {
                return EVENT_FLAG_PASS;
            }
            switch (event.getEventType()) {
                case Down:
                    clickChecker.down(event);
                    return EVENT_FLAG_HANDLED;
                case Move:
                    clickChecker.move(event);
                    return EVENT_FLAG_HANDLED;
                case Up:
                    clickChecker.up(event);
                    return EVENT_FLAG_HANDLED;
                case Cancel:
                    clickChecker.cancel();
                    return EVENT_FLAG_CANCELED;
            }
            return EVENT_FLAG_PASS;
        } else {
            return EVENT_FLAG_PASS;
        }
    }

    private ScrollChecker scrollChecker;

    protected int handleScroll(EdMotionEvent e) {
        if (scrollableFlag != 0) {
            if (scrollChecker == null) scrollChecker = new ScrollChecker();
            switch (scrollChecker.handleEvent(e)) {
                case ScrollChecker.STATE_PASS:
                    return EVENT_FLAG_PASS;
                case ScrollChecker.STATE_CHECKING:
                    return EVENT_FLAG_CHECKING;
                case ScrollChecker.STATE_INTERCEPT:
                    e.setEventType(EdMotionEvent.EventType.Cancel);
                    return EVENT_FLAG_CANCELED;
                case ScrollChecker.STATE_HANDLING:
                    return EVENT_FLAG_HANDLED;
                case ScrollChecker.STATE_CANCELED:
                default:
                    return EVENT_FLAG_CANCELED;
            }
        } else {
            return EVENT_FLAG_PASS;
        }
    }

    /**
     * 处理原始点击事件，重写后可能使对clickable,longclickable和scrollFlag的设置失效
     */
    public boolean onMotionEvent(EdMotionEvent e) {
        boolean result = false;
        switch (handleScroll(e)) {
            case EVENT_FLAG_HANDLED:
                return true;
            case EVENT_FLAG_CHECKING:
                result = true;
                break;
            case EVENT_FLAG_CANCELED:
                result = true;
                break;
            case EVENT_FLAG_PASS:
            default:
                break;
        }
        switch (handleClick(e)) {
            case EVENT_FLAG_HANDLED:
                return true;
            case EVENT_FLAG_CHECKING:
                result = true;
                break;
            case EVENT_FLAG_CANCELED:
                result = true;
                break;
            case EVENT_FLAG_PASS:
            default:
                break;
        }
        return result;
    }

    public boolean onOutsideTouch(EdMotionEvent e) {
        return false;
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public boolean onBackPressed() {

        return false;
    }

    /**
     * 处理悬浮事件
     */
    public boolean onHoverEvent(HoverEvent event) {
        return false;
    }


    /**
     *下面这些都必须获取焦点后再被应用
     */

    /**
     * 点击事件开始时被调用，只有被设置为clickable=true才会被调用
     */
    public void onStartClick() {

    }

    /**
     * 对应点击事件被触发
     */
    public void onClickEvent() {

    }

    /**
     * 对应长摁事件被触发
     */
    public void onLongClickEvent() {

    }

    public void onClickEventCancel() {

    }

    /**
     * 处理滚动事件
     */
    public boolean onScroll(ScrollEvent event) {
        return false;
    }

    public boolean inViewBound(float x, float y) {
        return RectF.inLTRB(x, y, getLeft(), getTop(), getRight(), getBottom());
    }

    public class ScrollChecker {
        public static final int STATE_PASS = 0;
        public static final int STATE_CHECKING = 1;
        public static final int STATE_INTERCEPT = 3;
        public static final int STATE_HANDLING = 4;
        public static final int STATE_CANCELED = 5;

        private int holdingPointer = -1;
        private float preX, preY;
        private double preTime;
        private float startX, startY;
        private boolean isScrolling = false;

        public void clearPointerData() {
            holdingPointer = -1;
            preX = 0;
            preY = 0;
            startX = 0;
            startY = 0;
            isScrolling = false;
        }

        public boolean checkPointer(EdMotionEvent event) {
            if (holdingPointer == -1) {
                return event.getEventType() == EdMotionEvent.EventType.Down;
            } else {
                return event.getPointerId() == holdingPointer;
            }
        }

        public int handleEvent(EdMotionEvent event) {
            if (!checkPointer(event)) {
                return STATE_PASS;
            }
            switch (event.getEventType()) {
                case Down:
                    holdingPointer = event.getPointerId();
                    startX = event.getX();
                    startY = event.getY();
                    preX = event.getX();
                    preY = event.getY();
                    preTime = event.getTime();
                    return STATE_CHECKING;
                case Move:
                    if (isScrolling) {
                        ScrollEvent se = new ScrollEvent();
                        se.setTime(event.getTime());
                        se.setDeltaTime(event.getTime() - preTime);
                        se.setState(ScrollEvent.STATE_SCROLLING);
                        if (BitUtil.match(scrollableFlag, ScrollEvent.DIRECTION_HORIZON)) {
                            se.addScrollFlag(ScrollEvent.DIRECTION_HORIZON);
                            se.setScrollX(event.getX() - preX);
                        }
                        if (BitUtil.match(scrollableFlag, ScrollEvent.DIRECTION_VERTICAL)) {
                            se.addScrollFlag(ScrollEvent.DIRECTION_VERTICAL);
                            se.setScrollY(event.getY() - preY);
                        }
                        preX = event.getX();
                        preY = event.getY();
                        preTime = event.getTime();
                        onScroll(se);
                        return STATE_HANDLING;
                    } else {
                        boolean startScroll = false;
                        ScrollEvent se = null;
                        if (BitUtil.match(scrollableFlag, ScrollEvent.DIRECTION_HORIZON)) {
                            if (Math.abs(event.getX() - startX) > ViewConfiguration.START_SCROLL_OFFSET) {
                                startScroll = true;
                                se = new ScrollEvent();
                                se.setDeltaTime(event.getTime() - preTime);
                                se.setTime(event.getTime());
                                se.setState(ScrollEvent.STATE_START);
                                se.addScrollFlag(ScrollEvent.DIRECTION_HORIZON);
                                se.setScrollX(event.getX() - startX);
                            }
                        }
                        if (BitUtil.match(scrollableFlag, ScrollEvent.DIRECTION_VERTICAL)) {
                            if (Math.abs(event.getY() - startY) > ViewConfiguration.START_SCROLL_OFFSET) {
                                startScroll = true;
                                if (se == null) {
                                    se = new ScrollEvent();
                                    se.setTime(event.getTime());
                                    se.setDeltaTime(event.getTime() - preTime);
                                    se.setState(ScrollEvent.STATE_START);
                                }
                                se.addScrollFlag(ScrollEvent.DIRECTION_VERTICAL);
                                se.setScrollY(event.getY() - startY);
                            }
                        }
                        preX = event.getX();
                        preY = event.getY();
                        preTime = event.getTime();
                        if (startScroll) {
                            onScroll(se);
                            isScrolling = true;
                            return STATE_INTERCEPT;
                        } else {
                            return STATE_CHECKING;
                        }
                    }
                case Up:
                    if (isScrolling) {
                        ScrollEvent se = new ScrollEvent();
                        se.setTime(event.getTime());
                        se.setDeltaTime(event.getTime() - preTime);
                        se.setState(ScrollEvent.STATE_END);
                        if (BitUtil.match(scrollableFlag, ScrollEvent.DIRECTION_HORIZON)) {
                            se.addScrollFlag(ScrollEvent.DIRECTION_HORIZON);
                            se.setScrollX(event.getX() - preX);
                        }
                        if (BitUtil.match(scrollableFlag, ScrollEvent.DIRECTION_VERTICAL)) {
                            se.addScrollFlag(ScrollEvent.DIRECTION_VERTICAL);
                            se.setScrollY(event.getY() - preY);
                        }
                        onScroll(se);
                        clearPointerData();
                        return STATE_HANDLING;
                    } else {
                        clearPointerData();
                        return STATE_PASS;
                    }
                case Cancel:
                default:
                    ScrollEvent se = new ScrollEvent();
                    se.setTime(event.getTime());
                    se.setDeltaTime(event.getTime() - preTime);
                    se.setState(ScrollEvent.STATE_CANCEL);
                    if (BitUtil.match(scrollableFlag, ScrollEvent.DIRECTION_HORIZON)) {
                        se.addScrollFlag(ScrollEvent.DIRECTION_HORIZON);
                        se.setScrollX(event.getX() - preX);
                    }
                    if (BitUtil.match(scrollableFlag, ScrollEvent.DIRECTION_VERTICAL)) {
                        se.addScrollFlag(ScrollEvent.DIRECTION_VERTICAL);
                        se.setScrollY(event.getY() - preY);
                    }
                    onScroll(se);
                    clearPointerData();
                    //getContext().toast("cancel by parent");
                    return STATE_CANCELED;
            }
        }
    }

    public class ClickChecker {
        private int clickPointer = -1;
        private float downX, downY, currentX, currentY;
        private double downTime;

        public void clearPointerData() {
            clickPointer = -1;
            downX = 0;
            downY = 0;
            currentX = 0;
            currentY = 0;
            downTime = 0;
        }

        public boolean checkPointer(EdMotionEvent event) {
            if (clickPointer == -1) {
                return event.getEventType() == EdMotionEvent.EventType.Down;
            } else {
                return event.getPointerId() == clickPointer;
            }
        }

        public void down(EdMotionEvent event) {
            setPressed(true);
            onStartClick();
            clickPointer = event.getPointerId();
            downX = event.getX();
            downY = event.getY();
            currentX = event.getX();
            currentY = event.getY();
            downTime = event.getTime();
            if (longclickable) {
                post(() -> {
                    if (isPressed()) {
                        setPressed(false);
                        clearPointerData();
                        onLongClickEvent();
                    }
                }, ViewConfiguration.LONGCLICK_DELAY_MS);
            }
            if (onClickWhenDown) {
                onClickEvent();
            }
        }

        public void move(EdMotionEvent event) {
            currentX = event.getX();
            currentY = event.getY();
            if (!inViewBound(currentX, currentY)) {
                clearPointerData();
                setPressed(false);
                onClickEventCancel();
                //getContext().toast(event.getX()+":"+event.getY()+" "+getLeft()+":"+getTop()+":"+getRight()+":"+getBottom());
            }
        }

        public void up(EdMotionEvent event) {
            clearPointerData();
            setPressed(false);
            if (!onClickWhenDown) {
                onClickEvent();
            }
        }

        public void cancel() {
            clearPointerData();
            setPressed(false);
            onClickEventCancel();
        }
    }

    public static abstract class BoundOverlay {
        public abstract float getLeft();

        public abstract float getTop();

        public abstract float getBottom();

        public abstract float getRight();

        public float getWidth() {
            return getRight() - getLeft();
        }

        public float getHeight() {
            return getBottom() - getTop();
        }
    }

    public static class BaseBoundOverlay extends BoundOverlay {
        float left, top, right, bottom;

        public void setLeft(float left) {
            this.left = left;
        }

        public float getLeft() {
            return left;
        }

        public void setTop(float top) {
            this.top = top;
        }

        public float getTop() {
            return top;
        }

        public void setRight(float right) {
            this.right = right;
        }

        public float getRight() {
            return right;
        }

        public void setBottom(float bottom) {
            this.bottom = bottom;
        }

        public float getBottom() {
            return bottom;
        }
    }

    public interface OnClickListener {
        public void onClick(EdView view);
    }

    public interface OnScrollListener {
        public void onScroll(ScrollEvent event, EdView view);
    }

    public interface OnCheckListener {
        public void onCheck(boolean c, EdView view);
    }

    public OnFinishListener setVisibilityWhenFinish(final int code) {
        return () -> setVisiblility(code);
    }

    public OnProgressListener invalideDrawDuringAnimation() {
        return p->invalidateDraw();
    }
}













