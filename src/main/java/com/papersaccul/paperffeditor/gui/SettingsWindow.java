package com.papersaccul.paperffeditor.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import com.papersaccul.paperffeditor.AppConfig;
import com.papersaccul.paperffeditor.model.VideoSettings;

public class SettingsWindow extends Stage {

    private TextField ffmpegPathField;
    private TextField languageField;
    private VideoSettings videoSettings;

    public SettingsWindow(VideoSettings videoSettings) {
        this.videoSettings = videoSettings;
        initUI();
    }

    private void initUI() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label ffmpegPathLabel = new Label("FFmpeg Path:");
        ffmpegPathField = new TextField();
        ffmpegPathField.setText(videoSettings.getFfmpegPath());

        Label languageLabel = new Label("Language:");
        languageField = new TextField();
        languageField.setText(AppConfig.getLocale().getLanguage());

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> saveSettings());

        gridPane.add(ffmpegPathLabel, 0, 0);
        gridPane.add(ffmpegPathField, 1, 0);
        gridPane.add(languageLabel, 0, 1);
        gridPane.add(languageField, 1, 1);
        gridPane.add(saveButton, 1, 2);

        Scene scene = new Scene(gridPane, 350, 150);
        this.setTitle("Settings");
        this.setScene(scene);
    }

    private void saveSettings() {
        videoSettings.setFfmpegPath(ffmpegPathField.getText());
        AppConfig.setLocale(new java.util.Locale(languageField.getText()));
        this.close();
    }
}
