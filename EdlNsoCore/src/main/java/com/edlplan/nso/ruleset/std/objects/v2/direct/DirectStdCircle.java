package com.edlplan.nso.ruleset.std.objects.v2.direct;

import com.edlplan.nso.ruleset.std.beatmap.StdBeatmap;
import com.edlplan.nso.ruleset.std.objects.v2.raw.StdCircle;
import com.edlplan.framework.math.Vec2;

public class DirectStdCircle extends DirectStdGameObject{

    private Vec2 position = new Vec2();

    private double size;

    private double hitTime; //the time when the circle should be hit

    private int comboIndex;

    public DirectStdCircle(StdCircle circle, int comboIndex, StdBeatmap beatmap, StdGameProperty property) {
        this(circle.getX(), circle.getY(), circle.getTime(), comboIndex, beatmap, property);
    }

    public DirectStdCircle(float x, float y, double hitTime, int comboIndex, StdBeatmap beatmap, StdGameProperty property) {
        super(beatmap, property);
        size = getSizeAfterCS();
        position.set(x, y);
        this.hitTime = hitTime;
        this.comboIndex = comboIndex;
    }

    public DirectStdCircle(Vec2 position, double hitTime, int comboIndex, StdBeatmap beatmap, StdGameProperty property) {
        this(position.x, position.y, hitTime, comboIndex, beatmap, property);
    }

    public int getComboIndex() {
        return comboIndex;
    }

    public void setComboIndex(int comboIndex) {
        this.comboIndex = comboIndex;
    }

    public double getHitTime() {
        return hitTime;
    }

    public void setHitTime(double hitTime) {
        this.hitTime = hitTime;
    }

    public Vec2 getPosition() {
        return position;
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

}
