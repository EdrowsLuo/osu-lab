package com.edlplan.framework.math;

import com.edlplan.framework.utils.interfaces.Copyable;

public class RectI implements Copyable, IQuad {


    private Vec2Int basePoint = new Vec2Int();

    private int width;

    private int height;

    public RectI() {

    }

    public RectI(RectI r) {
        basePoint = r.basePoint.copy();
        this.width = r.width;
        this.height = r.height;
    }

    public RectI(int l, int t, int w, int h) {
        setBasePoint(l, t);
        this.width = w;
        this.height = h;
    }

    public RectI set(RectI rect) {
        setBasePoint(rect.getLeft(), rect.getTop());
        width = rect.getWidth();
        height = rect.getHeight();
        return this;
    }

    public Vec2Int getBasePoint() {
        return basePoint;
    }

    public void setBasePoint(int x, int y) {
        getBasePoint().set(x, y);
    }

    public Vec2Int getPoint(int x, int y) {
        return basePoint.copy().add(x * width, y * height);
    }

    public int getX1() {
        return basePoint.x;
    }

    public int getX2() {
        return basePoint.x + width;
    }

    public int getY1() {
        return basePoint.y;
    }

    public int getY2() {
        return basePoint.y + height;
    }

    public int getTop() {
        return basePoint.y;
    }

    public int getLeft() {
        return basePoint.x;
    }

    public int getBottom() {
        return getTop() + getHeight();
    }

    public int getRight() {
        return getLeft() + getWidth();
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void move(int dx, int dy) {
        getBasePoint().add(dx, dy);
    }

    public RectI padding(int padding) {
        move(padding, padding);
        width -= 2 * padding;
        height -= 2 * padding;
        return this;
    }

    public RectI padding(int pl, int pt, int pr, int pb) {
        move(pl, pt);
        width -= pl + pr;
        height -= pt + pb;
        return this;
    }

    @Override
    public Vec2 getTopRight() {

        return new Vec2(basePoint.x + width, basePoint.y);
    }

    @Override
    public Vec2 getTopLeft() {

        return new Vec2(basePoint.x, basePoint.y);
    }

    @Override
    public Vec2 getBottomRight() {

        return new Vec2(basePoint.x + width, basePoint.y + height);
    }

    @Override
    public Vec2 getBottomLeft() {

        return new Vec2(basePoint.x, basePoint.y + height);
    }

    @Override
    public Vec2 getPoint(float x, float y) {

        return new Vec2(basePoint.x + x * width, basePoint.y + height * y);
    }

    public static RectI xywh(int x, int y, int width, int height) {
        return new RectI(x, y, width, height);
    }
	
	/*
	public RectI padding(Vec4 p){
		return padding(p.r,p.g,p.b,p.a);
	}*/

    @Override
    public RectI copy() {
        return new RectI(this);
    }
}
