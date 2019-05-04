package com.edlplan.testgame;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.widget.Fragment;

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
