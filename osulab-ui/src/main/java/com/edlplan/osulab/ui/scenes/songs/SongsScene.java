package com.edlplan.osulab.ui.scenes.songs;

import com.edlplan.osulab.LabGame;
import com.edlplan.osulab.ScenesName;
import com.edlplan.osulab.ui.UiConfig;
import com.edlplan.framework.MContext;
import com.edlplan.framework.math.RectF;
import com.edlplan.framework.ui.Anchor;
import com.edlplan.framework.ui.ViewConfiguration;
import com.edlplan.framework.ui.layout.Gravity;
import com.edlplan.framework.ui.layout.Param;
import com.edlplan.framework.ui.widget.component.Hideable;
import com.edlplan.osulab.ui.scenes.BaseScene;

import java.util.ArrayList;
import java.util.List;

public class SongsScene extends BaseScene {
    private BottomBar bottomBar;

    private DetailsPanel detailsPanel;

    private SongsListView songsListView;

    private List<Hideable> hideableContent = new ArrayList<Hideable>();

    public SongsScene(MContext c) {
        super(c);
        addAll(
                songsListView = new SongsListView(c){{
                    layoutParam(
                            new RelativeParam() {{
                                width = Param.makeUpDP(WIDTH_DP);
                                height = Param.MODE_MATCH_PARENT;
                                gravity = Gravity.TopRight;
                                marginTop = ViewConfiguration.dp(UiConfig.TOOLBAR_HEIGHT_DP);
                                marginBottom = ViewConfiguration.dp(BottomBar.HEIGHT_DP);
                            }});
                }},
                detailsPanel = new DetailsPanel(c){{
                    layoutParam(
                            new RelativeParam() {{
                                width = Param.makeUpDP(WIDTH_DP);
                                height = Param.MODE_MATCH_PARENT;
                                marginTop = ViewConfiguration.dp(UiConfig.TOOLBAR_HEIGHT_DP);
                                marginBottom = ViewConfiguration.dp(BottomBar.HEIGHT_DP);
                                gravity = Gravity.TopLeft;
                            }});
                }},
                bottomBar = new BottomBar(c){{
                    layoutParam(
                            new RelativeParam() {{
                                width = Param.MODE_MATCH_PARENT;
                                height = Param.makeUpDP(BottomBar.HEIGHT_DP);
                                gravity = Gravity.BottomCenter;
                            }});
                }}
        );

        hideableContent.add(songsListView);
        hideableContent.add(detailsPanel);
        hideableContent.add(bottomBar);
    }

    public void setBottomBar(BottomBar bottomBar) {
        this.bottomBar = bottomBar;
    }

    public BottomBar getBottomBar() {
        return bottomBar;
    }

    @Override
    public double getHideDuration() {

        return ViewConfiguration.DEFAULT_TRANSITION_TIME;
    }

    @Override
    public void hide() {
        for (Hideable h : hideableContent) h.hide();
    }

    @Override
    public void show() {
        for (Hideable h : hideableContent) h.show();
        BaseBoundOverlay bound = new BaseBoundOverlay();
        RectF area = RectF.anchorOWH(Anchor.Center,
                getRight(),
                getBottom(),
                ViewConfiguration.dp(200),
                ViewConfiguration.dp(200));
        bound.setLeft(area.getLeft());
        bound.setTop(area.getTop());
        bound.setRight(area.getRight());
        bound.setBottom(area.getBottom());
        LabGame.get().getJumpingCircle().setBoundOverlay(bound);
    }

    @Override
    public String getSceneName() {
        return ScenesName.SongSelect;
    }

}
