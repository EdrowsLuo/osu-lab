package com.edplan.nso.filepart;

import com.edplan.nso.OsuFilePart;
import com.edplan.nso.beatmapComponent.Tags;
import com.edplan.superutils.U;

public class PartMetadata implements OsuFilePart {
    public static final String Title = "Title";
    public static final String TitleUnicode = "TitleUnicode";
    public static final String Artist = "Artist";
    public static final String ArtistUnicode = "ArtistUnicode";
    public static final String Creator = "Creator";
    public static final String Version = "Version";
    public static final String Source = "Source";
    public static final String Tags = "Tags";
    public static final String BeatmapID = "BeatmapID";
    public static final String BeatmapSetID = "BeatmapSetID";

    public static final String TAG = "Metadata";

    private String title = null;
    private String titleUnicode = null;
    private String artist = null;
    private String artistUnicode = null;
    private String creator = null;
    private String version = null;
    private String source = null;
    private Tags tags = null;
    private int beatmapID = -1;
    private int beatmapSetID = -1;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitleUnicode(String titleUnicode) {
        this.titleUnicode = titleUnicode;
    }

    public String getTitleUnicode() {
        return titleUnicode;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtistUnicode(String artistUnicode) {
        this.artistUnicode = artistUnicode;
    }

    public String getArtistUnicode() {
        return artistUnicode;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator() {
        return creator;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setTags(Tags tags) {
        this.tags = tags;
    }

    public Tags getTags() {
        return tags;
    }

    public void setBeatmapID(int beatmapID) {
        this.beatmapID = beatmapID;
    }

    public int getBeatmapID() {
        return beatmapID;
    }

    public void setBeatmapSetID(int beatmapSetID) {
        this.beatmapSetID = beatmapSetID;
    }

    public int getBeatmapSetID() {
        return beatmapSetID;
    }

    @Override
    public String getTag() {

        return TAG;
    }

    @Override
    public String makeString() {

        StringBuilder sb = new StringBuilder();
        U.appendProperty(sb, PartMetadata.Title, getTitle()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartMetadata.TitleUnicode, getTitleUnicode()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartMetadata.Artist, getArtistUnicode()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartMetadata.Creator, getCreator()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartMetadata.Version, getVersion()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartMetadata.Source, getSource()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartMetadata.Tags, getTags().makeString()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartMetadata.BeatmapID, getBeatmapID()).append(U.NEXT_LINE);
        U.appendProperty(sb, PartMetadata.BeatmapSetID, getBeatmapSetID()).append(U.NEXT_LINE);
        return sb.toString();
    }
}
