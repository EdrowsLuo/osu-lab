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

    public WorkingStdHitCircle(StdCircle gameObject, StdBeatmap beatmap) {
        super(gameObject, beatmap);
    }

    @Override
    public void applyToGameField(StdGameField gameField) {

        CirclePiece circlePiece = new CirclePiece();
        circlePiece.initialTexture(
                gameField.skin.getTexture(StdSkin.hitcircle),
                gameField.skin.getTexture(StdSkin.hitcircleoverlay));
        circlePiece.initialBaseScale(gameField.globalScale);
        circlePiece.initialFadeInAnim(
                getGameObject().getTime() - getGameObject().getTimePreempt(gameField.beatmap),
                getGameObject().getFadeInDuration(gameField.beatmap));
        circlePiece.position.set(getGameObject().getX(), getGameObject().getY());

        ApproachCircle approachCircle = new ApproachCircle();
        approachCircle.initialApproachCircleTexture(gameField.skin.getTexture(StdSkin.approachcircle));
        approachCircle.initialBaseScale(gameField.globalScale);
        approachCircle.initialApproachAnim(
                getGameObject().getTime() - getGameObject().getTimePreempt(gameField.beatmap),
                getGameObject().getTimePreempt(gameField.beatmap),
                getGameObject().getTimePreempt(gameField.beatmap));
        approachCircle.position.set(getGameObject().getX(), getGameObject().getY());

        gameField.hitobjectLayer.scheduleAttachBehindAll(getShowTime(), approachCircle, circlePiece);

        PositionHitObject positionHitObject = new PositionHitObject() {{

            separateJudge = true;

            hitWindow = HitWindow.interval(
                    getGameObject().getTime(),
                    gameField.difficultyHelper.hitWindowFor50(gameField.beatmap.getDifficulty().getOverallDifficulty())
            );

            area = HitArea.circle(getGameObject().getX(), getGameObject().getY(), StdGameObject.BASE_OBJECT_SIZE / 2 * gameField.globalScale);

            onHit = (time, x, y) -> {
                circlePiece.postOperation(() -> {
                    approachCircle.expire(time);
                    circlePiece.expire(time);
                });
            };

            onTimeOut = time -> {
                circlePiece.postOperation(() -> {
                    approachCircle.detach();
                    circlePiece.detach();
                    gameField.addHitEffect(
                            StdGameObject.HitLevel.MISS,
                            time,
                            getGameObject().getX(), getGameObject().getY(),
                            gameField.globalScale,
                            gameField.skin);
                });
            };

        }};

        gameField.world.getJudgeWorld().addJudgeObject(positionHitObject);
    }
}
