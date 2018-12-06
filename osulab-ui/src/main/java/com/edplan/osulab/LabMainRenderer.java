package com.edplan.osulab;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.MainRenderer;
import com.edplan.framework.main.MainApplication;
import com.edplan.framework.ui.EdView;

public class LabMainRenderer extends MainRenderer {
    public LabMainRenderer(MContext c, MainApplication app) {
        super(c, app);
        setFrameLimit(60);
    }

    @Override
    public EdView createContentView(MContext c) {
        LabGame.createGame();
        return LabGame.get().createContentView(c);
    }
}
