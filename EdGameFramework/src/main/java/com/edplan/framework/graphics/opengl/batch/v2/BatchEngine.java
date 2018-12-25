package com.edplan.framework.graphics.opengl.batch.v2;

import com.edplan.framework.graphics.opengl.Camera;
import com.edplan.framework.graphics.opengl.shader.ShaderGlobals;

public class BatchEngine {

    private static AbstractBatch savedbatch = null;

    private static ShaderGlobals shaderGlobals = new ShaderGlobals();

    public static ShaderGlobals getShaderGlobals() {
        return shaderGlobals;
    }

    /**
     * 设置全局透明度，
     * @param alpha
     */
    public static void setGlobalAlpha(float alpha) {
        if (Math.abs(shaderGlobals.alpha - alpha) > 0.002f) {
            flush();
            shaderGlobals.alpha = alpha;
        }
    }

    /**
     * 更换全局相机，调用时必定触发flush
     * @param camera
     */
    public static void setGlobalCamera(Camera camera) {
        flush();
        shaderGlobals.camera.set(camera);
    }

    static void bind(AbstractBatch batch) {
        if (savedbatch != null) {
            savedbatch.flush();
        }
        savedbatch = batch;
    }

    static void unbind(AbstractBatch batch) {
        if (savedbatch == batch) {
            savedbatch = null;
        }
    }

    public static void flush() {
        if (savedbatch != null) {
            savedbatch.flush();
        }
    }

    public static AbstractBatch currentBatch() {
        return savedbatch;
    }
}
