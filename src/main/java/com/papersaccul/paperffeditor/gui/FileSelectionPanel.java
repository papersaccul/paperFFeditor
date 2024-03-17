package com.papersaccul.paperffeditor.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import com.papersaccul.paperffeditor.util.LocalizationUtil;

import java.io.File;

/**
 * FileSelectionPanel class provides the GUI elements for file selection.
 */
public class FileSelectionPanel extends GridPane {

    private TextField inputFilePathField;
    private TextField outputFilePathField;

    public FileSelectionPanel() {
        initUI();
    }

    /**
     * Initializes the user interface components and layout for file selection.
     */
    private void initUI() {
        this.setPadding(new Insets(10));
        this.setHgap(10);
        this.setVgap(10);

        Label inputLabel = new Label(LocalizationUtil.getString("label.inputFile"));
        inputFilePathField = new TextField();
        inputFilePathField.setEditable(false);
        Button inputBrowseButton = new Button(LocalizationUtil.getString("button.browse"));
        inputBrowseButton.setOnAction(e -> chooseFile(inputFilePathField, true));

        Label outputLabel = new Label(LocalizationUtil.getString("label.outputFile"));
        outputFilePathField = new TextField();
        outputFilePathField.setEditable(false);
        Button outputBrowseButton = new Button(LocalizationUtil.getString("button.browse"));
        outputBrowseButton.setOnAction(e -> chooseFile(outputFilePathField, false));

        this.add(inputLabel, 0, 0);
        this.add(inputFilePathField, 1, 0);
        this.add(inputBrowseButton, 2, 0);
        this.add(outputLabel, 0, 1);
        this.add(outputFilePathField, 1, 1);
        this.add(outputBrowseButton, 2, 1);
    }

    /**
     * Opens a FileChooser dialog to select a file and sets the file path to the appropriate TextField.
     * @param textField The TextField to set the file path.
     * @param isInputFile Flag to determine if the file is for input or output.
     */
    private void chooseFile(TextField textField, boolean isInputFile) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(LocalizationUtil.getString(isInputFile ? "fileChooser.inputTitle" : "fileChooser.outputTitle"));
        if (isInputFile) {
            File selectedFile = fileChooser.showOpenDialog(this.getScene().getWindow());
            if (selectedFile != null) {
                textField.setText(selectedFile.getAbsolutePath());
            }
        } else {
            File savedFile = fileChooser.showSaveDialog(this.getScene().getWindow());
            if (savedFile != null) {
                textField.setText(savedFile.getAbsolutePath());
            }
        }
    }
}
