package com.edplan.nso.ruleset.base;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.ui.EdContainer;
import com.edplan.nso.ruleset.base.beatmap.Beatmap;

public abstract class Ruleset {

    private final MContext context;

    public Ruleset(MContext context) {
        this.context = context;
    }

    public MContext getContext() {
        return context;
    }

    /**
     * @return 一般而言是显示给用户的简短名称
     */
    public abstract String getRulesetName();

    /**
     * @return Ruleset的名称，用来唯一标识Ruleset的
     */
    public abstract String getRulesetInternalName();

    public void applyName(RulesetNameManager manager) {
        manager.putNameRef(getRulesetName(),getRulesetInternalName());
    }

    public abstract AbstractTexture getModeIcon();

    /**
     * 加载Ruleset的时候被调用
     */
    public abstract void onLoad();

    public AbstractTexture getModeIconBig() {
        return getModeIcon();
    }

    public AbstractTexture getModeIconSmall() {
        return getModeIcon();
    }

}
