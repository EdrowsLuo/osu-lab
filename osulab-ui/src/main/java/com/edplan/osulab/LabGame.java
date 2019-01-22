package com.edplan.osulab;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.math.Mat4;
import com.edplan.framework.test.performance.Tracker;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.widget.RelativeLayout;
import com.edplan.nso.NsoCore;
import com.edplan.osulab.ui.MainBackground;
import com.edplan.osulab.ui.MessageList;
import com.edplan.osulab.ui.OptionList;
import com.edplan.osulab.ui.SceneOverlay;
import com.edplan.osulab.ui.UiConfig;
import com.edplan.osulab.ui.opening.MainCircleView;
import com.edplan.osulab.ui.pieces.BackButton;
import com.edplan.osulab.ui.pieces.JumpingCircle;
import com.edplan.osulab.ui.popup.PopupButtonGroup;
import com.edplan.osulab.ui.popup.PopupToast;
import com.edplan.osulab.ui.scenes.SceneSelectButtonBar;
import com.edplan.osulab.ui.scenes.Scenes;
import com.edplan.osulab.ui.toolbar.Toolbar;

/**
 * 全局的管理类
 */
public class LabGame {
    private static LabGame game;

    private Toolbar toolBar;

    private OptionList optionList;

    private MessageList messageList;

    private SceneOverlay sceneOverlay;

    private JumpingCircle jumpingCircle;

    private MainBackground mainBackground;

    private SceneSelectButtonBar sceneSelectButtonBar;

    private Scenes scenes;

    private BackButton backButton;

    private MContext context;

    private NsoCore nsoCore;

    public void setNsoCore(NsoCore nsoCore) {
        this.nsoCore = nsoCore;
    }

    public NsoCore getNsoCore() {
        return nsoCore;
    }

    public void setSceneSelectButtonBar(SceneSelectButtonBar sceneSelectButtonBar) {
        this.sceneSelectButtonBar = sceneSelectButtonBar;
    }

    public SceneSelectButtonBar getSceneSelectButtonBar() {
        return sceneSelectButtonBar;
    }

    public void setScenes(Scenes scenes) {
        this.scenes = scenes;
    }

    public Scenes getScenes() {
        return scenes;
    }

    public void setMessageList(MessageList messageList) {
        this.messageList = messageList;
    }

    public MessageList getMessageList() {
        return messageList;
    }

    public void setBackButton(BackButton backButton) {
        this.backButton = backButton;
    }

    public BackButton getBackButton() {
        return backButton;
    }

    public void setMainBackground(MainBackground mainBackground) {
        this.mainBackground = mainBackground;
    }

    public MainBackground getMainBackground() {
        return mainBackground;
    }

    public void setJumpingCircle(JumpingCircle jumpingCircle) {
        this.jumpingCircle = jumpingCircle;
    }

    public JumpingCircle getJumpingCircle() {
        return jumpingCircle;
    }

    public void setSceneOverlay(SceneOverlay sceneOverlay) {
        this.sceneOverlay = sceneOverlay;
    }

    public SceneOverlay getSceneOverlay() {
        return sceneOverlay;
    }

    public void setToolBar(Toolbar toolBar) {
        this.toolBar = toolBar;
    }

    public Toolbar getToolBar() {
        return toolBar;
    }

    public void setOptionList(OptionList optionList) {
        this.optionList = optionList;
    }

    public OptionList getOptionList() {
        return optionList;
    }


    public void exit() {
        final PopupButtonGroup p = new PopupButtonGroup(context);
        p.addButton("BACK TO GAME", view -> p.hide());
        p.addButton("EXIT", view -> {
            directExit();
            p.hide();
        });
        p.show();
    }

    private void directExit() {
        toolBar.hide();
        optionList.hide();
        sceneOverlay.hide();
        sceneSelectButtonBar.hide();
        jumpingCircle.exitAnimation();
    }

    private void initialNsoCore(MContext c) {
        nsoCore = new NsoCore(c, null);
        nsoCore.load(null).onLoadComplete(()->
                c.runOnUIThread(() -> PopupToast.toast(c, "ruleset loaded").show()));

    }

