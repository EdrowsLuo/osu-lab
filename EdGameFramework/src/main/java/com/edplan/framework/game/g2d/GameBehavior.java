package com.edplan.framework.game.g2d;

public class GameBehavior<T extends ICanBindBehavior> {

    private T bindObject;

    public final T getBindObject() {
        return bindObject;
    }

    /**
     * called when the behavior began
     *
     * @param bindObject the object where the behavior is bind
     */
    public final void start(T bindObject) {
        if (this.bindObject != null || bindObject == null) {
            throw new RuntimeException();
        }
        this.bindObject = bindObject;
        onStart();
    }

    public final void end() {
        if (bindObject != null) {
            onEnd();
            bindObject = null;
        }
    }

    public final void update() {
        if (bindObject != null) {
            onUpdate();
        }
    }

    protected void onStart() {

    }

    private void onEnd() {

    }

    private void onUpdate() {

    }

    private void onPause() {

    }

    private void onResume() {

    }

}
