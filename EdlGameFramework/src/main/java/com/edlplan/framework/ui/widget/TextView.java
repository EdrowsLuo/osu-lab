package com.edlplan.framework.ui.widget;

import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.ui.EdView;
import com.edlplan.framework.ui.ViewConfiguration;
import com.edlplan.framework.ui.layout.EdMeasureSpec;
import com.edlplan.framework.ui.layout.Gravity;
import com.edlplan.framework.ui.text.font.bmfont.BMFont;
import com.edlplan.framework.ui.text.font.drawing.TextPrinter;
import com.edlplan.framework.ui.text.font.drawing.TextUtils;

public class TextView extends EdView {

    private BMFont font;

    private String text = "";

    protected float textSize = ViewConfiguration.dp(15);

    private float scale, ascale;

    private int textHeight;

    private int textWidth;

    private String[] breaked;

    private float alpha = 1;

    private Color4 textColor = Color4.White.copyNew();

    private TextPrinter printer;

    private float paddingLeft, paddingRight, paddingTop, paddingBottom;

    public TextView(MContext c) {
        super(c);
        font = BMFont.getDefaultFont();
        //setDebug(true);
    }

    public void setTextColor(Color4 textColor) {
        this.textColor.set(textColor);
        invalidateDraw();
    }

    public void setPaddingLeft(float paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    @Override
    public float getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingRight(float paddingRight) {
        this.paddingRight = paddingRight;
    }

    @Override
    public float getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingTop(float paddingTop) {
        this.paddingTop = paddingTop;
    }

    @Override
    public float getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingBottom(float paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    @Override
    public float getPaddingBottom() {
        return paddingBottom;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        invalidate(FLAG_INVALIDATE_MEASURE | FLAG_INVALIDATE_LAYOUT);
    }

    public float getTextSize() {
        return textSize;
    }

    public void setFont(BMFont font) {
        this.font = font;
    }

    public void setFont(String face) {
        BMFont f = BMFont.getFont(face);
        if (f != null) {
            font = f;
        }
    }

    public BMFont getFont() {
        return font;
    }

    public void setText(String text) {
        if (text == null) text = "null";
        if (!this.text.equals(text)) {
            this.text = text;
            invalidateDraw();
            invalidate(FLAG_INVALIDATE_LAYOUT | FLAG_INVALIDATE_MEASURE);
        }
    }

    public String getText() {
        return text;
    }

    protected void rebuildTextBuffer(float maxWidth, float maxHeight) {
        final int lineHeight = font.getCommon().lineHeight;
        ascale = lineHeight / textSize;
        scale = textSize / lineHeight;
        int fw = (int) (maxWidth * ascale - 1);
        int hw = (int) (maxHeight * ascale - 1);
        breaked = TextUtils.breakText(font, text, fw);
        textWidth = 0;
        for (String b : breaked) {
            textWidth = Math.max(textWidth, TextUtils.calwidth(font, b));// Math.max(textWidth,b.width);
        }
        textHeight = breaked.length * lineHeight;
    }

    int prew, prel;
    String pres;

    @Override
    protected void onLayout(boolean changed, float left, float top, float right, float bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (breaked == null) {
            //when no text set, direct return
            return;
        }
        final int width = (int) Math.max(0, right - left - getPaddingHorizon());
        final int pleft = (int) getPaddingLeft();

        if (prel == pleft && text.equals(pres) && prew == prew) return;
        prel = pleft;
        prew = width;
        pres = text;

        printer = new TextPrinter(font, 0, 0, alpha, textColor);
        printer.setTextSize(textSize);
        printer.addOffset(pleft, getPaddingTop());
        printer.addOffsetRaw(0, font.getCommon().base);

        final float lineHight = textSize;

        final int gravity = getGravity();
        if (gravity == Gravity.Center) {
            float h = ((bottom - top) / 2 - getPaddingTop()) - breaked.length * lineHight / 2;
            printer.setCurrentBaseY(h);
            printer.addOffsetRaw(0, font.getCommon().base);
            float cx = width / 2f + pleft;
            for (int i = 0; i < breaked.length; i++) {
                final String s = breaked[i];
                printer.printLineCenter(s, cx);
                printer.addOffset(0, lineHight);
            }
            return;
        }
        switch (gravity & Gravity.MASK_HORIZON) {
            case Gravity.CENTER_HORIZON: {
                float cx = width / 2f + pleft;
                for (int i = 0; i < breaked.length; i++) {
                    final String s = breaked[i];
                    printer.printLineCenter(s, cx);
                    printer.addOffset(0, lineHight);
                }
            }
            break;
            case Gravity.RIGHT: {
                float cx = right - left - getPaddingRight();
                for (int i = 0; i < breaked.length; i++) {
                    final String s = breaked[i];
                    printer.printLineRight(s, cx);
                    printer.addOffset(0, lineHight);
                }
            }
            break;
            case Gravity.LEFT:
            default: {
                float cx = getPaddingLeft();
                for (int i = 0; i < breaked.length; i++) {
                    final String s = breaked[i];
                    printer.printLineLeft(s, cx);
                    printer.addOffset(0, lineHight);
                }

            }
            break;
        }
    }

    @Override
    protected void onMeasure(long widthSpec, long heightSpec) {
        float mwidth = 0, mheight = 0;
        switch (EdMeasureSpec.getMode(widthSpec)) {
            case EdMeasureSpec.MODE_NONE:
                mwidth = 1000000000;
                break;
            case EdMeasureSpec.MODE_AT_MOST:
                mwidth = EdMeasureSpec.getSize(widthSpec) - getPaddingHorizon();
                break;
            case EdMeasureSpec.MODE_DEFINEDED:
            default:
                mwidth = EdMeasureSpec.getSize(widthSpec) - getPaddingHorizon();
                break;
        }

        switch (EdMeasureSpec.getMode(heightSpec)) {
            case EdMeasureSpec.MODE_NONE:
                mheight = 1000000000;
                break;
            case EdMeasureSpec.MODE_AT_MOST:
                mheight = EdMeasureSpec.getSize(heightSpec) - getPaddingVertical();
                break;
            case EdMeasureSpec.MODE_DEFINEDED:
            default:
                mheight = EdMeasureSpec.getSize(heightSpec) - getPaddingVertical();
                break;
        }
        rebuildTextBuffer(mwidth, mheight);
        switch (EdMeasureSpec.getMode(widthSpec)) {
            case EdMeasureSpec.MODE_NONE:
                mwidth = textWidth * scale;
                break;
            case EdMeasureSpec.MODE_AT_MOST:
                mwidth = Math.min(textWidth * scale, EdMeasureSpec.getSize(widthSpec));
                break;
            case EdMeasureSpec.MODE_DEFINEDED:
            default:
                mwidth = EdMeasureSpec.getSize(widthSpec);
                break;
        }

        switch (EdMeasureSpec.getMode(heightSpec)) {
            case EdMeasureSpec.MODE_NONE:
                mheight = textHeight * scale;
                break;
            case EdMeasureSpec.MODE_AT_MOST:
                mheight = Math.min(textHeight * scale, EdMeasureSpec.getSize(heightSpec) - getPaddingVertical());
                break;
            case EdMeasureSpec.MODE_DEFINEDED:
            default:
                mheight = EdMeasureSpec.getSize(heightSpec) - getPaddingVertical();
                break;
        }
        mheight = Math.max(textSize, mheight);
        setMeasuredDimensition(mwidth, mheight);
    }

    @Override
    protected void onDraw(BaseCanvas canvas) {
        super.onDraw(canvas);
        if (printer != null) {
            printer.setAccentColor(textColor);
            printer.setAlpha(alpha);
            printer.draw(canvas);
        }
    }
}
