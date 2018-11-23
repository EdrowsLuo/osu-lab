package com.edplan.framework.ui;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.interfaces.Invoker;
import com.edplan.framework.ui.animation.interfaces.IHasAlpha;
import com.edplan.framework.ui.inputs.EdMotionEvent;
import com.edplan.framework.ui.layout.EdLayoutParam;
import com.edplan.framework.ui.layout.MeasureCore;

import java.util.Arrays;
import java.util.Iterator;

public abstract class EdAbstractViewGroup extends EdView implements IHasAlpha{
    private ChildrenWrapper childrenWrapper = new ChildrenWrapper();

    private LayoutTransition transition;

    private boolean backwardsDraw = false;

    public EdAbstractViewGroup(MContext context) {
        super(context);
    }

    private float paddingLeft, paddingTop, paddingRight, paddingBottom;

    private float alpha = 1;

    public void setBackwardsDraw(boolean backwardsDraw) {
        this.backwardsDraw = backwardsDraw;
    }

    public boolean isBackwardsDraw() {
        return backwardsDraw;
    }

    public void setPaddingLeft(float paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public ChildrenWrapper getChildrenWrapper() {
        return childrenWrapper;
    }

    public void setChildrenWrapper(ChildrenWrapper w) {
        childrenWrapper = w;
        invalidate();
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    @Override
    public void onFrameStart() {
        super.onFrameStart();
        final int count = getChildrenCount();
        for (int i = 0; i < count; i++) {
            final EdView view = getChildAt(i);
            if (view.getVisiblility() != VISIBILITY_GONE) {
                view.onFrameStart();
            }
        }
    }

    @Override
    public float getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingTop(float paddingTop) {
        this.paddingTop = paddingTop;
    }

    @Override
    public float getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingRight(float paddingRight) {
        this.paddingRight = paddingRight;
    }

    @Override
    public float getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingBottom(float paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    @Override
    public float getPaddingBottom() {
        return paddingBottom;
    }

    public void setPadding(float p) {
        paddingTop = p;
        paddingRight = p;
        paddingBottom = p;
        paddingLeft = p;
    }

    public EdLayoutParam getDefaultParam(EdView view) {

        final EdLayoutParam param = view.getLayoutParam();
        if (param != null) {
            return param;
        } else {
            return new EdLayoutParam();
        }
    }

    @Override
    public void onCreate() {

        final int count = getChildrenCount();
        for (int i = 0; i < count; i++) {
            final EdView view = getChildAt(i);
            if (!view.hasCreated()) view.onCreate();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        doForAvailableChildren(EdView::onPause);
    }

    @Override
    public void onResume() {
        super.onResume();
        doForAvailableChildren(EdView::onResume);
    }

    @Override
    public boolean onBackPressed() {
        final int c = getChildrenCount();
        for (int i = c - 1; i >= 0; i--) {
            final EdView view = getChildAt(i);
            if (view.getVisiblility() != VISIBILITY_GONE) {
                if (view.onBackPressed()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        doForAvailableChildren(EdView::onLowMemory);
    }

    public void doForAvailableChildren(Invoker<EdView> invoker) {
        final int c = getChildrenCount();
        for (int i = 0; i < c; i++) {
            final EdView view = getChildAt(i);
            if (view.getVisiblility() != VISIBILITY_GONE) {
                invoker.invoke(view);
            }
        }
    }

    @Override
    protected abstract void onLayout(boolean changed, float left, float top, float right, float bottom);

    @Override
    protected abstract void onMeasure(long widthSpec, long heightSpec);

    public EdView getChildAt(int i) {
        return childrenWrapper.children[i];
    }

    public int getChildrenCount() {
        return childrenWrapper.idx;
    }

    @Override
    public void performAnimation(double deltaTime) {
        super.performAnimation(deltaTime);
        final int count = getChildrenCount();
        for (int i = 0; i < count; i++) {
            final EdView view = getChildAt(i);
            if (view.getVisiblility() != VISIBILITY_GONE) {
                view.performAnimation(deltaTime);
            }
        }
    }

    @Override
    public final void layout(float left, float top, float right, float bottom) {
        if (transition == null || !transition.isChangingLayout()) {
            if (transition != null) {
                transition.layoutChange(this);
                super.layout(left, top, right, bottom);
            } else {
                super.layout(left, top, right, bottom);
            }
        }
    }

    protected void dispatchDraw(BaseCanvas canvas) {
        final int count = getChildrenCount();
        if (backwardsDraw) {
            for (int i = count - 1; i >= 0; i--) {
                final EdView view = getChildAt(i);
                if (view.getVisiblility() == VISIBILITY_SHOW) {
                    final int savedcount = canvas.save();
                    try {
                        canvas.translate(view.getLeft(), view.getTop());
                        canvas.clip(view.getWidth(), view.getHeight());
                        view.draw(canvas);
                    } finally {
                        canvas.restoreToCount(savedcount);
                    }
                }
            }
        } else {
            for (int i = 0; i < count; i++) {
                final EdView view = getChildAt(i);
                if (view.getVisiblility() == VISIBILITY_SHOW) {
                    final int savedcount = canvas.save();
                    try {
                        canvas.translate(view.getLeft(), view.getTop());
                        canvas.clip(view.getWidth(), view.getHeight());
                        view.draw(canvas);
                    } finally {
                        canvas.restoreToCount(savedcount);
                    }
                }
            }
        }

    }

    @Override
    protected void onDraw(BaseCanvas canvas) {
        canvas.save();
        canvas.setCanvasAlpha(canvas.getCanvasAlpha() * getAlpha());
        drawBackground(canvas);
        dispatchDraw(canvas);
        canvas.restore();
    }
	
	/*
	private int holdingPointer=-1;
	private EdView viewUsingPointer;
	
	protected void clearPointerInfo(){
		holdingPointer=-1;
		viewUsingPointer=null;
	}
	*/

    private PointerHolder holder;

    protected boolean dispatchMotionEvent(EdMotionEvent event) {
        if (holder == null) holder = new PointerHolder();
        if (holder.ifIgnore(event)) return false;

        boolean useevent = false;
		/*
		//暂时只支持单点触控
		if(holdingPointer!=-1&&holdingPointer!=event.getPointerId()){
			return false;
		}
		*/
        final float x = event.getX(), y = event.getY();
        final EdView viewUsingPointer = holder.getHoldingView(event);
        if (viewUsingPointer != null) {
            if (viewUsingPointer.getParent() != this || viewUsingPointer.getVisiblility() == VISIBILITY_GONE) {
                //已经不在这个ViewGroup中了
                holder.clearPointerInfo(event);
                return false;
            }
        }
        event.transform(-getLeft(), -getTop());
        switch (event.getEventType()) {
            case Down:
                final int count = getChildrenCount();
                for (int i = count - 1; i >= 0; i--) {
                    final EdView view = getChildAt(i);
                    if (view.getVisiblility() == VISIBILITY_GONE) continue;
                    if (view.inViewBound(event.getX(), event.getY())) {
                        if (view.onMotionEvent(event)) {
                            useevent = true;
                            holder.setHoldingView(event, view, false);
                            break;
                        }
                    } else {
                        if (view.isOutsideTouchable()) {
                            if (view.onOutsideTouch(event)) {
                                useevent = true;
                                holder.setHoldingView(event, view, true);
                                break;
                            }
                        }
                    }
                }
                break;
            case Move:
                useevent = viewUsingPointer.onMotionEvent(event);
                if (!useevent) holder.clearPointerInfo(event);
                break;
            case Up:
                useevent = viewUsingPointer.onMotionEvent(event);
                holder.clearPointerInfo(event);
                break;
            case Cancel:
                useevent = viewUsingPointer.onMotionEvent(event);
                holder.clearPointerInfo(event);
                break;
        }
        event.setEventPosition(x, y);
        return useevent;
    }

    @Override
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

        if (dispatchMotionEvent(e)) return true;

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

    protected void layoutChildren(float left, float top, float right, float bottom) {
        final int count = getChildrenCount();

        final float parentLeft = getPaddingLeft();
        final float parentTop = getPaddingTop();

        for (int i = 0; i < count; i++) {
            final EdView view = getChildAt(i);
            if (view.getVisiblility() != VISIBILITY_GONE) {
                final EdLayoutParam param = view.getLayoutParam();
                final float dx = param.xoffset + parentLeft;
                final float dy = param.yoffset + parentTop;
                view.layout(dx, dy, view.getMeasuredWidth() + dx, view.getMeasuredHeight() + dy);
            }
        }
    }

    protected float getDefaultMaxChildrenMeasuredWidthWithMargin() {
        final int count = getChildrenCount();
        float m = 0;
        for (int i = 0; i < count; i++) {
            final EdView v = getChildAt(i);
            m = Math.max(v.getMeasuredWidth() + v.getMarginHorizonIfHas(), m);
        }
        return m;
    }

    protected float getDefaultMaxChildrenMeasuredWidth() {
        final int count = getChildrenCount();
        float m = 0;
        for (int i = 0; i < count; i++) {
            final EdView v = getChildAt(i);
            m = Math.max(v.getMeasuredWidth(), m);
        }
        return m;
    }


    protected float getDefaultMaxChildrenMeasuredHeightWithMargin() {
        final int count = getChildrenCount();
        float m = 0;
        for (int i = 0; i < count; i++) {
            final EdView v = getChildAt(i);
            m = Math.max(v.getMeasuredHeight() + v.getMarginVerticalIfHas(), m);
        }
        return m;
    }

    protected float getDefaultMaxChildrenMeasuredHeight() {
        final int count = getChildrenCount();
        float m = 0;
        for (int i = 0; i < count; i++) {
            final EdView v = getChildAt(i);
            m = Math.max(v.getMeasuredHeight(), m);
        }
        return m;
    }

    protected float getDefaultMaxChildrenMeasuredWidthWithOffset() {
        final int count = getChildrenCount();
        float m = 0;
        for (int i = 0; i < count; i++) {
            final EdView v = getChildAt(i);
            m = Math.max(v.getMeasuredWidth() + v.getLayoutParam().xoffset, m);
        }
        return m;
    }

    protected float getDefaultMaxChildrenMeasuredHeightWithOffset() {
        final int count = getChildrenCount();
        float m = 0;
        for (int i = 0; i < count; i++) {
            final EdView v = getChildAt(i);
            m = Math.max(v.getMeasuredHeight() + v.getLayoutParam().yoffset, m);
        }
        return m;
    }

    protected void measureChildren(long widthSpec, long heightSpec) {
        final int count = getChildrenCount();
        for (int i = 0; i < count; i++) {
            final EdView v = getChildAt(i);
            if (v.getVisiblility() != VISIBILITY_GONE) {
                measureChild(v, widthSpec, heightSpec);
            }
        }
    }

    protected void measureChild(EdView view, long wspec, long hspec) {
        MeasureCore.measureChild(
                view,
                getPaddingHorizon(),
                getPaddingVertical(),
                wspec,
                hspec);
    }

    protected void measureChildWithMargin(EdView view, long wspec, long hspec, float wused, float hused) {
        MeasureCore.measureChildWithMargin(
                view,
                getPaddingHorizon(),
                getPaddingVertical(),
                wspec,
                hspec,
                wused,
                hused);
    }

    protected void measureChildWithMarginIgnorePadding(EdView view, long wspec, long hspec, float wused, float hused) {
        MeasureCore.measureChildWithMargin(
                view,
                0,
                0,
                wspec,
                hspec,
                wused,
                hused);
    }

    protected void addToList(EdView v) {
        v.setParent(this);
        if (childrenWrapper.idx >= childrenWrapper.children.length) {
            childrenWrapper.children = Arrays.copyOf(childrenWrapper.children, childrenWrapper.children.length * 3 / 2 + 1);
        }
        childrenWrapper.children[childrenWrapper.idx] = v;
        childrenWrapper.idx++;
        if (hasCreated()) {
            if (!v.hasCreated()) v.onCreate();
        }
    }

    public EdLayoutParam adjustParam(EdView view, EdLayoutParam param) {
        return param;
    }

    public void addView(EdView view, EdLayoutParam param) {
        view.setLayoutParam(adjustParam(view, param));
        addView(view);
    }

    public void addView(EdView view) {
        if (view.getLayoutParam() == null) {
            view.setLayoutParam(getDefaultParam(view));
        }
        addToList(view);
    }

    public void addAll(EdView... views) {
        for (EdView view : views) {
            addView(view);
        }
    }

    public class PointerHolder {
        public EdView[] holdingView = new EdView[EdMotionEvent.MAX_POINTER];
        public boolean[] isOutside = new boolean[EdMotionEvent.MAX_POINTER];
        //public boolean[] handlePointer=new boolean[EdMotionEvent.MAX_POINTER];

        public PointerHolder() {
            //Arrays.fill(handlePointer,false);
        }

        public void setHoldingView(EdMotionEvent e, EdView view, boolean isout) {
            holdingView[e.getPointerId()] = view;
            isOutside[e.getPointerId()] = isout;
        }

        public boolean ifIgnore(EdMotionEvent e) {
            if (e.getEventType() != EdMotionEvent.EventType.Down) {
                if (holdingView[e.getPointerId()] == null) return true;
                return false;
            } else {
                return false;
            }
        }

        public EdView getHoldingView(EdMotionEvent e) {
            return holdingView[e.getPointerId()];
        }

        public boolean isOutside(EdMotionEvent e) {
            return isOutside[e.getPointerId()];
        }

        public void clearPointerInfo(EdMotionEvent e) {
            holdingView[e.getPointerId()] = null;
        }
    }

    public static class ChildrenWrapper implements Iterable<EdView> {
        public EdView[] children = new EdView[1];
        public int idx;


        public static ChildrenWrapper combine(ChildrenWrapper w1, ChildrenWrapper w2) {
            ChildrenWrapper w = new ChildrenWrapper();
            w.idx = w1.idx + w2.idx;
            w.children = new EdView[w.idx];

            return w;
        }

        @Override
        public Iterator<EdView> iterator() {

            return new Iterator<EdView>() {
                int i = 0;

                @Override
                public boolean hasNext() {

                    return i < idx;
                }

                @Override
                public EdView next() {

                    return children[i++];
                }

                @Override
                public void remove() {

                }
            };
        }
    }
}
