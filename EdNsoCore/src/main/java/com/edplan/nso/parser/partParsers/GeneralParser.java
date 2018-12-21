package com.edplan.nso.parser.partParsers;

import com.edplan.nso.filepart.PartGeneral;
import com.edplan.framework.utils.U;
import com.edplan.nso.filepart.SampleSet;

public class GeneralParser extends PartParser<PartGeneral> {
    public PartGeneral part;

    public GeneralParser() {
        part = new PartGeneral();
    }

    @Override
    public PartGeneral getPart() {

        return part;
    }

    @Override
    public boolean parse(String l) {

        if (l == null || l.trim().length() == 0) {
            return true;
        } else {
            String[] entry = U.divide(l, l.indexOf(":"));
            if (entry.length < 2) {
                return false;
            } else {
                switch (entry[0]) {
                    case PartGeneral.AudioFilename:
                        part.setAudioFilename(entry[1]);
                        return true;
                    case PartGeneral.AudioLeadIn:
                        part.setAudioLeadIn(U.toInt(entry[1]));
                        return true;
                    case PartGeneral.Countdown:
                        part.setCountdown(U.toBool(entry[1]));
                        return true;
                    case PartGeneral.LetterboxInBreaks:
                        part.setLetterboxInBreaks(U.toBool(entry[1]));
                        return true;
                    case PartGeneral.Mode:
                        part.setModeOld(U.toInt(entry[1]));
                        return true;
                    case PartGeneral.PreviewTime:
                        part.setPreviewTime(U.toInt(entry[1]));
                        return true;
                    case PartGeneral.SampleSet:
                        part.setSampleSet(SampleSet.parse(entry[1]));
                        return true;
                    case PartGeneral.SampleVolume:
                        part.setSampleVolume(U.toInt(entry[1]));
                        return true;
                    case PartGeneral.StackLeniency:
                        part.setStackLeniency(U.toFloat(entry[1]));
                        return true;
                    case PartGeneral.WidescreenStoryboard:
                        part.setWidescreenStoryboard(U.toBool(entry[1]));
                        return true;
                    case PartGeneral.SpecialStyle:
                        part.setSpecialStyle(U.toBool(entry[1]));
                        return true;
                    case PartGeneral.UseSkinSprites:
                        part.setUseSkinSprites(U.toBool(entry[1]));
                        return true;
                    case PartGeneral.EpilepsyWarning:
                        part.setEpilepsyWarning(U.toBool(entry[1]));
                        return true;
                    case PartGeneral.StoryFireInFront:
                        part.setStoryFireInFront(U.toBool(entry[1]));
                        return true;
                    case PartGeneral.CountdownOffset:
                        part.setCountdownOffset(U.toInt(entry[1]));
                        return true;
                    default:
                        //handler err post
                        return false;
                }
            }
        }
    }
}
