package com.edplan.nso.ruleset.base.game.judge;

import com.edplan.nso.ruleset.base.game.World;

/**
 * 摁压事件，只判断手指在屏幕上就ok了
 */
public class AreaHoldObject extends BaseAreaJudgeObject {

    private boolean hold = false;

    private int holdingCursor = -1;

    @Override
    public boolean handle(JudgeData data, int subType, World world) {
        CursorData cursorData = (CursorData) data;

        if (!hold) {
            for (CursorData.CursorHolder holder : cursorData.getCursors()) {
                if (holder.isDown) {
                    if (hitWindow.checkTime(world.getJudgeFrameTime()) && area.checkPosition(holder.x, holder.y)) {
                        hold = true;
                        holdingCursor = holder.id;
                        break;
                    }
                }
            }
        }

        if (hold) {
            if (world.getJudgeFrameTime() >= hitWindow.getMiddle()) {
                hit(hitWindow.getMiddle(), 0, 0);
            } else {

            }
        }

        return false;
    }

    @Override
    public int[] getListeningDataSubTypes() {
        return new int[]{CursorData.SUB_STATE};
    }

}
