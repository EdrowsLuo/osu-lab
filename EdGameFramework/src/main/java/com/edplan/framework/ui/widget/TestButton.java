package com.edplan.framework.ui.widget;

import com.edplan.framework.ui.EdView;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.math.RectF;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.graphics.opengl.GLPaint;
import com.edplan.framework.graphics.opengl.objs.Color4;

public class TestButton extends EdView {
    private OnClickListener onClickListener;

    public TestButton(MContext c) {
        super(c);
        setClickable(true);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    @Override
    public void onStartClick() {

        super.onStartClick();
        //getContext().toast("start click");
    }

    @Override
    public void onLongClickEvent() {

        super.onLongClickEvent();
        //getContext().toast("onLongClick");
    }

    @Override
    public void onClickEvent() {

        super.onClickEvent();
        if (onClickListener != null) onClickListener.onClick(this);
        //getContext().toast("onClick");
    }

    @Override
    public void onClickEventCancel() {

        super.onClickEventCancel();
        //getContext().toast("onClickCancel");
    }

    @Override
    protected void onDraw(BaseCanvas canvas) {

        GLPaint paint = new GLPaint();
        if (isPressed()) {
            paint.setMixColor(Color4.gray(0.8f));
            canvas.drawTextureAnchorCenter(
                    GLTexture.White,
                    new Vec2(canvas.getWidth() / 2, canvas.getHeight() / 2),
                    new Vec2(canvas.getWidth() * 0.4f, canvas.getHeight() * 0.4f),
                    paint);
        } else {
            paint.setMixColor(Color4.White);
            canvas.drawTextureAnchorCenter(
                    GLTexture.White,
                    new Vec2(canvas.getWidth() / 2, canvas.getHeight() / 2),
                    new Vec2(canvas.getWidth() / 2, canvas.getHeight() / 2),
                    paint);
        }
    }
}
