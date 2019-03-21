package com.edlplan.testgame;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.math.RectF;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.widget.Fragment;

public class CanvasDrawTest implements ITest {

    @Override
    public Fragment createFragment() {
        return new Fragment(){
            @Override
            protected void onCreate(MContext context) {
                super.onCreate(context);
                setContentView(new EdView(context) {
                    @Override
                    protected void onDraw(BaseCanvas canvas) {
                        super.onDraw(canvas);
                        canvas.drawTexture(GLTexture.ErrorTexture, RectF.xywh(100, 100, 200, 200));
                        canvas.drawCircle(500, 500, 100, Color4.Green, 1);
                        canvas.drawRect(RectF.xywh(800, 800, 100, 100), Color4.White, 1);
                        canvas.drawRect(RectF.xywh(800, 400, 500, 300), Color4.Red, 1);
                        canvas.drawRoundedRect(RectF.xywh(800, 400, 500, 300), 50, Color4.White, 1);
                    }
                });
            }
        };
    }
}
