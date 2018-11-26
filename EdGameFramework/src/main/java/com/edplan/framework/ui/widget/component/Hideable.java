package com.edplan.framework.ui.widget.component;

public interface Hideable {
    void hide();

    void show();

    boolean isHidden();

    default void changeState() {
        if (isHidden()) {
            show();
        } else {
            hide();
        }
    }
}
