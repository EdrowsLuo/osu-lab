package com.edplan.framework.graphics.opengl;

import com.edplan.framework.math.Mat4;
import com.edplan.framework.math.Vec2;

public class Camera {
    private Mat4 maskMatrix = new Mat4();
    private Mat4 projectionMatrix = new Mat4();

    private Mat4 finalMatrix = new Mat4();
    private boolean hasChange = true;


    public Camera() {
        maskMatrix.setIden();
        projectionMatrix.setIden();
    }

    public Camera(Camera c) {
        maskMatrix.set(c.maskMatrix);
        projectionMatrix.set(c.projectionMatrix);
        hasChange = true;
    }

    public Vec2 toProjPostion(float x, float y) {
        return maskMatrix.mapToProj(x, y);
    }

    public void setMaskMatrix(Mat4 maskMatrix) {
        this.maskMatrix.set(maskMatrix);
    }

    public Mat4 getMaskMatrix() {
        return maskMatrix;
    }

    public void setProjectionMatrix(Mat4 projectionMatrix) {
        this.projectionMatrix.set(projectionMatrix);
    }

    public Mat4 getProjectionMatrix() {
        return projectionMatrix;
    }

    public void refresh() {
        hasChange = true;
    }

    public Mat4 getFinalMatrix() {
        if (hasChange) {
            finalMatrix.set(maskMatrix).post(projectionMatrix);
            hasChange = false;
            return finalMatrix;
        } else {
            return finalMatrix;
        }
    }

    public Camera copy() {
        return new Camera(this);
    }

}
