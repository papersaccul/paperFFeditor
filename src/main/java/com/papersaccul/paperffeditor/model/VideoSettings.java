package com.papersaccul.paperffeditor.model;

/**
 * VideoSettings class encapsulates the settings for video processing.
 */
public class VideoSettings {
    private String codec;
    private int bitrate;
    private double volume;
    private int frameRate;
    private String resolution;
    private String audioChannels;

    public VideoSettings() {
        // Default settings
        this.codec = "H.264";
        this.bitrate = 1000; // Default bitrate in kbps
        this.volume = 100; // Default volume percentage
        this.frameRate = 30; // Default frame rate
        this.resolution = "1920x1080"; // Default resolution
        this.audioChannels = "Stereo"; // Default audio channel configuration
    }

    // Getters and Setters
    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public int getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getAudioChannels() {
        return audioChannels;
    }

    public void setAudioChannels(String audioChannels) {
        this.audioChannels = audioChannels;
    }

    @Override
    public String toString() {
        return "VideoSettings{" +
                "codec='" + codec + '\'' +
                ", bitrate=" + bitrate +
                ", volume=" + volume +
                ", frameRate=" + frameRate +
                ", resolution='" + resolution + '\'' +
                ", audioChannels='" + audioChannels + '\'' +
                '}';
    }
}
