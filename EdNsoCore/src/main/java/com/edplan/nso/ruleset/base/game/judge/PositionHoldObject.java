package com.edplan.nso.ruleset.base.game.judge;

import com.edplan.nso.ruleset.base.game.World;

/**
 * 摁压事件，只判断手指在屏幕上就ok了
 */
public class PositionHoldObject extends BasePositionJudgeObject{

    private boolean hold = false;

    @Override
    public boolean handle(JudgeData data, int subType, World world) {
        CursorData cursorData = (CursorData) data;

        if (!hold) {
            for (CursorData.CursorHolder holder : cursorData.getCursors()) {
                if (holder.isDown) {
                    if (hitWindow.checkTime(holder.down.time) && area.checkPosition(holder.x, holder.y)) {
                        hold = true;
                        break;
                    }
                }
            }
        }

        if (hold) {
            if (world.getJudgeFrameTime() >= hitWindow.getMiddle()) {
                hit(hitWindow.getMiddle(), 0, 0);
            }
        }

        return false;
    }

    @Override
    public int[] getListeningDataSubTypes() {
        return new int[]{CursorData.SUB_STATE};
    }

}
