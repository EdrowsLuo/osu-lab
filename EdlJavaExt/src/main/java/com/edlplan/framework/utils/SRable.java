package com.edlplan.framework.utils;

import com.edlplan.framework.utils.interfaces.Copyable;

public class SRable<T extends Copyable> extends AbstractSRable<T> {
    private SROperation<T> op;

    public SRable(SROperation<T> op) {
        this.op = op;
    }

    @Override
    public void onSave(T t) {

        op.onSave(t);
    }

    @Override
    public void onRestore(T now, T pre) {

        op.onRestore(now, pre);
    }

    @Override
    public T getDefData() {

        return op.getDefData();
    }

    public interface SROperation<T extends Copyable> {
        public void onSave(T v);

        public void onRestore(T now, T pre);

        public T getDefData();
    }
}
