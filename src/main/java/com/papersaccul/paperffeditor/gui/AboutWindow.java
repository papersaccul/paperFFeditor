package com.papersaccul.paperffeditor.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AboutWindow extends Stage {

    public AboutWindow() { 
        initUI();
    }

    private void initUI() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label aboutLabel = new Label("PaperFFeditor - simple FFmpeg GUI.");
        Hyperlink link = new Hyperlink("https://github.com/papersaccul/paperFFeditor");
        link.setOnAction(e -> {
            try {
                java.awt.Desktop.getDesktop().browse(new java.net.URI(link.getText()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        gridPane.add(aboutLabel, 0, 0);
        gridPane.add(link, 0, 1);

        Scene scene = new Scene(gridPane, 350, 150);
        this.setTitle("About");
        this.setScene(scene);
    }
}
