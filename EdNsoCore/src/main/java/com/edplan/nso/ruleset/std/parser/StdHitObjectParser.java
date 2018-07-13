package com.edplan.nso.ruleset.std.parser;

import android.util.Log;

import com.edplan.framework.math.Vec2;
import com.edplan.nso.NsoException;
import com.edplan.nso.ParsingBeatmap;
import com.edplan.nso.ruleset.base.parser.HitObjectParser;
import com.edplan.nso.ruleset.mania.objects.ManiaHolder;
import com.edplan.nso.ruleset.std.objects.HitObjectAddition;
import com.edplan.nso.ruleset.std.objects.StdHitCircle;
import com.edplan.nso.ruleset.std.objects.StdHitObject;
import com.edplan.nso.ruleset.std.objects.StdHitObjectType;
import com.edplan.nso.ruleset.std.objects.StdPath;
import com.edplan.nso.ruleset.std.objects.StdSlider;
import com.edplan.nso.ruleset.std.objects.StdSpinner;
import com.edplan.superutils.U;
import com.edplan.superutils.classes.strings.StringSpliter;
import com.edplan.superutils.math.Vct2;

import java.util.ArrayList;
import java.util.List;

/**
 * Official format wikipage:
 * https://github.com/ppy/osu-wiki/blob/master/wiki/osu!_File_Formats/Osu_(file_format)/en.md
 */

public class StdHitObjectParser implements HitObjectParser<StdHitObject> {

    private ParsingBeatmap context;

    private StdTypedParser defParser;

    public StdHitObjectParser(ParsingBeatmap cont) {
        context = cont;
        defParser = createDefParser();
    }

    public void setDefParser(StdTypedParser defParser) {
        this.defParser = defParser;
    }

    public StdTypedParser getDefParser() {
        return defParser;
    }

    private int sliderIndex = 0;

    public StdTypedParser createDefParser() {
        return new StdTypedParser() {
            @Override
            public StdHitObject parse(StdHitObjectParser.HitObjectBaseDatas bd, StringSpliter spl) throws NsoException {

                switch (StdHitObjectType.parseType(bd.type)) {
                    case Circle:
                        StdHitCircle h = new StdHitCircle();
                        injectBaseDatas(bd, h);
                        h.setAddition(parseAddition(spl));
                        return h;
                    case Slider:
                        StdSlider s = new StdSlider();
                        injectBaseDatas(bd, s);
                        s.setSliderIndex(sliderIndex++);
                        s.setPath(parsePath(new Vec2(s.getStartX(), s.getStartY()), spl));
                        s.setRepeat(U.toInt(spl.next()));
                        s.setPixelLength(U.toDouble(spl.next()));
                        s.setEdgeHitsounds(parseEdgeHitsounds(spl));
                        s.setEdgeAdditions(parseEdgeAdditions(spl));
                        s.setAddition(parseAddition(spl));
                        return s;
                    case Spinner:
                        StdSpinner spn = new StdSpinner();
                        injectBaseDatas(bd, spn);
                        spn.setEndTime(U.toInt(spl.next()));
                        spn.setAddition(parseAddition(spl));
                        return spn;
                    case ManiaHolder:
                        ManiaHolder mh = new ManiaHolder();
                        injectBaseDatas(bd, mh);
                        mh.setEndTime(U.toInt(spl.next()));
                        mh.setAddition(parseAddition(spl));
                        return mh;
                    default:
                        return null;
                }
            }
        };
    }


    @Override
    public StdHitObject parse(String res) throws NsoException {

        try {
            StringSpliter spl = new StringSpliter(res, ",");
            HitObjectBaseDatas bd = parseBaseDatas(spl);
            return defParser.parse(bd, spl);
        } catch (Exception e) {
            throw new NsoException("err parsing : \"" + res + "\" from " + context.getResInfo() + "(Line:" + context.getParsingLineIndex() + ") msg : " + e.getMessage(), e);
        }
    }

    public List<Vct2<Integer, Integer>> parseEdgeAdditions(StringSpliter spl) throws NsoException {
        try {
            if (spl.hasNext()) {
                StringSpliter spl2 = new StringSpliter(spl.next(), "\\|");
                List<Vct2<Integer, Integer>> es = new ArrayList<Vct2<Integer, Integer>>(spl2.length());
                while (spl2.hasNext()) {
                    es.add(parseVct2II(spl2.next()));
                }
                return es;
            } else {
                List<Vct2<Integer, Integer>> l = new ArrayList<Vct2<Integer, Integer>>();
                l.add(new Vct2<Integer, Integer>(0, 0));
                l.add(new Vct2<Integer, Integer>(0, 0));
                return l;
            }
        } catch (Exception e) {
            throw new NsoException("err parsing edgeAdditions. msg: " + e.getMessage(), e);
        }
    }

