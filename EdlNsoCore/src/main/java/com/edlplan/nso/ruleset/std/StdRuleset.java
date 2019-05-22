package com.edlplan.nso.ruleset.std;

import com.edlplan.nso.NsoCore;
import com.edlplan.nso.ruleset.base.Ruleset;
import com.edlplan.nso.ruleset.base.RulesetNameManager;
import com.edlplan.nso.ruleset.base.beatmap.parser.BeatmapParser;
import com.edlplan.nso.ruleset.base.game.WorldLoader;
import com.edlplan.nso.ruleset.std.beatmap.StdBeatmapParser;
import com.edlplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edlplan.framework.resource.Skin;
import com.edlplan.framework.ui.text.font.FontAwesome;

public class StdRuleset extends Ruleset {

    public static final String NAME = "Std";

    public static final String ID_NAME = "ppy.osu.Std";

    AbstractTexture icon;

    Skin stdSkin;

    public StdRuleset(NsoCore context) {
        super(context);
    }

    public Skin getStdSkin() {
        return stdSkin;
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
        StdBeatmapParser.load();
        if (getCore().hasUI()) {
            icon = FontAwesome.fa_osu_osu_o.getTexture();
            stdSkin = StdSkin.createSkinDescription(getCore().getContext())
                    .load(getCore().getContext()
                            .getAssetResource()
                            .subResource("osu")
                            .subResource("skins")
                            .subResource("default"));
        }
    }

    @Override
    public BeatmapParser getBeatmapParser() {
        return StdBeatmapParser.get();
    }

    @Override
    public WorldLoader getWorldLoader() {
        return new StdWorldLoader(getCore());
    }

}
