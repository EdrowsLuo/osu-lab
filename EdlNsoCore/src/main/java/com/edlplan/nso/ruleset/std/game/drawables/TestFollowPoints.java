package com.edlplan.nso.ruleset.std.game.drawables;

import com.edlplan.framework.graphics.opengl.objs.GLTexture;
import com.edlplan.framework.math.FMath;
import com.edlplan.framework.math.Vec2;
import com.edlplan.framework.ui.animation.Easing;
import com.edlplan.framework.ui.animation.interpolate.EasingManager;
import com.edlplan.nso.ruleset.base.game.paint.TextureQuadObject;
import com.edlplan.nso.ruleset.std.game.StdGameField;
import com.edlplan.nso.ruleset.std.game.WorkingStdGameObject;

public class TestFollowPoints {

    public static final float PointDistance = 32;

    public static final double Preempt = 200;

    public static void addFollowPoints(
            StdGameField gameField,
            float baseScale,
            WorkingStdGameObject first, WorkingStdGameObject next) {
        //TODO : 添加follow points
        Vec2 start = first.getEndPosition(), end = next.getStartPosition();
        double fadeTime = next.getGameObject().getTimePreempt(gameField.beatmap) - next.getGameObject().getFadeInDuration(gameField.beatmap);
        double startTime = first.getEndTime(),
                endTime = next.getStartTime();
        endTime = Math.max(startTime, endTime);
        float ang = Vec2.calTheta(start, end);
        float distance = Vec2.length(start, end);
        float md = distance - PointDistance;

        float curPos = PointDistance * 1.5f;
        Vec2 moveVec = end.copy().minus(start).toNormal().zoom(PointDistance / 2);

        /*startTime += Preempt * 1.5;
        startTime = Math.min(startTime, endTime);*/

        while (curPos < md) {
            float progrees = curPos / distance;
            float time = (float) FMath.linear(progrees, startTime, endTime);
            Vec2 endPos = Vec2.onLine(start, end, progrees);

            TextureQuadObject quadObject = new TextureQuadObject();
            quadObject.sprite.setTextureAndSize(GLTexture.White);
            quadObject.sprite.size.set(10 * baseScale, 5 * baseScale);
            quadObject.sprite.position.set(endPos.x - moveVec.x, endPos.y - moveVec.y);
            quadObject.sprite.alpha.value = 0;
            quadObject.sprite.enableScale().scale.set(0.5f);
            quadObject.sprite.enableRotation().rotation.value = ang;

            float moveScale = 1 + progrees * 3;

            quadObject.addAnimTask(time - fadeTime * 2, fadeTime * 2, p -> {
                float pp = (float) EasingManager.apply(Easing.OutQuad, 1 - p);
                quadObject.sprite.position.set(endPos.x - moveVec.x * moveScale * pp, endPos.y - moveVec.y * moveScale * pp);
                quadObject.sprite.alpha.value = 1 - pp;
                quadObject.sprite.scale.set((2 - pp) / 2);
            });

            quadObject.addAnimTask(time, fadeTime, p -> {
                float pp = (float) (1 - p);
                quadObject.sprite.alpha.value = pp;
            });

            gameField.followPointsLayer.addEvent(time - fadeTime * 2, () -> gameField.followPointsLayer.attachBehind(quadObject));
            gameField.followPointsLayer.addEvent(time + fadeTime, quadObject::detach);

            curPos += PointDistance;
        }
    }
}
