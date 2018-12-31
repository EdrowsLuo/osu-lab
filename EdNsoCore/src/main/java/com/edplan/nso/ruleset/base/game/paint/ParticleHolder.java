package com.edplan.nso.ruleset.base.game.paint;

import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuad;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuadBatch;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.utils.Factory;
import com.edplan.framework.utils.FloatRef;
import com.edplan.framework.utils.advance.LinkedNode;
import com.edplan.nso.ruleset.base.game.World;

import java.util.Random;

public class ParticleHolder extends AdvancedDrawObject{

    LinkedNode<Particle> first = new LinkedNode<>();

    LinkedNode<Particle> last = new LinkedNode<>();

    ParticleFactory factory;

    public ParticleHolder() {
        first.value = last.value = new Particle(first) {
            @Override
            public void draw(float t) {

            }
        };
        first.next = last;
        last.pre = first;
    }

    public void setFactory(ParticleFactory factory) {
        this.factory = factory;
    }

    public void genarateParticles(ParticleHint hint, int size) {
        if (factory != null) {
            factory.nextParticles(hint, last.pre, size);
        }
    }

    @Override
    protected void onDraw(BaseCanvas canvas, World world) {
        LinkedNode<Particle> cur = first;
        LinkedNode<Particle> next = cur.next;
        final float time = (float) world.getPaintFrameTime();
        while (next != null) {
            cur.value.draw(time);
            cur = next;
            next = cur.next;
        }
    }

    public static abstract class Particle {

        LinkedNode<Particle> node;

        public Particle(LinkedNode<Particle> node) {
            this.node = node;
        }

        public void remove() {
            if (node != null) {
                node.removeFromList();
            }
        }

        public abstract void draw(float time);

    }

    public static class ParticleHint {

        public static Random random = new Random();

        public float startTime;

        public Factory<Color4> accentColor;

        public Factory<Vec2> position;

        public Factory<Vec2> size;

        public Factory<Vec2> scale;

        public Factory<FloatRef> duration;

        public Factory<FloatRef> gravity;

        public Factory<Vec2> speed;

        public AbstractTexture texture1;

    }

    public static abstract class ParticleFactory {

        public abstract void nextParticles(ParticleHint hint, LinkedNode<Particle> particles, int size);

    }

    public static class SimpleFadeParticleFactory extends ParticleFactory {

        /**
         * 需要提供：
         * startTime
         * duration
         * scale
         * position
         * texture1
         */
        @Override
        public void nextParticles(ParticleHint hint, LinkedNode<Particle> particles, int size) {
            final float startTime = hint.startTime;
            final TextureQuadBatch batch = TextureQuadBatch.getDefaultBatch();
            final AbstractTexture texture = hint.texture1;
            while (size > 0) {
                size--;
                LinkedNode<Particle> p = new LinkedNode<>();
                float duration = hint.duration.create().value;
                p.value = new Particle(p) {

                    TextureQuad quad = new TextureQuad();

                    {
                        quad.setTextureAndSize(texture);
                        quad.position = hint.position.create();
                        quad.size.multiple(hint.scale.create());
                        quad.enableScale();
                    }

                    @Override
                    public void draw(float time) {
                        if (time - startTime > duration) {
                            remove();
                        } else {
                            float p = (time - startTime) / duration;
                            quad.alpha.value = 1 - p;
                            quad.scale.set(1 - p * 0.5f);
                            batch.add(quad);
                        }
                    }
                };
                particles.insertToNext(p);
                particles = p;
            }
        }

    }

}
