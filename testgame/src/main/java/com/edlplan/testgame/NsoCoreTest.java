package com.edlplan.testgame;

import com.edplan.framework.MContext;
import com.edplan.framework.math.RectF;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.inputs.EdMotionEvent;
import com.edplan.framework.ui.widget.Fragment;
import com.edplan.nso.ruleset.base.game.World;
import com.edplan.nso.ruleset.base.game.judge.CursorData;

public class NsoCoreTest implements ITest {

    @Override
    public Fragment createFragment() {
        return new Fragment() {

            World world;

            @Override
            protected void onCreate(MContext context) {
                super.onCreate(context);
                world = new World(context);
                world.onLoadConfig(new World.WorldConfig() {{
                    judgeTypes.add(CursorData.class);
                }});

                setContentView(new EdView(context) {

                    @Override
                    public void onInitialLayouted() {
                        super.onInitialLayouted();
                        world.configDrawArea(RectF.xywh(0, 0, getWidth(), getHeight()));
                        world.start();
                    }
                });
            }
        };
    }

}
