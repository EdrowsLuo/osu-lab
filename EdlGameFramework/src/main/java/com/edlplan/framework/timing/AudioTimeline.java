package com.edlplan.framework.timing;

import com.edlplan.framework.media.bass.BassChannel;

/**
 * 一个同步音频的Timeline
 */
public class AudioTimeline extends PreciseTimeline {
    private double preTime = 0;

    private BassChannel channel;

    public AudioTimeline(BassChannel channel) {
        this.channel = channel;
    }

    @Override
    public void onLoop(double deltaTime) {

        double cur = channel.currentPlayTimeMS();
        if (Math.abs(frameTime() + deltaTime - cur) > 3) {
            super.onLoop(cur - frameTime());
        } else {
            super.onLoop(deltaTime);
        }
        preTime = cur;
    }
}
