package com.edplan.framework.async;

public class ProgressHolder {


    private String message = "";

    private double progress = 0;

    public double getProgress() {
        return progress;
    }

    public String getMessage() {
        return message;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public void setMessage(String message) {
        System.out.println("onMessageChange: " + message);
        this.message = message;
    }
}
