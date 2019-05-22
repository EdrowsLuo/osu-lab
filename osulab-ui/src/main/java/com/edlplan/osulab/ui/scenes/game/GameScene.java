package com.edlplan.osulab.ui.scenes.game;

import com.edlplan.osulab.LabGame;
import com.edlplan.osulab.ScenesName;
import com.edlplan.osulab.ui.BackQuery;
import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.math.RectF;
import com.edlplan.framework.resource.AResource;
import com.edlplan.framework.resource.DirResource;
import com.edlplan.framework.ui.additions.popupview.defviews.RenderStatPopupView;
import com.edlplan.framework.ui.inputs.DirectMotionHandler;
import com.edlplan.framework.ui.inputs.EdMotionEvent;
import com.edlplan.nso.ruleset.base.beatmap.BeatmapDescription;
import com.edlplan.nso.ruleset.base.game.World;
import com.edlplan.nso.ruleset.std.StdRuleset;
import com.edlplan.osulab.ui.scenes.BaseScene;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class GameScene extends BaseScene implements DirectMotionHandler{

    public static String testOsu = null;

    private World world;

    public GameScene(MContext c) {
        super(c);
        world = LabGame.get()
                .getNsoCore()
                .getRulesetById(StdRuleset.ID_NAME)
                .getWorldLoader()
                .loadWorld(
                        new BeatmapDescription() {

                            AResource test;

                            File testFile;

                            {
                                beatmapType = StdRuleset.ID_NAME;
                                if (testOsu == null) {
                                    test = c.getAssetResource()
                                            .subResource("test")
                                            .subResource("songs")
                                            .subResource("teo");
                                } else {
                                    System.out.println("test osu : " + testOsu);
                                    testFile = new File(testOsu);
                                    test = new DirResource(testFile.getParentFile());
                                }
                            }

                            @Override
                            public AResource openDirRes() {
                                return test;
                            }

                            @Override
                            public InputStream openBeatmapStream() throws IOException {
                                if (testFile == null) {
                                    return test.openInput("Araki - Teo (Nevo) [Descent].osu");
                                } else {
                                    return test.openInput(testFile.getName());
                                }
                            }
                        }
                );
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
        //RenderStatPopupView.getInstance(getContext()).setLowDetailMode(false);
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
        //RenderStatPopupView.getInstance(getContext()).setLowDetailMode(true);
        getViewRoot().registerDirectMotionHandler(this);
    }

    @Override
    public boolean isHidden() {
        return getVisiblility() != VISIBILITY_SHOW;
    }
}
