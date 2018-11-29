package com.edplan.framework.utils;

import com.edplan.framework.utils.interfaces.Copyable;
import com.edplan.framework.utils.interfaces.Recycleable;

import java.util.Stack;

public abstract class AbstractSRable<T extends Copyable> implements Recycleable {
    private Stack<T> saves;

    public T currentData;

    private int currentDataIndex = 0;

    public abstract void onSave(T t);

    public abstract void onRestore(T now, T pre);

    public abstract T getDefData();

    public void reinitial() {
        saves.clear();
        currentData = getDefData();
        currentDataIndex = 0;
        saves.push(currentData);
    }

    public void initial() {
        saves = new Stack<T>();
        currentData = getDefData();
        currentDataIndex = 0;
        saves.push(currentData);
        //onSave(currentData);
    }

    public T getData() {
        return currentData;
    }

    public void setCurrentData(T t) {
        currentData = t;
    }

    @SuppressWarnings({"unchecked"})
    public int save() {
        onSave(currentData);
        saves.push(currentData);
        currentData = (T) currentData.copy();
        currentDataIndex++;
        return currentDataIndex - 1;
    }

    public void restore() {
        T popData = saves.pop();
        T pre = currentData;
        onRestore(popData, pre);
        currentData = popData;
        currentDataIndex--;
    }

    public void restoreToCount(int idx) {
        while (currentDataIndex > 0 && idx != currentDataIndex) {
            restore();
        }
    }

    @Override
    public void recycle() {

        if (saves != null) saves.clear();
        saves = null;
    }
}
