package com.papersaccul.paperffeditor.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import com.papersaccul.paperffeditor.util.LocalizationUtil;
import com.papersaccul.paperffeditor.model.VideoSettingsObserver;
import com.papersaccul.paperffeditor.model.VideoSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * SettingsPanel class provides the GUI elements for configuring video and audio settings.
 */
public class SettingsPanel extends GridPane implements VideoSettingsObserver {

    private TextField videoBitrateField;
    private TextField audioBitrateField;
    private TextField frameRateField;
    private TextField videoWidthField;
    private TextField videoHeightField;
    private Slider volumeSlider;
    private ComboBox<String> videoCodecComboBox;
    private ComboBox<String> audioCodecComboBox;
    private ComboBox<String> audioChannelComboBox;
    private VideoSettings videoSettings;

    public SettingsPanel(VideoSettings videoSettings) {
        this.videoSettings = videoSettings;
        initUI();
    }

    /**
     * Initializes the user interface components and layout for settings.
     */
    private void initUI() {
        this.setPadding(new Insets(10));
        this.setVgap(10);
        this.setHgap(10);

        Label videoCodecLabel = new Label(LocalizationUtil.getString("label.videoCodec"));
        videoCodecComboBox = new ComboBox<>();
    

        Label videoBitrateLabel = new Label(LocalizationUtil.getString("label.videoBitrate"));
        videoBitrateField = new TextField();
        videoBitrateField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                videoSettings.setVideoBitrate(newValue);
            } catch (NumberFormatException e) {
                System.err.println("VideoBitrateField error");
            }
        });

        Label audioBitrateLabel = new Label(LocalizationUtil.getString("label.audioBitrate"));
        audioBitrateField = new TextField();
        audioBitrateField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                videoSettings.setAudioBitrate(newValue);
            } catch (NumberFormatException e) {
                System.err.println("AudioBitrateField error");
            }
        });

        Label volumeLabel = new Label(LocalizationUtil.getString("label.volume"));
        volumeSlider = new Slider(0, 100, 100);
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> videoSettings.setVolume(newValue.doubleValue()));

        Label frameRateLabel = new Label(LocalizationUtil.getString("label.frameRate"));
        frameRateField = new TextField();
        frameRateField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                videoSettings.setFrameRate(newValue);
            } catch (NumberFormatException e) {
                System.err.println("FrameRateField error");
            }
        });

        Label videoWidthLabel = new Label(LocalizationUtil.getString("label.videoWidth"));
        videoWidthField = new TextField();
        videoWidthField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                videoSettings.setVideoWidth(newValue);
            } catch (NumberFormatException e) {
                System.err.println("VideoWidthField error");
            }
        });
        

        Label videoHeightLabel = new Label(LocalizationUtil.getString("label.videoHeight"));
        videoHeightField = new TextField();
        videoHeightField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                videoSettings.setVideoHeight(newValue);
            } catch (NumberFormatException e) {
                System.err.println("VideoHeightField error");
            }
        });


        Label audioChannelLabel = new Label(LocalizationUtil.getString("label.audioChannels"));
        audioChannelComboBox = new ComboBox<>();
        audioChannelComboBox.getItems().addAll("Mono", "Stereo", "5.1", "7.1");
        //audioChannelComboBox.setValue("Stereo");
        audioChannelComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            videoSettings.setAudioChannels(newValue);
        });

        Label audioCodecLabel = new Label(LocalizationUtil.getString("label.audioCodec"));
        audioCodecComboBox = new ComboBox<>();
    
        
        /* 
         * Parse codecs from ffmpeg 
        */
        try {
            String command = "ffmpeg -codecs";
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("EV")) {
                    String videoCodecName = line.split(" ")[2];
                    videoCodecComboBox.getItems().add(videoCodecName);
                }
                if (line.contains("EA")) {
                    String audioCodecName = line.split(" ")[2];
                    audioCodecComboBox.getItems().add(audioCodecName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        videoCodecComboBox.valueProperty().addListener((observable, oldValue, newValue) -> videoSettings.setVideoCodec(newValue));
        audioCodecComboBox.valueProperty().addListener((observable, oldValue, newValue) -> videoSettings.setAudioCodec(newValue));


        /*
         * Add all the components to the grid
         */
        this.add(videoCodecLabel, 0, 0);
        this.add(videoCodecComboBox, 1, 0);
        this.add(audioCodecLabel, 0, 1);
        this.add(audioCodecComboBox, 1, 1);
        this.add(videoBitrateLabel, 0, 2);
        this.add(videoBitrateField, 1, 2);
        this.add(audioBitrateLabel, 0, 3);
        this.add(audioBitrateField, 1, 3);
        this.add(volumeLabel, 0, 4);
        this.add(volumeSlider, 1, 4);
        this.add(frameRateLabel, 0, 5);
        this.add(frameRateField, 1, 5);
        this.add(videoWidthLabel, 0, 6);
        this.add(videoWidthField, 1, 6);
        this.add(videoHeightLabel, 0, 7);
        this.add(videoHeightField, 1, 7);
        this.add(audioChannelLabel, 0, 8);
        this.add(audioChannelComboBox, 1, 8);
        
    }

    @Override
    public void updateVideoSettingsInfo(VideoSettings videoSettings) {
        if (videoSettings != null) {
            videoCodecComboBox.setValue(videoSettings.getVideoCodec());
            audioCodecComboBox.setValue(videoSettings.getAudioCodec());
            videoBitrateField.setText(videoSettings.getVideoBitrate());
            audioBitrateField.setText(videoSettings.getAudioBitrate());
            volumeSlider.setValue(videoSettings.getVolume());
            frameRateField.setText(String.valueOf(videoSettings.getFrameRate()));
            videoWidthField.setText(videoSettings.getVideoWidth());
            videoHeightField.setText(videoSettings.getVideoHeight());
            audioChannelComboBox.setValue(videoSettings.getAudioChannels());
        } else {
            // hardcode defalut valuess
            videoCodecComboBox.setValue("H.264");
            audioCodecComboBox.setValue("AAC");
            videoBitrateField.setText("");
            audioBitrateField.setText("");
            volumeSlider.setValue(100);
            frameRateField.setText("");
            videoHeightField.setText("600");
            videoWidthField.setText("800");
            audioChannelComboBox.setValue("Stereo");
        }
    }
}
