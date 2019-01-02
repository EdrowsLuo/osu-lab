package com.edplan.nso.ruleset.base.game.paint;

import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.timing.IHasIntervalSchedule;
import com.edplan.framework.utils.FloatRef;

public interface IAnimateObject extends IHasIntervalSchedule, IHasAlpha, IHasScale, IHasColor, IHasPositon, IHasRotation {

    @Override
    default FloatRef getAlphaRef() {
        throw new UnsupportedOperationException("这个属性没有被重载");
    }

    @Override
    default Vec2 getScaleRef() {
        throw new UnsupportedOperationException("这个属性没有被重载");
    }

    @Override
    default Color4 getColorRef() {
        throw new UnsupportedOperationException("这个属性没有被重载");
    }

    @Override
    default Vec2 getPositionRef() {
        throw new UnsupportedOperationException("这个属性没有被重载");
    }

    @Override
    default FloatRef getRotationRef() {
        throw new UnsupportedOperationException("这个属性没有被重载");
    }
}
