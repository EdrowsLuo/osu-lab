package com.edlplan.nso.ruleset.std.game;

import com.edlplan.nso.ruleset.std.beatmap.StdBeatmap;
import com.edlplan.nso.ruleset.std.objects.v2.raw.StdGameObject;
import com.edlplan.framework.math.Vec2;

public abstract class WorkingStdGameObject<T extends StdGameObject> {

    private final T gameObject;

    private final StdBeatmap beatmap;

    private final StdGameField gameField;

    private int comboIndex = 0;

    private boolean groupEnd = false;

    public WorkingStdGameObject(T gameObject, StdGameField gameField) {
        this.beatmap = gameField.beatmap;
        this.gameField = gameField;
        this.gameObject = gameObject;
    }

    public double getTimePreempt() {
        return gameObject.getTimePreempt(beatmap);
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

    public double getHideTime() {
        return getEndTime();
    }

    public double getFadeInDuration() {
        return getGameObject().getFadeInDuration(getBeatmap());
    }

    public StdBeatmap getBeatmap() {
        return beatmap;
    }

    public StdGameField getGameField() {
        return gameField;
    }

    public abstract void applyToGameField();
}
