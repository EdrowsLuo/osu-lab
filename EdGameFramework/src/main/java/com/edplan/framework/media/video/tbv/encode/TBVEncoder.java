package com.edplan.framework.media.video.tbv.encode;

import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.BlendProperty;
import com.edplan.framework.graphics.opengl.BlendSetting;
import com.edplan.framework.graphics.opengl.BlendType;
import com.edplan.framework.graphics.opengl.CanvasData;
import com.edplan.framework.graphics.opengl.ShaderManager;
import com.edplan.framework.graphics.opengl.batch.Texture3DBatch;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.TextureVertex3D;
import com.edplan.framework.graphics.opengl.shader.advance.Texture3DShader;
import com.edplan.framework.math.Mat4;
import com.edplan.framework.media.video.tbv.TBV;
import com.edplan.framework.media.video.tbv.TBVException;
import com.edplan.framework.media.video.tbv.element.DataDrawBaseTexture;
import com.edplan.framework.utils.Tag;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 操作应该严格按照规范
 * 1)初始化，将TBV.Header输出
 * 2)开始帧绘制
 * 3)Loop:
 * 1)重填帧头数据，开始缓存
 * 2)Loop:
 * 输出各种事件
 * break：当输出帧结束时结束
 * 3)回填缓存块长度，将帧头和缓存输出
 * break：输出结束帧
 * 结束encode
 */


public class TBVEncoder {
    public enum EncodeMainState {
        WriteHeader,
        FrameOutput,
        EndFrameOutput,
        EncodeDone
    }

    public enum EncodeFrameState {
        WriteFrameHeader,
        BufferFrameData,
        EndBufferFrameData,
        OutputFrameData,
        EndFrameOutput
    }

    public enum FrameEventState {
        WritingHeader
    }

    public EncodeMainState mainState;

    public EncodeFrameState frameState;

    private TBVOutputStream out;

    private TBVOutputStream bufferedOut;

    private BufferedByteOutputStream rawBufferedOut;

    public TBV.Header header = new TBV.Header();

    private TBV.FrameHeader frameHeader = new TBV.FrameHeader();

    private TBV.EventHeader eventHeader = new TBV.EventHeader();

    private float currentPlayTime = 0;

    private double frameDeltaTime = 1000 / 60d;

    private TBVCanvas canvas;

    public TBVEncoder(OutputStream out) {
        this.out = new TBVOutputStream(new DataOutputStream(out));
    }

    public void initial(int width, int height) {
        header.width = width;
        header.height = height;
        rawBufferedOut = new BufferedByteOutputStream(1024 * 32);
        bufferedOut = new TBVOutputStream(new DataOutputStream(rawBufferedOut));
        canvas = new TBVCanvas(this);
    }

    public TBVCanvas getCanvas() {
        return canvas;
    }

    public TBV.Header.Builder getHeaderBuilder() {
        mainState = EncodeMainState.WriteHeader;
        return TBV.Header.Builder.create(header);
    }

    public void loadBack(TBV.Header.Builder b) throws TBVException, IOException {
        if (b.target != header)
            throw new TBVException("wtf? you should loadBack the builder you got from getHeaderBuilder");
        b.build();
        writeTBVHeader();
    }

    protected void writeTBVHeader() throws TBVException, IOException {
        if (mainState != EncodeMainState.WriteHeader) {
            throw new TBVException("错误的步骤 " + mainState);
        }
        TBV.Header.write(out, header);
        mainState = EncodeMainState.FrameOutput;
        frameState = EncodeFrameState.WriteFrameHeader;
    }

    protected void checkFrameState(EncodeFrameState s) throws TBVException {
        if (frameState != s) {
            throw new TBVException("错误的步骤 " + frameState);
        }
    }

    public void toNewFrame(double deltaTime, boolean clearFrame, short flag) throws TBVException {
        if (deltaTime <= 0) {
            toNewFrame(frameDeltaTime, clearFrame, flag);
            return;
        }
        checkOutputFrame();
        checkFrameState(EncodeFrameState.WriteFrameHeader);
        frameHeader.flag = flag;
        frameHeader.clearCanvas = clearFrame;
        frameHeader.startTime = currentPlayTime;
        currentPlayTime += deltaTime;
        frameHeader.endTime = currentPlayTime;
        frameState = EncodeFrameState.BufferFrameData;
    }

    protected void flushEventHeader(short type) throws IOException {
        eventHeader.eventType = type;
        TBV.EventHeader.write(bufferedOut, eventHeader);
    }

    private TBV.SettingEvent settingEvent = new TBV.SettingEvent();

    @Tag("frameEvent::setting")
    public void setBlendType(boolean enable, BlendType type) throws IOException {
        flushEventHeader(TBV.FrameEvent.CHANGE_SETTING);
        settingEvent.setBlend(enable, type);
        TBV.SettingEvent.write(bufferedOut, settingEvent);
    }

    DataDrawBaseTexture bufferedDraw;

