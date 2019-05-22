package com.edlplan.framework.graphics.opengl.objs;

import android.graphics.Color;

import com.edlplan.framework.math.Vec4;

import java.nio.FloatBuffer;

public class Color4 {
    public static final int FLOATS = 4;

    public static final Color4 ONE = Color4.rgba(1, 1, 1, 1, true);

    public static final Color4 White = Color4.rgba(1, 1, 1, 1, true);

    public static final Color4 Alpha = Color4.rgba(0, 0, 0, 0, true);

    public static final Color4 Black = Color4.rgba(0, 0, 0, 1, true);

    public static final Color4 Blue = Color4.rgba(0, 0, 1, 1, true);

    public static final Color4 Green = Color4.rgba(0, 1, 0, 1, true);

    public static final Color4 Yellow = Color4.rgba(0, 1, 1, 1, true);

    public static final Color4 Red = Color4.rgba(1, 0, 0, 1, true);

    public static final float PackedONE = ONE.toFloatBitABGR();

    private static final float[] PackedAlphas = new float[256];

    static {
        for (int i = 0 ;i <256;i++) {
            float a = i / 255f;
            PackedAlphas[i] = Color4.rgba(a, a, a, a, true).toFloatBitABGR();
        }
    }

    public static float getPackedAlphaBit(float a) {
        int idx = Math.round(a * 255);
        if (idx > 255) {
            idx = 255;
        }
        return PackedAlphas[idx];
    }

    public float r, g, b, a;

    public boolean premultiple = false;

    protected Color4() {

    }

    protected Color4(float ar, float ag, float ab, float aa, boolean premu) {
        set(ar, ag, ab, aa, premu);
    }

    protected Color4(Color4 c) {
        set(c);
    }

    public Color4 multipleColor(float c) {
        this.r *= c;
        this.g *= c;
        this.b *= c;
        return this;
    }

    public Color4 multipleAlpha(float a) {
        if (premultiple) {
            this.r *= a;
            this.g *= a;
            this.b *= a;
            this.a *= a;
        } else {
            this.a *= a;
        }
        return this;
    }

    public void put2buffer(FloatBuffer bf) {
        bf.put(r).put(g).put(b).put(a);
    }

    public Color4 set(Color4 c) {
        set(c.r, c.g, c.b, c.a, c.premultiple);
        return this;
    }

    public Color4 setAlpha(float a) {
        this.a = a;
        return this;
    }

    public void set(int numberBit, boolean prem) {
        r = Color.red(numberBit) / 255f;
        g = Color.green(numberBit) / 255f;
        b = Color.blue(numberBit) / 255f;
        a = Color.alpha(numberBit) / 255f;
        premultiple = prem;
    }

    public Color4 toPremultipledThis() {
        if (premultiple) {
            return this;
        } else {
            applyPremu();
            return this;
        }
    }

    private void applyPremu() {
        premultiple = true;
        r *= a;
        g *= a;
        b *= a;
    }

    public Color4 toPremultipled() {
        if (premultiple) {
            return this.copyNew();
        } else {
            Color4 c = this.copyNew();
            return c.toPremultipledThis();
        }
    }

