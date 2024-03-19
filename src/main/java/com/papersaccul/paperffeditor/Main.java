package com.papersaccul.paperffeditor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.papersaccul.paperffeditor.gui.MainWindow;


import atlantafx.base.theme.NordDark;
//import atlantafx.base.theme.NordLight;

/**
 * The main class for the PaperFFmedia project.
 * This class launches the JavaFX application and adds start button logic for FFmpeg rendering.
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
     * Starts the JavaFX application, sets up the main window, and adds start button logic for FFmpeg rendering.
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        MainWindow mainWindow = new MainWindow();
        Scene scene = new Scene(mainWindow, 800, 600);

        // Old theme
        //scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());  

        // super-mega-duper theme by mkpaz - atlantafx
        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());


        primaryStage.setTitle("PaperFFeditor - FFmpeg GUI");
        primaryStage.setScene(scene);
        //primaryStage.getIcons().add(new javafx.scene.image.Image("file:src/main/resources/icon.png"));
        primaryStage.show();
    }
}
