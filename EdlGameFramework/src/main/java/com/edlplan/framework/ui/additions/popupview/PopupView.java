package com.edlplan.framework.ui.additions.popupview;

import com.edlplan.framework.MContext;
import com.edlplan.framework.ui.additions.PopupViewLayer;
import com.edlplan.framework.ui.widget.RelativeLayout;
import com.edlplan.framework.ui.widget.component.Hideable;

public class PopupView extends RelativeLayout implements Hideable {
    private boolean hideWhenBackpress = true;

    private PopupViewLayer layer;

    public PopupView(MContext c) {
        super(c);
        layer = c.getViewRoot().getPopupViewLayer();
    }

    public void setPopupViewLayer(PopupViewLayer layer) {
        this.layer = layer;
    }

    public void setHideWhenBackpress(boolean hideWhenBackpress) {
        this.hideWhenBackpress = hideWhenBackpress;
    }

    public boolean isHideWhenBackpress() {
        return hideWhenBackpress;
    }

    @Override
    public boolean onBackPressed() {
        if (isHideWhenBackpress()) {
            hide();
            return true;
        }
        return false;
    }

    protected void onHide() {
        setVisiblility(VISIBILITY_GONE);
    }

    protected void onShow() {

    }

    @Override
    public final void hide() {
        onHide();
    }

    @Override
    public final void show() {
        getContext().getViewRoot().getPopupViewLayer().register(this);
        setVisiblility(VISIBILITY_SHOW);
        invalidateDraw();
        onShow();
    }

    @Override
    public boolean isHidden() {
        return getVisiblility() == VISIBILITY_GONE || getParent() == null;
    }
}
