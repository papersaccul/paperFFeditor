package com.papersaccul.paperffeditor.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import com.papersaccul.paperffeditor.AppConfig;
import com.papersaccul.paperffeditor.util.LocalizationUtil;

/**
 * SettingsPanel class provides the GUI elements for configuring video and audio settings.
 */
public class SettingsPanel extends GridPane {

    private TextField bitrateField;
    private Slider volumeSlider;
    private ComboBox<String> codecComboBox;
    private TextField frameRateField;
    private TextField resolutionField;
    private ComboBox<String> audioChannelComboBox;

    public SettingsPanel() {
        initUI();
    }

    /**
     * Initializes the user interface components and layout for settings.
     */
    private void initUI() {
        this.setPadding(new Insets(10));
        this.setVgap(10);
        this.setHgap(10);

        Label codecLabel = new Label(LocalizationUtil.getString("label.codec"));
        codecComboBox = new ComboBox<>();
        codecComboBox.getItems().addAll("H.264", "H.265", "VP9", "AV1");
        codecComboBox.setValue("H.264");

        Label bitrateLabel = new Label(LocalizationUtil.getString("label.bitrate"));
        bitrateField = new TextField();

        Label volumeLabel = new Label(LocalizationUtil.getString("label.volume"));
        volumeSlider = new Slider(0, 100, 100);

        Label frameRateLabel = new Label(LocalizationUtil.getString("label.frameRate"));
        frameRateField = new TextField();

        Label resolutionLabel = new Label(LocalizationUtil.getString("label.resolution"));
        resolutionField = new TextField();

        Label audioChannelLabel = new Label(LocalizationUtil.getString("label.audioChannels"));
        audioChannelComboBox = new ComboBox<>();
        audioChannelComboBox.getItems().addAll("Mono", "Stereo", "5.1", "7.1");
        audioChannelComboBox.setValue("Stereo");

        this.add(codecLabel, 0, 0);
        this.add(codecComboBox, 1, 0);
        this.add(bitrateLabel, 0, 1);
        this.add(bitrateField, 1, 1);
        this.add(volumeLabel, 0, 2);
        this.add(volumeSlider, 1, 2);
        this.add(frameRateLabel, 0, 3);
        this.add(frameRateField, 1, 3);
        this.add(resolutionLabel, 0, 4);
        this.add(resolutionField, 1, 4);
        this.add(audioChannelLabel, 0, 5);
        this.add(audioChannelComboBox, 1, 5);
    }
}
