package com.edplan.framework.math;

import com.edplan.framework.utils.interfaces.Copyable;
import com.edplan.framework.ui.Anchor;

public class RectF implements Copyable, Area2D, IQuad {

    private Vec2 basePoint = new Vec2();

    private float width;

    private float height;

    public RectF() {

    }

    public RectF(RectF r) {
        basePoint = r.basePoint.copy();
        this.width = r.width;
        this.height = r.height;
    }

    public RectF(float l, float t, float w, float h) {
        setBasePoint(l, t);
        this.width = w;
        this.height = h;
    }

    public RectF set(RectI r) {
        setBasePoint(r.getX1(), r.getY1());
        width = r.getWidth();
        height = r.getHeight();
        return this;
    }

    public RectF set(RectF rect) {
        setBasePoint(rect.getX1(), rect.getY1());
        width = rect.getWidth();
        height = rect.getHeight();
        return this;
    }

    public RectF setXYWH(float x, float y, float w, float h) {
        setBasePoint(x, y);
        width = w;
        height = h;
        return this;
    }

    public RectF setLTRB(float l, float t, float r, float b) {
        basePoint.set(l, t);
        height = b - t;
        width = r - l;
        return this;
    }

    public Vec2 getBasePoint() {
        return basePoint;
    }

    public void setBasePoint(float x, float y) {
        getBasePoint().set(x, y);
    }

    public float getCenterHorizon() {
        return basePoint.x + width / 2;
    }

    public float getCenterVertical() {
        return basePoint.y + height / 2;
    }

    @Override
    public Vec2 getTopLeft() {
        return new Vec2(getLeft(), getTop());
    }

    @Override
    public Vec2 getTopRight() {
        return new Vec2(getRight(), getTop());
    }

    @Override
    public Vec2 getBottomLeft() {
        return new Vec2(getLeft(), getBottom());
    }

    @Override
    public Vec2 getBottomRight() {
        return new Vec2(getRight(), getBottom());
    }

    public Vec2 getCenter() {
        return new Vec2(getCenterHorizon(), getCenterVertical());
    }

    @Override
    public Vec2 getPoint(float x, float y) {
        return basePoint.copy().add(x * width, y * height);
    }

    public void getPoint(float x, float y, Vec2 target) {
        target.set(basePoint.x + width * x, basePoint.y + height * y);
    }

    public float getX1() {
        return basePoint.x;
    }

    public float getX2() {
        return basePoint.x + width;
    }

    public float getY1() {
        return basePoint.y;
    }

    public float getY2() {
        return basePoint.y + height;
    }

    public float getTop() {
        return basePoint.y;
    }

    public float getLeft() {
        return basePoint.x;
    }

    public float getBottom() {
        return basePoint.y + height;
    }

    public float getRight() {
        return basePoint.x + width;
    }

    public RectF setWidth(float width) {
        this.width = width;
        return this;
    }

    public float getWidth() {
        return width;
    }

    public RectF setHeight(float height) {
        this.height = height;
        return this;
    }

    public float getHeight() {
        return height;
    }

    public void move(float dx, float dy) {
        getBasePoint().add(dx, dy);
    }

    public RectF padding(float padding) {
        move(padding, padding);
        width -= 2 * padding;
        height -= 2 * padding;
        return this;
    }

    public RectF padding(float pl, float pt, float pr, float pb) {
        move(pl, pt);
        width -= pl + pr;
        height -= pt + pb;
        return this;
    }

    public RectF padding(Vec4 p) {
        return padding(p.r, p.g, p.b, p.a);
    }

    public RectF scale(Anchor anchor, float sx, float sy) {
        final float ox = basePoint.x + anchor.x() * width;
        final float oy = basePoint.y + anchor.y() * height;
        basePoint.zoom(ox, oy, sx, sy);
        width *= sx;
        height *= sy;
        return this;
    }

    public RectF scale(Anchor anchor, Vec2 s) {
        return scale(anchor, s.x, s.y);
    }

    public Quad toQuad() {
        return new Quad(this);
    }

    public Quad toQuad(Quad q) {
        return q.set(this);
    }

    public RectF thisXYWH(float x, float y, float w, float h) {
        this.basePoint.set(x, y);
        width = w;
        height = h;
        return this;
    }

    public RectF thisAnchorOWH(Anchor anchor, float ox, float oy, float w, float h) {
        return thisXYWH(ox - anchor.x() * w, oy - anchor.y() * h, w, h);
    }

    @Override
    public RectF copy() {
        return new RectF(this);
    }

    @Override
    public boolean inArea(Vec2 v) {

        Vec2 tmp = v.copy().minus(basePoint);
        return tmp.x >= 0 && tmp.x <= width && tmp.y >= 0 && tmp.y <= height;
    }

    @Override
    public float maxX() {

        return getRight();
    }

    @Override
    public float maxY() {

        return getBottom();
    }

    @Override
    public float minX() {

        return getLeft();
    }

    @Override
    public float minY() {

        return getTop();
    }

    @Override
    public String toString() {

        return "((" + getX1() + "," + getY1() + "),(" + getX2() + "," + getY2() + "))";
    }

    public static RectF ltrb(float l, float t, float r, float b) {
        return (new RectF()).setLTRB(l, t, r, b);
    }

    public static RectF xywh(float x, float y, float w, float h) {
        return new RectF(x, y, w, h);
    }

    public static RectF anchorOWH(Anchor anchor, float ox, float oy, float width, float height) {
        return xywh(ox - anchor.x() * width, oy - anchor.y() * height, width, height);
    }

    public static boolean inLTRB(float x, float y, float l, float t, float r, float b) {
        return x >= l && x <= r && y >= t && y <= b;
    }
}
