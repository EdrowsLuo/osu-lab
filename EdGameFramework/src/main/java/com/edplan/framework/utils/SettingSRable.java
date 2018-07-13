package com.edplan.framework.utils;

import com.edplan.framework.interfaces.Copyable;

public class SettingSRable<T extends SettingState> extends AbstractSRable<T> {
    private T defValue;

    public SettingSRable(T def) {
        defValue = def;
        initial();
        getData().set();
    }

    @Override
    public void onSave(T t) {

    }

    @Override
    public void onRestore(T now, T pre) {

        now.set();
    }

    @Override
    public T getDefData() {

        return defValue;
    }

    @Override
    public void setCurrentData(T t) {

        super.setCurrentData(t);
        t.set();
    }
}
