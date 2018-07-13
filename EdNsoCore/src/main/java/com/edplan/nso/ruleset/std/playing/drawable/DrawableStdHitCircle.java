package com.edplan.nso.ruleset.std.playing.drawable;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.FMath;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.ui.animation.precise.BasePreciseAnimation;
import com.edplan.nso.ruleset.base.playing.Judgment;
import com.edplan.nso.ruleset.base.playing.PlayingBeatmap;
import com.edplan.nso.ruleset.std.objects.StdHitCircle;
import com.edplan.nso.ruleset.std.objects.StdHitObject;
import com.edplan.nso.ruleset.std.playing.drawable.interfaces.IHasApproachCircle;
import com.edplan.nso.ruleset.std.playing.drawable.piece.ApproachCircle;
import com.edplan.nso.ruleset.std.playing.drawable.piece.BasePiece;
import com.edplan.nso.ruleset.std.playing.drawable.piece.HitCirclePiece;
import com.edplan.nso.ruleset.std.playing.judgment.StdJudgment;
import com.edplan.framework.utils.MLog;
import com.edplan.nso.ruleset.std.playing.drawable.piece.ComboIndexPiece;
import com.edplan.framework.graphics.opengl.BaseCanvas;

public class DrawableStdHitCircle extends DrawableStdHitObject implements IHasApproachCircle {
    private HitCirclePiece circlePiece;

    private ComboIndexPiece comboPiece;

    private ApproachCircle approachCircle;

    public DrawableStdHitCircle(MContext c, StdHitCircle obj) {
        super(c, obj);
    }

    /**
     * 测试用的构造方法，之后大概率被弃用
     */
    public DrawableStdHitCircle(MContext c, StdHitObject obj) {
        super(c, obj);
    }

    @Override
    public ApproachCircle getApproachCircle() {

        return approachCircle;
    }

    @Override
    public void applyDefault(PlayingBeatmap beatmap) {

        super.applyDefault(beatmap);
        circlePiece = new HitCirclePiece(getContext(), beatmap.getTimeLine());
        applyPiece(circlePiece, beatmap);
        approachCircle = new ApproachCircle(getContext(), beatmap.getTimeLine());
        applyPiece(approachCircle, beatmap);
        comboPiece = new ComboIndexPiece(getContext(), beatmap.getTimeLine(), getComboIndex());
        applyPiece(comboPiece, beatmap);
    }

    @Override
    public void onApplyStackHeight() {

        super.onApplyStackHeight();
        circlePiece.setOrigin(getStackedStartPoint());
        comboPiece.setOrigin(getStackedStartPoint());
        approachCircle.setOrigin(getStackedStartPoint());
    }

    @Override
    public void draw(BaseCanvas canvas) {

        super.draw(canvas);
        circlePiece.draw(canvas);
        comboPiece.draw(canvas);
    }

    @Override
    public void setAlpha(float a) {

        super.setAlpha(a);
        if (circlePiece != null) circlePiece.setAlpha(a);
        if (approachCircle != null) approachCircle.setAlpha(a);
        if (comboPiece != null) comboPiece.setAlpha(a);
    }

    @Override
    public void setBaseSize(float baseSize) {

        super.setBaseSize(baseSize);
        //circlePiece.setBaseSize(baseSize);
    }

    @Override
    public void setOrigin(Vec2 origin) {

        super.setOrigin(origin);
    }

    @Override
    public void onShow() {

        super.onShow();
        circlePiece.fadeIn(this);
        comboPiece.fadeIn(this);
        approachCircle.fadeAndScaleIn(this);
    }

    @Override
    public boolean isFinished() {

        return circlePiece.isFinished();
    }

    @Override
    public void onFinish() {

        super.onFinish();
        circlePiece.onFinish();
    }

    @Override
    public void onJudgment(Judgment judgment) {

        super.onJudgment(judgment);
        StdJudgment judg = (StdJudgment) judgment;
        switch (judg.getLevel()) {
            case S50:
            case S100:
            case S300:

                break;
            case Miss:

                break;
            case None:
                break;
            default:
                break;
        }
    }
}
