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

        ffmpegCommandLabel = new Label();
        ffmpegCommandLabel.setPrefWidth(900); 
        

        // Initialize video settings table
        videoSettingsTable = new TableView<>();
        videoSettingsTable.setEditable(false);
        videoSettingsTable.setMaxWidth(Double.MAX_VALUE);

        videoSettingsTable.setMinHeight(100); 
        videoSettingsTable.setPrefHeight(400); 
        //videoSettingsTable.setMaxHeight(700); 
        

        TableColumn<VideoSettingTable, String> settingColumn = new TableColumn<>("Setting");
        settingColumn.setCellValueFactory(cellData -> cellData.getValue().settingProperty());
        settingColumn.setPrefWidth(200);

        TableColumn<VideoSettingTable, String> inputColumn = new TableColumn<>("Input");
        inputColumn.setCellValueFactory(cellData -> cellData.getValue().inputProperty());
        inputColumn.setPrefWidth(350);

        TableColumn<VideoSettingTable, String> outputColumn = new TableColumn<>("Output");
        outputColumn.setCellValueFactory(cellData -> cellData.getValue().outputProperty());
        outputColumn.setPrefWidth(350);


        Collections.addAll(videoSettingsTable.getColumns(), settingColumn, inputColumn, outputColumn);

        this.add(videoSettingsTable, 0, 0, 2, 1); 
        
        // Initialize components
        progressBar = new ProgressBar(0);
        statusLabel = new Label("Ready");

        // Set component properties
        progressBar.setPrefWidth(900);
        progressBar.setPadding(new Insets(10, 0, 10, 0));

        this.add(ffmpegCommandLabel, 0, 1, 2, 1); 
        this.add(progressBar, 0, 2, 2, 1);
        this.add(statusLabel, 0, 3, 2, 1);
        
        // Set VBox properties
        this.setPadding(new Insets(20));
    }

    /**
     * Updates the display information for the input file, output file, and ffmpeg command.
     * 
     * @param videoSettings The VideoSettings object containing the settings.
     */
    @Override
    public void updateVideoSettingsInfo(VideoSettings videoSettings) {
        ObservableList<VideoSettingTable> data = FXCollections.observableArrayList();
        if (videoSettings != null) {
            String ffmpegCommand = FFmpegCommandBuilder.buildCommand(videoSettings.getInputFilePath(), videoSettings.getOutputFilePath(), videoSettings);
            ffmpegCommandLabel.setText("FFmpeg Command:     " + ffmpegCommand);
            data.add(new VideoSettingTable("File", videoSettings.getInputFilePath(), videoSettings.getOutputFilePath()));
            //data.add(new VideoSettingTable("FFmpeg Command", ffmpegCommand, ""));
            data.add(new VideoSettingTable("Resolution", videoSettings.getInputVideoWidth() + "x" + videoSettings.getInputVideoHeight(), videoSettings.getVideoWidth() + "x" + videoSettings.getVideoHeight()));
            data.add(new VideoSettingTable("Video Bitrate", videoSettings.getInputVideoBitrate() + " kbps", videoSettings.getVideoBitrate() + " kbps"));
            data.add(new VideoSettingTable("Audio Bitrate", videoSettings.getInputAudioBitrate() + " kbps", videoSettings.getAudioBitrate() + " kbps"));
            data.add(new VideoSettingTable("Frame Rate", videoSettings.getInputFrameRate() + " fps", videoSettings.getFrameRate() + " fps"));
            data.add(new VideoSettingTable("Video Codec", videoSettings.getInputVideoCodec(), videoSettings.getVideoCodec()));
            data.add(new VideoSettingTable("Audio Codec", videoSettings.getInputAudioCodec(), videoSettings.getAudioCodec()));
            data.add(new VideoSettingTable("Volume", "", videoSettings.getVolume() + "%"));
            data.add(new VideoSettingTable("Audio Channels", videoSettings.getInputAudioChannels(), videoSettings.getAudioChannels()));
            
            videoSettingsTable.setItems(data);
        } else {
            videoSettingsTable.setItems(FXCollections.observableArrayList(new VideoSettingTable("Default settings", "", "")));
            ffmpegCommandLabel.setText("Default settings");
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
