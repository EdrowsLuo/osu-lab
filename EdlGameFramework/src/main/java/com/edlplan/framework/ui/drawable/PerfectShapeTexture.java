package com.edlplan.framework.ui.drawable;

import com.edlplan.framework.graphics.shape.IPath;
import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.layer.BufferedLayer;
import com.edlplan.framework.graphics.opengl.BlendType;
import com.edlplan.framework.graphics.opengl.GLCanvas2D;
import com.edlplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.graphics.opengl.objs.GLTexture;
import com.edlplan.framework.graphics.opengl.shader.advance.ColorShader;
import com.edlplan.framework.graphics.shape.IHasPath;
import com.edlplan.framework.graphics.shape.Shape;
import com.edlplan.framework.math.Vec2;

public class PerfectShapeTexture {

    private MContext context;

    private GLTexture texture;

    private BufferedLayer layer;

    private GLCanvas2D canvas2D;

    private Vec2 renderSize = new Vec2();

    private int canvasX, canvasY;

    public PerfectShapeTexture(int canvasX, int canvasY, float renderX, float renderY, MContext context) {
        this.canvasX = canvasX;
        this.canvasY = canvasY;
        renderSize.set(renderX, renderY);
        this.context = context;
    }

    public void startDraw() {
        if (canvas2D == null) {
            layer = new BufferedLayer(context, canvasX, canvasY, true);
            //texture = GLTexture.createGPUTexture(canvasX, canvasY);
            //canvas2D = new GLCanvas2D(texture, context);
            canvas2D = new GLCanvas2D(layer);
            /*canvas2D.prepare();
            canvas2D.scale(canvasX / renderSize.x, canvasY / renderSize.y);
            canvas2D.unprepare();*/
        }
        canvas2D.prepare();
        canvas2D.save();
        canvas2D.scale(canvasX / renderSize.x, canvasY / renderSize.y);
        canvas2D.clearColor(Color4.Alpha);
        canvas2D.clearBuffer();
    }

    public void drawShape(Shape shape) {
        drawShape(shape, Shape.ShapeCombineType.Normal);
    }

    public void drawShape(Shape shape, Shape.ShapeCombineType type) {
        if (!shape.isComplexShape()) {
            IPath path = ((IHasPath) shape).createPath();
            canvas2D.getBlendSetting().save();
            switch (type) {
                case Add:
                    throw new IllegalArgumentException("Add is not supported");
                case Normal:
                    canvas2D.getBlendSetting().set(true, BlendType.DeleteRepeat);
                    break;
                case Sub:
                    canvas2D.getBlendSetting().set(true, BlendType.Delete_Alpha);
                    break;
            }
            canvas2D.drawPath(path, Color4.ONE, 1, ColorShader.ALPHA.get());
            canvas2D.getBlendSetting().restore();
        }
    }


    public void endDraw() {
        canvas2D.restore();
        canvas2D.unprepare();
    }

    public AbstractTexture getTexture() {
        return layer.getTexture();
    }


}
