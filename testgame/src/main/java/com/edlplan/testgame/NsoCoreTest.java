package com.edlplan.testgame;

import com.edlplan.framework.MContext;
import com.edlplan.framework.math.RectF;
import com.edlplan.framework.ui.EdView;
import com.edlplan.framework.ui.inputs.EdMotionEvent;
import com.edlplan.framework.ui.widget.Fragment;
import com.edlplan.nso.ruleset.base.game.World;
import com.edlplan.nso.ruleset.base.game.judge.CursorData;

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
