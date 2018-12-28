package com.edplan.nso.ruleset.std.game;

import com.edplan.framework.math.Vec2;
import com.edplan.nso.ruleset.std.beatmap.StdBeatmap;
import com.edplan.nso.ruleset.std.objects.v2.StdGameObject;

public abstract class WorkingStdGameObject<T extends StdGameObject> {

    private final T gameObject;

    private final StdBeatmap beatmap;

    private int comboIndex = 0;

    private boolean groupEnd = false;

    public WorkingStdGameObject(T gameObject, StdBeatmap beatmap) {
        this.beatmap = beatmap;
        this.gameObject = gameObject;
    }

    public boolean isGroupEnd() {
        return groupEnd;
    }

    public void setGroupEnd(boolean groupEnd) {
        this.groupEnd = groupEnd;
    }

    public void setComboIndex(int comboIndex) {
        this.comboIndex = comboIndex;
    }

    public int getComboIndex() {
        return comboIndex;
    }

    public T getGameObject() {
        return gameObject;
    }

    public Vec2 getStartPosition() {
        return new Vec2(gameObject.getX(), gameObject.getY());
    }

    public Vec2 getEndPosition() {
        return getStartPosition();
    }

    public double getStartTime() {
        return gameObject.getTime();
    }

    public double getShowTime() {
        return getStartTime() - getGameObject().getTimePreempt(beatmap);
    }

    public double getEndTime() {
        return getStartTime();
    }


    public abstract void applyToGameField(StdGameField gameField);
}
