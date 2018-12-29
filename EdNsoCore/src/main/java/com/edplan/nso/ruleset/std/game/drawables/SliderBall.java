package com.edplan.nso.ruleset.std.game.drawables;

import com.edplan.framework.graphics.line.LinePath;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuad;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuadBatch;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.nso.ruleset.base.game.World;
import com.edplan.nso.ruleset.base.game.paint.AnimateTextureQuadObject;

public class SliderBall extends AnimateTextureQuadObject {

    private float progress = 0;

    private boolean forward = true;

    private LinePath path;

    private float lenghth;

    public SliderBall(LinePath path,float lenghth) {
        this.path = path;
        this.lenghth = lenghth;
        sprite.enableRotation();
    }

    public void setProgress(float progress, boolean forward) {
        this.progress = progress;
        this.forward = forward;
    }

    public void update() {
        sprite.position.set(path.getMeasurer().atLength(lenghth * progress));
        sprite.rotation.value = path.getMeasurer().getTangentLine(lenghth * progress).theta();
    }

    public void buildAnimation(double start, double durationPerRev, int times) {
        addIntervalTask(start, start + durationPerRev * times, time -> {
            double t = time - start;
            double pro = t % durationPerRev;
            pro /= durationPerRev;
            boolean rev = ((int) (t / durationPerRev)) % 2 == 1;
            setProgress((float) (rev ? (1 - pro) : pro), rev);
        });
    }

    @Override
    protected void onDraw(BaseCanvas canvas, World world) {
        update();
        super.onDraw(canvas, world);
       /* TextureQuad quad = new TextureQuad();
        quad.setTextureAndSize(sprite.texture);
        quad.size = sprite.size;
        quad.position = sprite.position;
        quad.rotation = sprite.rotation;
        TextureQuadBatch.getDefaultBatch().add(quad);*/
    }
}
