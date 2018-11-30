package com.edplan.nso.ruleset.base.game.judge;

public class JudgeNode {

    public final JudgeDataUpdater updater;

    public final JudgeData data;

    public boolean interrupted = false;

    public JudgeNode(JudgeData data) {
        this.data = data;
        this.updater = data.getDataUpdater();
    }

}