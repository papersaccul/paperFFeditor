package com.papersaccul.paperffeditor.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;


import com.papersaccul.paperffeditor.model.TaskStatus;
import com.papersaccul.paperffeditor.model.VideoSettings;
import com.papersaccul.paperffeditor.util.FFmpegCommandBuilder;
import com.papersaccul.paperffeditor.util.LocalizationUtil;

/**
 * MainWindow class represents the main window of the application.
 * It sets up the GUI structure including tabs for different functionalities.
 */
public class MainWindow extends BorderPane  {

    private VideoSettings videoSettings = new VideoSettings();
    private TaskStatus taskStatus = new TaskStatus();

    public MainWindow() {
        initUI();
    }

    /**
     * Initializes the user interface components and layout.
     */
    private void initUI() {
    // Create TabPane
        TabPane tabPane = new TabPane();
        TaskMonitorPanel taskMonitorPanel = new TaskMonitorPanel();
        SettingsPanel settingsPanel = new SettingsPanel(videoSettings);

    // Create tabs
        Tab fileSelectionTab = new Tab(LocalizationUtil.getString("tab.fileSelection"));
        fileSelectionTab.setClosable(false);

        FileSelectionPanel fileSelectionPanel = new FileSelectionPanel(videoSettings);
        fileSelectionTab.setContent(fileSelectionPanel);
        
        Tab settingsTab = new Tab(LocalizationUtil.getString("tab.settings"));
        settingsTab.setClosable(false);
        settingsTab.setContent(settingsPanel);
        
        Tab taskMonitorTab = new Tab(LocalizationUtil.getString("tab.taskMonitor"));
        taskMonitorTab.setClosable(false);
        
        taskMonitorTab.setContent(taskMonitorPanel);

        videoSettings.addObserver(fileSelectionPanel);
        videoSettings.addObserver(settingsPanel);
        videoSettings.addObserver(taskMonitorPanel);
        taskStatus.addObserver(taskMonitorPanel);
        
    // Add Tabs
        tabPane.getTabs().addAll(fileSelectionTab, settingsTab, taskMonitorTab);
        
        this.setCenter(tabPane);
        this.setPadding(new Insets(10, 10, 10, 10));
        
    // Create buttons for start and about
        Button startButton = new Button(LocalizationUtil.getString("button.start"));
        startButton.setOnAction(e -> {

    // Overwrite check
            String command;
            if (new File(videoSettings.getOutputFilePath()).exists()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, String.format(LocalizationUtil.getString("alert.fileExists"), videoSettings.getOutputFilePath()), ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    command = FFmpegCommandBuilder.buildCommand(videoSettings.getInputFilePath(), videoSettings.getOutputFilePath(), videoSettings) + " -y";
                } else {
                    return; 
                }
            } else {
                command = FFmpegCommandBuilder.buildCommand(videoSettings.getInputFilePath(), videoSettings.getOutputFilePath(), videoSettings);
            }

    // command task
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.redirectErrorStream(true);
            try {
                Process process = processBuilder.start();
                new Thread(() -> {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    double progress = 0.0;
                    try {
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line);
                            progress += 0.01; 
                            taskStatus.setProgress(Math.min(progress, 1.0)); 
                            taskStatus.setMessage("Processing");
                        }
                        process.waitFor();
                        taskStatus.setProgress(1.0);
                        taskStatus.setMessage("Completed");
                    } catch (IOException | InterruptedException ioException) {
                        ioException.printStackTrace();
                        taskStatus.setMessage("Error: " + ioException.getMessage());
                    }
                }).start();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                taskStatus.setMessage("Error: " + ioException.getMessage());
            }
        });
        Button settingsButton = new Button(LocalizationUtil.getString("button.settings"));
        Button aboutButton = new Button(LocalizationUtil.getString("button.about"));
        
        // Add buttons 
        HBox buttonBox = new HBox(10, startButton, settingsButton, aboutButton);
        buttonBox.setPadding(new Insets(10, 0, 10, 0));
        this.setTop(buttonBox);
    }
}