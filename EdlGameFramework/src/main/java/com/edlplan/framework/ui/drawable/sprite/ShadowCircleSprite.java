package com.edlplan.framework.ui.drawable.sprite;

import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.graphics.opengl.shader.advance.BaseShader;
import com.edlplan.framework.graphics.opengl.shader.uniforms.UniformColor4;
import com.edlplan.framework.utils.StringUtil;

public class ShadowCircleSprite extends CircleSprite {
    private Color4 startColor = Color4.rgba(0, 0, 0, 0), endColor = Color4.rgba(0, 0, 0, 0);

    public ShadowCircleSprite(MContext c) {
        super(c);
    }

    boolean isInner = false;
    public void setInner() {
        isInner = true;
    }

    public void setStartColor(Color4 startColor) {
        this.startColor.set(startColor);
        this.startColor.toPremultipledThis();
    }

    public Color4 getStartColor() {
        return startColor;
    }

    public void setEndColor(Color4 endColor) {
        this.endColor.set(endColor);
        this.endColor.toPremultipledThis();
    }

    public Color4 getEndColor() {
        return endColor;
    }

    @Override
    protected CircleShader getShader() {
        return isInner ? InnerShadowCircleShader.get() : ShadowCircleShader.get();
    }

    @Override
    protected void prepareShader(BaseCanvas canvas) {
        super.prepareShader(canvas);
        ((ShadowCircleShader) getShader()).loadStartAndEndColor(startColor, endColor);
    }
}


class ShadowCircleShader extends CircleShader {

    public static final String VERTEX_SHADER, FRAGMENT_SHADER;

    private static ShadowCircleShader instance;

    public static ShadowCircleShader get() {
        if (instance == null) instance = new ShadowCircleShader();
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
                "uniform vec4 u_StartColor;",
                "uniform vec4 u_EndColor;",
                "void main(){",
                "    float d=u_Radius.y-u_Radius.x;",
                "    if(abs(d)<0.5)discard;",
                "    float v=length(f_Position);",
                "    float p=clamp((v-u_Radius.x)/d,0.0,1.0);",
                "    float a=min(",
                "        smoothstep(u_Radius.x-1.0,u_Radius.x,v),",
                "        1.0-smoothstep(u_Radius.y-1.0,u_Radius.y,v));",
                "    vec4 c=f_Color*mix(u_StartColor,u_EndColor,pow(p,0.5))*a;",
                "    @include <discard>",
                "    gl_FragColor=c;",
                "}"
        });
    }

    @PointerName
    public UniformColor4 uStartColor, uEndColor;

    public ShadowCircleShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER);
    }

    public ShadowCircleShader(String vs, String fs) {
        super(vs, fs);
    }

    public void loadCircleData(float innerRadius, float radius) {
        uRadius.loadData(innerRadius, radius);
    }

    public void loadStartAndEndColor(Color4 s, Color4 e) {
        uStartColor.loadData(s);
        uEndColor.loadData(e);
    }
}

class InnerShadowCircleShader extends ShadowCircleShader {

    public static final String VERTEX_SHADER, FRAGMENT_SHADER;

    private static InnerShadowCircleShader instance;

    public static InnerShadowCircleShader get() {
        if (instance == null) instance = new InnerShadowCircleShader();
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
                "uniform vec4 u_StartColor;",
                "uniform vec4 u_EndColor;",
                "void main(){",
                "    float d=u_Radius.y-u_Radius.x;",
                "    if(abs(d)<0.5)discard;",
                "    float v=length(f_Position);",
                "    float p=clamp((v-u_Radius.x)/d,0.0,1.0);",
                "    float a=min(",
                "        smoothstep(u_Radius.x-1.0,u_Radius.x,v),",
                "        1.0-smoothstep(u_Radius.y-1.0,u_Radius.y,v));",
                "    vec4 c=f_Color*mix(u_StartColor,u_EndColor,1.0-pow(1.0-p,0.5))*a;",
                "    if(c.a<0.0001)discard;",
                "    gl_FragColor=c;",
                "}"
        });
    }

    @BaseShader.PointerName
    public UniformColor4 uStartColor, uEndColor;

    public InnerShadowCircleShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER);
    }

    public void loadCircleData(float innerRadius, float radius) {
        uRadius.loadData(innerRadius, radius);
    }

    public void loadStartAndEndColor(Color4 s, Color4 e) {
        uStartColor.loadData(s);
        uEndColor.loadData(e);
    }
}
