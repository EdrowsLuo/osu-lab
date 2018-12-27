package com.edplan.framework.media.bass;

import android.util.Log;

import com.edplan.framework.MContext;
import com.edplan.framework.resource.AResource;
import com.un4seen.bass.BASS;

import java.io.IOException;
import java.nio.ByteBuffer;

public class BassChannel {
    private Type type;
    private int chaId;

    protected BassChannel(int chaId, Type type) {
        if (chaId == 0) {
            throw new RuntimeException("err bass: " + BASS.BASS_ErrorGetCode());
        }
        this.chaId = chaId;
        this.type = type;
    }


    public enum Type {
        Stream, Sample, Music
    }

    public int getChannelId() {
        return chaId;
    }

    public Type getType() {
        return type;
    }

    public boolean play(boolean loop) {
        return BASS.BASS_ChannelPlay(getChannelId(), loop);
    }

    public boolean play() {
        return play(false);
    }

    public double currentPlayTimeMS() {
        return currentPlayTimeS() * 1000;
    }

    public double currentPlayTimeS() {
        return BASS.BASS_ChannelBytes2Seconds(chaId, BASS.BASS_ChannelGetPosition(chaId, BASS.BASS_POS_BYTE));
    }

    public void seekTo(int ms) {
        if (ms >= 0) {
            BASS.BASS_ChannelSetPosition(chaId, BASS.BASS_ChannelSeconds2Bytes(chaId, ms / 1000d), BASS.BASS_POS_BYTE);
        } else {

        }
    }

    private int getFFT(ByteBuffer buf, int fft_size) {
        Log.v("ffts", "try get fft data");
        return BASS.BASS_ChannelGetData(getChannelId(), buf, BASS.BASS_DATA_FFT512);
    }

    public int getFFT(float[] b) {
        ByteBuffer buf = ByteBuffer.allocateDirect(1024 * 2);
        buf.order(null);
        int r = getFFT(buf, BASS.BASS_DATA_FFT1024);
        buf.asFloatBuffer().get(b);
        return r;
    }

    public boolean pause() {
        return BASS.BASS_ChannelPause(getChannelId());
    }

    public boolean stop() {
        return BASS.BASS_ChannelStop(getChannelId());
    }

    boolean freed = false;
    public boolean free() {
        if (!freed) {
            freed = true;
            return BASS.BASS_StreamFree(getChannelId());
        } else {
            return true;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        free();
    }

    public static BassChannel createStreamFromResource(AResource res, String path) throws IOException {
        return createStreamFromBuffer(res.loadBuffer(path));
    }

    public static BassChannel createStreamFromFile(String file, int offset, int length, int flags) {
        return new BassChannel(BASS.BASS_StreamCreateFile(file, offset, length, flags), Type.Stream);
    }

    public static BassChannel createStreamFromFile(String file) {
        return createStreamFromFile(file, 0, 0, 0);
    }

    public static BassChannel createStreamFromAsset(MContext context, String path) {
        BASS.Asset asset = new BASS.Asset(context.getNativeContext().getAssets(), path);
        return new BassChannel(BASS.BASS_StreamCreateFile(asset, 0, 0, 0), Type.Stream);
    }

    public static BassChannel createStreamFromBuffer(ByteBuffer buffer) {
        return new BassChannel(BASS.BASS_StreamCreateFile(buffer, 0, buffer.remaining(), 0), Type.Stream);
    }

    static {
        Bass.prepare();
    }

    public class AudioDelayHolder extends Thread {

        boolean ifStop = false;

        int delayMS;

        long startTime;

        boolean ifLoop;

        public AudioDelayHolder(int delayMS, boolean ifLoop) {
            this.delayMS = delayMS;
            this.ifLoop = ifLoop;
        }

        @Override
        public void start() {

            super.start();
            startTime = System.currentTimeMillis();
        }

        @Override
        public void run() {

            super.run();
            while (!ifStop) {
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                }
                if (delayMS < System.currentTimeMillis() - startTime) {
                    play(ifLoop);
                    break;
                }
            }
        }

    }
}
