package com.edplan.framework.ui.widget;

import com.edplan.framework.MContext;
import com.edplan.framework.ui.EdAbstractViewGroup;
import com.edplan.framework.ui.EdView;

public class Fragment {

    private EdView contentView;

    private boolean destroyOnHide = true;

    private boolean created = false;

    public void setContentView(EdView view) {
        this.contentView = view;
        if (contentView.hasCreated()) {
            this.contentView.onCreate();
        }
    }

    public EdView getContentView() {
        return contentView;
    }

    public boolean isDestroyOnHide() {
        return destroyOnHide;
    }

    public void setDestroyOnHide(boolean destroyOnHide) {
        this.destroyOnHide = destroyOnHide;
    }

    public boolean isCreated() {
        return created;
    }

    protected void create(MContext context) {
        if (created) return;
        onCreate(context);
        created = true;
    }

    protected void onCreate(MContext context) {

    }

    protected void onHide() {

    }

    protected void onResume() {

    }

    protected void onDestroy() {

    }

}
