package com.edplan.framework.graphics.opengl;

import com.edplan.framework.utils.AbstractSRable;

public class BlendSetting extends AbstractSRable<BlendProperty> {

    public BlendSetting() {

    }

    public BlendSetting setUp() {
        initial();
        apply(getData());
        return this;
    }

    public void apply(BlendProperty p) {
        p.applyToGL();
    }

    public boolean isEnable() {
        return getData().enable;
    }

    public BlendType getBlendType() {
        return getData().blendType;
    }

    public void set(boolean enable, BlendType blendType) {
        if (!getData().equals(enable, blendType)) {
            BlendProperty prop = new BlendProperty(enable, blendType);
            setCurrentData(prop);
            apply(prop);
        }
    }

    public void setEnable(boolean enable) {
        set(enable, getBlendType());
    }

    public void setBlendType(BlendType type) {
        set(isEnable(), type);
    }

    @Override
    public void onSave(BlendProperty t) {

    }

    @Override
    public void onRestore(BlendProperty now, BlendProperty pre) {

        if (!now.equals(pre)) {
            apply(now);
        }
    }

    @Override
    public BlendProperty getDefData() {

        return new BlendProperty();
    }
}
