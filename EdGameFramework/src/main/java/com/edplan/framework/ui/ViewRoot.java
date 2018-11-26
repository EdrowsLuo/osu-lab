package com.edplan.framework.ui;

import android.view.MotionEvent;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.main.MainCallBack;
import com.edplan.framework.test.performance.Tracker;
import com.edplan.framework.ui.additions.PopupViewLayer;
import com.edplan.framework.ui.additions.RootContainer;
import com.edplan.framework.ui.inputs.EdMotionEvent;
import com.edplan.framework.ui.inputs.NativeInputQuery;
import com.edplan.framework.ui.layout.EdLayoutParam;
import com.edplan.framework.ui.layout.EdMeasureSpec;
import com.edplan.framework.ui.layout.MeasureCore;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.utils.BitUtil;
import com.edplan.framework.utils.StringUtil;
import com.edplan.framework.utils.reflect.ReflectHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewRoot implements MainCallBack {
    public static final int FLAG_INVALIDATE_MEASURE = Invalidate.FLAG_INVALIDATE_MEASURE;

    public static final int FLAG_INVALIDATE_LAYOUT = Invalidate.FLAG_INVALIDATE_LAYOUT;

    public static final int FLAG_INVALIDATE_DRAW = Invalidate.FLAG_INVALIDATE_DRAW;

    private MContext context;

    private EdView contentView;

    private RootContainer rootContainer;

    private int invalidateFlag;

    private int frameInvalidateState;

    private boolean alwaysInvalidateMeasure = false;

    private boolean alwaysInvalidateLayout = false;

    private boolean alwaysInvalidateDraw = true;

    private NativeInputQuery nativeInputQuery;

    private InputManager inputManager;

    private Focus focus = new Focus();

    public ViewRoot(MContext c) {
        this.context = c;
        nativeInputQuery = new NativeInputQuery(c);
        inputManager = new InputManager();
    }

    public RootContainer getRootContainer() {
        if (rootContainer == null) {
            rootContainer = new RootContainer(context);
        }
        return rootContainer;
    }

    public Focus getFocus() {
        return focus;
    }

    public PopupViewLayer getPopupViewLayer() {
        return getRootContainer().getPopupViewLayer();
    }

    public void postNativeEvent(MotionEvent event) {
        nativeInputQuery.postEvent(event);
    }

    protected void handlerInputs() {
        List<EdMotionEvent> events = nativeInputQuery.getQuery();
        inputManager.postEvents(events);
    }

    public void setContentView(EdView contentView) {
        this.contentView = contentView;
        if (contentView.getLayoutParam() == null) {
            EdLayoutParam param = new EdLayoutParam();
            param.width = Param.MODE_MATCH_PARENT;
            param.height = Param.MODE_MATCH_PARENT;
            contentView.setLayoutParam(param);
        }
        if (rootContainer == null) {
            rootContainer = new RootContainer(context);
        }
        rootContainer.setContent(contentView);
    }

    public void onCreate() {
        rootContainer.onCreate();
    }

    public void onChange(int w, int h) {
        invalidate(FLAG_INVALIDATE_DRAW | FLAG_INVALIDATE_MEASURE | FLAG_INVALIDATE_LAYOUT);
    }

    public void invalidate(int flag) {
        invalidateFlag |= flag;
    }

    protected void postMeasure() {
        long wm =
                EdMeasureSpec.makeupMeasureSpec(
                        context.getDisplayWidth(),
                        EdMeasureSpec.MODE_AT_MOST);
        long hm =
                EdMeasureSpec.makeupMeasureSpec(
                        context.getDisplayHeight(),
                        EdMeasureSpec.MODE_AT_MOST);
        MeasureCore.measureChild(rootContainer, 0, 0, wm, hm);
    }

    protected void postLayout() {
        rootContainer.layout(0, 0, rootContainer.getMeasuredWidth(), rootContainer.getMeasuredHeight());
    }

    protected void checkInvalidate() {
        Tracker.InvalidateMeasureAndLayout.watch();
        if (alwaysInvalidateMeasure
                || BitUtil.match(invalidateFlag, FLAG_INVALIDATE_MEASURE)
                ) {
            invalidateFlag &= ~FLAG_INVALIDATE_MEASURE;
            frameInvalidateState |= FLAG_INVALIDATE_MEASURE;
            postMeasure();
        }

        if (alwaysInvalidateLayout
                || BitUtil.match(invalidateFlag, FLAG_INVALIDATE_LAYOUT)
                ) {
            invalidateFlag &= ~FLAG_INVALIDATE_LAYOUT;
            frameInvalidateState |= FLAG_INVALIDATE_LAYOUT;
            postLayout();
        }
        Tracker.InvalidateMeasureAndLayout.end();
    }

    protected void handlerAnimation() {
        rootContainer.performAnimation(context.getFrameDeltaTime());
    }

    public void onNewFrame(BaseCanvas canvas, double deltaTime) {
        frameInvalidateState = 0;
        rootContainer.onFrameStart();
        checkInvalidate();
        handlerAnimation();
        handlerInputs();
        checkInvalidate();


        if (rootContainer != null) {
            Tracker.DrawUI.watch();
            rootContainer.draw(canvas);
            Tracker.DrawUI.end();
        }
    }

    @Override
    public void onPause() {

        if (rootContainer != null) rootContainer.onPause();
    }

    @Override
    public void onResume() {

        if (rootContainer != null) rootContainer.onResume();
    }

    @Override
    public void onLowMemory() {

        if (rootContainer != null) rootContainer.onLowMemory();
    }

    @Override
    public boolean onBackPressed() {

        if (rootContainer != null) {
            return rootContainer.onBackPressed();
        } else {
            return false;
        }
    }

    public String loadViewTreeStruct() {
        if (rootContainer == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        loadGroup(rootContainer, sb, 0);
        return sb.toString();
    }

    private void loadGroup(EdAbstractViewGroup c, StringBuilder sb, int step) {
        appendStep(sb, step);
        appendGroup(c, sb);
        sb.append(StringUtil.LINE_BREAK);
        final int count = c.getChildrenCount();
        for (int i = 0; i < count; i++) {
            final EdView v = c.getChildAt(i);
            if (v instanceof EdAbstractViewGroup) {
                loadGroup((EdAbstractViewGroup) v, sb, step + 1);
            } else {
                loadView(v, sb, step + 1);
            }
        }
    }

    private void loadView(EdView view, StringBuilder sb, int step) {
        appendStep(sb, step);
        sb.append("V::").append(ReflectHelper.getRealClass(view.getClass()).getSimpleName())
                .append(" ").append((int) view.getWidth()).append("x").append((int) view.getHeight()).append(" (").append((int) view.getTop()).append(",").append((int) view.getLeft()).append(")")
                .append(StringUtil.LINE_BREAK);
    }

    private void appendStep(StringBuilder sb, int step) {
        for (int i = 0; i < step; i++) {
            sb.append("|     ");
        }
    }

    private void appendGroup(EdAbstractViewGroup c, StringBuilder sb) {
        sb.append((c instanceof EdBufferedContainer) ? "C::" : "G::").append(ReflectHelper.getRealClass(c.getClass()).getSimpleName())
                .append(" ").append((int) c.getWidth()).append("x").append((int) c.getHeight())
                .append(" (").append((int) c.getLeft()).append(",").append((int) c.getTop()).append(")");
        if (c instanceof EdBufferedContainer) {
            sb.append(" fresh=").append(((EdBufferedContainer) c).isNeedRefresh());
        }
    }

    public static final Comparator<EdMotionEvent> COMP = new Comparator<EdMotionEvent>() {
        @Override
        public int compare(EdMotionEvent p1, EdMotionEvent p2) {

            return (int) Math.signum(p1.time - p2.time);
        }
    };

    public class InputManager {


        public int focusedPointer = -1;

        public InputManager() {

        }

        public void postSingleEvent(EdMotionEvent event) {
            if (rootContainer == null) return;
            //context.toast(event.toString());
            //System.out.println(event.toString());
            if (rootContainer.onMotionEvent(event)) {
                //事件被直接拦截，直接交给内容view处理
            }

            switch (event.eventType) {
                case Down: {
                    //寻找focus列表
                }
                break;
                case Move: {

                }
                break;
                case Up: {
                    focusedPointer = -1;
                }
                break;
                case Cancel: {
                    focusedPointer = -1;
                }
                break;
            }
        }

        public void postEvents(List<EdMotionEvent> events) {
            ArrayList<EdMotionEvent> copy = new ArrayList<EdMotionEvent>(events);
            Collections.sort(copy, COMP);
            for (EdMotionEvent e : copy) {
                postSingleEvent(e);
            }
        }

        public class PointerInfo {
            public int id;

        }
    }

    public class Focus {
        public List<EdView> focusedViews = new ArrayList<EdView>();

        public void addFocus(EdView view) {
            focusedViews.add(view);
        }
    }
}
