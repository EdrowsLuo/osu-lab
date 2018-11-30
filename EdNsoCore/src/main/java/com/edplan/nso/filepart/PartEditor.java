package com.edplan.nso.filepart;

import com.edplan.framework.utils.dataobject.DataMapObject;
import com.edplan.framework.utils.dataobject.ItemInfo;
import com.edplan.framework.utils.dataobject.Struct;
import com.edplan.framework.utils.dataobject.def.DefaultFloat;
import com.edplan.framework.utils.dataobject.def.DefaultInt;
import com.edplan.nso.OsuFilePart;
import com.edplan.nso.beatmapComponent.Bookmarks;
import com.edplan.framework.utils.U;

public class PartEditor extends DataMapObject implements OsuFilePart {
    public static final String Bookmarks = "Bookmarks";
    public static final String DistanceSpacing = "DistanceSpacing";
    public static final String BeatDivisor = "BeatDivisor";
    public static final String GridSize = "GridSize";
    public static final String TimelineZoom = "TimelineZoom";

    public static final String TAG = "Editor";

    private Bookmarks bookmarks = null;

    @ItemInfo
    @DefaultFloat(1)
    private float distanceSpacing = 1;

    @ItemInfo
    @DefaultInt(8)
    private int beatDivisor = 8;

    @ItemInfo
    @DefaultInt(4)
    private int gridSize = 4;

    @ItemInfo
    @DefaultFloat(1)
    private float timelineZoom = 1;

    @Override
    protected void onLoadStruct(Struct struct) {
        struct.add(Bookmarks, String.class,
                () -> bookmarks != null ? bookmarks.toString() : "",
                s -> bookmarks = com.edplan.nso.beatmapComponent.Bookmarks.parse(s));
        loadStructAnnotation(struct);
    }

    public void setBookmarks(Bookmarks bookmarks) {
        this.bookmarks = bookmarks;
    }

    public Bookmarks getBookmarks() {
        return bookmarks;
    }

    public String getBookmarksString() {
        return (getBookmarks() != null) ? getBookmarks().toString() : "{@Bookmarks}";
    }

    public void setDistanceSpacing(float distanceSpacing) {
        this.distanceSpacing = distanceSpacing;
    }

    public float getDistanceSpacing() {
        return distanceSpacing;
    }

    public void setBeatDivisor(int beatDivisor) {
        this.beatDivisor = beatDivisor;
    }

    public int getBeatDivisor() {
        return beatDivisor;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setTimelineZoom(float timelineZoom) {
        this.timelineZoom = timelineZoom;
    }

    public float getTimelineZoom() {
        return timelineZoom;
    }

    @Override
    public String getTag() {

        return TAG;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        U.appendProperty(sb, PartEditor.Bookmarks, getBookmarksString()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartEditor.DistanceSpacing, getDistanceSpacing()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartEditor.BeatDivisor, getBeatDivisor()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartEditor.GridSize, getGridSize()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartEditor.TimelineZoom, getTimelineZoom()).append(U.NEXT_LINE);
        return sb.toString();
    }
}
