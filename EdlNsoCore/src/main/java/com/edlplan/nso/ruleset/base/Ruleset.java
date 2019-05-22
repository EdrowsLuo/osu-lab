package com.edlplan.nso.ruleset.base;

import com.edlplan.nso.ruleset.base.beatmap.parser.BeatmapParser;
import com.edlplan.nso.ruleset.base.beatmap.parser.PartFactory;
import com.edlplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edlplan.nso.NsoCore;
import com.edlplan.nso.ruleset.base.game.WorldLoader;

public abstract class Ruleset {

    private final NsoCore context;

    public Ruleset(NsoCore context) {
        this.context = context;
    }

    /**
     * @return 一般而言是显示给用户的简短名称
     */
    public abstract String getRulesetName();

    /**
     * @return Ruleset的名称，用来唯一标识Ruleset的
     */
    public abstract String getRulesetIdName();

    public abstract AbstractTexture getModeIcon();

    /**
     * 加载Ruleset的时候被调用
     */
    public abstract void onLoad();

    public abstract BeatmapParser getBeatmapParser();

    public abstract WorldLoader getWorldLoader();

    public NsoCore getCore() {
        return context;
    }

    public void applyName(RulesetNameManager manager) {
        manager.putNameRef(getRulesetName(),getRulesetIdName());
    }

    /**
     * @return 默认的对铺面各个部分组装的工厂
     */
    public PartFactory getPartFactory() {
        return PartFactory.get();
    }


    public AbstractTexture getModeIconBig() {
        return getModeIcon();
    }

    public AbstractTexture getModeIconSmall() {
        return getModeIcon();
    }

}