    @Tag("frameEvent::draw")
    public void drawBaseTexture() throws IOException {
        flushEventHeader(TBV.FrameEvent.DRAW_BASE_TEXTURE);
        DataDrawBaseTexture.write(bufferedOut, bufferedDraw);
    }

    public DataDrawBaseTexture getBufferedDraw() {
        if (bufferedDraw == null) bufferedDraw = new DataDrawBaseTexture(50);
        return bufferedDraw;
    }

    public void currentFrameFinish() throws TBVException, IOException {
        checkFrameState(EncodeFrameState.BufferFrameData);
        flushEventHeader(TBV.FrameEvent.FRAME_END);
        frameState = EncodeFrameState.EndBufferFrameData;
        outputFrame();
    }

    protected void outputFrame() throws TBVException, IOException {
        checkOutputFrame();
        checkFrameState(EncodeFrameState.EndBufferFrameData);
        frameHeader.blockSize = rawBufferedOut.size();
        TBV.FrameHeader.write(out, frameHeader);
        if (rawBufferedOut.size() != 0)
            out.writeBytes(rawBufferedOut.ary, 0, rawBufferedOut.size());
        rawBufferedOut.reset();
        if (frameHeader.flag == TBV.Frame.END_FRAME) {
            frameState = EncodeFrameState.EndFrameOutput;
            mainState = EncodeMainState.EndFrameOutput;
            endEncode();
        } else {
            frameState = EncodeFrameState.WriteFrameHeader;
        }
    }

    public void endEncode() throws TBVException, IOException {
        if (mainState != EncodeMainState.EndFrameOutput) {
            throw new TBVException("错误的步骤 " + mainState);
        }
        mainState = EncodeMainState.EncodeDone;
    }

    public void checkOutputFrame() throws TBVException {
        if (mainState != EncodeMainState.FrameOutput) {
            throw new TBVException("错误的步骤 " + mainState);
        }
    }

    public void setFrameDeltaTime(double frameDeltaTime) {
        this.frameDeltaTime = frameDeltaTime;
    }

    public double getFrameDeltaTime() {
        return frameDeltaTime;
    }

    private void handlerException(Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
    }

    public class TBVCanvas extends BaseCanvas {

        private boolean prepare = false;

        private TBVBlending tbvBlending = new TBVBlending();

        private TBVEncoder e;

        public TBVCanvas(TBVEncoder e) {
            super(false);
            this.e = e;
            System.out.println("encoder : " + e);
            initial();
            initialBatch();
        }

        @Override
        protected Texture3DBatch<TextureVertex3D> createTexture3DBatch() {

            return null;//e.getBufferedDraw();
        }

        @Override
        protected void onPostBlendingChange(BlendType type) {

            super.onPostBlendingChange(type);
            try {
                setBlendType(true, type);
            } catch (IOException e) {
                handlerException(e);
            }
        }

        @Override
        public boolean isPrepared() {

            return prepare;
        }

        @Override
        public void prepare() {

            prepare = true;
        }

        @Override
        public void unprepare() {

            prepare = false;
        }

        @Override
        public int getDefWidth() {

            return e.header.width;
        }

        @Override
        public int getDefHeight() {

            return e.header.height;
        }

        @Override
        public BlendSetting getBlendSetting() {

            return tbvBlending;
        }

        @Override
        protected void checkCanDraw() {

            try {
                e.checkFrameState(EncodeFrameState.BufferFrameData);
                if (!isEnablePost()) {
                    handlerException(new TBVException("in TBVCanvas, you can only draw in post mode"));
                }
            } catch (TBVException ex) {
                e.handlerException(ex);
            }
        }

        @Override
        public CanvasData getDefData() {

            CanvasData d = new CanvasData();
            d.setCurrentProjMatrix(createDefProjMatrix());
            d.setCurrentMaskMatrix(Mat4.createIdentity());
            d.setHeight(getDefWidth());
            d.setWidth(getDefHeight());
            d.setShaders(ShaderManager.getNewDefault());
            d.getShaders().setTexture3DShader(new TBVShader(e));
            return d;
        }

        @Override
        public void clearBuffer() {


        }

        @Override
        public void drawColor(Color4 c) {

        }
    }

    public class TBVBlending extends BlendSetting {

        public TBVBlending() {
            initial();
        }

        @Override
        public void apply(BlendProperty p) {


        }

    }

    public class TBVShader extends Texture3DShader.InvalidTexture3DShader {

        private TBVEncoder ec;

        public TBVShader(TBVEncoder ec) {
            this.ec = ec;
        }


        @Override
        public void loadTexture(AbstractTexture texture) {

            ec.getBufferedDraw().textureId = ec.header.getTextureReflections().get(texture.getTextureId());
        }

        @Override
        public void applyToGL(int mode, int offset, int count) {

            try {
                ec.drawBaseTexture();
            } catch (IOException e) {
                ec.handlerException(e);
            }
        }
    }
}
