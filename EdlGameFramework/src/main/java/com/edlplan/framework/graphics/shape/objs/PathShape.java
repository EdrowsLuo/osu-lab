package com.edlplan.framework.graphics.shape.objs;

import com.edlplan.framework.graphics.shape.IHasPath;
import com.edlplan.framework.graphics.shape.Shape;

public interface PathShape extends Shape,IHasPath {

    default boolean isComplexShape() {
        return false;
    }
}
