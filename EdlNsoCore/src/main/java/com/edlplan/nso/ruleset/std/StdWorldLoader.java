package com.edlplan.nso.ruleset.std;

import com.edlplan.nso.NsoCore;
import com.edlplan.nso.ruleset.base.beatmap.BeatmapDescription;
import com.edlplan.nso.ruleset.base.beatmap.parser.BeatmapDecoder;
import com.edlplan.nso.ruleset.base.game.World;
import com.edlplan.nso.ruleset.base.game.WorldLoader;
import com.edlplan.nso.ruleset.std.beatmap.StdBeatmap;
import com.edlplan.nso.ruleset.std.game.StdGameField;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class StdWorldLoader extends WorldLoader {

    public StdWorldLoader(NsoCore core) {
        super(core);
    }

    @Override
    public Set<String> acceptedBeatmapType() {
        return Collections.singleton(StdRuleset.ID_NAME);
    }

    @Override
    public World loadWorld(BeatmapDescription description) {

        StdBeatmap beatmap;
        if (description.cachedBeatmap == null || !(description.cachedBeatmap instanceof StdBeatmap)) {
            try {
                BeatmapDecoder decoder = getCore().getBeatmapDecoder();
                decoder.decode(description.openBeatmapStream());
                BeatmapDecoder.Result result = decoder.getResult();
                if (result.isSuccess()) {
                    if (result.getBeatmap() instanceof StdBeatmap) {
                        beatmap = (StdBeatmap) result.getBeatmap();
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            beatmap = (StdBeatmap) description.cachedBeatmap;
        }

        return (new StdGameField(getCore())).load(beatmap, description.openDirRes());
    }

}
