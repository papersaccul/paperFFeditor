package com.papersaccul.paperffeditor.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import com.papersaccul.paperffeditor.util.LocalizationUtil;
import com.papersaccul.paperffeditor.model.VideoSettings;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * SettingsPanel class provides the GUI elements for configuring video and audio settings.
 */
public class SettingsPanel extends GridPane {

    private TextField bitrateField;
    private Slider volumeSlider;
    private ComboBox<String> videoCodecComboBox;
    private ComboBox<String> audioCodecComboBox;
    private TextField frameRateField;
    private TextField resolutionField;
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
    

        Label bitrateLabel = new Label(LocalizationUtil.getString("label.bitrate"));
        bitrateField = new TextField();
        bitrateField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                videoSettings.setBitrate(Integer.parseInt(newValue));
            } catch (NumberFormatException e) {
                // Handle exception or invalid input
            }
        });

        Label volumeLabel = new Label(LocalizationUtil.getString("label.volume"));
        volumeSlider = new Slider(0, 100, 100);
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> videoSettings.setVolume(newValue.doubleValue()));

        Label frameRateLabel = new Label(LocalizationUtil.getString("label.frameRate"));
        frameRateField = new TextField();
        frameRateField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                videoSettings.setFrameRate(Integer.parseInt(newValue));
            } catch (NumberFormatException e) {
                System.err.println("Handle exception or invalid input | VideoSettings");
            }
        });

        Label resolutionLabel = new Label(LocalizationUtil.getString("label.resolution"));
        resolutionField = new TextField();
        resolutionField.textProperty().addListener((observable, oldValue, newValue) -> videoSettings.setResolution(newValue));

        Label audioChannelLabel = new Label(LocalizationUtil.getString("label.audioChannels"));
        audioChannelComboBox = new ComboBox<>();
        audioChannelComboBox.getItems().addAll("Mono", "Stereo", "5.1", "7.1");
        audioChannelComboBox.setValue("Stereo");
        audioChannelComboBox.valueProperty().addListener((observable, oldValue, newValue) -> videoSettings.setAudioChannels(newValue));

        Label audioCodecLabel = new Label(LocalizationUtil.getString("label.audioCodec"));
        audioCodecComboBox = new ComboBox<>();
    
        
        // Parse video codecs from ffmpeg
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
        
        videoCodecComboBox.setValue("H.264");
        audioCodecComboBox.setValue("AAC");
        videoCodecComboBox.valueProperty().addListener((observable, oldValue, newValue) -> videoSettings.setVideoCodec(newValue));
        audioCodecComboBox.valueProperty().addListener((observable, oldValue, newValue) -> videoSettings.setAudioCodec(newValue));


        this.add(videoCodecLabel, 0, 0);
        this.add(videoCodecComboBox, 1, 0);
        this.add(audioCodecLabel, 0, 1);
        this.add(audioCodecComboBox, 1, 1);
        this.add(bitrateLabel, 0, 2);
        this.add(bitrateField, 1, 2);
        this.add(volumeLabel, 0, 3);
        this.add(volumeSlider, 1, 3);
        this.add(frameRateLabel, 0, 4);
        this.add(frameRateField, 1, 4);
        this.add(resolutionLabel, 0, 5);
        this.add(resolutionField, 1, 5);
        this.add(audioChannelLabel, 0, 6);
        this.add(audioChannelComboBox, 1, 6);
        
    }
}
