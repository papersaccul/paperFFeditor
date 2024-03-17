package com.papersaccul.paperffeditor.util;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

/**
 * FileHandler class provides utility methods for file operations.
 */
public class FileHandler {

    /**
     * Opens a file chooser dialog to select a file.
     * 
     * @param stage The parent stage for the file chooser dialog.
     * @param forInput Specifies whether the file is for input or output.
     * @return The selected file, or null if no file was selected.
     */
    public static File chooseFile(Stage stage, boolean forInput) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(forInput ? "Select Input File" : "Select Output File");

        // Set extension filters
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Media Files", "*.mp4", "*.avi", "*.mkv", "*.mov");
        fileChooser.getExtensionFilters().add(filter);

        if (forInput) {
            return fileChooser.showOpenDialog(stage);
        } else {
            return fileChooser.showSaveDialog(stage);
        }
    }

    /**
     * Updates the label with the path of the selected file.
     * 
     * @param filePathField The text field to update with the file path.
     * @param file The file selected by the user.
     */
    public static void updateFilePathField(javafx.scene.control.TextField filePathField, File file) {
        if (file != null) {
            filePathField.setText(file.getAbsolutePath());
        }
    }
}
