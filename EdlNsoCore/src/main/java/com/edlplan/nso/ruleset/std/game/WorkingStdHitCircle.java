package com.edlplan.nso.ruleset.std.game;

import com.edlplan.nso.ruleset.base.game.judge.AreaHitObject;
import com.edlplan.nso.ruleset.base.game.judge.HitArea;
import com.edlplan.nso.ruleset.base.game.judge.HitWindow;
import com.edlplan.nso.ruleset.std.game.drawables.ApproachCircle;
import com.edlplan.nso.ruleset.std.game.drawables.CirclePiece;
import com.edlplan.nso.ruleset.std.objects.v2.raw.StdCircle;
import com.edlplan.nso.ruleset.std.objects.v2.raw.StdGameObject;

public class WorkingStdHitCircle extends WorkingStdGameObject<StdCircle> {

    CirclePiece circlePiece;

    ApproachCircle approachCircle;

    StdGameField gameField;

    public WorkingStdHitCircle(StdCircle gameObject, StdGameField gameField) {
        super(gameObject, gameField);
        this.gameField = gameField;
    }

    @Override
    public void applyToGameField() {

        circlePiece = new CirclePiece() {{

            CirclePiece.DefaultStyle.apply(this, gameField.buildContext);

            position.set(getGameObject().getX(), getGameObject().getY());

            initialFadeInAnim(
                    getShowTime(),
                    getFadeInDuration());

        }};


        approachCircle = gameField.buildApprochCircle(getGameObject());

        gameField.hitobjectLayer.scheduleAttachBehind(getShowTime(), circlePiece);
        gameField.approachCircleLayer.scheduleAttachBehind(getShowTime(), approachCircle);

        AreaHitObject areaHitObject = new AreaHitObject() {{

            separateJudge = true;

            hitWindow = HitWindow.interval(getGameObject().getTime(), gameField.difficultyHelper.hitWindowFor50());

            area = HitArea.circle(getGameObject().getX(), getGameObject().getY(), StdGameObject.BASE_OBJECT_SIZE / 2 * gameField.globalScale);

            onHit = WorkingStdHitCircle.this::onHit;

            onTimeOut = WorkingStdHitCircle.this::onTimeOut;

        }};

        gameField.world.getJudgeWorld().addJudgeObject(areaHitObject);
    }

    protected void onHit(double time, float x, float y) {
        circlePiece.postOperation(() -> {
            approachCircle.detach();
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
