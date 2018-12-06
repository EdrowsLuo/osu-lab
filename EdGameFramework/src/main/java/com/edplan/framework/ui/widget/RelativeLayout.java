package com.edplan.framework.ui.widget;

import com.edplan.framework.MContext;
import com.edplan.framework.ui.EdAbstractViewGroup;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.layout.EdLayoutParam;
import com.edplan.framework.ui.layout.EdMeasureSpec;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.layout.MarginLayoutParam;

public class RelativeLayout extends EdAbstractViewGroup {
    public RelativeLayout(MContext c) {
        super(c);
    }

    @Override
    public EdLayoutParam getDefaultParam(EdView view) {
        return new RelativeParam();
    }

    @Override
    public EdLayoutParam adjustParam(EdView view, EdLayoutParam param) {
        if (param instanceof RelativeParam) {
            return param;
        } else {
            return new RelativeParam(param);
        }
    }

    @Override
    public EdLayoutParam createLayoutParam() {
        return new RelativeParam();
    }

    @Override
    protected void onLayout(boolean changed, float left, float top, float right, float bottom) {

        final int count = getChildrenCount();

        final float parentLeft = getPaddingLeft();
        final float parentTop = getPaddingTop();
        final float parentBottom = bottom - top - getPaddingBottom();
        final float parentRight = right - left - getPaddingRight();

        final float parentCenterHorizon = (parentLeft + parentRight) / 2;
        final float parentCenterVertical = (parentTop + parentBottom) / 2;

        for (int i = 0; i < count; i++) {
            final EdView view = getChildAt(i);
            if (view.getVisiblility() != VISIBILITY_GONE) {
                final RelativeParam param = (RelativeParam) view.getLayoutParam();
                final float ox;
                final float oy;
                switch (param.gravity & Gravity.MASK_HORIZON) {

                    case Gravity.CENTER_HORIZON:
                        ox = param.xoffset + parentCenterHorizon - view.getMeasuredWidth() / 2;
                        break;
                    case Gravity.RIGHT:
                        ox = param.xoffset + parentRight - view.getMeasuredWidth() - param.marginRight;
                        break;
                    case Gravity.LEFT:
                    default:
                        ox = param.xoffset + parentLeft + param.marginLeft;
                        break;
                }
                switch (param.gravity & Gravity.MASK_VERTICAL) {
                    case Gravity.CENTER_VERTICAL:
                        oy = param.yoffset + parentCenterVertical - view.getMeasuredHeight() / 2;
                        break;
                    case Gravity.BOTTOM:
                        oy = param.yoffset + parentBottom - view.getMeasuredHeight() - param.marginBottom;
                        break;
                    case Gravity.TOP:
                    default:
                        oy = param.yoffset + parentTop + param.marginTop;
                        break;
                }
                view.layout(ox, oy, ox + view.getMeasuredWidth(), oy + view.getMeasuredHeight());
            }
        }
    }

    @Override
    protected void onMeasure(long widthSpec, long heightSpec) {

        final int count = getChildrenCount();
        for (int i = 0; i < count; i++) {
            final EdView view = getChildAt(i);
            if (view.getVisiblility() != VISIBILITY_GONE) {
                measureChildWithMargin(view, widthSpec, heightSpec, 0, 0);
            }
        }
        final float xd;
        final float yd;
        {
            final int mode = EdMeasureSpec.getMode(widthSpec);
            switch (mode) {
                case EdMeasureSpec.MODE_DEFINEDED:
                    xd = EdMeasureSpec.getSize(widthSpec);
                    break;
                case EdMeasureSpec.MODE_AT_MOST:
                    xd = Math.min(EdMeasureSpec.getSize(widthSpec), getPaddingHorizon() + getDefaultMaxChildrenMeasuredWidthWithMargin());
                    break;
                case EdMeasureSpec.MODE_NONE:
                default:
                    xd = getPaddingHorizon() + getDefaultMaxChildrenMeasuredWidthWithMargin();
                    break;
            }
        }
        {
            final int mode = EdMeasureSpec.getMode(heightSpec);
            switch (mode) {
                case EdMeasureSpec.MODE_DEFINEDED:
                    yd = EdMeasureSpec.getSize(heightSpec);
                    break;
                case EdMeasureSpec.MODE_AT_MOST:
                    yd = Math.min(EdMeasureSpec.getSize(heightSpec), getPaddingVertical() + getDefaultMaxChildrenMeasuredHeightWithMargin());
                    break;
                case EdMeasureSpec.MODE_NONE:
                default:
                    yd = getPaddingVertical() + getDefaultMaxChildrenMeasuredHeightWithMargin();
                    break;
            }
        }
        setMeasuredDimensition(xd, yd);
    }

    public static class RelativeParam extends MarginLayoutParam {
        public int gravity;

        public RelativeParam(EdLayoutParam p) {
            super(p);
            gravity = Gravity.TopLeft;
        }

        public RelativeParam() {
            gravity = Gravity.TopLeft;
        }
    }
}
