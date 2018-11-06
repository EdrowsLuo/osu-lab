package com.edplan.nso.filepart;

import com.edplan.framework.utils.dataobject.DataObject;
import com.edplan.framework.utils.dataobject.ItemInfo;
import com.edplan.framework.utils.dataobject.Struct;
import com.edplan.framework.utils.dataobject.def.DefaultBoolean;
import com.edplan.framework.utils.dataobject.def.DefaultFloat;
import com.edplan.framework.utils.dataobject.def.DefaultInt;
import com.edplan.nso.OsuFilePart;
import com.edplan.nso.beatmapComponent.SampleSet;
import com.edplan.superutils.U;

public class PartGeneral extends DataObject implements OsuFilePart {
    public static final String AudioFilename = "AudioFilename";
    public static final String AudioLeadIn = "AudioLeadIn";
    public static final String PreviewTime = "PreviewTime";
    public static final String Countdown = "Countdown";
    public static final String SampleSet = "SampleSet";
    public static final String SampleVolume = "SampleVolume";
    public static final String StackLeniency = "StackLeniency";
    public static final String Mode = "Mode";
    public static final String LetterboxInBreaks = "LetterboxInBreaks";
    public static final String WidescreenStoryboard = "WidescreenStoryboard";
    public static final String SpecialStyle = "SpecialStyle";
    public static final String UseSkinSprites = "UseSkinSprites";
    public static final String StoryFireInFront = "StoryFireInFront";
    public static final String EpilepsyWarning = "EpilepsyWarning";
    public static final String CountdownOffset = "CountdownOffset";
    public static final String TAG = "General";

    @ItemInfo(AudioFilename)
    private String audioFilename = "";

    @ItemInfo(AudioLeadIn)
    @DefaultInt(0)
    private int audioLeadIn = 0;

    @ItemInfo(PreviewTime)
    @DefaultInt(0)
    private int previewTime = 0;

    @ItemInfo(Countdown)
    @DefaultBoolean(false)
    private boolean countdown = false;

    @ItemInfo(CountdownOffset)
    @DefaultInt(0)
    private int countdownOffset = 0;

    //单独设置
    private SampleSet sampleSet = null;


    @ItemInfo(SampleVolume)
    @DefaultInt(100)
    private int sampleVolume = 100;

    @ItemInfo(StackLeniency)
    @DefaultFloat(0.7f)
    private float stackLeniency = 0.7f;

    //更换方式
    private int modeOld = -1;

    private String ruleset;

    @ItemInfo(LetterboxInBreaks)
    @DefaultBoolean(false)
    private boolean letterboxInBreaks = false;

    @ItemInfo(WidescreenStoryboard)
    @DefaultBoolean(false)
    private boolean widescreenStoryboard = false;

    @ItemInfo(SpecialStyle)
    @DefaultBoolean(false)
    private boolean specialStyle = false;

    @ItemInfo(UseSkinSprites)
    @DefaultBoolean(false)
    private boolean useSkinSprites = false;

    @ItemInfo(StoryFireInFront)
    @DefaultBoolean(false)
    private boolean storyFireInFront = false;

    @ItemInfo(EpilepsyWarning)
    @DefaultBoolean(false)
    private boolean epilepsyWarning = false;

    public PartGeneral() {
        //Map<String,Object> map=U.makeMap(String.class,Object.class,);
    }

    @Override
    protected void onLoadStruct(Struct struct) {
        struct.add(AudioFilename, String.class, this::getAudioFilename, this::setAudioFilename)
                .add(AudioLeadIn, Integer.class, this::getAudioLeadIn, this::setAudioLeadIn, 0)
                .add(PreviewTime, Integer.class, this::getPreviewTime, this::setPreviewTime, 0)
                .add(Countdown, Boolean.class, this::ifCountdown, this::setCountdown, false)
                .add(CountdownOffset, Integer.class, this::getCountdownOffset, this::setCountdownOffset, 0)
                .add(SampleSet, String.class, this::getSampleSet, this::setSampleSet)
                .add(SampleVolume, Integer.class, this::getSampleVolume, this::setSampleVolume, 100)
                .add(StackLeniency, Float.class, this::getStackLeniency, this::setStackLeniency, 0.7f)
                .add(LetterboxInBreaks, Boolean.class, this::isLetterboxInBreaks, this::setLetterboxInBreaks, false)
                .add(WidescreenStoryboard, Boolean.class, this::isLetterboxInBreaks, this::setLetterboxInBreaks, false)
                .add(SpecialStyle, Boolean.class, this::isSpecialStyle, this::setSpecialStyle, false)
                .add(UseSkinSprites, Boolean.class, this::isUseSkinSprites, this::setUseSkinSprites, false)
                .add(StoryFireInFront, Boolean.class, this::isStoryFireInFront, this::setStoryFireInFront, false)
                .add(EpilepsyWarning, Boolean.class, this::isEpilepsyWarning, this::setEpilepsyWarning, false);
    }

