package com.edplan.nso.ruleset.std.game;

import com.edplan.framework.math.Vec2;
import com.edplan.framework.media.bass.BassChannel;
import com.edplan.framework.resource.AResource;
import com.edplan.framework.resource.Skin;
import com.edplan.nso.NsoCore;
import com.edplan.nso.NsoCoreBased;
import com.edplan.nso.difficulty.DifficultyUtil;
import com.edplan.nso.ruleset.base.game.GameObject;
import com.edplan.nso.ruleset.base.game.World;
import com.edplan.nso.ruleset.base.game.build.BuildContext;
import com.edplan.nso.ruleset.base.game.judge.CursorData;
import com.edplan.nso.ruleset.base.game.judge.CursorTestObject;
import com.edplan.nso.ruleset.base.game.paint.CursorLayer;
import com.edplan.nso.ruleset.base.game.paint.GroupDrawObjectWithSchedule;
import com.edplan.nso.ruleset.base.game.paint.TextureQuadObject;
import com.edplan.nso.ruleset.std.StdRuleset;
import com.edplan.nso.ruleset.std.StdSkin;
import com.edplan.nso.ruleset.std.beatmap.StdBeatmap;
import com.edplan.nso.ruleset.std.game.drawables.ApproachCircle;
import com.edplan.nso.ruleset.std.game.drawables.TestFollowPoints;
import com.edplan.nso.ruleset.std.objects.v2.StdCircle;
import com.edplan.nso.ruleset.std.objects.v2.StdGameObject;
import com.edplan.nso.ruleset.std.objects.v2.StdSlider;
import com.edplan.nso.ruleset.std.objects.v2.StdSpinner;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StdGameField extends NsoCoreBased {
    public static final String KEY_STD_GAME_FIELD = "STD_GAME_FIELD";

    public static final float BASE_X = 640;
    public static final float BASE_Y = 480;
    public static final float PADDING_X = 64;
    public static final float PADDING_Y = 48;
    public static final float CANVAS_SIZE_X = BASE_X - 2 * PADDING_X;//512
    public static final float CANVAS_SIZE_Y = BASE_Y - 2 * PADDING_Y;//384

    public StdRuleset ruleset = (StdRuleset) getCore().getRulesetById(StdRuleset.ID_NAME);
    public Skin skin = ruleset.getStdSkin();

    public World world;
    public GroupDrawObjectWithSchedule backgroundEffectLayer = new GroupDrawObjectWithSchedule();
    public GroupDrawObjectWithSchedule followPointsLayer = new GroupDrawObjectWithSchedule();
    public GroupDrawObjectWithSchedule sliderLayer = new GroupDrawObjectWithSchedule();
    public GroupDrawObjectWithSchedule hitobjectLayer = new GroupDrawObjectWithSchedule();
    public GroupDrawObjectWithSchedule approachCircleLayer = new GroupDrawObjectWithSchedule();
    public GroupDrawObjectWithSchedule topEffectLayer = new GroupDrawObjectWithSchedule();
    public CursorLayer cursorLayer;

    public DifficultyUtil.BuiltDifficultyHelper difficultyHelper;
    public float globalScale;
    public StdBeatmap beatmap;

    public CursorTestObject cursorTestObject;

    public BuildContext buildContext = new BuildContext();

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
                sliderLayer,
                hitobjectLayer,
                approachCircleLayer,
                topEffectLayer);

        buildContext.setProperty(KEY_STD_GAME_FIELD, this);
        buildContext.setSkin(skin);
    }

    public World load(StdBeatmap beatmap, AResource dir, JSONObject config) {
        difficultyHelper = new DifficultyUtil.BuiltDifficultyHelper(
                DifficultyUtil.DifficultyHelper.StdDifficulty,
                beatmap.getDifficulty().getOverallDifficulty());
        this.beatmap = beatmap;

        try {
            world.setSong(BassChannel.createStreamFromResource(dir, beatmap.getGeneral().getAudioFilename()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        globalScale = DifficultyUtil.stdCircleSizeScale(beatmap.getDifficulty().getCircleSize()) * 1.3f;


        List<GameObject> gameObjects = beatmap.getAllHitObjects();
        final int size = gameObjects.size();
        List<WorkingStdGameObject<?>> workingStdGameObjects = new ArrayList<>(gameObjects.size());

        cursorTestObject = new CursorTestObject();
        world.getJudgeWorld().addJudgeObject(cursorTestObject);

        int comboIndex = 0;

        for (int i = 0; i < size; i++) {
            StdGameObject object = (StdGameObject) gameObjects.get(i);
            if (!object.isNewCombo()) {
                comboIndex++;
                if (i == 0) {
                    throw new RuntimeException("第一个开始的物件不能不是new combo");
                }
            } else {
                comboIndex = 1;
                if (i != 0) {
                    workingStdGameObjects.get(i - 1).setGroupEnd(true);
                }
            }

            if (object instanceof StdCircle) {
                workingStdGameObjects.add(new WorkingStdHitCircle((StdCircle) object, this));
            } else if (object instanceof StdSlider) {
                workingStdGameObjects.add(new WorkingStdSlider((StdSlider) object, this));
            } else if (object instanceof StdSpinner) {
                workingStdGameObjects.add(new WorkingStdSpinner((StdSpinner) object, this));
            } else {
                throw new RuntimeException("错误的物件类型 " + object.getClass());
            }
            workingStdGameObjects.get(i).setComboIndex(comboIndex);
        }

        for (WorkingStdGameObject object : workingStdGameObjects) {
            object.applyToGameField();
        }

        for (int i = 1; i < size; i++) {
            WorkingStdGameObject object = workingStdGameObjects.get(i);
            if (object.getComboIndex() != 1) {
                TestFollowPoints.addFollowPoints(
                        this,
                        globalScale,
                        workingStdGameObjects.get(i - 1),
                        object
                );
            }
        }

        /*TextureQuadObject cursor = new TextureQuadObject();
        cursor.sprite.setTextureAndSize(skin.getTexture(StdSkin.cursor));
        cursor.sprite.position = cursorTestObject.holders[0].pos;
        cursor.sprite.enableScale().scale.set(0.5f * globalScale);
        topEffectLayer.attachFront(cursor);*/

        cursorLayer = new CursorLayer(cursorTestObject, skin.getTexture(StdSkin.cursor), skin.getTexture(StdSkin.cursortrail), globalScale * 0.5f);
        world.getPaintWorld().addDrawObject(cursorLayer);

        world.load();
        return world;
    }

    public ApproachCircle buildApprochCircle(StdGameObject object) {
        ApproachCircle approachCircle = new ApproachCircle();
        approachCircle.initialApproachCircleTexture(skin.getTexture(StdSkin.approachcircle));
        approachCircle.initialBaseScale(globalScale);
        approachCircle.initialApproachAnim(
                object.getTime() - object.getTimePreempt(beatmap),
                object.getTimePreempt(beatmap),
                object.getTimePreempt(beatmap));
        approachCircle.position.set(object.getX(), object.getY());
        return approachCircle;
    }

    protected void addHitEffect(StdScore.HitLevel level, double time, float x, float y, float scale, Skin skin) {
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
