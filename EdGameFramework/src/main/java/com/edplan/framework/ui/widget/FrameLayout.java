package com.edplan.framework.ui.widget;

import com.edplan.framework.MContext;
import com.edplan.framework.ui.EdView;

public class FrameLayout extends AbsoluteLayout {

    private Fragment fragment;

    public FrameLayout(MContext con) {
        super(con);
    }

    @Override
    public EdView getChildAt(int i) {
        if (i > 0) {
            return null;
        }
        return fragment != null ? fragment.getContentView() : null;
    }

    @Override
    public int getChildrenCount() {
        return fragment != null ? fragment.getContentView() != null ? 1 : 0 : 0;
    }

    public void setFragment(Fragment fragment) {
        if (this.fragment != fragment) {
            removeFragment();
            this.fragment = fragment;
            if (!fragment.isCreated()) {
                fragment.create(getContext());
                fragment.getContentView().onCreate();
            }
            if (!fragment.isDestroyOnHide()) {
                fragment.onResume();
            }
            fragment.getContentView().setParent(this);
        }
    }

    public void removeFragment() {
        if (fragment != null) {
            fragment.onHide();
            if (fragment.isDestroyOnHide()) {
                fragment.onDestroy();
            }
            fragment.getContentView().setParent(null);
        }
    }
}
