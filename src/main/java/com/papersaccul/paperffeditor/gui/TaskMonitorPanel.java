package com.papersaccul.paperffeditor.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import com.papersaccul.paperffeditor.model.TaskStatus;

/**
 * TaskMonitorPanel class represents the panel for monitoring tasks in the application.
 * It displays the progress and status of ongoing tasks.
 */
public class TaskMonitorPanel extends VBox {

    private ProgressBar progressBar;
    private Label statusLabel;

    public TaskMonitorPanel() {
        initUI();
    }

    /**
     * Initializes the user interface components and layout for the task monitor panel.
     */
    private void initUI() {
        // Initialize components
        progressBar = new ProgressBar(0);
        statusLabel = new Label("Ready");

        // Set component properties
        progressBar.setPrefWidth(300);
        progressBar.setPadding(new Insets(10, 0, 10, 0));

        // Add components to the VBox
        this.getChildren().addAll(progressBar, statusLabel);

        // Set VBox properties
        this.setPadding(new Insets(20));
        this.setSpacing(10);
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
