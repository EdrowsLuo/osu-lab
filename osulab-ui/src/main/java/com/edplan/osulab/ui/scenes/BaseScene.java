package com.edplan.osulab.ui.scenes;

import com.edplan.framework.ui.widget.component.Hideable;
import com.edplan.framework.ui.widget.ViewPage;
import com.edplan.framework.MContext;
import com.edplan.osulab.LabGame;
import com.edplan.framework.ui.widget.RelativeContainer;
import com.edplan.framework.ui.widget.RelativeLayout;
import com.edplan.framework.ui.layout.Param;

/**
 * 使用反射，确保继承类有getSceneNameStatic和isSingleInstanceStatic方法
 * 以及(MContext c)构造方法
 */
public abstract class BaseScene extends RelativeLayout implements Hideable {
    public BaseScene(MContext c) {
        super(c);

        setLayoutParam(
                new RelativeParam() {{
                    width = Param.MODE_MATCH_PARENT;
                    height = Param.MODE_MATCH_PARENT;
                }});
    }

    public abstract String getSceneName();

    public double getHideDuration() {
        return Scenes.SCENE_TRANSITION_DURATION;
    }

    @Override
    public boolean isHidden() {
        return getVisiblility() == VISIBILITY_GONE || LabGame.get().getScenes().getCurrentScene() != this;
    }
}
