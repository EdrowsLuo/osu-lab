package com.edplan.framework.media.video.tbv.decode;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.layer.BufferedLayer;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.BlendType;
import com.edplan.framework.graphics.opengl.CanvasUtil;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.interfaces.Reflection;
import com.edplan.framework.media.video.tbv.TBV;
import com.edplan.framework.media.video.tbv.TBVException;
import com.edplan.framework.media.video.tbv.TBVJson;
import com.edplan.framework.media.video.tbv.TextureNode;
import com.edplan.framework.media.video.tbv.element.DataDrawBaseTexture;
import com.edplan.framework.resource.AResource;

import org.json.JSONException;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TBVRenderer {
    private boolean hasReacheEndFrame = false;

    private TBVInputStream in;

    private GLTexture[] textures;

    private TBV.Header header;

    private TBV.FrameHeader frameHeader;

    private TBV.EventHeader eventHeader;

    private float currentPlayTime = 0;

    private BufferedLayer layer;

    private BaseCanvas canvas;

    private boolean frameIsRead = true;

    private MContext context;

    public TBVRenderer(InputStream in) {
        this.in = new TBVInputStream(new DataInputStream(in));

    }

    public float getCurrentPlayTime() {
        return currentPlayTime;
    }

    public void initial(MContext context, final AResource res) throws JSONException, TBVException, IOException {
        initial(context, new Reflection<String, GLTexture>() {
            @Override
            public GLTexture invoke(String t) {

                try {
                    return res.loadTexture(t);
                } catch (IOException e) {
                    e.printStackTrace();
                    return GLTexture.ErrorTexture;
                }
            }
        });
    }

    public void initial(MContext context, Reflection<String, GLTexture> loader) throws JSONException, TBVException, IOException {
        this.context = context;
        header = TBV.Header.read(in, header);
        textures = new GLTexture[header.textures.length];
        for (TextureNode msg : header.textures) {
            GLTexture t = loader.invoke(msg.texture);
            textures[msg.id] = t;
            header.getTextureReflections().put(t.getTextureId(), msg.id);
        }
        layer = new BufferedLayer(context, header.width, header.height, true);
        canvas = new GLCanvas2D(layer);
        String canvasOperation = header.jsonData.optString(TBVJson.OperatCanvas);
        if (canvasOperation != null && !canvasOperation.isEmpty()) {
            CanvasUtil.operateCanvas(canvas, canvasOperation);
        }
    }

    public AbstractTexture getResult() {
        return layer.getTexture();
    }

    public void postFrame(float currentPlayTime) {
        if (hasReacheEndFrame) return;
        this.currentPlayTime = currentPlayTime;
        try {
            handlerFrame();
        } catch (IOException e) {
            e.printStackTrace();
            onErr(e);
        }
    }

    protected void handlerFrame() throws IOException {
        if (frameHeader != null && frameHeader.startTime > currentPlayTime) {
            return;
        }
        handlerNewFrame(false);
    }

    protected void handlerNewFrame(boolean forceClear) throws IOException {
        if (hasReacheEndFrame) return;
        frameHeader = frameIsRead ? TBV.FrameHeader.read(in, frameHeader) : frameHeader;
        frameIsRead = false;
        switch (frameHeader.flag) {
            case TBV.Frame.START_FRAME:
            case TBV.Frame.NORMAL_FRAME: {
                if (currentPlayTime > frameHeader.endTime) {
                    skipCurrentFrame();
                    handlerNewFrame(forceClear || frameHeader.clearCanvas);
                    return;
                }
                renderNewFrame(forceClear);
            }
            break;
            case TBV.Frame.END_FRAME: {
                hasReacheEndFrame = true;
                return;
            }
            default:
                return;
        }
    }

    private void skipCurrentFrame() throws IOException {
        if (!frameIsRead) {
            in.skip(frameHeader.blockSize);
            frameIsRead = true;
        }
    }

    protected void renderNewFrame(boolean forceClear) throws IOException {
        frameIsRead = true;
        if (!canvas.isPrepared()) canvas.prepare();
        if (frameHeader.clearCanvas || forceClear || true) {
            canvas.drawColor(Color4.Alpha);
            canvas.clearBuffer();
        }
        if (frameHeader.blockSize == 0) return;
        //in.mark(frameHeader.blockSize);

        int canvasState = canvas.save();
        int blendState = canvas.getBlendSetting().save();
        try {
            L:
            while (true) {
                eventHeader = TBV.EventHeader.read(in, eventHeader);
                switch (eventHeader.eventType) {
                    case TBV.FrameEvent.CANVAS_OPERATION: {
                        operateCanvas();
                    }
                    break;
                    case TBV.FrameEvent.CHANGE_SETTING: {
                        changeSetting();
                    }
                    break;
                    case TBV.FrameEvent.DRAW_BASE_TEXTURE: {
                        drawBaseTexture();
                    }
                    break;
                    case TBV.FrameEvent.DRAW_COLOR_VERTEX: {
                        drawColorVertex();
                    }
                    break;
                    case TBV.FrameEvent.FRAME_END:
                    default:
                        break L;
                }
            }
        } catch (IOException ie) {
            ie.printStackTrace();
            throw ie;
        } catch (Exception e) {
            e.printStackTrace();
            onErr(e);
            frameIsRead = false;
            //in.reset();
        } finally {
            canvas.restoreToCount(canvasState);
            canvas.getBlendSetting().restoreToCount(blendState);
            if (canvas.isPrepared()) canvas.unprepare();
        }
    }


    protected void operateCanvas() {

    }

    TBV.SettingEvent settingEvent;

    protected void changeSetting() throws IOException {
        settingEvent = TBV.SettingEvent.read(in, settingEvent);
        switch (settingEvent.type) {
            case TBV.SettingEvent.CHANGE_BLEND_TYPE: {
                changeBlend(settingEvent.data);
            }
            break;
            default:
                break;
        }
    }

    public void changeBlend(byte[] data) {
        boolean enable = data[0] != 0;
        BlendType type = BlendType.values()[data[1]];
        canvas.getBlendSetting().set(enable, type);
    }

    DataDrawBaseTexture dataDrawBaseTexture;

    protected void drawBaseTexture() throws IOException {
        dataDrawBaseTexture = DataDrawBaseTexture.read(in, dataDrawBaseTexture);
        canvas.drawTexture3DBatch(dataDrawBaseTexture, textures[dataDrawBaseTexture.textureId]);
        dataDrawBaseTexture.clear();
    }

    protected void drawColorVertex() {

    }

    public void onErr(Throwable e) {

    }
}
