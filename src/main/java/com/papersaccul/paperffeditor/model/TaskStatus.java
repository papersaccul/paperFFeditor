package com.papersaccul.paperffeditor.model;

/**
 * TaskStatus class represents the status of a task in the application.
 * It holds information about the progress and message of the task.
 */
public class TaskStatus {
    private double progress;
    private String message;

    /**
     * Constructor for TaskStatus.
     * @param progress the progress of the task, ranging from 0.0 to 1.0
     * @param message the message or status description of the task
     */
    public TaskStatus(double progress, String message) {
        this.progress = progress;
        this.message = message;
    }

    /**
     * Gets the progress of the task.
     * @return the progress as a double
     */
    public double getProgress() {
        return progress;
    }

    /**
     * Sets the progress of the task.
     * @param progress the progress to set, ranging from 0.0 to 1.0
     */
    public void setProgress(double progress) {
        this.progress = progress;
    }

    /**
     * Gets the message or status description of the task.
     * @return the message as a String
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message or status description of the task.
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
