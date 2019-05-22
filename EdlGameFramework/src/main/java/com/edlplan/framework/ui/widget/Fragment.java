package com.edlplan.framework.ui.widget;

import com.edlplan.framework.MContext;
import com.edlplan.framework.ui.EdView;
import com.edlplan.framework.ui.layout.MarginLayoutParam;
import com.edlplan.framework.ui.layout.Param;

public class Fragment {

    private EdView contentView;

    private boolean destroyOnHide = true;

    private boolean created = false;

    public void setContentView(EdView view) {
        this.contentView = view;
        if (contentView.getLayoutParam() == null) {
            contentView.setLayoutParam(new MarginLayoutParam() {{
                width = height = Param.MODE_MATCH_PARENT;
            }});
        }
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
