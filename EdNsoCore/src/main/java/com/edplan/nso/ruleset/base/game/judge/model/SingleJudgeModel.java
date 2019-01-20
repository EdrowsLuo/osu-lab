package com.edplan.nso.ruleset.base.game.judge.model;


import com.edplan.nso.ruleset.base.game.judge.JudgeObject;
import com.edplan.nso.ruleset.base.game.judge.PositionHitObject;

/**
 * 标准化的单时间点判定
 */
public class SingleJudgeModel extends JudgeModel {

    public JudgeType judgeType = JudgeType.Note;

    public JudgeObject createJudgeObject(double time) {
        if (judgeType == JudgeType.Note) {
            PositionHitObject positionHitObject = new PositionHitObject();

            return positionHitObject;
        } else {
            return null;
        }
    }

    public enum JudgeType {
        Note, Hold,
    }

}
