package com.papersaccul.paperffeditor.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import com.papersaccul.paperffeditor.util.FFmpegCommandBuilder;

/**
 * VideoSettings class encapsulates the settings for video processing.
 */
public class VideoSettings {
    private String videoCodec;
    private String audioCodec;
    private String videoBitrate;
    private String audioBitrate;
    private double volume;
    private String frameRate;
    private String resolution;
    private String videoWidth;
    private String videoHeight;
    private String drawingMethod;
    private String audioChannels;
    private String inputFilePath; 
    private String outputFilePath; 

    public VideoSettings() {
        // Default settings are now set based on input file information
        this.inputFilePath = "";
        setDefaultSettingsOnInputFile(this.inputFilePath);
        notifyObservers();
    }

    private void setDefaultSettingsOnInputFile(String inputFilePath) {
        // Use FFmpegCommandBuilder to get video information for each detail
        this.videoCodec = FFmpegCommandBuilder.getVideoInfo(inputFilePath, "videoCodec");
        this.audioCodec = FFmpegCommandBuilder.getVideoInfo(inputFilePath, "audioCodec");

        //String videoBitrateStr = FFmpegCommandBuilder.getVideoInfo(inputFilePath, "videoBitrate").replaceAll("[^0-9]", "");
        this.videoBitrate = FFmpegCommandBuilder.getVideoInfo(inputFilePath, "videoBitrate");

        //String audioBitrateStr = FFmpegCommandBuilder.getVideoInfo(inputFilePath, "audioBitrate").replaceAll("[^0-9]", "");
        this.audioBitrate = FFmpegCommandBuilder.getVideoInfo(inputFilePath, "audioBitrate");
        System.out.println("audioBitrate: " + audioBitrate);
        System.out.println("videoBitrate: " + videoBitrate);
        System.out.println("frameRate: " + frameRate);
        this.volume = 100; // default to 100

        //String frameRateStr = FFmpegCommandBuilder.getVideoInfo(inputFilePath, "frameRate").replaceAll("[^0-9]", "");
        this.frameRate = FFmpegCommandBuilder.getVideoInfo(inputFilePath, "frameRate").isEmpty() ? "30" : FFmpegCommandBuilder.getVideoInfo(inputFilePath, "frameRate");

        this.videoWidth = FFmpegCommandBuilder.getVideoInfo(inputFilePath, "videoWidth");
        this.videoHeight = FFmpegCommandBuilder.getVideoInfo(inputFilePath, "videoHeight");
        this.drawingMethod = FFmpegCommandBuilder.getVideoInfo(inputFilePath, "drawingMethod");
        this.audioChannels = "Stereo"; // todo

        if (inputFilePath != "") {
        int dotIndex = inputFilePath.lastIndexOf('.');
        String baseName = inputFilePath.substring(0, dotIndex);
        String extension = inputFilePath.substring(dotIndex);
        this.outputFilePath = baseName + "_out" + extension;
        } else this.outputFilePath = "1";

        notifyObservers();
    }

    // Observer
    public interface VideoSettingsObserver {
        void updateVideoSettingsInfo(VideoSettings videoSettings);
    }

    private List<VideoSettingsObserver> observers = new ArrayList<>();

    
    public void addObserver(VideoSettingsObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(VideoSettingsObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (VideoSettingsObserver observer : observers) {
            observer.updateVideoSettingsInfo(this);
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

    // Getters and Setters
    public String getVideoCodec() {
        return videoCodec;
    }

    public void setVideoCodec(String videoCodec) {
        setField("videoCodec", videoCodec);
    }

    public String getAudioCodec() {
        return audioCodec;
    }

    public void setAudioCodec(String audioCodec) {
        setField("audioCodec", audioCodec);
    }

    public String getVideoBitrate() {
        return videoBitrate;
    }

    public void setVideoBitrate(String videoBitrate) {
        setField("videoBitrate", videoBitrate);
    }

    public String getAudioBitrate() {
        return audioBitrate;
    }

    public void setAudioBitrate(String audioBitrate) {
        setField("audioBitrate", audioBitrate);
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        setField("volume", volume);
    }

    public String getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(String frameRate) {
        setField("frameRate", frameRate);
    }

    public String getDrawingMethod() {
        return drawingMethod;
    }

    public void setDrawingMethod(String drawingMethod) {
        setField("drawingMethod", drawingMethod);
    }

    
    public String getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(String videoHeight) {
        setField("videoHeight", videoHeight);
    }

    public String getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(String videoWidth) {
        setField("videoWidth", videoWidth);
    }

    public String getAudioChannels() {
        return audioChannels;
    }

    public void setAudioChannels(String audioChannels) {
        setField("audioChannels", audioChannels);
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
        setDefaultSettingsOnInputFile(inputFilePath);
    }

    public String getOutputFilePath() { 
        return outputFilePath;
    }

    public void setOutputFilePath(String outputFilePath) {
        setField("outputFilePath", outputFilePath);
    }

    // I forgot about this method and went the observer pattern way. 
    @Override
    public String toString() {
        return "VideoSettings{" +
                "audioCodec='" + audioCodec + '\'' +
                ", videoCodec=" + videoCodec + '\'' +
                ", bitrate=" + videoBitrate +
                ", volume=" + volume +
                ", frameRate=" + frameRate +
                ", resolution='" + resolution + '\'' +
                ", audioChannels='" + audioChannels + '\'' +
                ", inputFilePath='" + inputFilePath + '\'' + 
                ", outputFilePath='" + outputFilePath + '\'' + 
                '}';
    }
}

