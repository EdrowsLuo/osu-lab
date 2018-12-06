package com.edplan.osulab.ui.scenes.game;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.math.RectF;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.additions.popupview.defviews.RenderStatPopupView;
import com.edplan.framework.ui.widget.component.Hideable;
import com.edplan.nso.ruleset.base.game.paint.GroupDrawObject;
import com.edplan.nso.ruleset.base.game.paint.PaintWorld;
import com.edplan.nso.ruleset.base.game.paint.TextureRect;
import com.edplan.osulab.LabGame;
import com.edplan.osulab.ScenesName;
import com.edplan.osulab.ui.BackQuery;
import com.edplan.osulab.ui.scenes.BaseScene;
import com.edplan.osulab.ui.toolbar.Toolbar;

public class GameScene extends BaseScene{

    private PaintWorld paintWorld;

    public GameScene(MContext c) {
        super(c);
        paintWorld = new PaintWorld();
        paintWorld.addDrawObject(
                new GroupDrawObject() {{
                    for (int i = 0 ;i<100;i++) {
                        final int ii = i * 4;
                        attachFront(
                                new TextureRect(getContext()) {{
                                    setTexture(GLTexture.Yellow);
                                    setQuad(RectF.xywh(200 + ii, 200 + ii, 400, 400));
                                }}
                        );
                    }
                }}

        );
    }

    public static String getSceneNameStatic() {
        return ScenesName.GameScene;
    }

    public static boolean isSingleInstanceStatic() {
        return true;
    }

    @Override
    public String getSceneName() {
        return ScenesName.GameScene;
    }

    @Override
    protected void onDraw(BaseCanvas canvas) {
        super.onDraw(canvas);
        paintWorld.draw(canvas);
    }

    @Override
    public void hide() {
        setVisiblility(VISIBILITY_GONE);
        LabGame.get().getToolBar().show();
        LabGame.get().getJumpingCircle().show();
        BackQuery.get().setForceHideBackButton(false);
        //TODO : 隐藏显示FPS计数器的方法应该统一管理
        RenderStatPopupView.getInstance(getContext()).show();
    }

    @Override
    public void show() {
        setVisiblility(VISIBILITY_SHOW);
        LabGame.get().getToolBar().hide();
        LabGame.get().getJumpingCircle().hide();
        BackQuery.get().setForceHideBackButton(true);
        //TODO : 隐藏显示FPS计数器的方法应该统一管理
        RenderStatPopupView.getInstance(getContext()).hide();
    }

    @Override
    public boolean isHidden() {
        return getVisiblility() != VISIBILITY_SHOW;
    }
}
