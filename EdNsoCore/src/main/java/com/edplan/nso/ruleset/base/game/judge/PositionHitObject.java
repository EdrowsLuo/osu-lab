package com.edplan.nso.ruleset.base.game.judge;

import com.edplan.framework.utils.interfaces.Consumer;
import com.edplan.nso.ruleset.base.game.World;

public class PositionHitObject extends JudgeObject {

    //是否是分离判定，即判定时其他点的点击是否可以被记为点击
    protected boolean separateJudge = false;

    private boolean hit = false;

    protected void setSeparateJudge(boolean separateJudge) {
        this.separateJudge = separateJudge;
    }

    public HitWindow hitWindow;

    public PositionChecker area;

    public OnHit onHit;

    public Consumer<Double> onTimeOut;

    @FunctionalInterface
    public interface OnHit{
        void onHit(double time, float x, float y);
    }

    @Override
    protected void onRelease() {
        if (!hit) {
            onTimeOut.consume(hitWindow.getEnd());
        }
    }

    @Override
    public boolean handle(JudgeData data, int subType, World world) {
        CursorData cursorData = (CursorData) data;
        if (separateJudge) {
            CursorData.CursorHolder timeHolder = null;
            CursorData.CursorHolder posHolder = null;
            for (CursorData.CursorHolder holder : cursorData.getCursors()) {
                if (holder.down.empty()) {
                    continue;
                }
                if (timeHolder == null && hitWindow.checkTime(holder.down.time)) {
                    timeHolder = holder;
                }
                if (posHolder == null) {
                    if (area.checkPosition(holder.x, holder.y)) {
                        posHolder = holder;
                    }
                }
            }
            if (timeHolder != null && posHolder != null) {
                hit = true;
                onHit.onHit(timeHolder.down.time, posHolder.x, posHolder.y);
                releaseObject();
                timeHolder.down.consume();
                return !cursorData.hasMoreAction();
            }
        } else {
            for (CursorData.CursorHolder holder : cursorData.getCursors()) {
                if (holder.down.empty()) {
                    continue;
                }
                if (hitWindow.checkTime(holder.down.time) && area.checkPosition(holder.x, holder.y)) {
                    hit = true;
                    onHit.onHit(holder.down.time, holder.x, holder.y);
                    releaseObject();
                    holder.down.consume();
                    return !cursorData.hasMoreAction();
                }
            }
        }
        return false;
    }

    @Override
    public double getJudgeFailedTime() {
        return hitWindow.getEnd();
    }

    @Override
    public double getStartJudgeTime() {
        return hitWindow.getStart();
    }

    @Override
    public Class<? extends JudgeData>[] getListeningData() {
        return new Class[]{CursorData.class};
    }

    @Override
    public int[] getListeningDataSubTypes() {
        return new int[]{CursorData.SUB_ACTION};
    }

}
