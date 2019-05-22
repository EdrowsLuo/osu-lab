package com.edlplan.framework.math;

import com.edlplan.framework.ui.Anchor;

public class Quad implements IQuad {
    public final Vec2 topLeft = new Vec2();
    public final Vec2 topRight = new Vec2();
    public final Vec2 bottomLeft = new Vec2();
    public final Vec2 bottomRight = new Vec2();
    public final Vec2[] vertexs;

    public final Vec2[] savedAnchor = new Vec2[Anchor.SPECIAL_ANCHOR_COUNT];

    public Quad(Vec2 v1, Vec2 v2, Vec2 v3, Vec2 v4) {
        set(v1, v2, v3, v4);
        vertexs = new Vec2[]{topLeft, topRight, bottomRight, bottomLeft};
    }

    public Quad() {
        vertexs = new Vec2[]{topLeft, topRight, bottomRight, bottomLeft};
    }

    public Quad(RectF r) {
        vertexs = new Vec2[]{topLeft, topRight, bottomRight, bottomLeft};
        set(r);
    }

    private void invalidateAnchorSaves() {
        savedAnchor[0] = topLeft;
        savedAnchor[1] = new Vec2((topLeft.x + topRight.x) / 2, (topLeft.y + topRight.y) / 2);
        savedAnchor[2] = topRight;
        savedAnchor[3] = new Vec2((topLeft.x + bottomLeft.x) / 2, (topLeft.y + bottomLeft.y) / 2);
        savedAnchor[4] = new Vec2((topLeft.x + bottomLeft.x + topRight.x + bottomRight.x) / 4, (topLeft.y + bottomLeft.y + topRight.y + bottomRight.y) / 4);
        savedAnchor[5] = new Vec2((topRight.x + bottomRight.x) / 2, (topRight.y + bottomRight.y) / 2);
        savedAnchor[6] = bottomLeft;
        savedAnchor[7] = new Vec2((bottomLeft.x + bottomRight.x) / 2, (bottomLeft.y + bottomLeft.x) / 2);
        savedAnchor[8] = bottomRight;
    }

    public Vec2 getPointByAnchor(Anchor a) {
        switch (a.id / 3) {
            case 0:
                switch (a.id % 3) {
                    case 0:
                        return topLeft;
                    case 1:
                        return new Vec2((topLeft.x + topRight.x) / 2, (topLeft.y + topRight.y) / 2);
                    case 2:
                        return topRight;
                }
                break;
            case 1:
                switch (a.id % 3) {
                    case 0:
                        return new Vec2((topLeft.x + bottomLeft.x) / 2, (topLeft.y + bottomLeft.y) / 2);
                    case 1:
                        return new Vec2((topLeft.x + bottomLeft.x + topRight.x + bottomRight.x) / 4, (topLeft.y + bottomLeft.y + topRight.y + bottomRight.y) / 4);
                    case 2:
                        return new Vec2((topRight.x + bottomRight.x) / 2, (topRight.y + bottomRight.y) / 2);
                }
                break;
            case 2:
                switch (a.id % 3) {
                    case 0:
                        return bottomLeft;
                    case 1:
                        return new Vec2((bottomLeft.x + bottomRight.x) / 2, (bottomLeft.y + bottomRight.y) / 2);
                    case 2:
                        return bottomRight;
                }
                break;
        }
        return getPoint(a.x(), a.y());
    }

    public Quad swapXY() {
        swap(topRight, bottomLeft);
        return this;
    }

    public void setTopLeft(Vec2 topLeft) {
        this.topLeft.set(topLeft);
    }

    public void flip(boolean h, boolean v) {
        if (h) {
            swap(topLeft, topRight);
            swap(bottomLeft, bottomRight);
        }
        if (v) {
            swap(topLeft, bottomLeft);
            swap(topRight, bottomRight);
        }
    }

    private void swap(Vec2 v1, Vec2 v2) {
        final float x = v1.x, y = v1.y;
        v1.set(v2.x, v2.y);
        v2.set(x, y);
    }

    @Override
    public Vec2 getTopLeft() {
        return topLeft;
    }

    public void setTopRight(Vec2 topRight) {
        this.topRight.set(topRight);
    }

    @Override
    public Vec2 getTopRight() {
        return topRight;
    }

    public void setBottomLeft(Vec2 bottomLeft) {
        this.bottomLeft.set(bottomLeft);
    }

    @Override
    public Vec2 getBottomLeft() {
        return bottomLeft;
    }

    public void setBottomRight(Vec2 bottomRight) {
        this.bottomRight.set(bottomRight);
    }

    @Override
    public Vec2 getBottomRight() {
        return bottomRight;
    }

    private Vec2 getPoint(Anchor a) {
        if (a.id < Anchor.SPECIAL_ANCHOR_COUNT) {
            return savedAnchor[a.id];
        } else {
            return getPoint(a.x(), a.y());
        }
    }

    @Override
    public Vec2 getPoint(float x, float y) {
        return mapPoint(topLeft, topRight, bottomLeft, bottomRight, x, y);
    }

    public Quad set(Quad res) {
        topLeft.set(res.topLeft);
        topRight.set(res.topRight);
        bottomLeft.set(res.bottomLeft);
        bottomRight.set(res.bottomRight);
        return this;
    }

    public Quad set(Vec2 tl, Vec2 tr, Vec2 bl, Vec2 br) {
        topLeft.set(tl);
        topRight.set(tr);
        bottomLeft.set(bl);
        bottomRight.set(br);
        return this;
    }

    public Quad set(IQuad r) {
        return set(r.getTopLeft(), r.getTopRight(), r.getBottomLeft(), r.getBottomRight());
    }

    public void rotate(float ox, float oy, float ang) {
        final float s = (float) Math.sin(ang);
        final float c = (float) Math.cos(ang);
        for (Vec2 v : vertexs) {
            v.rotate(ox, oy, s, c);
        }
    }

    public void rotate(Vec2 o, float ang) {
        rotate(o.x, o.y, ang);
    }

    public void rotate(Anchor anchor, float ang) {
        rotate(getPoint(anchor.x(), anchor.y()), ang);
    }

    public void translate(float tx, float ty) {
        for (Vec2 v : vertexs) {
            v.move(tx, ty);
        }
    }

    /**
     * 通过射线法判断p是否在范围内
     */
    public boolean inArea(Vec2 p) {
        return PolygonUtil.inPolygon(p, vertexs);
    }

    public static Vec2 mapPoint(Vec2 ptl, Vec2 ptr, Vec2 pbl, Vec2 pbr, float vx, float vy) {
		/*
		 float x=((ptl.x+pbl.x)*(1-vx)+(ptr.x+pbr.x)*vx)/2;
		 float y=((ptl.y+ptr.y)*(1-vy)+(pbl.y+pbr.y)*vy)/2;
		 */
        final float omx = 1 - vx;
        final float omy = 1 - vy;
        final float lt = omx * omy;
        final float lb = omx * vy;
        final float rt = vx * omy;
        final float rb = vx * vy;
        final float x = lt * ptl.x + lb * pbl.x + rt * ptr.x + rb * pbr.x;
        final float y = lt * ptl.y + lb * pbl.y + rt * ptr.y + rb * pbr.y;
        return new Vec2(x, y);
    }
}
