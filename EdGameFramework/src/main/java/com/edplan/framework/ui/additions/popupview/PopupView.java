package com.edplan.framework.ui.additions.popupview;

import com.edplan.framework.ui.widget.RelativeContainer;
import com.edplan.framework.MContext;
import com.edplan.framework.ui.widget.RelativeLayout;
import com.edplan.framework.ui.widget.component.Hideable;
import com.edplan.framework.ui.additions.PopupViewLayer;

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
