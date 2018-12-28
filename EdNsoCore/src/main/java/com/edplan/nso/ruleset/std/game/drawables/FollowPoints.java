package com.edplan.nso.ruleset.std.game.drawables;

import com.edplan.framework.math.FMath;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.utils.FloatRef;
import com.edplan.nso.ruleset.base.game.paint.TextureQuadObject;
import com.edplan.nso.ruleset.std.StdSkin;
import com.edplan.nso.ruleset.std.game.StdGameField;
import com.edplan.nso.ruleset.std.game.WorkingStdGameObject;

public class FollowPoints {

    public static int PointDistance = 32;

    public static void addFollowPoints(
            StdGameField gameField,
            float baseScale,
            WorkingStdGameObject first, WorkingStdGameObject next) {

        Vec2 startPosition = first.getEndPosition();
        Vec2 endPosition = next.getStartPosition();
        Vec2 distanceVector = new Vec2(
                endPosition.x - startPosition.x,
                endPosition.y - startPosition.y
        );

        int distance = (int) distanceVector.length();

        float rotation = FMath.atan2(distanceVector.y, distanceVector.x);

        double startTime = first.getEndTime();
        double endTime = next.getStartTime();
        double duration = endTime - startTime;


        for (int d = (int) (PointDistance * 1.5); d < distance - PointDistance * 1.5; d += PointDistance) {

            float fraction = (float) d / distance;
            Vec2 pointPosition = new Vec2(
                    startPosition.x + distanceVector.x * fraction,
                    startPosition.y + distanceVector.y * fraction
            );

            TextureQuadObject followPoint = new TextureQuadObject();
            followPoint.sprite.enableScale().enableRotation();
            followPoint.sprite.size.zoom(baseScale);
            followPoint.sprite.rotation.value = rotation;

            followPoint.sprite.setTextureAndSize(gameField.skin.getTextureList(StdSkin.followpoint).get(5));
            followPoint.sprite.alpha.value = 0;
            followPoint.sprite.position.set(pointPosition);

            addTask(followPoint, startTime, endTime);

            gameField.followPointsLayer.addEvent(
                    startTime,
                    () -> gameField.followPointsLayer.attachBehind(followPoint)
            );
            gameField.followPointsLayer.addEvent(duration, followPoint::detach);
        }
    }

    private static void addTask(TextureQuadObject fp, double startTime, double endTime) {

        double helfDuration = (endTime - startTime) / 2;

        fp.addAnimTask(
                startTime,
                helfDuration,
                p -> fp.sprite.alpha.value = (float) p
        );
        fp.addAnimTask(
                startTime + helfDuration,
                helfDuration,
                p -> fp.sprite.alpha.value = (float) (1 - p)
        );
    }

}
