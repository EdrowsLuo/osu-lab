package com.edplan.nso.filepart;
import com.edplan.nso.OsuFilePart;
import com.edplan.nso.beatmapComponent.Bookmarks;
import com.edplan.superutils.U;

public class PartEditor implements OsuFilePart
{
	public static final String Bookmarks="Bookmarks";
	public static final String DistanceSpacing="DistanceSpacing";
	public static final String BeatDivisor="BeatDivisor";
	public static final String GridSize="GridSize";
	public static final String TimelineZoom="TimelineZoom";
	
	public static final String TAG="Editor";
	
	private Bookmarks bookmarks=null;
	private float distanceSpacing=0;
	private int beatDivisor=0;
	private int gridSize=0;
	private float timelineZoom=0;

	public void setBookmarks(Bookmarks bookmarks){
		this.bookmarks=bookmarks;
	}

	public Bookmarks getBookmarks(){
		return bookmarks;
	}
	
	public String getBookmarksString(){
		return (getBookmarks()!=null)?getBookmarks().makeString():"{@Bookmarks}";
	}

	public void setDistanceSpacing(float distanceSpacing){
		this.distanceSpacing=distanceSpacing;
	}

	public float getDistanceSpacing(){
		return distanceSpacing;
	}

	public void setBeatDivisor(int beatDivisor){
		this.beatDivisor=beatDivisor;
	}

	public int getBeatDivisor(){
		return beatDivisor;
	}

	public void setGridSize(int gridSize){
		this.gridSize=gridSize;
	}

	public int getGridSize(){
		return gridSize;
	}

	public void setTimelineZoom(float timelineZoom){
		this.timelineZoom=timelineZoom;
	}

	public float getTimelineZoom(){
		return timelineZoom;
	}
	
	@Override
	public String getTag(){

		return TAG;
	}

	@Override
	public String makeString(){

		StringBuilder sb=new StringBuilder();
		U.appendProperty(sb,PartEditor.Bookmarks       ,getBookmarksString() ).append(U.NEXT_LINE);
		U.appendProperty(sb,PartEditor.DistanceSpacing ,getDistanceSpacing() ).append(U.NEXT_LINE);
		U.appendProperty(sb,PartEditor.BeatDivisor     ,getBeatDivisor()     ).append(U.NEXT_LINE);
		U.appendProperty(sb,PartEditor.GridSize        ,getGridSize()        ).append(U.NEXT_LINE);
		U.appendProperty(sb,PartEditor.TimelineZoom    ,getTimelineZoom()    ).append(U.NEXT_LINE);
		return sb.toString();
	}
}
