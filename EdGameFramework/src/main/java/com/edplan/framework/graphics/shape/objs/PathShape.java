package com.edplan.framework.graphics.shape.objs;

import com.edplan.framework.graphics.shape.IHasPath;
import com.edplan.framework.graphics.shape.Shape;

public interface PathShape extends Shape,IHasPath{

    default boolean isComplexShape() {
        return false;
    }
}
