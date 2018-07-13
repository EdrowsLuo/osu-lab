package com.edplan.nso.ruleset.std.parser;

import com.edplan.nso.ruleset.base.parser.HitObjectReparser;
import com.edplan.nso.ruleset.std.objects.StdHitObject;
import com.edplan.nso.NsoException;
import com.edplan.nso.ruleset.std.objects.StdHitCircle;
import com.edplan.nso.ruleset.std.objects.StdSlider;
import com.edplan.nso.ruleset.std.objects.StdSpinner;
import com.edplan.nso.ruleset.std.objects.StdPath;
import com.edplan.superutils.math.Vct2;
import com.edplan.nso.ruleset.mania.objects.ManiaHolder;
import com.edplan.framework.math.Vec2;

public class StdHitObjectReparser implements HitObjectReparser<StdHitObject> {

    @Override
    public String reparse(StdHitObject t) throws NsoException {

        StringBuilder sb = new StringBuilder();
        buildBaseDatas(sb, t);
        if (t instanceof StdHitCircle) {
            buildAddition(sb, t);
        } else if (t instanceof StdSlider) {
            StdSlider s = (StdSlider) t;
            buildSliderPath(sb, s.getPath());
            sb.append(s.getRepeat()).append(",");
            sb.append(s.getPixelLength()).append(",");
            buildEdgeHitsounds(sb, s);
            buildEdgeAddition(sb, s);
            buildAddition(sb, s);
        } else if (t instanceof StdSpinner) {
            StdSpinner s = (StdSpinner) t;
            sb.append(s.getEndTime()).append(",");
            buildAddition(sb, s);
        } else if (t instanceof ManiaHolder) {
            ManiaHolder s = (ManiaHolder) t;
            sb.append(s.getEndTime()).append(",");
            buildAddition(sb, s);
        }
        return sb.toString();
    }

    public void buildEdgeHitsounds(StringBuilder sb, StdSlider s) {
        for (int i : s.getEdgeHitsounds()) {
            sb.append(i).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(",");
    }

    public void buildEdgeAddition(StringBuilder sb, StdSlider s) {
        for (Vct2<Integer, Integer> v : s.getEdgeAdditions()) {
            sb.append(v.getX()).append(":").append(v.getY()).append("|");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(",");
    }

    public void buildSliderPath(StringBuilder sb, StdPath path) {
        sb.append(path.getType().getTag());
        for (Vec2 v : path.getControlPoints()) {
            sb.append("|").append((int) v.x).append(":").append((int) v.y);
        }
        sb.append(",");
    }

    public void buildAddition(StringBuilder sb, StdHitObject t) {
        sb.append(t.getSampleSet()).append(":");
        sb.append(t.getSampleSetAddition()).append(":");
        sb.append(t.getCustomIndex()).append(":");
        sb.append(t.getSampleVolume()).append(":");
        sb.append((t.hasOverrideSampleFile()) ? (t.getOverrideSampleFile()) : "");
    }

    public void buildBaseDatas(StringBuilder sb, StdHitObject t) {
        sb.append(t.getStartX()).append(",");
        sb.append(t.getStartY()).append(",");
        sb.append(t.getStartTime()).append(",");
        sb.append(buildType(t)).append(",");
        sb.append(t.getHitSound()).append(",");
    }

    public static int buildType(StdHitObject h) {
        int i = 0;
        if (h instanceof StdHitCircle) {
            i = i | 0x00000001;
        } else if (h instanceof StdSlider) {
            i = i | 0x00000002;
        } else if (h instanceof StdSpinner) {
            i = i | 0x00000008;
        }
        if (h.isNewCombo()) {
            i = i | 0x0000004;
        }
        i = i | (h.getComboColorsSkip() * 16);
        return i;
    }
}
