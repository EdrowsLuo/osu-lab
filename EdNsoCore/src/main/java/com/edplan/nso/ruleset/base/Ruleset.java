package com.edplan.nso.ruleset.base;

import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.nso.NsoCore;
import com.edplan.nso.ruleset.base.beatmap.parser.BeatmapParser;
import com.edplan.nso.ruleset.base.beatmap.parser.PartFactory;

public abstract class Ruleset {

    private final NsoCore context;

    public Ruleset(NsoCore context) {
        this.context = context;
    }

    public NsoCore getCore() {
        return context;
    }

    /**
     * @return 一般而言是显示给用户的简短名称
     */
    public abstract String getRulesetName();

    /**
     * @return Ruleset的名称，用来唯一标识Ruleset的
     */
    public abstract String getRulesetIdName();

    public void applyName(RulesetNameManager manager) {
        manager.putNameRef(getRulesetName(),getRulesetIdName());
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

    public abstract BeatmapParser getBeatmapParser();


    /**
     * @return 默认的对铺面各个部分组装的工厂
     */
    public PartFactory getPartFactory() {
        return PartFactory.get();
    }

}
