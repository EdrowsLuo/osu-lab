package com.edlplan.osulab;

import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.MainRenderer;
import com.edlplan.framework.main.MainApplication;
import com.edlplan.framework.ui.EdView;

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
