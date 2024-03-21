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

import java.io.File;

import com.papersaccul.paperffeditor.model.TaskStatus;
import com.papersaccul.paperffeditor.model.VideoSettings;
import com.papersaccul.paperffeditor.util.LocalizationUtil;
import com.papersaccul.paperffeditor.util.TaskRunner;

/**
 * MainWindow class represents the main window of the application.
 * It sets up the GUI structure including tabs for different functionalities.
 */
public class MainWindow extends BorderPane  {

    private VideoSettings videoSettings = new VideoSettings();
    private TaskStatus taskStatus = new TaskStatus();
    private TaskRunner taskRunner = new TaskRunner(videoSettings, taskStatus);

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
        
    // Create buttons
        Button startButton = new Button(LocalizationUtil.getString("button.start"));
        startButton.setOnAction(e -> {
            tabPane.getSelectionModel().select(taskMonitorTab);
            taskStatus.setProgress(0);
            taskStatus.setMessage("Starting");

            // Проверка существования файла
            if (new File(videoSettings.getOutputFilePath()).exists()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, 
                                        String.format(LocalizationUtil.getString("alert.fileExists"), 
                                        videoSettings.getOutputFilePath()), // for %s in LocalizationUtil
                                        ButtonType.YES, ButtonType.NO);
                alert.setHeaderText(null);
                alert.setGraphic(null);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    // user agreed to overwrite
                    taskRunner.render(true); 
                } else {
                    // user refused to be overwritten
                    taskStatus.setMessage("Ready");
                    return;
                }
            } else {
                // file does not exist, it is safe to run
                taskRunner.render(false);
            }
        });


        Button settingsButton = new Button(LocalizationUtil.getString("button.settings"));
        settingsButton.setOnAction(e -> new SettingsWindow(videoSettings).show());

        Button aboutButton = new Button(LocalizationUtil.getString("button.about"));
        aboutButton.setOnAction(e -> new AboutWindow().show());

        Button minimizeButton = new Button("➖"); // emoji for the button - cringe
        Button closeButton = new Button("❌");

        //minimizeButton.setOnAction(event -> ((Stage) minimizeButton.getScene().getWindow()).setIconified(true));
        //closeButton.setOnAction(event -> ((Stage) closeButton.getScene().getWindow()).close());
        closeButton.setStyle(getAccessibleHelp());
        closeButton.getStyleClass().add("close-button");

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
        //mainButtonBox.setAlignment(Pos.CENTER);

        this.setTop(combinedBox);
    }
}