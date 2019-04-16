package com.edlplan.testgame;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.graphics.shape.IPath;
import com.edplan.framework.graphics.shape.Path;
import com.edplan.framework.graphics.shape.PathBuilder;
import com.edplan.framework.graphics.shape.Shape;
import com.edplan.framework.graphics.shape.objs.Circle;
import com.edplan.framework.graphics.shape.objs.PathShape;
import com.edplan.framework.math.FMath;
import com.edplan.framework.math.RectF;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.drawable.PerfectShapeTexture;
import com.edplan.framework.ui.widget.Fragment;

public class CanvasDrawTest implements ITest {

    @Override
    public Fragment createFragment() {
        return new Fragment(){
            @Override
            protected void onCreate(MContext context) {
                super.onCreate(context);
                setContentView(new EdView(context) {
                    float ang;
                    PerfectShapeTexture shapeTexture;
                    Shape shape = (PathShape) () -> {
                        Path path = new Path();
                        PathBuilder builder = new PathBuilder(path);

                        builder.moveTo(12, 12);

                        float l = 0.5f;
                        float dis = 0.5f;
                        for (int i = 0; i < 5; i++) {
                            //builder.move(l, 0);
                            builder.circleRotateRelative(new Vec2(l, 0), -FMath.Pi);
                            l += dis;
                            //builder.move(0, -l);
                            builder.circleRotateRelative(new Vec2(0, -l), FMath.Pi);
                            l += dis;
                            //builder.move(-l, 0);
                            builder.circleRotateRelative(new Vec2(-l, 0), -FMath.Pi);
                            l += dis;
                            //builder.move(0, l);
                            builder.circleRotateRelative(new Vec2(0, l), FMath.Pi);
                            l += dis;
                        }
                        Vec2 end = Vec2.atCircle(ang).zoom(12).move(12, 12);
                        builder.moveTo(end.x, end.y);

                        /*builder.moveTo(12, 12)
                                .moveTo(12, 6)
                                .moveTo(6, 6)
                                .moveTo(6, 18)
                                .moveTo(18, 18)
                                .moveTo(18, 12)
                                .moveTo(24, 6)
                                .moveTo(12, 0)
                                .moveTo(18, 6);*/
                        return path;
                    };

                    @Override
                    protected void onDraw(BaseCanvas canvas) {
                        super.onDraw(canvas);
                        ang += FMath.Pi2 / 60;
                        canvas.drawTexture(GLTexture.ErrorTexture, RectF.xywh(100, 100, 200, 200));
                        canvas.drawCircle(500, 500, 100, Color4.Green, 1);
                        canvas.drawRect(RectF.xywh(800, 800, 100, 100), Color4.White, 1);
                        canvas.drawRect(RectF.xywh(800, 400, 500, 300), Color4.Red, 1);
                        canvas.drawRoundedRect(RectF.xywh(700, 200, 500, 400), 70, Color4.White, 1);
                        canvas.drawRing(800, 400, 200, 150, Color4.Green, 1);
                        if (shapeTexture == null) {
                            shapeTexture = new PerfectShapeTexture(1000, 1000, 24, 24, getContext());
                        }
                        //canvas.drawRoundedRect(RectF.xywh(100, 100, 500, 500), 70, Color4.White, 1);
                        canvas.save();
                        shapeTexture.startDraw();
                        shapeTexture.drawShape(shape);
                        shapeTexture.drawShape(new Circle(new Vec2(12, 12), 5), Shape.ShapeCombineType.Sub);
                        shapeTexture.endDraw();
                        canvas.restore();
                        //canvas.drawTexture(GLTexture.ErrorTexture, RectF.xywh(100, 100, 500, 500));
                        //canvas.drawRoundedRect(RectF.xywh(150, 150, 500, 500), 70, Color4.Red, 1);
                        //canvas.drawTexture(GLTexture.ErrorTexture, RectF.xywh(100, 100, 500, 500));
                        canvas.drawTexture(shapeTexture.getTexture(), RectF.xywh(100, 100, 500, 500));
                    }
                });
            }
        };
    }
}
