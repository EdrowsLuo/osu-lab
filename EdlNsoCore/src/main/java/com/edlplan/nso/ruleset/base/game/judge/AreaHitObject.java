package com.edlplan.nso.ruleset.base.game.judge;

import com.edlplan.nso.ruleset.base.game.World;

/**
 * 处理单次点击判定
 */
public class AreaHitObject extends BaseAreaJudgeObject {

    //是否是分离判定，即判定时其他点的点击是否可以被记为点击
    protected boolean separateJudge = false;

    protected void setSeparateJudge(boolean separateJudge) {
        this.separateJudge = separateJudge;
    }

    @Override
    public boolean handle(JudgeData data, int subType, World world) {
        CursorData cursorData = (CursorData) data;
        if (separateJudge) {
            CursorData.CursorHolder timeHolder = null;
            CursorData.CursorHolder posHolder = null;
            for (CursorData.CursorHolder holder : cursorData.getCursors()) {
                if ((!holder.down.empty()) && hitWindow.checkTime(holder.down.time)) {
                    timeHolder = holder;
                    break;
                }
            }
            for (CursorData.CursorHolder holder : cursorData.getCursors()) {
                if (holder.isDown && area.checkPosition(holder.x, holder.y)) {
                    posHolder = holder;
                    break;
                }
            }

            if (timeHolder != null && posHolder != null) {
                hit(timeHolder.down.time, posHolder.x, posHolder.y);
                timeHolder.down.consume();
                return !cursorData.hasMoreAction();
            }
        } else {
            for (CursorData.CursorHolder holder : cursorData.getCursors()) {
                if (holder.down.empty()) {
                    continue;
                }
                if (hitWindow.checkTime(holder.down.time) && area.checkPosition(holder.x, holder.y)) {
                    hit(holder.down.time, holder.x, holder.y);
                    holder.down.consume();
                    return !cursorData.hasMoreAction();
                }
            }
        }
        return false;
    }

    @Override
    public int[] getListeningDataSubTypes() {
        return new int[]{CursorData.SUB_ACTION};
    }

}
