package com.edlplan.framework.ui.additions;

import com.edlplan.framework.ui.layout.EdMeasureSpec;
import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.ui.EdBufferedContainer;
import com.edlplan.framework.ui.EdView;
import com.edlplan.framework.ui.layout.EdLayoutParam;
import com.edlplan.framework.ui.layout.Param;

public class RootContainer extends EdBufferedContainer {
    /**
     * 内容层
     */
    private EdView content;

    /**
     * 弹窗层
     */
    private PopupViewLayer popupViewLayer;

    public RootContainer(MContext c) {
        super(c);
        setAlwaysRefresh(true);
        setClearColor(Color4.Black);
        {
            EdLayoutParam p = new EdLayoutParam();
            p.width = Param.MODE_MATCH_PARENT;
            p.height = Param.MODE_MATCH_PARENT;
            setLayoutParam(p);
        }
        popupViewLayer = new PopupViewLayer(c);
        popupViewLayer.setParent(this);
        EdLayoutParam p = new EdLayoutParam();
        p.width = Param.MODE_MATCH_PARENT;
        p.height = Param.MODE_MATCH_PARENT;
        popupViewLayer.setLayoutParam(p);
    }

    @Override
    public void invalidateDraw() {
        super.invalidateDraw();
    }

    @Override
    public void invalidate(int flag) {

        getContent().getViewRoot().invalidate(flag);
    }

    public void setPopupViewLayer(PopupViewLayer popupViewLayer) {
        this.popupViewLayer = popupViewLayer;
    }

    public PopupViewLayer getPopupViewLayer() {
        return popupViewLayer;
    }

    public void setContent(EdView content) {
        this.content = content;
        content.setParent(this);
    }

    public EdView getContent() {
        return content;
    }

    /*@Override
    protected void onDraw(BaseCanvas canvas) {
        c:
        {
            final EdView view = content;
            if (view == null) break c;
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
        p:
        {
            final EdView view = popupViewLayer;
            if (view == null) break p;
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
    }*/

    @Override
    public int getChildrenCount() {
        return 2;
    }

    @Override
    public EdView getChildAt(int i) {

        switch (i) {
            case 0:
                return content;
            case 1:
                return popupViewLayer;
        }
        return null;
    }

    @Override
    protected void onLayout(boolean changed, float left, float top, float right, float bottom) {

        layoutChildren(left, top, right, bottom);
    }

    @Override
    protected void onMeasure(long widthSpec, long heightSpec) {

        measureChildren(widthSpec, heightSpec);
        setMeasuredDimensition(EdMeasureSpec.getSize(widthSpec), EdMeasureSpec.getSize(heightSpec));
    }
}
