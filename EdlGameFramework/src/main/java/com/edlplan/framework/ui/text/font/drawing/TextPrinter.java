package com.edlplan.framework.ui.text.font.drawing;

import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.graphics.opengl.batch.v2.BatchEngine;
import com.edlplan.framework.graphics.opengl.batch.v2.object.PackedTriangles;
import com.edlplan.framework.graphics.opengl.batch.v2.object.TextureTriangle;
import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.graphics.opengl.shader.advance.Texture3DShader;
import com.edlplan.framework.math.IQuad;
import com.edlplan.framework.math.RectF;
import com.edlplan.framework.ui.text.font.bmfont.BMFont;
import com.edlplan.framework.ui.text.font.bmfont.FNTChar;
import com.edlplan.framework.ui.text.font.bmfont.FNTKerning;

public class TextPrinter {

    public static final char NO_PREVIOUS_CHAR = 0;
    /**
     * @field textSize:实际绘制时的大小
     * @field currentBaseY:当前绘制的起始Y坐标
     * @field currentX:当前绘制的起始X
     * @field scale:缩放，设置textSize时自动计算
     */
    private float textSize;

    private float lineHeight;

    private float currentBaseY;

    private float currentX;

    private float startX;

    private float startY;

    private float scale;

    private float alpha = 1;

    private Color4 accentColor = Color4.White.copyNew();

    private PackedTriangles[] triangles;

    private char preChar = 0;

    private BMFont font;

    private boolean singleline = false;

    private boolean useFontShader = false;

    public TextPrinter(BMFont font, float startX, float startY, float alpha, Color4 accentColor) {
        this.font = font;
        this.accentColor.set(accentColor);
        this.alpha = alpha;
        this.startX = startX;
        this.startY = startY;
        initial(startX, startY);
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void setAccentColor(Color4 accentColor) {
        this.accentColor.set(accentColor);
    }

    public void setUseFontShader(boolean useFontShader) {
        this.useFontShader = useFontShader;
    }

    public void setSingleline(boolean singleline) {
        this.singleline = singleline;
    }

    public boolean isSingleline() {
        return singleline;
    }

    public void setCurrentBaseY(float currentBaseY) {
        this.currentBaseY = currentBaseY;
    }

    public float getCurrentBaseY() {
        return currentBaseY;
    }

    public void setCurrentX(float currentX) {
        this.currentX = currentX;
    }

    public float getCurrentX() {
        return currentX;
    }


    public void initial(float startX, float startY) {
        this.lineHeight = font.getCommon().lineHeight;
        recalScale();
        currentX = startX;
        currentBaseY = startY;
        setTextSize(lineHeight);

        triangles = new PackedTriangles[font.getPageCount()];
        for (int i = 0; i < font.getPageCount(); i++) {
            triangles[i] = new PackedTriangles();
        }
    }

    private void recalScale() {
        scale = textSize / lineHeight;
    }

    public float getScale() {
        return scale;
    }

    public void setTextSize(float s) {
        textSize = s;
        recalScale();
    }

    public void printString(String str) {
        for (int i = 0; i < str.length(); i++) {
            printChar(str.charAt(i));
        }
    }

    public void printChars(char... cs) {
        for (char c : cs) {
            printChar(c);
        }
    }

    public void toNextLine() {
        currentX = startX;
        currentBaseY += textSize;
    }

    //移动当前光标
    public void addOffset(float x, float y) {
        currentX += x;
        currentBaseY += y;
    }

    public void addOffsetRaw(float x, float y) {
        currentX += x * scale;
        currentBaseY += y * scale;
    }

    public void printLineCenter(String text, float cx) {
        currentX = cx - TextUtils.calwidth(font, text) * scale / 2;
        printString(text);
    }

    public void printLineLeft(String text, float x) {
        currentX = x;
        printString(text);
    }

    public void printLineRight(String text, float r) {
        currentX = r - TextUtils.calwidth(font, text) * scale;
        printString(text);
    }

    public void printChar(char c) {
        if ((!singleline) && c == '\n') {
            toNextLine();
            return;
        }
        if (c == '	') {
            printString("    ");
            return;
        }
        FNTChar fntc = font.getFNTChar(c);
        if (fntc != null) {
            RectF area = calCharArea(fntc);
            float xadvance = fntc.xadvance;
            if (preChar != NO_PREVIOUS_CHAR) {
                FNTKerning kerning = font.getKerning(preChar, c);
                if (kerning != null) {
                    //area.move(kerning.amount,0);
                    //xadvance+=kerning.amount;
                }
            }
            xadvance *= scale;
            IQuad tq = fntc.rawTextureArea;
            triangles[fntc.page].add(new TextureTriangle(
                    area.getTopLeft(),
                    area.getTopRight(),
                    area.getBottomRight(),
                    tq.getTopLeft(),
                    tq.getTopRight(),
                    tq.getBottomRight(),
                    alpha
            ));
            triangles[fntc.page].add(new TextureTriangle(
                    area.getTopLeft(),
                    area.getBottomRight(),
                    area.getBottomLeft(),
                    tq.getTopLeft(),
                    tq.getBottomRight(),
                    tq.getBottomLeft(),
                    alpha
            ));


            preChar = c;
            currentX += xadvance;
        } else {
            printErrCharacter();
        }
    }

    private RectF calCharArea(FNTChar fntc) {
        float x = currentX + fntc.xoffset * scale;
        float y = currentBaseY - fntc.tobase * scale;
        RectF area = RectF.xywh(x, y, fntc.width * scale, fntc.height * scale);
        //y方向的offset和绘制坐标系的方向相反
        //area.move(fntc.xoffset*scale,-fntc.yoffset*scale*0);
        return area;
    }

    public void printErrCharacter() {
        FNTChar fntc = font.getErrCharacter();
        if (fntc == null) {
            fntc = font.getFNTChar(' ');
        }
        RectF area = calCharArea(fntc);
        float xadvance = fntc.xadvance;
        xadvance *= scale;
        IQuad tq = fntc.rawTextureArea;
        triangles[fntc.page].add(new TextureTriangle(
                area.getTopLeft(),
                area.getTopRight(),
                area.getBottomRight(),
                tq.getTopLeft(),
                tq.getTopRight(),
                tq.getBottomRight(),
                alpha
        ));
        triangles[fntc.page].add(new TextureTriangle(
                area.getTopLeft(),
                area.getBottomRight(),
                area.getBottomLeft(),
                tq.getTopLeft(),
                tq.getBottomRight(),
                tq.getBottomLeft(),
                alpha
        ));

        preChar = NO_PREVIOUS_CHAR;
        currentX += xadvance;
    }

    public boolean useFontShader() {
        return useFontShader || font.isUseFontShader();
    }

    public void draw(BaseCanvas canvas) {
        BatchEngine.flush();
        Color4 color4 = BatchEngine.getShaderGlobals().accentColor;
        BatchEngine.getShaderGlobals().accentColor = accentColor;

        for (int i = 0; i < font.getPageCount(); i++) {
            triangles[i].render(
                    useFontShader() ? Texture3DShader.FONT_SHADER.get() : Texture3DShader.DEFAULT.get(),
                    font.getPage(i).texture.getTexture(),
                    BatchEngine.getShaderGlobals()
            );
        }

        BatchEngine.getShaderGlobals().accentColor = color4;
    }

}
