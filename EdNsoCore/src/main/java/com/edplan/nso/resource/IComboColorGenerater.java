package com.edplan.nso.resource;

import com.edplan.framework.graphics.opengl.objs.Color4;

public interface IComboColorGenerater {
    public Color4 currentColor();

    public Color4 nextColor();

    public void skipColors(int count);
}
