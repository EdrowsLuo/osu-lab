package com.edlplan.framework.utils;

import com.edlplan.framework.utils.interfaces.Consumer;

import java.util.LinkedList;

public class Behavior {

    private Runnable runnable;

    private LinkedList<Runnable> before,after;

    public Behavior(Runnable runnable) {
        this.runnable = runnable;
    }

    public void run() {
        if (before != null) {
            for (Runnable r : before) {
                r.run();
            }
        }
        runnable.run();
        if (after != null) {
            for (Runnable r : after) {
                r.run();
            }
        }
    }

    public Behavior before(Runnable r) {
        if (before == null) {
            before = new LinkedList<>();
        }
        before.addFirst(r);
        return this;
    }

    public Behavior then(Runnable r) {
        if (after == null) {
            after = new LinkedList<>();
        }
        after.addLast(r);
        return this;
    }

    public Behavior countTime(Consumer<Integer> consumer) {
        Ref<Long> time = new Ref<>();
        before(() -> time.set(System.currentTimeMillis()));
        then(() -> consumer.consume((int) (System.currentTimeMillis() - time.get())));
        return this;
    }

    public static Behavior wrap(Runnable runnable) {
        return new Behavior(runnable);
    }

}
