package com.edplan.nso.ruleset.std.objects.v2;

import com.edplan.framework.math.Vec2;
import com.edplan.nso.NsoException;
import com.edplan.nso.parser.ParseException;
import com.edplan.nso.ruleset.std.objects.StdPath;
import com.edplan.superutils.U;
import com.edplan.superutils.classes.strings.StringSplitter;

public class StdSlider extends StdHitObject {

    public static final int TYPE_MASK = 2;

    private StdPath stdPath;

    private int repeat;

    private double pixelLength;

    private String edgeHitSounds;

    private String edgeAdditions;

    public StdPath getStdPath() {
        return stdPath;
    }

    public void setStdPath(StdPath stdPath) {
        this.stdPath = stdPath;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public double getPixelLength() {
        return pixelLength;
    }

    public void setPixelLength(double pixelLength) {
        this.pixelLength = pixelLength;
    }

    public String getEdgeHitSounds() {
        return edgeHitSounds;
    }

    public void setEdgeHitSounds(String edgeHitSounds) {
        this.edgeHitSounds = edgeHitSounds;
    }

    public String getEdgeAdditions() {
        return edgeAdditions;
    }

    public void setEdgeAdditions(String edgeAdditions) {
        this.edgeAdditions = edgeAdditions;
    }

    @Override
    public void parseCustomDatas(StringSplitter spl) throws ParseException {
        stdPath = parsePath(new Vec2(getX(), getY()), spl);
        repeat = Integer.parseInt(spl.next());
        pixelLength = Double.parseDouble(spl.next());
        edgeHitSounds = spl.next();
        edgeAdditions = spl.next();
    }
}
