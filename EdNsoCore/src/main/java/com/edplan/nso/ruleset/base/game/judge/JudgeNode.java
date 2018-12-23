package com.edplan.nso.ruleset.base.game.judge;

public class JudgeNode {

    public final JudgeDataUpdater updater;

    public final JudgeData data;

    public final int subType;

    public boolean interrupted = false;

    public JudgeNode(JudgeData data, int subType) {
        this.subType = subType;
        this.data = data;
        this.updater = data.getDataUpdater();
    }

}