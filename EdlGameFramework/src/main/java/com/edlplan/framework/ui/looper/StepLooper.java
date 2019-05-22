package com.edlplan.framework.ui.looper;

import com.edlplan.framework.timing.ILooper;
import com.edlplan.framework.timing.Loopable;

public class StepLooper extends BaseLooper<StepedLoopable> {
    private int step;

    protected void setStep(int step) {
        this.step = step;
    }

    public int getStep() {
        return step;
    }

    public void addLoopable(Loopable l, int step) {
        addLoopable(new WrapStepedLoopable(l, step));
    }

    @Override
    protected void runLoopable(StepedLoopable l) {

        setStep(l.getStep());
        super.runLoopable(l);
    }

    public class WrapStepedLoopable extends StepedLoopable {

        private int step = -1;

        private Loopable loopable;

        public WrapStepedLoopable(Loopable l, int step) {
            this.loopable = l;
            this.step = step;
        }

        @Override
        public void onLoop(double deltaTime) {

            loopable.onLoop(deltaTime);
        }

        @Override
        public void onRemove() {

            loopable.onRemove();
        }

        @Override
        public Loopable.Flag getFlag() {

            return loopable.getFlag();
        }


        @Override
        public void setFlag(Loopable.Flag flag) {

            loopable.setFlag(flag);
        }

        @Override
        public ILooper getLooper() {

            return loopable.getLooper();
        }

        @Override
        public void setLooper(ILooper lp) {

            loopable.setLooper(lp);
        }

        @Override
        public int getStep() {

            return step;
        }
    }
}
