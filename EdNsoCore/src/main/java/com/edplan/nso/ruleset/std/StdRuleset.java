package com.edplan.nso.ruleset.std;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.ui.text.font.FontAwesome;
import com.edplan.framework.ui.text.font.bmfont.BMFont;
import com.edplan.nso.ruleset.base.Ruleset;
import com.edplan.nso.ruleset.base.RulesetNameManager;
import com.edplan.nso.ruleset.base.beatmap.parser.BeatmapParser;

import java.io.IOException;

public class StdRuleset extends Ruleset{

    public static final String NAME = "Std";

    public static final String ID_NAME = "ppy.osu.Std";

    AbstractTexture icon;

    public StdRuleset(MContext context) {
        super(context);
    }

    @Override
    public String getRulesetName() {
        return NAME;
    }

    @Override
    public String getRulesetIdName() {
        return ID_NAME;
    }

    @Override
    public void applyName(RulesetNameManager manager) {
        manager.putNameRef("0", ID_NAME);
    }

    @Override
    public AbstractTexture getModeIcon() {
        return icon;
    }

    @Override
    public void onLoad() {
        icon = FontAwesome.fa_osu_osu_o.getTexture();
    }

    @Override
    public BeatmapParser getBeatmapParser() {
        return null;
    }
}
