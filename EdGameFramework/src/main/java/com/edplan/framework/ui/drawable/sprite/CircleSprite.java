package com.edplan.framework.ui.drawable.sprite;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformVec2;
import com.edplan.framework.utils.StringUtil;

public class CircleSprite extends RectSprite<CircleShader> {
    private float innerRadius;
    private float radius;

    public CircleSprite(MContext c) {
        super(c);
    }

    public void resetRadius() {
        setInnerRadius(0);
        setRadius(0);
    }

    public void setInnerRadius(float innerRadius) {
        this.innerRadius = innerRadius;
    }

    public float getInnerRadius() {
        return innerRadius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    protected CircleShader createShader() {

        return CircleShader.get();
    }

    @Override
    protected void prepareShader(BaseCanvas canvas) {

        super.prepareShader(canvas);
        shader.loadCircleData(innerRadius, radius);
    }
}

class CircleShader extends SpriteShader {

    public static final String VERTEX_SHADER, FRAGMENT_SHADER;

    private static CircleShader instance;

    public static CircleShader get() {
        if (instance == null) instance = new CircleShader();
        return instance;
    }

    static {
        VERTEX_SHADER = StringUtil.link(StringUtil.LINE_BREAK, new String[]{
                "@include <SpriteBase.vs>",
                "void main(){ setUpSpriteBase(); }"
        });
        FRAGMENT_SHADER = StringUtil.link(StringUtil.LINE_BREAK, new String[]{
                "precision highp float;",
                "@include <SpriteBase.fs>",
                "uniform vec2 u_Radius;",
                "void main(){",
                "    float v=length(f_Position);",
                "    float a=min(",
                "        smoothstep(u_Radius.x-1.0,u_Radius.x,v),",
                "        1.0-smoothstep(u_Radius.y-1.0,u_Radius.y,v));",
                "    gl_FragColor=f_Color*a;",
                "}"
        });
    }


    @PointerName
    public UniformVec2 uRadius;

    public CircleShader(String vs, String fs) {
        super(vs, fs);
    }

    public CircleShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER);
    }

    public void loadCircleData(float innerRadius, float radius) {
        uRadius.loadData(innerRadius, radius);
    }
}
