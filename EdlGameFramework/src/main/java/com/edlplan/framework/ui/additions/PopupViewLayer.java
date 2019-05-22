package com.edlplan.framework.ui.additions;

import com.edlplan.framework.ui.additions.popupview.PopupView;
import com.edlplan.framework.MContext;
import com.edlplan.framework.ui.EdView;
import com.edlplan.framework.ui.layout.Param;
import com.edlplan.framework.ui.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Iterator;

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
