package com.edplan.nso.resource;

import com.edplan.framework.graphics.opengl.objs.Color4;

import java.util.ArrayList;
import java.util.Arrays;

public class BaseComboColorGenerater implements IComboColorGenerater {
    private ArrayList<Color4> colorList = new ArrayList<Color4>();

    private int index = 0;

    private Color4 currentColor;

    public BaseComboColorGenerater() {

    }

    public BaseComboColorGenerater(Color4... cs) {
        addColors(cs);
    }

    private void moveStep(int step) {
        index = (index + step) % colorList.size();
    }

    public void addColor(Color4 c) {
        colorList.add(c);
    }

    public void addColors(Color4... cs) {
        colorList.addAll(Arrays.asList(cs));
    }

    @Override
    public Color4 currentColor() {

        return currentColor;
    }

    @Override
    public Color4 nextColor() {

        Color4 c = colorList.get(index);
        currentColor = c;
        moveStep(1);
        return c;
    }

    @Override
    public void skipColors(int count) {

        moveStep(count);
    }

    public static BaseComboColorGenerater createBy(Color4... cs) {
        return new BaseComboColorGenerater(cs);
    }
}
