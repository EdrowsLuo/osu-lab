package com.edplan.nso.ruleset.std.game;

import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.media.bass.BassChannel;
import com.edplan.framework.resource.AResource;
import com.edplan.framework.resource.Skin;
import com.edplan.nso.NsoCore;
import com.edplan.nso.NsoCoreBased;
import com.edplan.nso.difficulty.DifficultyUtil;
import com.edplan.nso.ruleset.base.game.GameObject;
import com.edplan.nso.ruleset.base.game.World;
import com.edplan.nso.ruleset.base.game.judge.CursorData;
import com.edplan.nso.ruleset.base.game.paint.GroupDrawObjectWithSchedule;
import com.edplan.nso.ruleset.base.game.paint.TextureQuadObject;
import com.edplan.nso.ruleset.std.StdRuleset;
import com.edplan.nso.ruleset.std.StdSkin;
import com.edplan.nso.ruleset.std.beatmap.StdBeatmap;
import com.edplan.nso.ruleset.std.game.drawables.ApproachCircle;
import com.edplan.nso.ruleset.std.game.drawables.CirclePiece;
import com.edplan.nso.ruleset.std.objects.v2.StdCircle;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class StdGameField extends NsoCoreBased{
    public static final float BASE_X = 640;
    public static final float BASE_Y = 480;
    public static final float PADDING_X = 64;
    public static final float PADDING_Y = 48;
    public static final float CANVAS_SIZE_X = BASE_X - 2 * PADDING_X;//512
    public static final float CANVAS_SIZE_Y = BASE_Y - 2 * PADDING_Y;//384

    World world;

    GroupDrawObjectWithSchedule backgroundEffectLayer = new GroupDrawObjectWithSchedule();
    GroupDrawObjectWithSchedule followPointsLayer = new GroupDrawObjectWithSchedule();
    GroupDrawObjectWithSchedule hitobjectLayer = new GroupDrawObjectWithSchedule();
    GroupDrawObjectWithSchedule approachCircleLayer = new GroupDrawObjectWithSchedule();
    GroupDrawObjectWithSchedule topEffectLayer = new GroupDrawObjectWithSchedule();

    public StdGameField(NsoCore core) {
        super(core);
        world = new World(getContext());
        world.onLoadConfig(new World.WorldConfig() {{
            judgeTypes.add(CursorData.class);
        }});

        /* 设置坐标转换方式  */
        world.setOnConfigDrawArea(rect -> {
            float scale = Math.max(StdGameField.BASE_X / rect.getWidth(), StdGameField.BASE_Y / rect.getHeight());
            Vec2 startOffset = rect.getCenter()
                    .minus(StdGameField.BASE_X * 0.5f / scale, StdGameField.BASE_Y * 0.5f / scale);

            world.setOnDrawStart(canvas -> canvas.translate(startOffset.x, startOffset.y).expendAxis(scale).translate(PADDING_X, PADDING_Y));
            world.setMotionEventDec(event -> event.eventPosition.minus(startOffset).zoom(scale));
        });

        world.getPaintWorld().addDrawObjects(
                backgroundEffectLayer,
                followPointsLayer,
                hitobjectLayer,
                approachCircleLayer,
                topEffectLayer);
    }

    public World load(StdBeatmap beatmap, AResource dir, JSONObject config) {

        try {
            world.setSong(BassChannel.createStreamFromResource(dir, beatmap.getGeneral().getAudioFilename()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        StdRuleset ruleset = (StdRuleset) getCore().getRulesetById(StdRuleset.ID_NAME);
        Skin skin = ruleset.getStdSkin();

        List<GameObject> gameObjects = beatmap.getAllHitObjects();
        final int size = gameObjects.size();
        final float scale = DifficultyUtil.stdCircleSizeScale(beatmap.getDifficulty().getCircleSize());
        for (int i = 0; i < size; i++) {
            GameObject object = gameObjects.get(i);
            if (object instanceof StdCircle) {
                StdCircle stdCircle = (StdCircle) object;
                if (!stdCircle.isNewCombo()) {
                    if (i == 0) {
                        throw new RuntimeException("第一个开始的物件不能不是new combo");
                    } else {
                        //TODO : 添加follow points
                    }
                }

                CirclePiece circlePiece = new CirclePiece();
                circlePiece.initialTexture(
                        skin.getTexture(StdSkin.hitcircle),
                        skin.getTexture(StdSkin.hitcircleoverlay));
                circlePiece.initialBaseScale(scale);
                circlePiece.initialFadeInAnim(
                        stdCircle.getTime() - stdCircle.getTimePreempt(beatmap),
                        stdCircle.getFadeInDuration(beatmap));
                circlePiece.position.set(stdCircle.getX(), stdCircle.getY());

                ApproachCircle approachCircle = new ApproachCircle();
                approachCircle.initialApproachCircleTexture(skin.getTexture(StdSkin.approachcircle));
                approachCircle.initialBaseScale(scale);
                approachCircle.initialApproachAnim(
                        stdCircle.getTime() - stdCircle.getTimePreempt(beatmap),
                        stdCircle.getTimePreempt(beatmap),
                        stdCircle.getFadeInDuration(beatmap));
                approachCircle.position.set(stdCircle.getX(), stdCircle.getY());

                hitobjectLayer.addEvent(
                        stdCircle.getTime() - stdCircle.getTimePreempt(beatmap),
                        () -> {
                            hitobjectLayer.attachBehind(approachCircle);
                            hitobjectLayer.attachBehind(circlePiece);
                        });
                hitobjectLayer.addEvent(stdCircle.getTime(), () -> {
                    circlePiece.expire(stdCircle.getTime());
                    approachCircle.expire(stdCircle.getTime());
                });
            }
        }

        world.load();
        return world;
    }
}
