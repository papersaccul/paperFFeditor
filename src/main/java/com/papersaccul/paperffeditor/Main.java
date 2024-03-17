package com.papersaccul.paperffeditor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.papersaccul.paperffeditor.gui.MainWindow;

/**
 * The main class for the PaperFFmedia project.
 * This class launches the JavaFX application.
 */
public class Main extends Application {

    /**
     * The main entry point for the application.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }


    /**
     * Starts the JavaFX application and sets up the main window.
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        MainWindow mainWindow = new MainWindow();
        Scene scene = new Scene(mainWindow, 800, 600);
        
        primaryStage.setTitle("PaperFFmedia - FFmpeg GUI");
        primaryStage.setScene(scene);
        //primaryStage.getIcons().add(new javafx.scene.image.Image("file:src/main/resources/icon.png"));
        primaryStage.show();
    }
}