    public String getRuleset() {
        return ruleset;
    }

    public void setRuleset(String ruleset) {
        this.ruleset = ruleset;
    }

    public void setCountdownOffset(int countdownOffset) {
        this.countdownOffset = countdownOffset;
    }

    public int getCountdownOffset() {
        return countdownOffset;
    }

    public void setStoryFireInFront(boolean storyFireInFront) {
        this.storyFireInFront = storyFireInFront;
    }

    public boolean isStoryFireInFront() {
        return storyFireInFront;
    }

    public void setUseSkinSprites(boolean useSkinSprites) {
        this.useSkinSprites = useSkinSprites;
    }

    public boolean isUseSkinSprites() {
        return useSkinSprites;
    }

    public void setEpilepsyWarning(boolean epilepsyWarning) {
        this.epilepsyWarning = epilepsyWarning;
    }

    public boolean isEpilepsyWarning() {
        return epilepsyWarning;
    }

    public void setSampleVolume(int sampleVolume) {
        this.sampleVolume = sampleVolume;
    }

    public int getSampleVolume() {
        return sampleVolume;
    }

    public void setSpecialStyle(boolean specialStyle) {
        this.specialStyle = specialStyle;
    }

    public boolean isSpecialStyle() {
        return specialStyle;
    }

    public void setWidescreenStoryboard(boolean widescreenStoryboard) {
        this.widescreenStoryboard = widescreenStoryboard;
    }

    public boolean isWidescreenStoryboard() {
        return widescreenStoryboard;
    }

    public void setAudioFilename(String audioFilename) {
        this.audioFilename = audioFilename;
    }

    public String getAudioFilename() {
        return audioFilename;
    }

    public void setAudioLeadIn(int audioLeadIn) {
        this.audioLeadIn = audioLeadIn;
    }

    public int getAudioLeadIn() {
        return audioLeadIn;
    }

    public void setPreviewTime(int previewTime) {
        this.previewTime = previewTime;
    }

    public int getPreviewTime() {
        return previewTime;
    }

    public void setCountdown(boolean countdown) {
        this.countdown = countdown;
    }

    public boolean ifCountdown() {
        return countdown;
    }

    public void setSampleSet(SampleSet sampleSet) {
        this.sampleSet = sampleSet;
    }

    public void setSampleSet(String sampleSet) {
        this.sampleSet = com.edplan.nso.beatmapComponent.SampleSet.parse(sampleSet);
    }

    public String getSampleSet() {
        return sampleSet.toString();
    }

    public void setStackLeniency(float stackLeniency) {
        this.stackLeniency = stackLeniency;
    }

    public float getStackLeniency() {
        return stackLeniency;
    }

    public void setModeOld(int modeOld) {
        this.modeOld = modeOld;
    }

    public int getModeOld() {
        return modeOld;
    }

    public void setLetterboxInBreaks(boolean letterboxInBreaks) {
        this.letterboxInBreaks = letterboxInBreaks;
    }

    public boolean isLetterboxInBreaks() {
        return letterboxInBreaks;
    }

    @Override
    public String getTag() {

        return TAG;
    }

    @Override
    public String makeString() {

        StringBuilder sb = new StringBuilder();
        U.appendProperty(sb, PartGeneral.AudioFilename, getAudioFilename()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartGeneral.AudioLeadIn, getAudioLeadIn()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartGeneral.PreviewTime, getPreviewTime()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartGeneral.Countdown, U.toVString(ifCountdown())).append(U.NEXT_LINE);
        U.appendProperty(sb, PartGeneral.SampleSet, getSampleSet()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartGeneral.SampleVolume, getSampleVolume()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartGeneral.StackLeniency, getStackLeniency()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartGeneral.Mode, getModeOld()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartGeneral.LetterboxInBreaks, U.toVString(isLetterboxInBreaks())).append(U.NEXT_LINE);
        if (isSpecialStyle())
            U.appendProperty(sb, PartGeneral.SpecialStyle, U.toVString(isSpecialStyle())).append(U.NEXT_LINE);
        if (isEpilepsyWarning())
            U.appendProperty(sb, PartGeneral.EpilepsyWarning, U.toVString(isEpilepsyWarning())).append(U.NEXT_LINE);
        if (isWidescreenStoryboard())
            U.appendProperty(sb, PartGeneral.WidescreenStoryboard, U.toVString(isWidescreenStoryboard())).append(U.NEXT_LINE);

        return sb.toString();
    }
}
