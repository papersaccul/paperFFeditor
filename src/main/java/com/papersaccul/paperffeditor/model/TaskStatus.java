package com.papersaccul.paperffeditor.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TaskStatus {
    private double progress;
    private String message;
    private List<TaskStatusObserver> observers = new ArrayList<>();

    public TaskStatus() {
        this.progress = 0;
        this.message = "0";
    }

    public void addObserver(TaskStatusObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TaskStatusObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (TaskStatusObserver observer : observers) {
            observer.onTaskStatusUpdate(this);
        }
    }

    private void setField(String fieldName, Object value) {
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(this, value);
            notifyObservers();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        setField("progress", progress);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        setField("message", message);
    }
}
