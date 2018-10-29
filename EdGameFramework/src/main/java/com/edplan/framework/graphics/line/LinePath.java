package com.edplan.framework.graphics.line;

import com.edplan.framework.math.RectF;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.utils.MLog;

import java.util.ArrayList;
import java.util.List;

public class LinePath implements AbstractPath {
    private List<Vec2> positions;

    private float maxX, minX, maxY, minY;

    private float width;

    private PathMeasurer measurer;

    //private float tolerance=0.1f;

    public LinePath(List<Vec2> ps, float width) {
        positions = ps;
        this.width = width;
        recomputeBounds();
    }

    public LinePath(float width) {
        positions = new ArrayList<Vec2>();
        this.width = width;
    }

    public LinePath() {
        positions = new ArrayList<Vec2>();
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMinX() {
        return minX;
    }

    public float getMaxY() {
        return maxY;
    }

    public float getMinY() {
        return minY;
    }

    public PathMeasurer getMeasurer() {
        return measurer;
    }

    public void measure() {
        if (measurer != null) {
            measurer.clear();
        }
        measurer = new PathMeasurer(this);
        measurer.measure();
    }

    /**
     * 扩展到指定长度
     */
    public void bufferLength(float length) {
        if (measurer.maxLength() >= length) return;
        Vec2 added = measurer.atLength(length);
        add(added);
        measurer.onAddPoint(added, positions.get(positions.size() - 2));
    }

    /**
     *
     */
    public void translate(float dx, float dy) {
        for (Vec2 v : positions) {
            v.add(dx, dy);
        }
        recomputeBounds();
    }

    @Override
    public int size() {
        return positions.size();
    }

    @Override
    public Vec2 get(int index) {
        return positions.get(index);
    }

    public Vec2 getLast() {
        return get(size() - 1);
    }

    public List<Vec2> getAll() {
        return positions;
    }

    public RectF getDrawArea() {
        return RectF.ltrb(minX - width, minY - width, maxX + width, maxY + width);
    }

    public float getAreaWidth() {
        return maxX - minX + 2 * width;
    }

    public float getAreaHeight() {
        return maxY - minY + 2 * width;
    }

    public void setWidth(float width) {
        this.width = width;
        //recomputeBounds();
    }

    @Override
    public float getWidth() {
        return width;
    }

    public void set(List<Vec2> ps) {
        positions = ps;
        recomputeBounds();
    }

    public void clearPositionData() {
        positions.clear();
        resetBounds();
    }

    public void add(Vec2 v) {
        positions.add(v);
        expandBounds(v);
    }

    private void resetBounds() {
        minX = minY = 9999999;
        maxX = maxY = -9999999;
    }

    private void expandBounds(Vec2 v) {
        if (v.x < minX) {
            minX = v.x;
        }
        if (v.x > maxX) {
            maxX = v.x;
        }
        if (v.y < minY) {
            minY = v.y;
        }
        if (v.y > maxY) {
            maxY = v.y;
        }
    }

    private void recomputeBounds() {
        resetBounds();
        for (Vec2 v : positions) {
            expandBounds(v);
        }
    }

    public AbstractPath cutPath(float start, float end) {

        int s = measurer.binarySearch(start) + 1;
        int e = measurer.binarySearch(end);
        Vec2 startPoint = measurer.atLength(start);
        Vec2 endPoint = measurer.atLength(end);

        /**
         *这部分是用来防止当剪切路径刚好在路径点上的情况。。
         *不过现在测试感觉没什么用，暂时注释掉
         */
		
		/*
		if(Vec2.near(get(s),startPoint,tolerance)){
			if(Vec2.near(get(e),endPoint,tolerance)){
				return new SubLinePath(s,e);
			}else{
				return new SubLinePathR(endPoint,s,e);
			}
		}else{
			if(Vec2.near(get(e),endPoint,tolerance)){
				return new SubLinePathL(startPoint,s,e);
			}else{
				return new SubLinePathLR(startPoint,endPoint,s,e);
			}
		}*/

        return new SubLinePathLR(startPoint, endPoint, s, e);
    }

    public class SubLinePath implements AbstractPath {

        private int startIndex;

        private int size;

        public SubLinePath(int startIndex, int endIndex) {
            this.startIndex = startIndex;
            this.size = endIndex - startIndex + 1;
        }

        @Override
        public Vec2 get(int idx) {

            return LinePath.this.get(startIndex + idx);
        }

        @Override
        public int size() {

            return size;
        }

        @Override
        public float getWidth() {

            return LinePath.this.getWidth();
        }
    }

    public class SubLinePathL implements AbstractPath {

        private Vec2 startPoint;

        private int startIndex;

        private int startIndexM1;

        private int size;

        public SubLinePathL(Vec2 startPoint, int startIndex, int endIndex) {
            this.startPoint = startPoint;
            this.startIndex = startIndex;
            startIndexM1 = startIndex - 1;
            this.size = endIndex - startIndex + 2;
        }

        @Override
        public Vec2 get(int idx) {

            return (idx == 0) ? startPoint : LinePath.this.get(startIndexM1 + idx);
        }

        @Override
        public int size() {

            return size;
        }

        @Override
        public float getWidth() {

            return LinePath.this.getWidth();
        }
    }

    public class SubLinePathR implements AbstractPath {

        private Vec2 endPoint;

        private int startIndex;

        private int endIndexA1;

        private int size;

        public SubLinePathR(Vec2 endPoint, int startIndex, int endIndex) {
            this.endPoint = endPoint;
            this.startIndex = startIndex;
            this.endIndexA1 = endIndex + 1;
            this.size = endIndex - startIndex + 2;
        }

        @Override
        public Vec2 get(int idx) {

            return (idx == endIndexA1) ? endPoint : LinePath.this.get(startIndex + idx);
        }

        @Override
        public int size() {

            return size;
        }

        @Override
        public float getWidth() {

            return LinePath.this.getWidth();
        }
    }

    public class SubLinePathLR implements AbstractPath {

        private Vec2 startPoint;

        private Vec2 endPoint;

        private int endPointIdx;

        private int startIndexM1;

        private int size;

        public SubLinePathLR(Vec2 startPoint, Vec2 endPoint, int startIndex, int endIndex) {
            this.endPoint = endPoint;
            this.startPoint = startPoint;
            this.startIndexM1 = startIndex - 1;
            this.endPointIdx = endIndex - startIndex + 2;
            this.size = endIndex - startIndex + 3;
            MLog.test.vOnce("sub", "path-test", "e:" + endIndex + " s:" + startIndex + " size: " + size);
        }

        @Override
        public Vec2 get(int idx) {

            if (idx == 0) {
                return startPoint;
            } else if (idx == endPointIdx) {
                return endPoint;
            } else {
                if (startIndexM1 + idx == 3) {
                    MLog.test.vOnce("sub2", "path-test", "err called 3 :" + idx + ":" + endPointIdx);
                }
                return LinePath.this.get(startIndexM1 + idx);
            }
        }

        @Override
        public int size() {

            return size;
        }

        @Override
        public float getWidth() {

            return LinePath.this.getWidth();
        }
    }
}
