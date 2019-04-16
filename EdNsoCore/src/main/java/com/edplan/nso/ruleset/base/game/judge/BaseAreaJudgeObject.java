package com.edplan.nso.ruleset.base.game.judge;

import com.edplan.framework.utils.interfaces.Consumer;

public abstract class BaseAreaJudgeObject extends JudgeObject{

    protected boolean isHit = false;

    public HitWindow hitWindow;

    public PositionChecker area;

    public AreaHitObject.OnHit onHit;

    public Consumer<Double> onTimeOut;

    @FunctionalInterface
    public interface OnHit{
        void onHit(double time, float x, float y);
    }

    @Override
    protected void onRelease() {
        if (!isHit) {
            onTimeOut.consume(hitWindow.getEnd());
        }
    }

    protected final void hit(double time, float x, float y) {
        if (isHit) return;
        isHit = true;
        onHit.onHit(time, x, y);
        releaseObject();
    }

    @Override
    public double getJudgeFailedTime() {
        return hitWindow.getEnd();
    }

    @Override
    public double getStartJudgeTime() {
        return hitWindow.getStart();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends JudgeData>[] getListeningData() {
        return new Class[]{CursorData.class};
    }

}
