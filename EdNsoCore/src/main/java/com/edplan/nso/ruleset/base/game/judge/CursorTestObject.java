package com.edplan.nso.ruleset.base.game.judge;

import com.edplan.framework.math.Vec2;
import com.edplan.nso.ruleset.base.game.World;

public class CursorTestObject extends JudgeObject {

    public final Holder[] holders = new Holder[CursorData.MAX_CURSOR_COUNT];

    public CursorTestObject() {
        for (int i = 0 ;i <holders.length;i++) {
            holders[i] = new Holder();
        }
    }

    @Override
    public double getStartJudgeTime() {
        return -1000000000;
    }

    @Override
    public double getJudgeFailedTime() {
        return 1000000000;
    }

    @Override
    public boolean handle(JudgeData data, int subType, World world) {
        if (data instanceof CursorData) {
            CursorData cursorData = (CursorData) data;
            for (CursorData.CursorHolder holder : cursorData.getCursors()) {
                holders[holder.id].down = holder.isDown;
                holders[holder.id].pos.set(holder.x, holder.y);
            }
        }
        return false;
    }

    @Override
    public Class<? extends JudgeData>[] getListeningData() {
        return new Class[]{CursorData.class};
    }

    @Override
    public int[] getListeningDataSubTypes() {
        return new int[]{CursorData.SUB_STATE};
    }

    public static class Holder {
        public boolean down = false;
        public final Vec2 pos = new Vec2();
    }
}
