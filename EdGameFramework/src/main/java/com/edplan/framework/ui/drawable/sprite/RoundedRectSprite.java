package com.edplan.framework.ui.drawable.sprite;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformColor4;
import com.edplan.framework.graphics.opengl.shader.uniforms.UniformSample2D;
import com.edplan.framework.math.RectF;
import com.edplan.framework.utils.StringUtil;

public class RoundedRectSprite extends BaseRectTextureSprite<RoundedShader> {

    private float l, t, r, b;
    private float r1, r2, r3, r4;

    public RoundedRectSprite(MContext c) {
        super(c);
    }

    public void setRoundedRadius(float r) {
        r1 = r;
        r2 = r;
        r3 = r;
        r4 = r;
    }

    public float getRoundedRadius() {
        return r1;
    }

    public void setRect(RectF r) {
        setRect(r.getLeft(), r.getTop(), r.getRight(), r.getBottom());
    }

    public void setRect(float l, float t, float r, float b) {
        this.l = l;
        this.r = r;
        this.t = t;
        this.b = b;
    }

    @Override
    protected void prepareShader(BaseCanvas canvas) {

        super.prepareShader(canvas);
        //getShader().uBackbuffer.loadData(((GLCanvas2D) canvas).getLayer().getTexture().getTexture());
        getShader().loadRadius(r1, r2, r3, r4);
        getShader().loadRect(l, t, r, b);
    }

    @Override
    protected RoundedShader getShader() {

        return RoundedShader.get();
    }

}

class RoundedShader extends TextureSpriteShader {

    public static final String VERTEX_SHADER, FRAGMENT_SHADER;

    private static RoundedShader instance;

    public static RoundedShader get() {
        if (instance == null) instance = new RoundedShader(VERTEX_SHADER, FRAGMENT_SHADER);
        return instance;
    }

    static {
        VERTEX_SHADER = StringUtil.link(StringUtil.LINE_BREAK,
                "@include <TextureSpriteBase.vs>",
                "void main(){ setUpSpriteBase(); }");
        FRAGMENT_SHADER = StringUtil.link(StringUtil.LINE_BREAK,
                "precision highp float;",
                "@include <TextureSpriteBase.fs>",
                "@include <Rounded>",
                "uniform vec4 u_Rect,u_RoundedRadius;",
                //"uniform sampler2D u_Backbuffer;",
                "void main(){",
                "    float r=u_RoundedRadius.x;",
                "    vec4 inner=vec4(u_Rect.x+r,u_Rect.y+r,u_Rect.z-r,u_Rect.w-r);",
                "    float v=distanceFromRoundedRect(f_Position.xy,inner,r);",
                "    float a=1.0-smoothstep(0.0,1.0,v);",
                //"    vec4 c=f_Color*texture2D(u_Backbuffer,f_Position.xy)*getTextureColor()*a;",
                "    vec4 c=f_Color*getTextureColor()*a;",
                "    @include <discard>",
                "    gl_FragColor=c;",
                "}");
    }

    @PointerName
    public UniformColor4 uRect, uRoundedRadius;

    //@PointerName
    //public UniformSample2D uBackbuffer;

    public RoundedShader(String v, String f) {
        super(v, f);
    }

    public void loadRect(float l, float t, float r, float b) {
        uRect.loadData(l, t, r, b);
    }

    public void loadRadius(float tl, float tr, float bl, float br) {
        uRoundedRadius.loadData(tl, tr, bl, br);
    }
}
