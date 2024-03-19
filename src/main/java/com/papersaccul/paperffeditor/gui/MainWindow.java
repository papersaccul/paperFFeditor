package com.papersaccul.paperffeditor.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

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
        Tab settingsTab = new Tab(LocalizationUtil.getString("tab.settings"));
        Tab taskMonitorTab = new Tab(LocalizationUtil.getString("tab.taskMonitor"));

        fileSelectionTab.setClosable(false);

        FileSelectionPanel fileSelectionPanel = new FileSelectionPanel(videoSettings);
        fileSelectionTab.setContent(fileSelectionPanel);
        
        settingsTab.setClosable(false);
        settingsTab.setContent(settingsPanel);
        
        
        taskMonitorTab.setClosable(false);
        taskMonitorTab.setContent(taskMonitorPanel);

    // Add Observers
        videoSettings.addObserver(fileSelectionPanel);
        videoSettings.addObserver(settingsPanel);
        videoSettings.addObserver(taskMonitorPanel);
        taskStatus.addObserver(taskMonitorPanel);
        
    // Add Tabs
        tabPane.setStyle("-fx-background-color: #292e39;"); 
        tabPane.getTabs().addAll(fileSelectionTab, settingsTab, taskMonitorTab);
        
        this.setCenter(tabPane);
        this.setPadding(new Insets(0, 0, 0, 0));
        
    // Create buttons for start and about
        Button startButton = new Button(LocalizationUtil.getString("button.start"));
        //startButton.setDisable(videoSettings.getInputFilePath() == "");
        startButton.setOnAction(e -> {

            tabPane.getSelectionModel().select(taskMonitorTab);
            taskStatus.setProgress(0);
            taskStatus.setMessage("Starting");
    // Overwrite check
            String command;
            if (new File(videoSettings.getOutputFilePath()).exists()) {
                //Dialog alert = new Dialog();
                Alert alert = new Alert(Alert.AlertType.NONE, 
                                        String.format(LocalizationUtil.getString("alert.fileExists"), 
                                        videoSettings.getOutputFilePath()), // for %s in LocalizationUtil
                                        ButtonType.YES, ButtonType.NO
                                        );
                alert.setHeaderText(null);
                alert.setGraphic(null);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    command = FFmpegCommandBuilder.buildCommand(videoSettings.getInputFilePath(), videoSettings.getOutputFilePath(), videoSettings) + " -y";
                } else {
                    taskStatus.setMessage("Ready");
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
        Button minimizeButton = new Button("➖"); // emoji for the button - cringe
        Button closeButton = new Button("❌");

        //minimizeButton.setOnAction(event -> ((Stage) minimizeButton.getScene().getWindow()).setIconified(true));
        //closeButton.setOnAction(event -> ((Stage) closeButton.getScene().getWindow()).close());
        closeButton.setStyle(getAccessibleHelp());
        closeButton.getStyleClass().add("close-button");

        // Import javafx.stage.Stage to fix the error
        minimizeButton.setOnAction(event -> ((Stage) minimizeButton.getScene().getWindow()).setIconified(true));
        closeButton.setOnAction(event -> ((Stage) closeButton.getScene().getWindow()).close());
        // Add main buttons
        HBox mainButtonBox = new HBox(0, startButton, settingsButton, aboutButton);
        HBox controlButtonBox = new HBox(0, minimizeButton, closeButton);
        HBox combinedBox = new HBox(); 

        // mainButtonBox.setPadding(new Insets(0, 0, 0, 0));
        // controlButtonBox.setPadding(new Insets(0, 0, 0, 0));

        HBox.setHgrow(controlButtonBox, Priority.NEVER); 
        HBox.setHgrow(mainButtonBox, Priority.ALWAYS); 
        combinedBox.getChildren().addAll(mainButtonBox, controlButtonBox);
        combinedBox.setAlignment(Pos.TOP_RIGHT);

        this.setTop(combinedBox);
    }
}