package com.edplan.nso.ruleset.base.object;

public class StdFormatHitObject extends JudgeObject {

    protected int x;

    protected int y;

    protected int time;

    protected int type;

    protected int hitSound;

    protected String rawExtras;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHitSound() {
        return hitSound;
    }

    public void setHitSound(int hitSound) {
        this.hitSound = hitSound;
    }

    public String getRawExtras() {
        return rawExtras;
    }

    public void setRawExtras(String rawExtras) {
        this.rawExtras = rawExtras;
    }

}
