package com.papersaccul.paperffeditor.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import com.papersaccul.paperffeditor.model.TaskStatus;
import com.papersaccul.paperffeditor.model.VideoSettings;

/**
 * TaskMonitorPanel class represents the panel for monitoring tasks in the application.
 * It displays the progress and status of ongoing tasks.
 */
public class TaskMonitorPanel extends GridPane {

    private ProgressBar progressBar;
    private Label statusLabel;
    private Label inputFileLabel;
    private Label outputFileLabel;
    private Label videoSettingsLabel;

    public TaskMonitorPanel() {
        initUI();
    }

    /**
     * Initializes the user interface components and layout for the task monitor panel.
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

        // Initialize components
        progressBar = new ProgressBar(0);
        statusLabel = new Label("Ready");

        // Set component properties
        progressBar.setPrefWidth(300);
        progressBar.setPadding(new Insets(10, 0, 10, 0));

        this.add(progressBar, 0, 3);
        this.add(statusLabel, 0, 4);


        // Set VBox properties
        this.setPadding(new Insets(20));
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

    /**
     * Updates the progress bar and status label based on the given task status.
     * @param taskStatus the current status of the task
     */
    public void updateTaskStatus(TaskStatus taskStatus) {
        progressBar.setProgress(taskStatus.getProgress());
        statusLabel.setText(taskStatus.getMessage());
    }
}
