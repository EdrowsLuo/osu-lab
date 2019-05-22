package com.edlplan.framework.tasks;

import java.util.Scanner;

public abstract class Task {


    public static void main(String[] args) {
        Task task = new Task() {
            @Override
            protected void doTask() throws TaskStopException {
                try {
                    setOnTaskStoppedBehavior(() -> System.out.println("stop"));
                    System.out.println("s1");
                    Thread.sleep(2000);
                    checkStop();
                    System.out.println("s2");
                    Thread.sleep(2000);
                    checkStop();
                    System.out.println("s3");
                    Thread.sleep(2000);
                    checkStop();
                    System.out.println("s4");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        task.start();
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        task.requestStop();
    }

    private String info;

    private boolean shouldStop = false;

    private Thread thread;

    private Runnable onTaskStopped;

    protected abstract void doTask() throws TaskStopException;

    public void start() {
        if (thread == null) {
            thread = new Thread(this::run);
            thread.start();
        } else {
            throw new RuntimeException();
        }
    }

    private void run() {
        try {
            doTask();
        } catch (TaskStopException e) {
            if (onTaskStopped != null) {
                onTaskStopped.run();
            }
        } catch (Exception e) {

        }
    }

    public void setOnTaskStoppedBehavior(Runnable onTaskStopped) {
        this.onTaskStopped = onTaskStopped;
    }

    /**
     * called when running task, it will allow user to stop the task
     */
    protected void checkStop() throws TaskStopException {
        if (shouldStop) throw new TaskStopException();
    }


    /**
     * request for stop
     */
    public void requestStop() {
        shouldStop = true;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public interface OnTaskInfoChange {
        void onInfoChange(Task task, String info);
    }

    public static class TaskStopException extends Exception {
    }
}
