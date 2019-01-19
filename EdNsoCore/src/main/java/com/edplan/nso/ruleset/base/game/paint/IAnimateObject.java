package com.edplan.nso.ruleset.base.game.paint;

import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.timing.IHasIntervalSchedule;
import com.edplan.framework.timing.IntervalSchedule;
import com.edplan.framework.utils.FloatRef;

public interface IAnimateObject extends IHasIntervalSchedule, IHasAnimateProperty {

    default IAnimateObject fade(double start, double duration, float startAlpha, float endAlpha) {
        return animFloat(getAlphaRef(), start, duration, startAlpha, endAlpha);
    }

    default IAnimateObject scale(double start, double duration, float startScale, float endScale) {
        return animVec2(getScaleRef(), start, duration, startScale, endScale);
    }

    default IAnimateObject rotate(double start, double duration, float startV, float endV) {
        return animFloat(getRotationRef(), start, duration, startV, endV);
    }

    default IAnimateObject translate(double start, double duration, Vec2 startV, Vec2 endV) {
        return animVec2(getPositionRef(), start, duration, startV, endV);
    }

    default IAnimateObject animVec2(Vec2 ref, double start, double duration, float startV, float endV) {
        float change = endV - startV;
        getIntervalSchedule().addAnimTask(start, duration, time -> ref.x = ref.y = startV + change * (float) time);
        return this;
    }

    default IAnimateObject animVec2(Vec2 ref, double start, double duration, Vec2 startV, Vec2 endV) {
        float changeX = endV.x - startV.x;
        float changeY = endV.y - startV.y;
        float sx = startV.x, sy = startV.y;
        getIntervalSchedule().addAnimTask(start, duration, time -> {
            ref.x = sx + changeX * (float) time;
            ref.y = sy + changeY * (float) time;
        });
        return this;
    }

    default IAnimateObject animFloat(FloatRef ref, double start, double duration, float startV, float endV) {
        float change = endV - startV;
        getIntervalSchedule().addAnimTask(start, duration, time -> ref.value = startV + change * (float) time);
        return this;
    }

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

    default IAnimateObject of(IHasIntervalSchedule schedule, IHasAnimateProperty property) {
        return new IAnimateObject() {
            @Override
            public IntervalSchedule getIntervalSchedule() {
                return schedule.getIntervalSchedule();
            }

            @Override
            public FloatRef getAlphaRef() {
                return property.getAlphaRef();
            }

            @Override
            public FloatRef getRotationRef() {
                return property.getRotationRef();
            }

            @Override
            public Vec2 getScaleRef() {
                return property.getScaleRef();
            }

            @Override
            public Color4 getColorRef() {
                return property.getColorRef();
            }

            @Override
            public Vec2 getPositionRef() {
                return property.getPositionRef();
            }
        };
    }
}
