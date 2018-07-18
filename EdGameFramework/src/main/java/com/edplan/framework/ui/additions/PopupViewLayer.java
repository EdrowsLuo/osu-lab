package com.edplan.framework.ui.additions;

import com.edplan.framework.ui.EdContainer;
import com.edplan.framework.MContext;
import com.edplan.framework.ui.widget.RelativeContainer;

import java.util.ArrayList;

import com.edplan.framework.ui.additions.popupview.PopupView;
import com.edplan.framework.ui.EdView;

import java.util.Iterator;

import com.edplan.framework.ui.widget.RelativeLayout;
import com.edplan.framework.ui.layout.Param;

public class PopupViewLayer extends RelativeLayout {
    private ArrayList<PopupView> popupViews = new ArrayList<PopupView>();

    public PopupViewLayer(MContext c) {
        super(c);
    }

    public void register(PopupView v) {
        popupViews.add(v);
        v.setParent(this);
        if (v.getLayoutParam() == null) {
            RelativeLayout.RelativeParam p = new RelativeLayout.RelativeParam();
            p.width = Param.MODE_MATCH_PARENT;
            p.height = Param.MODE_MATCH_PARENT;
            v.setLayoutParam(p);
        }
        invalidate();
        invalidateDraw();
    }

    @Override
    public int getChildrenCount() {

        return popupViews.size();
    }

    @Override
    public EdView getChildAt(int i) {

        return popupViews.get(i);
    }

    @Override
    public void onFrameStart() {
        Iterator<PopupView> iter = popupViews.iterator();
        while (iter.hasNext()) {
            final PopupView v = iter.next();
            if (v.getVisiblility() == VISIBILITY_GONE) {
                v.setParent(null);
                iter.remove();
                invalidateDraw();
            }
        }
        super.onFrameStart();
    }
}
