package com.edlplan.nso.ruleset.std.game.v2;

import com.edlplan.nso.ruleset.base.game.World;
import com.edlplan.nso.ruleset.base.game.judge.CursorData;
import com.edlplan.nso.ruleset.std.objects.v2.direct.DirectStdBeatmap;
import com.edlplan.framework.math.RectF;
import com.edlplan.framework.math.Vec2;
import com.edlplan.framework.resource.Skin;
import com.edlplan.framework.utils.interfaces.Consumer;
import com.edlplan.nso.NsoCore;
import com.edlplan.nso.NsoCoreBased;
import com.edlplan.nso.ruleset.base.game.judge.CursorTestObject;
import com.edlplan.nso.ruleset.base.game.paint.CursorLayer;
import com.edlplan.nso.ruleset.base.game.paint.GroupDrawObjectWithSchedule;
import com.edlplan.nso.ruleset.std.StdRuleset;
import com.edlplan.nso.ruleset.std.StdSkin;

public class StdGameField extends NsoCoreBased {

    public static final float BASE_X = 640;
    public static final float BASE_Y = 480;
    public static final float PADDING_X = 64;
    public static final float PADDING_Y = 48;
    public static final float CANVAS_SIZE_X = BASE_X - 2 * PADDING_X;//512
    public static final float CANVAS_SIZE_Y = BASE_Y - 2 * PADDING_Y;//384

    public World world;
    public GroupDrawObjectWithSchedule backgroundEffectLayer = new GroupDrawObjectWithSchedule();
    public GroupDrawObjectWithSchedule followPointsLayer = new GroupDrawObjectWithSchedule();
    public GroupDrawObjectWithSchedule sliderLayer = new GroupDrawObjectWithSchedule();
    public GroupDrawObjectWithSchedule hitobjectLayer = new GroupDrawObjectWithSchedule();
    public GroupDrawObjectWithSchedule approachCircleLayer = new GroupDrawObjectWithSchedule();
    public GroupDrawObjectWithSchedule topEffectLayer = new GroupDrawObjectWithSchedule();
    public CursorLayer cursorLayer;

    private CursorTestObject cursorTestObject;

    private StdRuleset ruleset;

    public StdGameField(NsoCore core) {
        super(core);
        ruleset = core.getRulesetById(StdRuleset.ID_NAME);
    }

    public StdRuleset getRuleset() {
        return ruleset;
    }

    public Skin getSkin() {
        return getRuleset().getStdSkin();
    }

    public StdGameField load(DirectStdBeatmap beatmap) {
        Skin skin = getSkin();
        world = new World(getContext());
        world.onLoadConfig(new World.WorldConfig() {{
            judgeTypes.add(CursorData.class);
        }});

        /* 设置坐标转换方式  */
        world.setOnConfigDrawArea(this::onConfigDrawArea);

        /* 设置基础绘制层 */
        setUpDrawLayers();




        /* 设置测试时光标绘制 */
        cursorTestObject = new CursorTestObject();
        world.getJudgeWorld().addJudgeObject(cursorTestObject);

        cursorLayer = new CursorLayer(cursorTestObject, skin.getTexture(StdSkin.cursor), skin.getTexture(StdSkin.cursortrail), 0.5f);
        world.getPaintWorld().addDrawObject(cursorLayer);
        return this;
    }

    protected void setUpDrawLayers() {
        world.getPaintWorld().addDrawObjects(
                backgroundEffectLayer,
                followPointsLayer,
                sliderLayer,
                hitobjectLayer,
                approachCircleLayer,
                topEffectLayer);
    }

    protected void onConfigDrawArea(RectF rect) {
        float scale = Math.max(com.edlplan.nso.ruleset.std.game.StdGameField.BASE_X / rect.getWidth(), com.edlplan.nso.ruleset.std.game.StdGameField.BASE_Y / rect.getHeight());
        Vec2 startOffset = rect.getCenter()
                .minus(com.edlplan.nso.ruleset.std.game.StdGameField.BASE_X * 0.5f / scale, com.edlplan.nso.ruleset.std.game.StdGameField.BASE_Y * 0.5f / scale);

        world.setOnDrawStart(canvas -> canvas.translate(startOffset.x, startOffset.y).expendAxis(scale).translate(PADDING_X, PADDING_Y));
        world.setMotionEventDec(event -> {
            event.eventPosition.minus(startOffset).zoom(scale).minus(PADDING_X, PADDING_Y);
            event.time += 50;
        });
    }


}
