package com.edplan.framework.ui.widget;

import com.edplan.framework.ui.EdAbstractViewGroup;
import com.edplan.framework.MContext;
import com.edplan.framework.ui.layout.EdLayoutParam;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.layout.EdMeasureSpec;

public class AbsoluteLayout extends EdAbstractViewGroup {
    public AbsoluteLayout(MContext con) {
        super(con);
    }

    @Override
    protected void onLayout(boolean changed, float left, float top, float right, float bottom) {

        layoutChildren(left, top, right, bottom);
    }

    @Override
    protected void onMeasure(long widthSpec, long heightSpec) {

        measureChildren(widthSpec, heightSpec);
        final float xd;
        final float yd;
        {
            final int mode = EdMeasureSpec.getMode(widthSpec);
            switch (mode) {
                case EdMeasureSpec.MODE_DEFINEDED:
                    xd = EdMeasureSpec.getSize(widthSpec) + getPaddingHorizon();
                    break;
                case EdMeasureSpec.MODE_AT_MOST:
                    xd = Math.min(EdMeasureSpec.getSize(widthSpec), getPaddingHorizon() + getDefaultMaxChildrenMeasuredWidth());
                    break;
                case EdMeasureSpec.MODE_NONE:
                default:
                    xd = getPaddingHorizon() + getDefaultMaxChildrenMeasuredWidth();
                    break;
            }
        }
        {
            final int mode = EdMeasureSpec.getMode(heightSpec);
            switch (mode) {
                case EdMeasureSpec.MODE_DEFINEDED:
                    yd = getPaddingVertical() + EdMeasureSpec.getSize(heightSpec);
                    break;
                case EdMeasureSpec.MODE_AT_MOST:
                    yd = Math.min(EdMeasureSpec.getSize(heightSpec), getPaddingVertical() + getDefaultMaxChildrenMeasuredHeight());
                    break;
                case EdMeasureSpec.MODE_NONE:
                default:
                    yd = getPaddingVertical() + getDefaultMaxChildrenMeasuredHeight();
                    break;
            }
        }
        setMeasuredDimensition(xd, yd);
    }
}
