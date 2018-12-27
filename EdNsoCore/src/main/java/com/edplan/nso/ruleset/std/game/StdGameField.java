package com.edplan.nso.ruleset.std.game;

import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.math.FMath;
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
import com.edplan.nso.ruleset.base.game.judge.CursorTestObject;
import com.edplan.nso.ruleset.base.game.judge.HitArea;
import com.edplan.nso.ruleset.base.game.judge.HitWindow;
import com.edplan.nso.ruleset.base.game.judge.JudgeData;
import com.edplan.nso.ruleset.base.game.judge.JudgeWorld;
import com.edplan.nso.ruleset.base.game.judge.PositionHitObject;
import com.edplan.nso.ruleset.base.game.paint.GroupDrawObjectWithSchedule;
import com.edplan.nso.ruleset.base.game.paint.TextureQuadObject;
import com.edplan.nso.ruleset.std.StdRuleset;
import com.edplan.nso.ruleset.std.StdSkin;
import com.edplan.nso.ruleset.std.beatmap.StdBeatmap;
import com.edplan.nso.ruleset.std.game.drawables.ApproachCircle;
import com.edplan.nso.ruleset.std.game.drawables.CirclePiece;
import com.edplan.nso.ruleset.std.objects.v2.StdCircle;
import com.edplan.nso.ruleset.std.objects.v2.StdGameObject;

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
            world.setMotionEventDec(event -> {
                event.eventPosition.minus(startOffset).zoom(scale).minus(PADDING_X, PADDING_Y);
                event.time += 50;
            });
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
        JudgeWorld judgeWorld = world.getJudgeWorld();
        Skin skin = ruleset.getStdSkin();
        DifficultyUtil.DifficultyHelper difficultyHelper = DifficultyUtil.DifficultyHelper.StdDifficulty;

        List<GameObject> gameObjects = beatmap.getAllHitObjects();
        final int size = gameObjects.size();
        final float scale = DifficultyUtil.stdCircleSizeScale(beatmap.getDifficulty().getCircleSize()) * 1.3f;



        CursorTestObject cursorTestObject = new CursorTestObject();
        judgeWorld.addJudgeObject(cursorTestObject);

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
                        stdCircle.getTimePreempt(beatmap));
                approachCircle.position.set(stdCircle.getX(), stdCircle.getY());

                hitobjectLayer.addEvent(
                        stdCircle.getTime() - stdCircle.getTimePreempt(beatmap),
                        () -> {
                            hitobjectLayer.attachBehind(approachCircle);
                            hitobjectLayer.attachBehind(circlePiece);
                        });

                PositionHitObject positionHitObject = new PositionHitObject() {{

                    separateJudge = true;

                    hitWindow = HitWindow.interval(
                            stdCircle.getTime(),
                            difficultyHelper.hitWindowFor50(beatmap.getDifficulty().getOverallDifficulty())
                    );

                    area = HitArea.circle(stdCircle.getX(), stdCircle.getY(), StdGameObject.BASE_OBJECT_SIZE / 2 * scale);

                    onHit = (time, x, y) -> {
                        circlePiece.postOperation(()->{
                            approachCircle.expire(time);
                            circlePiece.expire(time);
                        });
                    };

                    onTimeOut = time -> {
                        circlePiece.postOperation(()->{
                            approachCircle.detach();
                            circlePiece.detach();
                            addHitEffect(
                                    StdGameObject.HitLevel.MISS,
                                    time,
                                    stdCircle.getX(), stdCircle.getY(),
                                    scale,
                                    skin);
                        });
                    };

                }};

                judgeWorld.addJudgeObject(positionHitObject);

            }
        }


        TextureQuadObject cursor = new TextureQuadObject();
        cursor.sprite.setTextureAndSize(skin.getTexture(StdSkin.cursor));
        cursor.sprite.position = cursorTestObject.holders[0].pos;
        cursor.sprite.enableScale().scale.set(0.5f * scale);
        topEffectLayer.attachFront(cursor);

        world.load();
        return world;
    }

    protected void addHitCircle(StdCircle circle) {

    }

    protected void addHitEffect(StdGameObject.HitLevel level, double time, float x, float y, float scale, Skin skin) {
        switch (level) {
            case MISS: {

                TextureQuadObject miss = new TextureQuadObject();
                miss.sprite.setTextureAndSize(skin.getTexture(StdSkin.hit0_0));
                miss.sprite.enableScale().enableRotation();
                miss.sprite.size.zoom(scale);
                miss.sprite.position.set(x, y);

                miss.addAnimTask(time, 400, p -> {
                    miss.sprite.alpha.value = (float) (1 - p);
                });
                miss.addTask(time + 400, miss::detach);

                backgroundEffectLayer.attachFront(miss);

            }
            break;
            default:

        }
    }


}
