package com.edplan.framework.ui.widget;

import com.edplan.framework.ui.layout.EdMeasureSpec;
import com.edplan.framework.ui.EdContainer;
import com.edplan.framework.MContext;

import java.util.HashMap;
import java.util.TreeMap;

import com.edplan.framework.ui.EdView;

import android.util.ArrayMap;

import com.edplan.framework.utils.BadMap;

public class FrameContainer extends EdContainer {
    private int currentId;
    private BadMap<Node, ViewPage> viewPages = new BadMap<Node, ViewPage>();

    private ViewPage currentPage;

    public FrameContainer(MContext c) {
        super(c);
    }

    public void setCurrentPage(ViewPage currentPage) {
        this.currentPage = currentPage;
    }

    public ViewPage getCurrentPage() {
        return currentPage;
    }

    public void swapToNaxtPage() {
        if (currentPage == null) {
            if (viewPages.size() > 0) {
                currentPage = viewPages.getValueByIndex(0);
                currentPage.onSwapShow();
            }
        } else {
            int idx = (viewPages.getValueIndex(currentPage) + 1) % viewPages.size();
            swapPage(viewPages.getValueByIndex(idx));
        }
    }

    public void addPage(ViewPage page) {
        if (!viewPages.containsValue(page)) {
            page.setParent(this);
            viewPages.put(createNode(page), page);
        }
    }

    private Node createNode(ViewPage page) {
        return new Node(page.getName(), currentId++);
    }

    public boolean swapPage(ViewPage p) {
        if (p != null && currentPage != p && viewPages.containsValue(p)) {
            if (currentPage != null) {
                currentPage.onSwapHidden();
            }
            currentPage = p;
            currentPage.onSwapShow();
            invalidate();
            return true;
        } else {
            return false;
        }
    }

    public boolean swapPageByName(String name) {
        ViewPage p = viewPages.get(name);
        return swapPage(p);
    }

    public boolean swapPageByIdx(int idx) {
        ViewPage p = viewPages.get(idx);
        return swapPage(p);
    }


    @Override
    public int getChildrenCount() {

        return currentPage != null ? 1 : 0;
    }

    @Override
    public EdView getChildAt(int i) {

        return currentPage != null ? currentPage : null;
    }

    @Override
    protected void onLayout(boolean changed, float left, float top, float right, float bottom) {

        layoutChildren(left, top, right, bottom);
    }

    @Override
    protected void onMeasure(long widthSpec, long heightSpec) {

        if (currentPage != null) {
            measureChild(currentPage, widthSpec, heightSpec);
        }
        final float xd;
        final float yd;
        {
            final int mode = EdMeasureSpec.getMode(widthSpec);
            switch (mode) {
                case EdMeasureSpec.MODE_DEFINEDED:
                    xd = EdMeasureSpec.getSize(widthSpec) + getPaddingHorizon();
                    break;
                case EdMeasureSpec.MODE_AT_MOST:
                    xd = getPaddingHorizon() + Math.min(EdMeasureSpec.getSize(widthSpec), getDefaultMaxChildrenMeasuredWidth());
                    break;
                case EdMeasureSpec.MODE_NONE:
                default:
                    xd = getPaddingHorizon() + getDefaultMaxChildrenMeasuredWidth();
                    break;
            }
        }
        {
            final int mode = EdMeasureSpec.getMode(heightSpec);
            switch (mode) {
                case EdMeasureSpec.MODE_DEFINEDED:
                    yd = getPaddingVertical() + EdMeasureSpec.getSize(heightSpec);
                    break;
                case EdMeasureSpec.MODE_AT_MOST:
                    yd = getPaddingVertical() + Math.min(EdMeasureSpec.getSize(heightSpec), getDefaultMaxChildrenMeasuredHeight());
                    break;
                case EdMeasureSpec.MODE_NONE:
                default:
                    yd = getPaddingVertical() + getDefaultMaxChildrenMeasuredHeight();
                    break;
            }
        }
        setMeasuredDimensition(xd, yd);
    }


    public class Node implements Comparable<Node> {
        private static final int IDX_MASK = (1 << 10) - 1;

        public final String name;
        public final int idx;

        public final int keyCode;

        public Node(String name, int idx) {
            this.name = name;
            this.idx = idx;
            keyCode = calKeyCode();
        }

        @Override
        public boolean equals(Object obj) {

            if (obj instanceof String) {
                return ((String) obj).equals(name);
            } else if (obj instanceof Integer) {
                return idx == (int) obj;
            } else {
                return obj == this;
            }
        }

        private int calKeyCode() {
            int code = 0;
            code |= (idx & IDX_MASK);
            return code;
        }

        public int getKeyCode() {
            return keyCode;
        }

        @Override
        public int compareTo(FrameContainer.Node p1) {

            return 0;
        }
    }
}
