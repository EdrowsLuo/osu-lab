package com.edlplan.framework.ui.drawable.sprite;

import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.math.Vec2;

public abstract class ObjectSprite<S extends SpriteShader> extends Sprite<S> {
    private float rotation;

    private float scaleX = 1, scaleY = 1;

    private Vec2 position = new Vec2();

    public ObjectSprite(MContext c) {
        super(c);
    }

    public void setScale(float s) {
        setScaleX(s);
        setScaleY(s);
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public Vec2 getPosition() {
        return position;
    }

    public float getPositionX() {
        return getPosition().x;
    }

    public float getPositionY() {
        return getPosition().y;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getRotation() {
        return rotation;
    }

    @Override
    protected void startDraw(BaseCanvas canvas) {
        super.startDraw(canvas);
        canvas.save();
        canvas.translate(position.x, position.y);
        canvas.scale(scaleX, scaleY);
        canvas.rotate(rotation);
    }

    @Override
    protected void endDraw(BaseCanvas canvas) {

        super.endDraw(canvas);
        canvas.restore();
    }
}
