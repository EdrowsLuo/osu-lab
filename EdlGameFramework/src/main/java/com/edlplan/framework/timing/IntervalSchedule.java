package com.edlplan.framework.timing;


import com.edlplan.framework.utils.advance.LinkedNode;

public class IntervalSchedule implements TimeUpdateable {

    //LinkedList<Task> tasks = new LinkedList<>();

    LinkedNode<Task> first, last;

    public IntervalSchedule() {
        first = new LinkedNode<>();
        first.value = new Task() {{
            start = Double.NEGATIVE_INFINITY;
            end = Double.POSITIVE_INFINITY;
            body = time -> { };
        }};
        last = new LinkedNode<>();
        last.value = new Task() {{
            start = end = Double.POSITIVE_INFINITY;
            body = time -> { };
        }};
        first.insertToNext(last);
    }


    @Override
    public void update(double time) {
        LinkedNode<Task> cur = first;
        LinkedNode<Task> next = cur.next;
        while (next != null) {
            cur = next;
            next = cur.next;
            final Task task = cur.value;
            if (task.start > time) {
                return;
            }
            if (task.end <= time) {
                cur.removeFromList();
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

        LinkedNode<Task> node = new LinkedNode<>();
        node.value = task;
        if (start >= last.pre.value.start) {
            last.pre.insertToNext(node);
        } else if (start < first.next.value.start) {
            first.next.insertToNext(node);
        } else {
            LinkedNode<Task> cur = last.pre;
            while (cur != null) {
                if (cur.value.start <= start) {
                    cur.insertToNext(node);
                    return;
                }
                cur = cur.pre;
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
