package com.papersaccul.paperffeditor.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.papersaccul.paperffeditor.model.TaskStatus;
import com.papersaccul.paperffeditor.model.VideoSettings;
import com.papersaccul.paperffeditor.util.FFmpegCommandBuilder;
import com.papersaccul.paperffeditor.util.LocalizationUtil;

/**
 * MainWindow class represents the main window of the application.
 * It sets up the GUI structure including tabs for different functionalities.
 */
public class MainWindow extends BorderPane {

    private VideoSettings videoSettings = new VideoSettings();

    public MainWindow() {
        // Initialize the main layout components
        initUI();
    }

    /**
     * Initializes the user interface components and layout.
     */
    private void initUI() {
        // Create a TabPane as the central component
        TabPane tabPane = new TabPane();
        TaskMonitorPanel taskMonitorPanel = new TaskMonitorPanel();
        SettingsPanel settingsPanel = new SettingsPanel(videoSettings);

        // Create tabs for different functionalities
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
        
        
        // Add tabs to the TabPane
        tabPane.getTabs().addAll(fileSelectionTab, settingsTab, taskMonitorTab);
        
        // Set the TabPane as the center component of the BorderPane
        this.setCenter(tabPane);
        
        // Set padding for the BorderPane
        this.setPadding(new Insets(10, 10, 10, 10));
        
        // Create buttons for start and about
        Button startButton = new Button(LocalizationUtil.getString("button.start"));
        startButton.setOnAction(e -> {
            TaskStatus taskStatus = new TaskStatus(0, "Starting");
            String command = FFmpegCommandBuilder.buildCommand(videoSettings.getInputFilePath(), videoSettings.getOutputFilePath(), videoSettings);
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
                            // Update progress based on the output of the process
                            // This is a placeholder for actual progress parsing logic
                            progress += 0.01; // Increment progress for demonstration
                            taskStatus.setProgress(Math.min(progress, 1.0)); // Ensure progress does not exceed 100%
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