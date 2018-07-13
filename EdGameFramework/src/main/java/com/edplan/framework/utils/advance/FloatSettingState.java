package com.edplan.framework.utils.advance;

import com.edplan.framework.utils.SettingState;
import com.edplan.framework.interfaces.Copyable;

public abstract class FloatSettingState extends SettingState {
    protected float value;

    public FloatSettingState(float value) {
        this.value = value;
    }

    @Override
    public void set() {

    }

    @Override
    public Copyable copy() {

        return null;
    }
}
