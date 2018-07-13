package com.edplan.framework.graphics.opengl;

import com.edplan.framework.graphics.opengl.shader.advance.Texture3DShader;
import com.edplan.framework.interfaces.Copyable;
import com.edplan.framework.interfaces.Recycleable;
import com.edplan.framework.math.Mat4;
import com.edplan.framework.math.Vec2;

public class CanvasData implements Recycleable, Copyable {

    private float width;

    private float height;

    private Camera camera;

    private float pixelDensity = 1;

    private float canvasAlpha = 1;

    private ShaderManager shaders = new ShaderManager();

    public CanvasData(CanvasData c) {
        this.camera = c.camera.copy();
        this.width = c.width;
        this.height = c.height;
        this.pixelDensity = c.pixelDensity;
        this.canvasAlpha = c.canvasAlpha;
        this.shaders.set(c.shaders);
    }

    public CanvasData() {
        camera = new Camera();
    }

    public void setShaders(ShaderManager shaders) {
        this.shaders.set(shaders);
    }

    public ShaderManager getShaders() {
        return shaders;
    }

    public void setCanvasAlpha(float canvasAlpha) {
        this.canvasAlpha = canvasAlpha;
    }

    public float getCanvasAlpha() {
        return canvasAlpha;
    }

    public void setPixelDensity(float pixelDensity) {
        this.pixelDensity = pixelDensity;
    }

    /**
     * 定义了canvas上每单位有多少像素
     */
    public float getPixelDensity() {
        return pixelDensity;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getWidth() {
        return width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getHeight() {
        return height;
    }

    public void setCurrentProjMatrix(Mat4 projMatrix) {
        this.camera.setProjectionMatrix(projMatrix);
        freshMatrix();
    }

    public Mat4 getCurrentProjMatrix() {
        return camera.getProjectionMatrix();
    }

    public void setTexture3DShader(Texture3DShader texture3DShader) {
        this.shaders.setTexture3DShader(texture3DShader);
    }

    public Texture3DShader getTexture3DShader() {
        return shaders.getTexture3DShader();
    }

    public void setCurrentMaskMatrix(Mat4 matrix) {
        this.camera.setMaskMatrix(matrix);
    }

    /**
     * 每次直接操作之后要freshMatrix，否则效果不会显示
     */
    public Mat4 getCurrentMaskMatrix() {
        return camera.getMaskMatrix();
    }

    public CanvasData translate(float tx, float ty) {
        getCurrentMaskMatrix().translate(tx, ty, 0);
        freshMatrix();
        return this;
    }

    public CanvasData rotate(float rotation) {
        getCurrentMaskMatrix().rotate2D(0, 0, rotation, true);
        freshMatrix();
        return this;
    }

    public CanvasData rotate(float ox, float oy, float rotation) {
        getCurrentMaskMatrix().rotate2D(ox, oy, rotation, true);
        freshMatrix();
        return this;
    }

    //可能导致部分运算误差（像素密度相关）
    public CanvasData scale(float sx, float sy) {
        getCurrentMaskMatrix().scale(sx, sy, 1);
        freshMatrix();
        return this;
    }

    /**
     * 对轴进行缩放，而不是对物件缩放，所以处理Matrix时用倒数
     */
    public CanvasData scaleContent(float s) {
        if (s == 0)
            throw new IllegalArgumentException("you can't scale content using a scale rate ==0");
        float rs = 1 / s;
        getCurrentMaskMatrix().scale(rs, rs, 1);
        freshMatrix();
        this.pixelDensity *= s;
        return this;
    }

    public void freshMatrix() {
        camera.refresh();
    }
	
	/*
	public Mat4 getFinalMatrix(){
		return camera.getFinalMatrix();
	}
	*/

    public Camera getCamera() {
        return camera;
    }

    public CanvasData clip(float w, float h) {
        setWidth(w);
        setHeight(h);
        return this;
    }

    @Override
    public void recycle() {

        this.camera = null;
    }

    @Override
    public Copyable copy() {

        return new CanvasData(this);
    }
}
