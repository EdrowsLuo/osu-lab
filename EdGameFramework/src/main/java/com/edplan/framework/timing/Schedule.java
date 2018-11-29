package com.edplan.framework.timing;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * 管理一批事件，被动更新时间，在特定时间触发特定事件
 */

public class Schedule {

    private LinkedList<Task> tasks = new LinkedList<>();

    /**
     * 添加一个事件
     * @param time 事件事件戳
     * @param runnable 具体操作
     */
    public void addEvent(double time, Runnable runnable) {
        if (tasks.isEmpty()) {
            tasks.add(new Task(time, runnable));
        } else {
            if (time >= tasks.getLast().time) {
                tasks.addLast(new Task(time, runnable));
            } else if (time < tasks.getFirst().time) {
                tasks.addFirst(new Task(time, runnable));
            } else {
                Iterator<Task> iterator = tasks.descendingIterator();
                int idx = tasks.size();
                while (iterator.hasNext()) {
                    final Task t = iterator.next();
                    idx--;
                    if (t.time <= time) {
                        tasks.add(idx + 1, new Task(time, runnable));
                        break;
                    }
                }
            }
        }
    }

    public void update(double time) {
        Task task;
        while (!tasks.isEmpty() && (task = tasks.getFirst()).time <= time) {
            task.runnable.run();
            tasks.removeFirst();
        }
    }

    public void clear() {
        tasks.clear();
    }

    public static class Task {

        public final double time;

        public final Runnable runnable;

        public Task(double time, Runnable runnable) {
            this.time = time;
            this.runnable = runnable;
        }
    }
}
