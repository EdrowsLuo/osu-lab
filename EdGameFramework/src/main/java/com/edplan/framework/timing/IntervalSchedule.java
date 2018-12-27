package com.edplan.framework.timing;


import java.util.Iterator;
import java.util.LinkedList;

public class IntervalSchedule implements TimeUpdateable {

    LinkedList<Task> tasks = new LinkedList<>();

    @Override
    public void update(double time) {
        Iterator<Task> taskIterator = tasks.iterator();
        while (taskIterator.hasNext()) {
            final Task task = taskIterator.next();
            if (task.start > time) {
                return;
            }
            if (task.end <= time) {
                taskIterator.remove();
                if (task.start == task.end || task.strictEnd) {
                    task.body.update(task.end);
                }
            } else {
                task.body.update(time);
            }
        }
    }

    public void addAnimTask(double start, double duration, TimeUpdateable anim) {
        addTask(start, start + duration, true, time -> anim.update((time - start) / duration));
    }

    public void addTask(double start, double end, boolean strictEnd, TimeUpdateable body) {
        Task task = new Task();
        task.strictEnd = strictEnd;
        task.body = body;
        task.start = start;
        task.end = end;
        if (tasks.isEmpty()) {
            tasks.addLast(task);
        } else {
            if (start >= tasks.getLast().start) {
                tasks.addLast(task);
            } else if (start < tasks.getFirst().start) {
                tasks.addFirst(task);
            } else {
                Iterator<Task> iterator = tasks.descendingIterator();
                int idx = tasks.size();
                while (iterator.hasNext()) {
                    final Task t = iterator.next();
                    idx--;
                    if (t.start <= start) {
                        tasks.add(idx + 1, task);
                        break;
                    }
                }
            }
        }
    }

    public static class Task {
        double start;
        double end;
        boolean strictEnd = false;
        TimeUpdateable body;
    }
}
