package com.edplan.nso.ruleset.std.playing.drawable;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.timing.PreciseTimeline;
import com.edplan.nso.difficulty.DifficultyUtil;
import com.edplan.nso.ruleset.base.playing.Judgment;
import com.edplan.nso.ruleset.base.playing.PlayingBeatmap;
import com.edplan.nso.ruleset.base.playing.drawable.DrawableHitObject;
import com.edplan.nso.ruleset.std.objects.StdHitObject;
import com.edplan.nso.ruleset.std.playing.drawable.piece.BasePiece;
import com.edplan.framework.graphics.opengl.BaseCanvas;

public class DrawableStdHitObject extends DrawableHitObject {
    private float baseSize = 64;

    private Vec2 origin = new Vec2();

    private StdHitObject hitObject;

    private int showTime;

    private int timePreempt;

    private int timeFadein;

    private float alpha = 1;

    private PreciseTimeline timeLine;

    private boolean finished = false;

    private Color4 accentColor = Color4.rgba(1, 1, 1, 1);

    private int comboIndex = 1;

    private float stackHeight;

    private float baseScale = 1;

    public DrawableStdHitObject(MContext c, StdHitObject obj) {
        super(c);
        hitObject = obj;
        setOrigin(new Vec2(obj.getStartX(), obj.getStartY()));
    }

    public float getStackOffset() {
        return getStackHeight() * baseScale * -6.4f;
    }

    public void setStackHeight(float stackHeight) {
        this.stackHeight = stackHeight;
    }

    public float getStackHeight() {
        return stackHeight;
    }

    public void setComboIndex(int comboIndex) {
        this.comboIndex = comboIndex;
    }

    public int getComboIndex() {
        return comboIndex;
    }

    public void setShowTime(int showTime) {
        this.showTime = showTime;
    }

    public int getObjStartTime() {
        return getHitObject().getStartTime();
    }

    public int getObjPredictedEndTime() {
        return getObjStartTime();
    }

    public Vec2 getStackedStartPoint() {
        return getStartPoint().add(getStackOffset());
    }

    public Vec2 getStackedEndPoint() {
        return getEndPoint().add(getStackOffset());
    }

    public Vec2 getStartPoint() {
        return new Vec2(getHitObject().getStartX(), getHitObject().getStartY());
    }

    public Vec2 getEndPoint() {
        return new Vec2(getHitObject().getStartX(), getHitObject().getStartY());
    }

    public void setAccentColor(Color4 accentColor) {
        this.accentColor.set(accentColor);
    }

    public Color4 getAccentColor() {
        return accentColor;
    }

    public StdHitObject getHitObject() {
        return hitObject;
    }

    public void setTimePreempt(int timePreempt) {
        this.timePreempt = timePreempt;
    }

    public int getTimePreempt() {
        return timePreempt;
    }

    public void setTimeFadein(int timeFadein) {
        this.timeFadein = timeFadein;
    }

    public int getTimeFadein() {
        return timeFadein;
    }

    public void setBaseSize(float baseSize) {
        this.baseSize = baseSize;
        baseScale = baseSize / 64;
    }

    public float getBaseSize() {
        return baseSize;
    }

    public void setOrigin(Vec2 origin) {
        this.origin.set(origin);
    }

    public Vec2 getOrigin() {
        return origin;
    }

    /**
     * 通过HitObject无法确定的属性在这里设置（如和难度相关的）
     */
    public void applyDefault(PlayingBeatmap beatmap) {
        timeLine = beatmap.getTimeLine();
        timePreempt = DifficultyUtil.stdHitObjectTimePreempt(beatmap.getDifficulty().getApproachRate());
        timeFadein = DifficultyUtil.stdHitObjectTimeFadein(beatmap.getDifficulty().getApproachRate());
        baseSize *= DifficultyUtil.stdCircleSizeScale(beatmap.getDifficulty().getCircleSize());
        showTime = hitObject.getStartTime() - timePreempt;
		/*if(!getHitObject().isNewCombo()){
			baseSize*=0.1f;
		}*/
    }

    protected void applyPiece(BasePiece p, PlayingBeatmap beatmap) {
        p.setAccentColor(getAccentColor());
        p.setOrigin(getStackedStartPoint());
        p.setBaseSize(getBaseSize());
        p.setSkin(beatmap.getSkin());
    }

    public void onApplyStackHeight() {

    }

    /**
     * 通知PlayField回收这个HitObject
     */
    public void finish() {
        finished = true;
    }

    public void onFinish() {

    }

    @Override
    public void draw(BaseCanvas canvas) {

    }

    @Override
    public void setAlpha(float a) {

        this.alpha = a;
    }

    @Override
    public float getAlpha() {

        return alpha;
    }

    @Override
    public int getShowTime() {

        return showTime;
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onJudgment(Judgment judgment) {

    }

    @Override
    public PreciseTimeline getTimeLine() {

        return timeLine;
    }

    @Override
    public boolean isFinished() {

        return finished;
    }
}
