package com.papersaccul.paperffeditor.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.GridPane;
import com.papersaccul.paperffeditor.model.TaskStatus;
import com.papersaccul.paperffeditor.model.VideoSettingTable;
import com.papersaccul.paperffeditor.model.VideoSettings;
import com.papersaccul.paperffeditor.model.VideoSettings.VideoSettingsObserver;
import com.papersaccul.paperffeditor.util.FFmpegCommandBuilder; 
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Collections;


/**
 * TaskMonitorPanel class represents the panel for monitoring tasks in the application.
 * It displays the progress and status of ongoing tasks.
 */
public class TaskMonitorPanel extends GridPane implements VideoSettingsObserver {

    private ProgressBar progressBar;
    private Label statusLabel;
    private Label inputFileLabel;
    private Label outputFileLabel;
    private TableView<VideoSettingTable> videoSettingsTable; 
    private Label ffmpegCommandLabel; 

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
        ffmpegCommandLabel = new Label(); 

    // Initialize video settings table
        videoSettingsTable = new TableView<>();
        videoSettingsTable.setEditable(false);
        TableColumn<VideoSettingTable, String> settingColumn = new TableColumn<>("Setting");
        settingColumn.setCellValueFactory(cellData -> cellData.getValue().settingProperty());

        TableColumn<VideoSettingTable, String> inputColumn = new TableColumn<>("Input");
        inputColumn.setCellValueFactory(cellData -> cellData.getValue().inputProperty());

        TableColumn<VideoSettingTable, String> outputColumn = new TableColumn<>("Output");
        outputColumn.setCellValueFactory(cellData -> cellData.getValue().outputProperty());

        Collections.addAll(videoSettingsTable.getColumns(), settingColumn, inputColumn, outputColumn);

        this.add(new Label("Input File:"), 0, 0);
        this.add(inputFileLabel, 1, 0);
        this.add(new Label("Output File:"), 0, 1);
        this.add(outputFileLabel, 1, 1);
        this.add(new Label("Video Settings:"), 0, 2);
        this.add(videoSettingsTable, 1, 2); 
        this.add(new Label("FFmpeg Command:"), 0, 3); 
        this.add(ffmpegCommandLabel, 1, 3); 
        
        // Initialize components
        progressBar = new ProgressBar(0);
        statusLabel = new Label("Ready");

        // Set component properties
        progressBar.setPrefWidth(300);
        progressBar.setPadding(new Insets(10, 0, 10, 0));

        this.add(progressBar, 0, 4);
        this.add(statusLabel, 0, 5);

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
    @Override
    public void updateVideoSettingsInfo(VideoSettings videoSettings) {
        ObservableList<VideoSettingTable> data = FXCollections.observableArrayList();
        if (videoSettings != null) {
            // setting | input | output
            data.add(new VideoSettingTable("Resolution", videoSettings.getInputVideoWidth() + "x" + videoSettings.getInputVideoHeight(), videoSettings.getVideoWidth() + "x" + videoSettings.getVideoHeight()));
            data.add(new VideoSettingTable("Video Bitrate", videoSettings.getInputVideoBitrate() + " kbps", videoSettings.getVideoBitrate() + " kbps"));
            data.add(new VideoSettingTable("Audio Bitrate", videoSettings.getInputAudioBitrate() + " kbps", videoSettings.getAudioBitrate() + " kbps"));
            data.add(new VideoSettingTable("Frame Rate", videoSettings.getInputFrameRate() + " fps", videoSettings.getFrameRate() + " fps"));
            data.add(new VideoSettingTable("Video Codec", videoSettings.getInputVideoCodec(), videoSettings.getVideoCodec()));
            data.add(new VideoSettingTable("Audio Codec", videoSettings.getInputAudioCodec(), videoSettings.getAudioCodec()));
            data.add(new VideoSettingTable("Volume", "100", videoSettings.getVolume() + "%"));
            data.add(new VideoSettingTable("Audio Channels", videoSettings.getInputAudioChannels(), videoSettings.getAudioChannels()));
            
            videoSettingsTable.setItems(data);
            updateInputFileInfo(videoSettings.getInputFilePath());
            updateOutputFileInfo(videoSettings.getOutputFilePath());

            String ffmpegCommand = FFmpegCommandBuilder.buildCommand(videoSettings.getInputFilePath(), videoSettings.getOutputFilePath(), videoSettings);
            ffmpegCommandLabel.setText(ffmpegCommand); 
        } else {
            videoSettingsTable.setItems(FXCollections.observableArrayList(new VideoSettingTable("Default settings", "", "")));
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
