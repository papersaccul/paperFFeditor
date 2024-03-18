package com.papersaccul.paperffeditor.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import java.io.IOException;


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
        fileSelectionTab.setContent(new FileSelectionPanel(videoSettings));
        
        Tab settingsTab = new Tab(LocalizationUtil.getString("tab.settings"));
        settingsTab.setClosable(false);
        settingsTab.setContent(settingsPanel);
        
        Tab taskMonitorTab = new Tab(LocalizationUtil.getString("tab.taskMonitor"));
        taskMonitorTab.setClosable(false);
        
        taskMonitorTab.setContent(taskMonitorPanel);

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
            String command = FFmpegCommandBuilder.buildCommand(videoSettings.getInputFilePath(), videoSettings.getOutputFilePath(), videoSettings);
            try {
                Runtime.getRuntime().exec(command);
            } catch (IOException ioException) {
                ioException.printStackTrace();
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