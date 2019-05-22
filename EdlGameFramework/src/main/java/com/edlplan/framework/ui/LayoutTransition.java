package com.edlplan.framework.ui;

public interface LayoutTransition {
    public boolean isChangingLayout();

    public void layoutChange(EdAbstractViewGroup view);
}
