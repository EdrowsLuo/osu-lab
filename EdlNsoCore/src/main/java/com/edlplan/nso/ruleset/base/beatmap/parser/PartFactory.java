package com.edlplan.nso.ruleset.base.beatmap.parser;

import com.edlplan.nso.filepart.PartColours;
import com.edlplan.nso.filepart.PartDifficulty;
import com.edlplan.nso.filepart.PartEditor;
import com.edlplan.nso.filepart.PartGeneral;
import com.edlplan.nso.filepart.PartMetadata;
import com.edlplan.nso.filepart.PartTimingPoints;
import com.edlplan.nso.filepart.SampleSet;
import com.edlplan.nso.parser.IniParser;
import com.edlplan.nso.timing.TimingPoint;
import com.edlplan.framework.utils.U;

import java.util.regex.Matcher;

/**
 * 默认解析铺面各部分的工厂类
 */
public class PartFactory {
    private static PartFactory defaultFactory = new PartFactory();

    public static PartFactory get() {
        return defaultFactory;
    }

    public PartGeneral createPartGeneral(IniParser.StdOptionPage page, BeatmapDecoder.OpenInfo info) {
        try {
            PartGeneral general = new PartGeneral();
            page.load(general);
            return general;
        } catch (Exception e) {
            e.printStackTrace();
            info.error(e.getMessage());
        }
        return null;
    }

    public PartEditor createPartEditor(IniParser.StdOptionPage page, BeatmapDecoder.OpenInfo info) {
        try {
            PartEditor part = new PartEditor();
            page.load(part);
            return part;
        } catch (Exception e) {
            e.printStackTrace();
            info.error(e.getMessage());
        }
        return null;
    }

    public PartMetadata createPartMetadata(IniParser.StdOptionPage page, BeatmapDecoder.OpenInfo info) {
        try {
            PartMetadata part = new PartMetadata();
            page.load(part);
            return part;
        } catch (Exception e) {
            e.printStackTrace();
            info.error(e.getMessage());
        }
        return null;
    }

    public PartDifficulty createPartDifficulty(IniParser.StdOptionPage page, BeatmapDecoder.OpenInfo info) {
        try {
            PartDifficulty part = new PartDifficulty();
            page.load(part);
            return part;
        } catch (Exception e) {
            e.printStackTrace();
            info.error(e.getMessage());
        }
        return null;
    }

    public PartColours createPartColors(IniParser.StdOptionPage page, BeatmapDecoder.OpenInfo info) {
        if (page == null) {
            return null;
        } else {
            PartColours part = new PartColours();
            if (page.hasKey(PartColours.SliderBorder)) {
                String[] sp = page.getString(PartColours.SliderBorder).split(",");
                part.setSliderBorder(
                        Integer.parseInt(sp[0].trim()),
                        Integer.parseInt(sp[1].trim()),
                        Integer.parseInt(sp[2].trim()));
            }
            if (page.hasKey(PartColours.SliderTrackOverride)) {
                String[] sp = page.getString(PartColours.SliderTrackOverride).split(",");
                part.setSliderTrackOverride(
                        Integer.parseInt(sp[0].trim()),
                        Integer.parseInt(sp[1].trim()),
                        Integer.parseInt(sp[2].trim()));
            }

            for (String key : page.keys()) {
                Matcher matcher = PartColours.COLOUR_HEAD.matcher(key);
                if (matcher.groupCount() > 1) {
                    String[] sp = page.getString(PartColours.SliderTrackOverride).split(",");
                    part.addColour(
                            Integer.parseInt(matcher.group(1)),
                            Integer.parseInt(sp[0].trim()),
                            Integer.parseInt(sp[1].trim()),
                            Integer.parseInt(sp[2].trim()));
                }
            }

            return part;
        }
    }

    public PartTimingPoints createPartTimingPoints(IniParser.CSVPage page, BeatmapDecoder.OpenInfo info) {
        PartTimingPoints timingPoints = new PartTimingPoints();
        for (String[] l : page.getData()) {
            TimingPoint t = new TimingPoint();
            t.setTime((int) Math.round(Double.parseDouble(l[0])));
            t.setBeatLength(Double.parseDouble(l[1]));
            t.setMeter(Integer.parseInt(l[2]));
            t.setSampleType(Integer.parseInt(l[3]));
            t.setSampleSet(SampleSet.parse(l[4]));
            t.setVolume(Integer.parseInt(l[5]));
            t.setInherited(U.toBool(l[6]));
            int eff = Integer.parseInt(l[7]);
            t.setKiaiMode((eff & 1) > 0);
            t.setOmitFirstBarSignature((eff & 8) > 0);
            timingPoints.addTimingPoint(t);
        }
        return timingPoints;
    }
}
