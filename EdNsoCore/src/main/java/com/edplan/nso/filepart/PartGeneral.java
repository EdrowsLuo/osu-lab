package com.edplan.nso.filepart;
import com.edplan.nso.OsuFilePart;
import com.edplan.nso.beatmapComponent.SampleSet;
import com.edplan.superutils.U;

public class PartGeneral implements OsuFilePart
{
	public static final String AudioFilename="AudioFilename";
	public static final String AudioLeadIn="AudioLeadIn";
	public static final String PreviewTime="PreviewTime";
	public static final String Countdown="Countdown";
	public static final String SampleSet="SampleSet";
	public static final String SampleVolume="SampleVolume";
	public static final String StackLeniency="StackLeniency";
	public static final String Mode="Mode";
	public static final String LetterboxInBreaks="LetterboxInBreaks";
	public static final String WidescreenStoryboard="WidescreenStoryboard";
	public static final String SpecialStyle="SpecialStyle";
	public static final String UseSkinSprites="UseSkinSprites";
	public static final String StoryFireInFront="StoryFireInFront";
	public static final String EpilepsyWarning="EpilepsyWarning";
	public static final String CountdownOffset="CountdownOffset";
	public static final String TAG="General";
	
	private String audioFilename="";
	private int audioLeadIn=0;
	private int previewTime=0;
	private boolean countdown=false;
	private int countdownOffset=0;
	private SampleSet sampleSet=null;
	private int sampleVolume=100;
	private float stackLeniency=0.7f;
	private int mode=-1;
	private boolean letterboxInBreaks=false;
	private boolean widescreenStoryboard=false;
	private boolean specialStyle=false;
	private boolean useSkinSprites=false;
	private boolean storyFireInFront=false;
	private boolean epilepsyWarning=false;
	
	public PartGeneral(){
		//Map<String,Object> map=U.makeMap(String.class,Object.class,);
	}

	public void setCountdownOffset(int countdownOffset){
		this.countdownOffset=countdownOffset;
	}

	public int getCountdownOffset(){
		return countdownOffset;
	}

	public void setStoryFireInFront(boolean storyFireInFront){
		this.storyFireInFront=storyFireInFront;
	}

	public boolean isStoryFireInFront(){
		return storyFireInFront;
	}

	public void setUseSkinSprites(boolean useSkinSprites) {
		this.useSkinSprites=useSkinSprites;
	}

	public boolean isUseSkinSprites() {
		return useSkinSprites;
	}

	public void setEpilepsyWarning(boolean epilepsyWarning) {
		this.epilepsyWarning=epilepsyWarning;
	}

	public boolean isEpilepsyWarning() {
		return epilepsyWarning;
	}

	public void setSampleVolume(int sampleVolume) {
		this.sampleVolume=sampleVolume;
	}

	public int getSampleVolume() {
		return sampleVolume;
	}

	public void setSpecialStyle(boolean specialStyle){
		this.specialStyle=specialStyle;
	}

	public boolean isSpecialStyle(){
		return specialStyle;
	}
	
	public void setWidescreenStoryboard(boolean widescreenStoryboard){
		this.widescreenStoryboard=widescreenStoryboard;
	}

	public boolean isWidescreenStoryboard(){
		return widescreenStoryboard;
	}

	public void setAudioFilename(String audioFilename){
		this.audioFilename=audioFilename;
	}

	public String getAudioFilename(){
		return audioFilename;
	}

	public void setAudioLeadIn(int audioLeadIn){
		this.audioLeadIn=audioLeadIn;
	}

	public int getAudioLeadIn(){
		return audioLeadIn;
	}

	public void setPreviewTime(int previewTime){
		this.previewTime=previewTime;
	}

	public int getPreviewTime(){
		return previewTime;
	}

	public void setCountdown(boolean countdown){
		this.countdown=countdown;
	}

	public boolean ifCountdown(){
		return countdown;
	}

	public void setSampleSet(SampleSet sampleSet){
		this.sampleSet=sampleSet;
	}

	public SampleSet getSampleSet(){
		return sampleSet;
	}

	public void setStackLeniency(float stackLeniency){
		this.stackLeniency=stackLeniency;
	}

	public float getStackLeniency(){
		return stackLeniency;
	}

	public void setMode(int mode){
		this.mode=mode;
	}

	public int getMode(){
		return mode;
	}

	public void setLetterboxInBreaks(boolean letterboxInBreaks){
		this.letterboxInBreaks=letterboxInBreaks;
	}

	public boolean isLetterboxInBreaks(){
		return letterboxInBreaks;
	}
	
	@Override
	public String getTag(){

		return TAG;
	}

	@Override
	public String makeString(){

		StringBuilder sb=new StringBuilder();
		U.appendProperty(sb,PartGeneral.AudioFilename        ,getAudioFilename()                    ).append(U.NEXT_LINE);
		U.appendProperty(sb,PartGeneral.AudioLeadIn          ,getAudioLeadIn()                      ).append(U.NEXT_LINE);
		U.appendProperty(sb,PartGeneral.PreviewTime          ,getPreviewTime()                      ).append(U.NEXT_LINE);
		U.appendProperty(sb,PartGeneral.Countdown            ,U.toVString(ifCountdown())            ).append(U.NEXT_LINE);
		U.appendProperty(sb,PartGeneral.SampleSet            ,getSampleSet().makeString()           ).append(U.NEXT_LINE);
		U.appendProperty(sb,PartGeneral.SampleVolume         ,getSampleVolume()                     ).append(U.NEXT_LINE);
		U.appendProperty(sb,PartGeneral.StackLeniency        ,getStackLeniency()                    ).append(U.NEXT_LINE);
		U.appendProperty(sb,PartGeneral.Mode                 ,getMode()                             ).append(U.NEXT_LINE);
		U.appendProperty(sb,PartGeneral.LetterboxInBreaks    ,U.toVString(isLetterboxInBreaks())    ).append(U.NEXT_LINE);
		if(isSpecialStyle())
			U.appendProperty(sb,PartGeneral.SpecialStyle         ,U.toVString(isSpecialStyle())         ).append(U.NEXT_LINE);
		if(isEpilepsyWarning())
			U.appendProperty(sb,PartGeneral.EpilepsyWarning      ,U.toVString(isEpilepsyWarning())      ).append(U.NEXT_LINE);
		if(isWidescreenStoryboard())
			U.appendProperty(sb,PartGeneral.WidescreenStoryboard ,U.toVString(isWidescreenStoryboard()) ).append(U.NEXT_LINE);
		
		return sb.toString();
	}
}
