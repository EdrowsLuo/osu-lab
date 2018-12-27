package com.edplan.osulab.ui.scenes.game;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.math.RectF;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.ui.additions.popupview.defviews.RenderStatPopupView;
import com.edplan.framework.ui.inputs.DirectMotionHandler;
import com.edplan.framework.ui.inputs.EdMotionEvent;
import com.edplan.nso.ruleset.base.beatmap.BeatmapDescription;
import com.edplan.nso.ruleset.base.game.World;
import com.edplan.nso.ruleset.base.game.judge.CursorData;
import com.edplan.nso.ruleset.base.game.judge.HitArea;
import com.edplan.nso.ruleset.base.game.judge.HitWindow;
import com.edplan.nso.ruleset.base.game.judge.JudgeData;
import com.edplan.nso.ruleset.base.game.judge.JudgeObject;
import com.edplan.nso.ruleset.base.game.judge.PositionHitObject;
import com.edplan.nso.ruleset.base.game.paint.TextureQuadObject;
import com.edplan.nso.ruleset.std.StdRuleset;
import com.edplan.osulab.LabGame;
import com.edplan.osulab.ScenesName;
import com.edplan.osulab.ui.BackQuery;
import com.edplan.osulab.ui.scenes.BaseScene;

import java.io.IOException;

public class GameScene extends BaseScene implements DirectMotionHandler{

    private World world;

    public GameScene(MContext c) {
        super(c);
        world = LabGame.get()
                .getNsoCore()
                .getRulesetById(StdRuleset.ID_NAME)
                .getWorldLoader()
                .loadWorld(
                        new BeatmapDescription() {{
                            beatmapType = StdRuleset.ID_NAME;
                            filePath = "/storage/emulated/0/osu!droid/Songs/346314 Mai Zang - Si Ye Cao De Huang Xiang/Mai Zang - Si Ye Cao De Huang Xiang (Axarious) [Despair].osu";
                        }},
                        null
                );
        /*world = new World(c);
        world.onLoadConfig(new World.WorldConfig() {{
            judgeTypes.add(CursorData.class);
        }});

        AbstractTexture cust = null;
        try {
            cust = c.getAssetResource().loadTexture("osu/skins/default/cursor.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 10; i++) {
            final int ii = i;

            TextureQuadObject textureQuadObject = new TextureQuadObject(c);
            textureQuadObject.sprite.setTextureAndSize(cust);
            textureQuadObject.sprite.setBaseWidth(200);
            textureQuadObject.sprite.position.set(100, 100);
            textureQuadObject.sprite.enableScale();
            textureQuadObject.addAnimTask(3000 + 2000 * ii + 200,300, time -> {
                textureQuadObject.sprite.position.set((float) (200 + (1 - time) * 300));
                textureQuadObject.sprite.scale.set((float) (3 - time * 2));
            });

            PositionHitObject noteHit = new PositionHitObject() {{
                hitWindow = HitWindow.interval(3000 + 2000 * ii + 500, 500);
                positionChecker = HitArea.circle(200, 200, 1000);
                onHit = (time, x, y) -> {
                    System.out.println("hit offset " + (3000 + 2000 * ii + 500 - time));
                    textureQuadObject.postDetach();
                };
                onTimeOut = textureQuadObject::postDetach;
            }};

            world.getPaintWorld().addDrawObject(textureQuadObject);
            world.getJudgeWorld().addJudgeObject(noteHit);
        }



        Vec2 pos = new Vec2();
        TextureQuadObject cus = new TextureQuadObject(c) {
            @Override
            protected void onDraw(BaseCanvas canvas, World world) {
                sprite.position.set(pos.x, pos.y);
                super.onDraw(canvas, world);
            }
        };
        try {
            cus.sprite.setTextureAndSize(c.getAssetResource().loadTexture("osu/skins/default/cursor.png"));
            cus.sprite.setBaseWidth(500);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JudgeObject cst = new JudgeObject() {
            @Override
            public double getStartJudgeTime() {
                return 2000;
            }

            @Override
            public double getJudgeFailedTime() {
                return 300000;
            }

            @Override
            protected void onRelease() {
                super.onRelease();
                cus.postOperation(()->{
                    cus.detach();
                    //System.out.println("detach");
                });
            }

            @Override
            public boolean handle(JudgeData data, int type, World world) {
                CursorData cursorData = (CursorData) data;
                CursorData.CursorHolder holder = cursorData.getCursors()[0];
                final float x = holder.x;
                final float y = holder.y;
                pos.x = x;
                pos.y = y;
                return false;
            }

            @Override
            public Class<? extends JudgeData>[] getListeningData() {
                return new Class[]{CursorData.class};
            }
        };

        world.getJudgeWorld().addJudgeObject(cst);
        world.getPaintWorld().addDrawObject(cus);
        world.load();
        world.start();*/
    }

    @Override
    public void onInitialLayouted() {
        super.onInitialLayouted();
        world.configDrawArea(RectF.xywh(0, 0, getWidth(), getHeight()));
        world.start();
    }

    @Override
    public boolean onDirectMotionEvent(EdMotionEvent... events) {
        world.onMotionEvent(events);
        return true;
    }

    @Override
    public String getSceneName() {
        return ScenesName.GameScene;
    }

    @Override
    protected void onDraw(BaseCanvas canvas) {
        super.onDraw(canvas);
        world.onDraw(canvas);
    }

    @Override
    public void hide() {
        setVisiblility(VISIBILITY_GONE);
        LabGame.get().getToolBar().show();
        LabGame.get().getJumpingCircle().show();
        BackQuery.get().setForceHideBackButton(false);
        //TODO : 隐藏显示FPS计数器的方法应该统一管理
        RenderStatPopupView.getInstance(getContext()).setLowDetailMode(false);
        getViewRoot().unregisterDirectMotionHandler(this);
        world.stop();
    }

    @Override
    public void show() {
        setVisiblility(VISIBILITY_SHOW);
        LabGame.get().getToolBar().hide();
        LabGame.get().getJumpingCircle().hide();
        BackQuery.get().setForceHideBackButton(true);
        //TODO : 隐藏显示FPS计数器的方法应该统一管理
        RenderStatPopupView.getInstance(getContext()).setLowDetailMode(true);
        getViewRoot().registerDirectMotionHandler(this);
    }

    @Override
    public boolean isHidden() {
        return getVisiblility() != VISIBILITY_SHOW;
    }
}