    public int[] parseEdgeHitsounds(StringSpliter spl) throws NsoException {
        try {
            if (spl.hasNext()) {
                StringSpliter spl2 = new StringSpliter(spl.next(), "\\|");
                int[] es = new int[spl2.length()];
                for (int i = 0; i < es.length; i++) {
                    es[i] = U.toInt(spl2.next());
                }
                return es;
            } else {
                return new int[]{0, 0};
            }
        } catch (Exception e) {
            throw new NsoException("err parsing edgeSounds. msg: " + e.getMessage(), e);
        }
    }

    public static void injectBaseDatas(HitObjectBaseDatas bd, StdHitObject obj) {
        obj.setStartX(bd.x);
        obj.setStartY(bd.y);
        obj.setStartTime(bd.time);
        obj.setComboColorsSkip(bd.colorsToSkip);
        obj.setIsNewCombo(bd.newCombo);
        obj.setHitSound(bd.hitSound);
    }

    public StdPath parsePath(Vec2 startPoint, StringSpliter spl) throws NsoException {
        try {
            StdPath p = new StdPath();
            p.addControlPoint(startPoint);
            spl = new StringSpliter(spl.next(), "\\|");
            p.setType(StdPath.Type.forName(spl.next()));
            while (spl.hasNext()) {
                p.addControlPoint(parseVec2FF(spl.next()));
            }
            return p;
        } catch (Exception e) {
            throw new NsoException("err parsing sliderpath. msg: " + e.getMessage(), e);
        }
    }

    public Vec2 parseVec2FF(String s) throws Exception {
        String[] sp = s.split(":");
        return new Vec2(U.toFloat(sp[0]), U.toFloat(sp[1]));
    }

    public Vct2<Integer, Integer> parseVct2II(String s) throws Exception {
        String[] sp = s.split(":");
        return new Vct2<Integer, Integer>(U.toInt(sp[0]), U.toInt(sp[1]));
    }

    public HitObjectBaseDatas parseBaseDatas(StringSpliter sp) throws NsoException {
        try {
            HitObjectBaseDatas bd = new HitObjectBaseDatas();
            bd.x = U.toInt(sp.next());
            bd.y = U.toInt(sp.next());
            bd.time = U.toInt(sp.next());
            bd.type = U.toInt(sp.next());
            bd.hitSound = U.toInt(sp.next());

            bd.newCombo = isNewCombo(bd.type);
            bd.colorsToSkip = parseComboColorsToSkip(bd.type);
            return bd;
        } catch (Exception e) {
            throw new NsoException("err parsing BaseDatas. msg: " + e.getMessage(), e);
        }
    }

    public static HitObjectAddition parseAddition(StringSpliter spl) throws NsoException {
        HitObjectAddition adt = new HitObjectAddition();
        try {
            if (spl.hasNext()) {
                StringSpliter spl2 = new StringSpliter(spl.next(), ":");
                adt.setSampleSet(U.toInt(spl2.next()));
                adt.setSampleAddition(U.toInt(spl2.next()));
                adt.setCustomIndex(U.toInt(spl2.next()));
                adt.setSampleVolume(U.toInt(spl2.next()));
                if (spl2.hasNext() && spl2.getNow().length() > 0) {
                    adt.setOverrideSampleFile(spl2.getNow());
                }
            } else {
                addtionApplyDefault(adt);
            }
            return adt;
        } catch (Exception e) {
            Log.e("test", "parse err", e);
            throw new NsoException("err parsing Addition. msg: " + e.getMessage() + " class : " + e.getClass().getCanonicalName(), e);
        }
    }

    public static void addtionApplyDefault(HitObjectAddition add) {
        add.setCustomIndex(0);
        add.setOverrideSampleFile(null);
        add.setSampleAddition(0);
        add.setSampleSet(0);
        add.setSampleVolume(0);
    }

    public static int parseComboColorsToSkip(int i) {
        return (i & 0x000000F0) << 4;
    }

    public static boolean isNewCombo(int i) {
        return (i & 0x00000004) == 0x00000004;
    }


    public static class HitObjectBaseDatas {
        int x = 0;
        int y = 0;
        int type = 0;
        int time = 0;
        boolean newCombo = false;
        int colorsToSkip = 0;
        int hitSound = 0;
    }

    public interface StdTypedParser {
        public StdHitObject parse(HitObjectBaseDatas bd, StringSpliter spl) throws NsoException;
    }

}
