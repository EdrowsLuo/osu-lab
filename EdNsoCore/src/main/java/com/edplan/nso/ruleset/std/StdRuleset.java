package com.edplan.nso.ruleset.std;

import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.ui.text.font.FontAwesome;
import com.edplan.nso.NsoCore;
import com.edplan.nso.ruleset.base.Ruleset;
import com.edplan.nso.ruleset.base.RulesetNameManager;
import com.edplan.nso.ruleset.base.beatmap.Beatmap;
import com.edplan.nso.ruleset.base.beatmap.BeatmapDescription;
import com.edplan.nso.ruleset.base.beatmap.parser.BeatmapParser;
import com.edplan.nso.ruleset.base.game.GameObject;
import com.edplan.nso.ruleset.base.game.World;
import com.edplan.nso.ruleset.base.game.WorldLoader;
import com.edplan.nso.ruleset.base.game.judge.CursorData;
import com.edplan.nso.ruleset.std.beatmap.StdBeatmap;
import com.edplan.nso.ruleset.std.beatmap.StdBeatmapParser;

public class StdRuleset extends Ruleset{

    public static final String NAME = "Std";

    public static final String ID_NAME = "ppy.osu.Std";

    AbstractTexture icon;

    public StdRuleset(NsoCore context) {
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
        StdBeatmapParser.load();
    }

    @Override
    public BeatmapParser getBeatmapParser() {
        return StdBeatmapParser.get();
    }

    @Override
    public WorldLoader getWorldLoader() {
        return null;
    }
}
