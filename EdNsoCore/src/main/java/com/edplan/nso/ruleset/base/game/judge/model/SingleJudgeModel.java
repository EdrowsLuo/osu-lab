package com.edplan.nso.ruleset.base.game.judge.model;


import com.edplan.nso.ruleset.base.game.judge.JudgeObject;
import com.edplan.nso.ruleset.base.game.judge.AreaHitObject;

/**
 * 标准化的单时间点判定
 */
public class SingleJudgeModel extends JudgeModel {

    public JudgeType judgeType = JudgeType.Note;

    public JudgeObject createJudgeObject(double time) {
        if (judgeType == JudgeType.Note) {
            AreaHitObject areaHitObject = new AreaHitObject();

            return areaHitObject;
        } else {
            return null;
        }
    }

    public enum JudgeType {
        Note, Hold,
    }

}
