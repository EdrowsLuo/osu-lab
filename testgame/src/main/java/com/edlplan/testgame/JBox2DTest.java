package com.edlplan.testgame;

import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.ui.EdView;
import com.edlplan.framework.ui.widget.Fragment;

public class JBox2DTest implements ITest {
    @Override
    public Fragment createFragment() {
        return new Fragment() {

            @Override
            protected void onCreate(MContext context) {
                super.onCreate(context);
                setContentView(new EdView(context) {

                    @Override
                    protected void onDraw(BaseCanvas canvas) {
                        super.onDraw(canvas);
                    }
                });
            }

        };
    }
}