    public EdView createContentView(MContext c) {
        this.context = c;

        initialNsoCore(c);

        toolBar = new Toolbar(c) {{
            layoutParam(
                    new RelativeParam() {{
                        width = Param.MODE_MATCH_PARENT;
                        height = Param.makeUpDP(UiConfig.TOOLBAR_HEIGHT_DP);
                        gravity = Gravity.TopCenter;
                    }}
            );

            shadow.setLayoutParam(
                    new RelativeParam() {{
                        width = Param.MODE_MATCH_PARENT;
                        height = Param.makeUpDP(UiConfig.TOOLBAR_HEIGHT_DP);
                        gravity = Gravity.TopCenter;
                    }}
            );
        }};


        RelativeLayout mainLayout = new RelativeLayout(c) {
            {
                layoutParam(
                        new RelativeParam() {{
                            gravity = Gravity.TopLeft;
                            width = Param.makeupScaleOfParentParam(1f);
                            height = Param.makeupScaleOfParentParam(1f);
                        }}
                );

                children(
                        mainBackground = new MainBackground(c) {{
                            layoutParam(
                                    new RelativeParam() {{
                                        width = Param.MODE_MATCH_PARENT;
                                        height = Param.MODE_MATCH_PARENT;
                                    }}
                            );
                        }},
                        scenes = new Scenes(c) {{
                            layoutParam(
                                    new RelativeParam() {{
                                        width = Param.MODE_MATCH_PARENT;
                                        height = Param.MODE_MATCH_PARENT;
                                    }}
                            );
                        }},
                        toolBar.shadow,
                        sceneSelectButtonBar = new SceneSelectButtonBar(c) {{
                            layoutParam(
                                    new RelativeParam() {{
                                        width = Param.MODE_MATCH_PARENT;
                                        height = Param.makeUpDP(UiConfig.SCENE_SELECT_BUTTON_BAR_HEIGHT);
                                        gravity = Gravity.Center;
                                    }}
                            );
                        }},
                        new MainCircleView(c) {{
                            layoutParam(
                                    new RelativeParam() {{
                                        width = Param.makeupScaleOfParentOtherParam(0.6f);
                                        height = Param.makeupScaleOfParentParam(0.6f);
                                        gravity = Gravity.Center;
                                    }}
                            );
                        }},
                        jumpingCircle = new JumpingCircle(c) {{
                            layoutParam(
                                    new RelativeParam() {{
                                        width = Param.makeupScaleOfParentOtherParam(0.6f);
                                        height = Param.makeupScaleOfParentParam(0.6f);
                                        gravity = Gravity.Center;
                                    }}
                            );
                        }},
                        sceneOverlay = new SceneOverlay(c) {{
                            layoutParam(
                                    new RelativeParam() {{
                                        width = Param.MODE_MATCH_PARENT;
                                        height = Param.MODE_MATCH_PARENT;
                                        marginTop = ViewConfiguration.dp(UiConfig.TOOLBAR_HEIGHT_DP);
                                        gravity = Gravity.BottomCenter;
                                    }}
                            );
                        }},
                        optionList = new OptionList(c) {{
                            layoutParam(
                                    new RelativeParam() {{
                                        width = Param.makeUpDP(350);
                                        height = Param.MODE_MATCH_PARENT;
                                        marginTop = ViewConfiguration.dp(UiConfig.TOOLBAR_HEIGHT_DP);
                                    }}
                            );
                        }},
                        messageList = new MessageList(c) {{
                            layoutParam(
                                    new RelativeParam() {{
                                        width = Param.makeUpDP(350);
                                        height = Param.MODE_MATCH_PARENT;
                                        marginTop = ViewConfiguration.dp(UiConfig.TOOLBAR_HEIGHT_DP);
                                        gravity = Gravity.BottomRight;
                                    }}
                            );
                        }},
                        toolBar,
                        backButton = new BackButton(c) {{
                            layoutParam(
                                    new RelativeParam() {{
                                        width = Param.makeUpDP(40);
                                        height = Param.makeUpDP(40);
                                        gravity = Gravity.BottomLeft;
                                    }}
                            );
                        }}
                );
            }

            boolean hasTest = false;

            @Override
            protected void onDraw(BaseCanvas canvas) {
                super.onDraw(canvas);
                if (!hasTest) {
                    hasTest = true;
                    Tracker.createTmpNode("canvas-operation").wrap(() -> {
                        Mat4 mat4 = Mat4.createIdentity();
                        for (int i = 0; i < 1000; i++) {
                            mat4.translate(10, 10, 10);
                        }
                    }).then(System.out::println);
                }
            }
        };
        return mainLayout;
    }

    public static void createGame() {
        game = new LabGame();
    }

    public static LabGame get() {
        return game;
    }
}
