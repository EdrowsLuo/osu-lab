package com.edlplan.nso.ruleset.std.objects.v2.raw;

import com.edlplan.nso.difficulty.DifficultyUtil;
import com.edlplan.nso.parser.ParseException;
import com.edlplan.nso.ruleset.base.game.StdFormatGameObject;
import com.edlplan.framework.math.Vec2;
import com.edlplan.nso.ruleset.std.beatmap.StdBeatmap;
import com.edlplan.framework.utils.advance.StringSplitter;
import com.edlplan.nso.ruleset.std.objects.v2.StdPath;

public abstract class StdGameObject extends StdFormatGameObject {

    public static final int NEW_COMBO_MASK = 0x00000004;

    public static final float APPROACH_CIRCLE_START_SCALE = 4;

    public static final float APPROACH_CIRCLE_END_SCALE = 1.1f;

    public static final float BASE_OBJECT_SIZE = 128;

    public boolean isNewCombo() {
        return (getType() & NEW_COMBO_MASK) > 0;
    }

    /**
     * @return 物件提前出现的时间
     */
    public double getTimePreempt(StdBeatmap beatmap) {
        return DifficultyUtil.stdHitObjectTimePreempt(beatmap.getDifficulty().getApproachRate());
    }

    /**
     * @return 物件渐出动画的时长
     */
    public double getFadeInDuration(StdBeatmap beatmap) {
        return DifficultyUtil.stdHitObjectTimeFadein(beatmap.getDifficulty().getApproachRate());
    }

    protected static StdPath parsePath(Vec2 startPoint, StringSplitter spl) throws ParseException {
        try {
            StdPath p = new StdPath();
            p.addControlPoint(startPoint);
            spl = new StringSplitter(spl.next(), "\\|");
            p.setType(StdPath.Type.forName(spl.next()));
            while (spl.hasNext()) {
                p.addControlPoint(parseVec2FF(spl.next()));
            }
            return p;
        } catch (Exception e) {
            throw new ParseException("err parsing sliderpath. msg: " + e.getMessage(), e);
        }
    }

    protected static Vec2 parseVec2FF(String s) {
        String[] sp = s.split(":");
        return new Vec2(Float.parseFloat(sp[0]), Float.parseFloat(sp[1]));
    }
}
