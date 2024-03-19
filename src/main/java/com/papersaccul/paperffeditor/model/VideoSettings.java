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
    // New fields for input file settings
    private String inputVideoCodec;
    private String inputAudioCodec;
    private String inputVideoBitrate;
    private String inputAudioBitrate;
    private String inputFrameRate;
    private String inputResolution;
    private String inputVideoWidth;
    private String inputVideoHeight;
    private String inputDrawingMethod;
    private String inputAudioChannels;

    public VideoSettings() {
        // Default settings are now set based on input file information
        this.inputFilePath = "";
        setDefaultSettingsOnInputFile(this.inputFilePath);
        notifyObservers();
    }

    private void setDefaultSettingsOnInputFile(String inputFilePath) {
        // Use FFmpegCommandBuilder to get video information for each detail
        this.inputVideoCodec = FFmpegCommandBuilder.getVideoInfo(inputFilePath, "videoCodec");
        this.inputAudioCodec = FFmpegCommandBuilder.getVideoInfo(inputFilePath, "audioCodec");
        this.inputVideoBitrate = FFmpegCommandBuilder.getVideoInfo(inputFilePath, "videoBitrate");
        this.inputAudioBitrate = FFmpegCommandBuilder.getVideoInfo(inputFilePath, "audioBitrate");
        this.inputFrameRate = FFmpegCommandBuilder.getVideoInfo(inputFilePath, "frameRate").isEmpty() ? "30" : FFmpegCommandBuilder.getVideoInfo(inputFilePath, "frameRate");
        this.inputVideoWidth = FFmpegCommandBuilder.getVideoInfo(inputFilePath, "videoWidth");
        this.inputVideoHeight = FFmpegCommandBuilder.getVideoInfo(inputFilePath, "videoHeight");
        this.inputDrawingMethod = FFmpegCommandBuilder.getVideoInfo(inputFilePath, "drawingMethod");
        this.inputAudioChannels = "Stereo"; // todo

        if (inputFilePath != "") {
        int dotIndex = inputFilePath.lastIndexOf('.');
        String baseName = inputFilePath.substring(0, dotIndex);
        String extension = inputFilePath.substring(dotIndex);
        this.outputFilePath = baseName + "_out" + extension;
        } else this.outputFilePath = "1";


    // set input settings for output as default
        this.videoCodec = this.inputVideoCodec;
        this.audioCodec = this.inputAudioCodec;
        this.videoBitrate = this.inputVideoBitrate;
        this.audioBitrate = this.inputAudioBitrate;
        this.volume = 100;
        this.frameRate = this.inputFrameRate;
        this.videoWidth = this.inputVideoWidth;
        this.videoHeight = this.inputVideoHeight;
        this.drawingMethod = this.inputDrawingMethod;
        this.audioChannels = this.inputAudioChannels;

        notifyObservers();
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

// Getters and Setters for both input and output settings
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

    // Getters for input settings
    public String getInputVideoCodec() {
        return inputVideoCodec;
    }

    public String getInputAudioCodec() {
        return inputAudioCodec;
    }

    public String getInputVideoBitrate() {
        return inputVideoBitrate;
    }

    public String getInputAudioBitrate() {
        return inputAudioBitrate;
    }

    public String getInputFrameRate() {
        return inputFrameRate;
    }

    public String getInputResolution() {
        return inputResolution;
    }

    public String getInputVideoWidth() {
        return inputVideoWidth;
    }

    public String getInputVideoHeight() {
        return inputVideoHeight;
    }

    public String getInputDrawingMethod() {
        return inputDrawingMethod;
    }

    public String getInputAudioChannels() {
        return inputAudioChannels;
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

