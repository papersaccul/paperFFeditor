package com.papersaccul.paperffeditor.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import com.papersaccul.paperffeditor.model.VideoSettings;
import com.papersaccul.paperffeditor.util.FileHandler;
/**
 * InfoDisplayPanel class represents the panel that displays information about the input and output files.
 */
public class InfoDisplayPanel extends GridPane {

    private Label inputFileLabel;
    private Label outputFileLabel;
    private Label videoSettingsLabel;

    public InfoDisplayPanel() {
        initUI();
    }

    /**
     * Initializes the user interface components and layout for the InfoDisplayPanel.
     */
    private void initUI() {
        this.setPadding(new Insets(10));
        this.setHgap(10);
        this.setVgap(10);

        inputFileLabel = new Label();
        outputFileLabel = new Label();
        videoSettingsLabel = new Label();

        this.add(new Label("Input File:"), 0, 0);
        this.add(inputFileLabel, 1, 0);
        this.add(new Label("Output File:"), 0, 1);
        this.add(outputFileLabel, 1, 1);
        this.add(new Label("Video Settings:"), 0, 2);
        this.add(videoSettingsLabel, 1, 2);
    }

    /**
     * Updates the display information for the input file.
     * 
     * @param filePath The path of the input file.
     */
    public void updateInputFileInfo(String filePath) {
        inputFileLabel.setText(filePath != null ? filePath : "Not selected");
    }

    /**
     * Updates the display information for the output file.
     * 
     * @param filePath The path of the output file.
     */
    public void updateOutputFileInfo(String filePath) {
        outputFileLabel.setText(filePath != null ? filePath : "Not selected");
    }

    /**
     * Updates the display information for the video settings.
     * 
     * @param videoSettings The VideoSettings object containing the settings.
     */
    public void updateVideoSettingsInfo(VideoSettings videoSettings) {
        if (videoSettings != null) {
            String settingsInfo = String.format("Resolution: %s, Bitrate: %s, Frame Rate: %s",
                    videoSettings.getResolution(), videoSettings.getBitrate(), videoSettings.getFrameRate());
            videoSettingsLabel.setText(settingsInfo);
        } else {
            videoSettingsLabel.setText("Default settings");
        }
    }
}
