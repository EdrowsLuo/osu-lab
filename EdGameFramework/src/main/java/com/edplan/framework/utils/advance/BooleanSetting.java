package com.edplan.framework.utils.advance;

import com.edplan.framework.interfaces.Setter;
import com.edplan.framework.utils.SRable;
import com.edplan.framework.utils.SRable.SROperation;

public class BooleanSetting {
    private SRable<BooleanCopyable> saves;

    private Setter<Boolean> setter;

    private boolean defValue;

    public BooleanSetting(Setter<Boolean> s, boolean d) {
        setter = s;
        defValue = d;
    }

    public BooleanSetting initial() {
        setter.set(defValue);
        saves = new SRable<BooleanCopyable>(new SROperation<BooleanCopyable>() {
            @Override
            public void onSave(BooleanCopyable v) {

            }

            @Override
            public void onRestore(BooleanCopyable now, BooleanCopyable pre) {

                set(now.getValue());
            }

            @Override
            public BooleanCopyable getDefData() {

                return new BooleanCopyable(defValue);
            }
        });
        saves.initial();
        return this;
    }

    public void set(boolean v) {
        if (saves.getData().getValue() != v) {
            setter.set(v);
            saves.getData().setValue(v);
        }
    }

    public void forceSet(boolean v) {
        setter.set(v);
        saves.getData().setValue(v);
    }

    public int save() {
        return saves.save();
    }

    public void restore() {
        saves.restore();
    }

    public void restorToCount(int id) {
        saves.restoreToCount(id);
    }
}
