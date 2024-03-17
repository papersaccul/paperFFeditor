package com.papersaccul.paperffeditor.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import com.papersaccul.paperffeditor.AppConfig;

/**
 * MainWindow class represents the main window of the application.
 * It sets up the GUI structure including tabs for different functionalities.
 */
public class MainWindow extends BorderPane {

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
        
        // Create tabs for different functionalities
        Tab fileSelectionTab = new Tab(AppConfig.getResourceBundle().getString("tab.fileSelection"));
        fileSelectionTab.setClosable(false);
        fileSelectionTab.setContent(new FileSelectionPanel());
        
        Tab settingsTab = new Tab(AppConfig.getResourceBundle().getString("tab.settings"));
        settingsTab.setClosable(false);
        settingsTab.setContent(new SettingsPanel());
        
        Tab taskMonitorTab = new Tab(AppConfig.getResourceBundle().getString("tab.taskMonitor"));
        taskMonitorTab.setClosable(false);
        taskMonitorTab.setContent(new TaskMonitorPanel());
        
        Tab infoDisplayTab = new Tab(AppConfig.getResourceBundle().getString("tab.infoDisplay"));
        infoDisplayTab.setClosable(false);
        infoDisplayTab.setContent(new InfoDisplayPanel());
        
        // Add tabs to the TabPane
        tabPane.getTabs().addAll(fileSelectionTab, settingsTab, taskMonitorTab, infoDisplayTab);
        
        // Set the TabPane as the center component of the BorderPane
        this.setCenter(tabPane);
        
        // Set padding for the BorderPane
        this.setPadding(new Insets(10, 10, 10, 10));
    }
}
