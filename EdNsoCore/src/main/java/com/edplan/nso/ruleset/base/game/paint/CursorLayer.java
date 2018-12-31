package com.edplan.nso.ruleset.base.game.paint;

import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuad;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuadBatch;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.utils.FloatRef;
import com.edplan.nso.ruleset.base.game.World;
import com.edplan.nso.ruleset.base.game.judge.CursorData;
import com.edplan.nso.ruleset.base.game.judge.CursorTestObject;

public class CursorLayer extends AdvancedDrawObject {

    private CursorTestObject cursor;

    private ParticleHolder particles;

    private TextureQuad[] cursorQuads;

    private AbstractTexture cursorTexture, cursorTrail;

    private FloatRef duration = new FloatRef(200);

    private Vec2 scale;

    public CursorLayer(CursorTestObject cursor, AbstractTexture cursorTexture, AbstractTexture cursorTrail, float scale) {
        this.cursor = cursor;
        this.scale = new Vec2(scale);

        this.cursorTexture = cursorTexture;
        this.cursorTrail = cursorTrail;

        particles = new ParticleHolder();
        particles.setFactory(new ParticleHolder.SimpleFadeParticleFactory());

        cursorQuads = new TextureQuad[cursor.holders.length];
        for (int i = 0; i < cursorQuads.length; i++) {
            cursorQuads[i] = new TextureQuad();
            cursorQuads[i].setTextureAndSize(cursorTexture);
            cursorQuads[i].size.zoom(scale);
            cursorQuads[i].position = cursor.holders[i].pos;
        }
    }

    Vec2[] prePos = new Vec2[CursorData.MAX_CURSOR_COUNT];

    @Override
    protected void onDraw(BaseCanvas canvas, World world) {

        for (CursorTestObject.Holder holder : cursor.holders) {
            if (holder.down) {
                Vec2 pre = prePos[holder.id];
                if (pre != null) {
                    int psize = (int) (Vec2.length(pre, holder.pos) / 5) + 1;
                    particles.genarateParticles(
                            new ParticleHolder.ParticleHint() {
                                int idx;

                                {
                                    startTime = (float) world.getPaintFrameTime();
                                    duration = () -> CursorLayer.this.duration;
                                    scale = () -> CursorLayer.this.scale;
                                    position = () -> {
                                        idx++;
                                        float p = idx / (float) psize;
                                        return pre.copy().zoom(1 - p).add(holder.pos.copy().zoom(p));
                                    };
                                    texture1 = cursorTrail;
                                }
                            },
                            psize
                    );

                }
                prePos[holder.id] = holder.pos.copy();
            } else {
                prePos[holder.id] = null;
            }
        }

        particles.onDraw(canvas, world);

        for (CursorTestObject.Holder holder : cursor.holders) {
            if (holder.down) {
                TextureQuadBatch.getDefaultBatch().add(cursorQuads[holder.id]);
            }
        }
    }
}