    public Color4 set(float r, float g, float b, float a, boolean premu) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.premultiple = premu;
        return this;
    }

    public Color4 set(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        return this;
    }

    public Color4 multiple(Color4 c) {
        return multiple(c.r, c.g, c.b, c.a);
    }

    public Color4 multiple(float fr, float fg, float fb, float fa) {
        this.r *= fr;
        this.g *= fg;
        this.b *= fb;
        this.a *= fa;
        return this;
    }

    public Color4 multiple(float f) {
        return multiple(f, f, f, f);
    }

    public Color4 add(Color4 c) {
        return add(c.r, c.g, c.b, c.a);
    }

    public Color4 add(float ra, float ga, float ba, float aa) {
        return this.addRed(ra).addGreen(ga).addBlue(ba).addAlpha(aa);
    }

    public Color4 addRed(float a) {
        this.r += a;
        return this;
    }

    public Color4 addGreen(float a) {
        this.g += a;
        return this;
    }

    public Color4 addBlue(float a) {
        this.b += a;
        return this;
    }

    public Color4 addAlpha(float a) {
        this.a += a;
        return this;
    }

    public Color4 copyNew() {
        return new Color4(this);
    }

    public Vec4 toVec4() {
        return new Vec4(this);
    }

    public int getA255() {
        return (int) (a * 255);
    }

    public int getR255() {
        return (int) (r * 255);
    }

    public int getG255() {
        return (int) (g * 255);
    }

    public int getB255() {
        return (int) (b * 255);
    }

    //默认模式是argb
    public int toIntBit() {
        return Color.argb(getA255(), getR255(), getG255(), getB255());
    }

    public float toFloatBitABGR() {
        if (premultiple) {
            return Float.intBitsToFloat(((int) (255 * a) << 24) | ((int) (255 * b) << 16) | ((int) (255 * g) << 8) | ((int) (255 * r)));
        } else {
            return Float.intBitsToFloat(((int) (255 * a) << 24) | ((int) (255 * b * a) << 16) | ((int) (255 * g * a) << 8) | ((int) (255 * r * a)));
        }
    }

    public float toFloatBitABGR(float aa) {
        if (premultiple) {
            return Float.intBitsToFloat(
                    ((int) (255 * a * aa) << 24) | ((int) (255 * b * aa) << 16)
                            | ((int) (255 * g * aa) << 8) | ((int) (255 * r * aa)));
        } else {
            float a = this.a * aa;
            return Float.intBitsToFloat(
                    ((int) (255 * a ) << 24) | ((int) (255 * b * a) << 16)
                            | ((int) (255 * g * a) << 8) | ((int) (255 * r * a)));
        }
    }

    @Override
    public String toString() {

        return "(r,g,b,a)=(" + r + "," + g + "," + b + "," + a + ")";
    }
	
	/*
	public static Color4 max(Color4 c1,Color4 c2){
		return Color4.rgba(Math.max(c1.r,c2.r),Math.max(c1.g,c2.g),Math.max(c1.b,c2.b),Math.max(c1.a,c2.a));
	}
	
	public static Color4 min(Color4 c1,Color4 c2){
		return Color4.rgba(Math.min(c1.r,c2.r),Math.min(c1.g,c2.g),Math.min(c1.b,c2.b),Math.min(c1.a,c2.a));
	}
	*/


    public static Color4 mix(Color4 c1, Color4 c2, float fac) {
        return c1.copyNew().multiple(1 - fac).add(c2.r * fac, c2.g * fac, c2.b * fac, c2.a * fac);
    }

    public static Color4 mixByAlpha(Color4 dst, Color4 rsc) {
        return mix(rsc, dst, dst.a);
    }

    public static Color4 rgb(float r, float g, float b) {
        return rgba(r, g, b, 1);
    }

    public static Color4 rgba(float r, float g, float b, float a) {
        return new Color4(r, g, b, a, false);
    }

    public static Color4 rgba(float r, float g, float b, float a, boolean premu) {
        return new Color4(r, g, b, a, premu);
    }

    public static Color4 rgba255(float r, float g, float b, float a) {
        return Color4.rgba(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    public static Color4 argb255(float a, float r, float g, float b) {
        return Color4.rgba(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    public static Color4 rgb255(float r, float g, float b) {
        return Color4.rgba(r / 255f, g / 255f, b / 255f, 1);
    }

    public static Color4 argb255(int v) {
        return rgba255(Color.red(v), Color.green(v), Color.blue(v), Color.alpha(v));
    }

    public static Color4 gray(float g) {
        return rgb(g, g, g);
    }

    public static Color4 alphaMultipler(float a) {
        return Color4.rgba(1, 1, 1, a);
    }
}
