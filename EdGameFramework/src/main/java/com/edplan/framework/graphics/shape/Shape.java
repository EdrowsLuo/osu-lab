package com.edplan.framework.graphics.shape;

public interface Shape {

    /**
     * @return if this shape need more than one path to describe
     */
    boolean isComplexShape();

    enum ShapeCombineType {
        Sub, // 删除重复部分
        Add, // 并集
        Normal, //正常多边形模式（重复删减）
    }

    enum CombineFunction {
        OR, // DST∪SRC (union set of two shape)
        AND, // DST∩SRC (intersection of two shape)
    }
}
