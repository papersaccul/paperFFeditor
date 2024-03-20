package com.papersaccul.paperffeditor.model;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.papersaccul.paperffeditor.util.FFmpegCommandBuilder;

/**
 * VideoSettings class encapsulates the settings for video processing.
 */
public class VideoSettings {
    // Output file settings
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
    // Input file settings
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

    private static String ffmpegPath;

    public VideoSettings() {
        // Default settings are now set based on input file information
        this.inputFilePath = "";
        setDefaultSettingsOnInputFile(this.inputFilePath);
        notifyObservers();
    }

    // Set default settings on input file
    private void setDefaultSettingsOnInputFile(String inputFilePath) {
        File inputFile = new File(inputFilePath);
        if (inputFile.exists()) {
            Map<String, String> videoInfo = FFmpegCommandBuilder.parseVideoInfo(inputFilePath, this);
            this.inputVideoCodec = videoInfo.getOrDefault("videoCodec", "");
            this.inputAudioCodec = videoInfo.getOrDefault("audioCodec", "");
            this.inputVideoBitrate = videoInfo.getOrDefault("videoBitrate", "");
            this.inputAudioBitrate = videoInfo.getOrDefault("audioBitrate", "");
            this.inputFrameRate = videoInfo.getOrDefault("frameRate", "");
            this.inputVideoWidth = videoInfo.getOrDefault("videoWidth", "");
            this.inputVideoHeight = videoInfo.getOrDefault("videoHeight", "");
            this.inputDrawingMethod = videoInfo.getOrDefault("drawingMethod", "");
            this.inputAudioChannels = "Stereo"; // todo

            int dotIndex = inputFilePath.lastIndexOf('.');
            String baseName = inputFilePath.substring(0, dotIndex);
            String extension = inputFilePath.substring(dotIndex);
            this.outputFilePath = baseName + "_out" + extension;

            // Set input settings for output as default
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

            ffmpegPath = "ffmpeg"; // Use system env as default
        } else {
            this.outputFilePath = "";
        }
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

    public String getFfmpegPath() {
        return ffmpegPath;
    }

    public void setFfmpegPath(String ffmpegpath) {
        ffmpegPath = ffmpegpath;                  // without notify observer
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

