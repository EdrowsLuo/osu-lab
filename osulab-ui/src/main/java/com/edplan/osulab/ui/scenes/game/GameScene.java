package com.edplan.osulab.ui.scenes.game;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.math.RectF;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.ui.Anchor;
import com.edplan.framework.ui.additions.popupview.defviews.RenderStatPopupView;
import com.edplan.framework.ui.inputs.DirectMotionHandler;
import com.edplan.framework.ui.inputs.EdMotionEvent;
import com.edplan.framework.utils.Ref;
import com.edplan.nso.ruleset.base.game.World;
import com.edplan.nso.ruleset.base.game.judge.CursorData;
import com.edplan.nso.ruleset.base.game.judge.JudgeData;
import com.edplan.nso.ruleset.base.game.judge.JudgeObject;
import com.edplan.nso.ruleset.base.game.paint.TextureRect;
import com.edplan.osulab.LabGame;
import com.edplan.osulab.ScenesName;
import com.edplan.osulab.ui.BackQuery;
import com.edplan.osulab.ui.scenes.BaseScene;

import java.io.IOException;

public class GameScene extends BaseScene implements DirectMotionHandler{

    private World world;

    public GameScene(MContext c) {
        super(c);
        world = new World(c);
        world.onLoadConfig(new World.WorldConfig() {{
            judgeTypes.add(CursorData.class);
        }});

        Vec2 pos = new Vec2();
        TextureRect cus = new TextureRect(c) {
            @Override
            protected void onDraw(BaseCanvas canvas, World world) {
                setQuad(
                        RectF.anchorOWH(
                                Anchor.Center,
                                pos.x,
                                pos.y,
                                500,
                                500));
                super.onDraw(canvas, world);
            }
        };
        try {
            cus.setTexture(c.getAssetResource().loadTexture("osu/skins/default/cursor.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JudgeObject cst = new JudgeObject() {
            @Override
            public double getStartJudgeTime() {
                return 3000;
            }

            @Override
            public double getJudgeFailedTime() {
                return 30000;
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
            public boolean handle(JudgeData data, World world) {
                CursorData cursorData = (CursorData) data;
                CursorData.CursorHolder holder = cursorData.getCursors()[0];
                final float x = holder.x;
                final float y = holder.y;
                pos.x = x;
                pos.y = y;
                return false;
            }

            @Override
            public Class<? extends JudgeData>[] getListeningDatas() {
                return new Class[]{CursorData.class};
            }
        };

        world.getJudgeWorld().addJudgeObject(cst);
        world.getPaintWorld().addDrawObject(cus);
        world.load();
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
