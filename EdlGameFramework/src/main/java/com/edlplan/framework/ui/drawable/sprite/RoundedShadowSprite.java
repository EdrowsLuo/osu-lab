package com.edlplan.framework.ui.drawable.sprite;

import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.graphics.opengl.shader.uniforms.UniformColor4;
import com.edlplan.framework.graphics.opengl.shader.uniforms.UniformFloat;
import com.edlplan.framework.utils.StringUtil;

public class RoundedShadowSprite extends RoundedRectSprite {
    private float shadowWidth;

    private Color4 shadowStart = Color4.ONE.copyNew();
    private Color4 shadowEnd = Color4.Alpha.copyNew();

    public RoundedShadowSprite(MContext c) {
        super(c);
    }

    public void setShadowWidth(float shadowWidth) {
        this.shadowWidth = shadowWidth;
    }

    public float getShadowWidth() {
        return shadowWidth;
    }

    public void setShadowColor(Color4 s, Color4 e) {
        shadowStart.set(s.toPremultipled());
        shadowEnd.set(e.toPremultipled());
    }

    @Override
    protected void prepareShader(BaseCanvas canvas) {

        super.prepareShader(canvas);
        RoundedShadowShader s = (RoundedShadowShader) getShader();
        s.uShadowWidth.loadData(shadowWidth);
        s.uShadowStart.loadData(shadowStart);
        s.uShadowEnd.loadData(shadowEnd);
    }

    @Override
    protected RoundedShader getShader() {

        return RoundedShadowShader.get();
    }
}

class RoundedShadowShader extends RoundedShader {

    public static final String VERTEX_SHADER, FRAGMENT_SHADER;

    private static RoundedShadowShader instance;

    public static RoundedShadowShader get() {
        if (instance == null) instance = new RoundedShadowShader(VERTEX_SHADER, FRAGMENT_SHADER);
        return instance;
    }

    static {
        VERTEX_SHADER = StringUtil.link(StringUtil.LINE_BREAK, new String[]{
                "@include <TextureSpriteBase.vs>",
                "void main(){ setUpSpriteBase(); }"
        });
        FRAGMENT_SHADER = StringUtil.link(StringUtil.LINE_BREAK, new String[]{
                "precision highp float;",
                "@include <TextureSpriteBase.fs>",
                "@include <Rounded>",
                "uniform vec4 u_Rect,u_RoundedRadius;",
                "uniform float u_ShadowWidth;",
                "uniform vec4 u_ShadowStart;",
                "uniform vec4 u_ShadowEnd;",
                "void main(){",
                "    float r=u_RoundedRadius.x;",
                "    vec4 inner=vec4(u_Rect.x+r,u_Rect.y+r,u_Rect.z-r,u_Rect.w-r);",
                "    float v=distanceFromRoundedRect(f_Position.xy,inner,r);",
                "    float a=clamp(v,0.0,u_ShadowWidth)/u_ShadowWidth;",
                "    vec4 c=f_Color*getTextureColor()*smoothstep(-1.0,0.0,v)*(1.0-smoothstep(0.0,1.0,v-u_ShadowWidth))*mix(u_ShadowStart,u_ShadowEnd,a);",
                "    @include <discard>",
                "    gl_FragColor=c;",
                "}"
        });
    }

    @PointerName
    public UniformColor4 uShadowStart, uShadowEnd;

    @PointerName
    public UniformFloat uShadowWidth;

    public RoundedShadowShader(String v, String f) {
        super(v, f);
    }
}
