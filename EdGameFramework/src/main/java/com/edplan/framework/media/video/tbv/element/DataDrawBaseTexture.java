package com.edplan.framework.media.video.tbv.element;

import com.edplan.framework.graphics.opengl.objs.TextureVertex3D;
import com.edplan.framework.media.video.tbv.decode.TBVInputStream;

import java.io.IOException;

import com.edplan.framework.media.video.tbv.encode.TBVOutputStream;
import com.edplan.framework.graphics.opengl.buffer.Vec2Buffer;
import com.edplan.framework.graphics.opengl.buffer.Vec3Buffer;
import com.edplan.framework.graphics.opengl.buffer.Color4Buffer;
import com.edplan.framework.graphics.opengl.batch.BaseBatch;
import com.edplan.framework.graphics.opengl.batch.base.IHasPosition;
import com.edplan.framework.graphics.opengl.batch.base.IHasColor;
import com.edplan.framework.graphics.opengl.batch.base.IHasTexturePosition;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.math.Vec3;
import com.edplan.framework.graphics.opengl.objs.Color4;

import java.util.List;

import com.edplan.framework.graphics.opengl.batch.interfaces.ITexture3DBatch;
import com.edplan.framework.utils.Tag;

import java.nio.FloatBuffer;

public class DataDrawBaseTexture implements ITexture3DBatch<TextureVertex3D> {

    public int textureId;
    public TextureVertex3D[] vertexs;
    public int length;

    public Vec2Buffer texturePositionBuffer;
    public Vec3Buffer positionBuffer;
    public Color4Buffer colorBuffer;


    public DataDrawBaseTexture(int buffersize) {
        vertexs = new TextureVertex3D[buffersize];
        Vec2[] tl = new Vec2[vertexs.length];
        Vec3[] pl = new Vec3[vertexs.length];
        Color4[] cl = new Color4[vertexs.length];
        TextureVertex3D v;
        for (int i = 0; i < vertexs.length; i++) {
            v = new TextureVertex3D();
            vertexs[i] = v;
            tl[i] = v.texturePoint;
            pl[i] = v.position;
            cl[i] = v.color;
        }
        texturePositionBuffer = new Vec2Buffer(tl);
        positionBuffer = new Vec3Buffer(pl);
        colorBuffer = new Color4Buffer(cl);
    }

    public void expand(int targetSize) {
        if (vertexs.length >= targetSize) return;
        final TextureVertex3D[] pre = vertexs;
        vertexs = new TextureVertex3D[targetSize];
        for (int i = 0; i < pre.length; i++) {
            vertexs[i] = pre[i];
            pre[i] = null;
        }
        for (int i = pre.length; i < targetSize; i++) {
            final TextureVertex3D v = new TextureVertex3D();
            vertexs[i] = v;
            texturePositionBuffer.add(v.texturePoint);
            positionBuffer.add(v.position);
            colorBuffer.add(v.color);
        }
    }

    @Override
    public void add(TextureVertex3D[] ts) {

        for (TextureVertex3D v : ts) {
            add(v);
        }
    }

    @Override
    public void add(TextureVertex3D t) {

        if (length >= vertexs.length) expand(vertexs.length * 3 / 2 + 5);
        vertexs[length].set(t);
        length++;
    }

	/*
	@Override
	public void add(TextureVertex3D[] ts) {

		for(TextureVertex3D v:ts){
			add(v);
		}
	}
	*/

    @Override
    public int getVertexCount() {

        return length;
    }

    @Override
    public FloatBuffer makePositionBuffer() {

        positionBuffer.limit(true, 0, length);
        return null;//positionBuffer;
    }

    @Override
    public FloatBuffer makeColorBuffer() {

        colorBuffer.limit(true, 0, length);
        return null;//colorBuffer;
    }

    @Override
    public FloatBuffer makeTexturePositionBuffer() {

        texturePositionBuffer.limit(true, 0, length);
        return null;//texturePositionBuffer;
    }

    @Override
    public void clear() {

        length = 0;
    }

    public static DataDrawBaseTexture read(TBVInputStream in, DataDrawBaseTexture d) throws IOException {
        int id = in.readInt();
        int length = in.readInt();
        if (d == null) {
            d = new DataDrawBaseTexture(length + 20);
        } else if (d.vertexs.length < length) {
            d.expand(Math.max(length, d.vertexs.length * 3 / 2 + 5));
        }
        d.textureId = id;
        d.length = length;
        for (int i = 0; i < length; i++) {
            in.readTextureVertex3D(d.vertexs[i]);
        }
        return d;
    }

    public static DataDrawBaseTexture load(int id, List<TextureVertex3D> l, DataDrawBaseTexture d) {
        if (d == null) {
            d = new DataDrawBaseTexture(l.size() + 20);
        } else if (d.vertexs.length < l.size()) {
            d.expand(Math.max(l.size(), d.vertexs.length * 3 / 2 + 5));
        }
        d.textureId = id;
        d.length = l.size();
        for (int i = 0; i < d.length; i++) {
            d.vertexs[i].set(l.get(i));
        }
        return d;
    }

    public static DataDrawBaseTexture load(int id, BaseBatch batch, DataDrawBaseTexture d) {
        if (d == null) {
            d = new DataDrawBaseTexture(batch.getVertexCount() + 20);
        } else if (d.vertexs.length < batch.getVertexCount()) {
            d.expand(Math.max(batch.getVertexCount(), d.vertexs.length * 3 / 2 + 5));
        }
        d.textureId = id;
        d.length = batch.getVertexCount();
		/*
		Vec2Buffer tp=((IHasTexturePosition)batch).makeTexturePositionBuffer();
		Vec3Buffer pp=((IHasPosition)batch).makePositionBuffer();
		Color4Buffer cp=((IHasColor)batch).makeColorBuffer();
		for(int i=0;i<d.length;i++){
			d.vertexs[i].texturePoint.set(tp.get(i));
			d.vertexs[i].position.set(pp.get(i));
			d.vertexs[i].color.set(cp.get(i));
		}
		*/
        return d;
    }

    public static void write(TBVOutputStream out, DataDrawBaseTexture d) throws IOException {
        out.writeInt(d.textureId);
        out.writeInt(d.length);
        for (int i = 0; i < d.length; i++) {
            out.writeTextureVertex3D(d.vertexs[i]);
        }
    }

}
