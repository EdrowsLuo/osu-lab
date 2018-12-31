package com.edplan.nso.ruleset.std.game;

import com.edplan.nso.ruleset.base.game.judge.HitArea;
import com.edplan.nso.ruleset.base.game.judge.HitWindow;
import com.edplan.nso.ruleset.base.game.judge.PositionHitObject;
import com.edplan.nso.ruleset.std.StdSkin;
import com.edplan.nso.ruleset.std.beatmap.StdBeatmap;
import com.edplan.nso.ruleset.std.game.drawables.ApproachCircle;
import com.edplan.nso.ruleset.std.game.drawables.CirclePiece;

import com.edplan.nso.ruleset.std.objects.v2.StdCircle;
import com.edplan.nso.ruleset.std.objects.v2.StdGameObject;

public class WorkingStdHitCircle extends WorkingStdGameObject<StdCircle> {

    CirclePiece circlePiece;

    ApproachCircle approachCircle;

    StdGameField gameField;

    public WorkingStdHitCircle(StdCircle gameObject, StdBeatmap beatmap) {
        super(gameObject, beatmap);
    }

    @Override
    public void applyToGameField(StdGameField gameField) {

        this.gameField = gameField;

        circlePiece = new CirclePiece();
        circlePiece.initialTexture(
                gameField.skin.getTexture(StdSkin.hitcircle),
                gameField.skin.getTexture(StdSkin.hitcircleoverlay));
        circlePiece.initialBaseScale(gameField.globalScale);
        circlePiece.initialFadeInAnim(
                getGameObject().getTime() - getTimePreempt(),
                getGameObject().getFadeInDuration(gameField.beatmap));
        circlePiece.position.set(getGameObject().getX(), getGameObject().getY());

        approachCircle = gameField.buildApprochCircle(getGameObject());

        gameField.hitobjectLayer.scheduleAttachBehind(getShowTime(), circlePiece);
        gameField.approachCircleLayer.scheduleAttachBehind(getShowTime(), approachCircle);

        PositionHitObject positionHitObject = new PositionHitObject() {{

            separateJudge = true;

            hitWindow = HitWindow.interval(getGameObject().getTime(), gameField.difficultyHelper.hitWindowFor50());

            area = HitArea.circle(getGameObject().getX(), getGameObject().getY(), StdGameObject.BASE_OBJECT_SIZE / 2 * gameField.globalScale);

            onHit = WorkingStdHitCircle.this::onHit;

            onTimeOut = WorkingStdHitCircle.this::onTimeOut;

        }};

        gameField.world.getJudgeWorld().addJudgeObject(positionHitObject);
    }

    protected void onHit(double time, float x, float y) {
        circlePiece.postOperation(() -> {
            approachCircle.expire(time);
            circlePiece.expire(time);
        });
    }

    protected void onTimeOut(double time) {
        circlePiece.postOperation(() -> {
            approachCircle.detach();
            circlePiece.detach();
            gameField.addHitEffect(
                    StdScore.HitLevel.MISS,
                    time,
                    getGameObject().getX(), getGameObject().getY(),
                    gameField.globalScale,
                    gameField.skin);
        });
    }
}
