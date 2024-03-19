package com.papersaccul.paperffeditor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
        BorderPane borderPane = new BorderPane();

        var windowSize = new Object() {
            int width = 920;
            int height = 700;
        };

        Scene scene = new Scene(borderPane, windowSize.width, windowSize.height);
        Rectangle rect = new Rectangle(windowSize.width, windowSize.height);

        
        //stage.initStyle(StageStyle.TRANSPARENT);
        scene.getStylesheets().add(getClass().getResource("buttonStyle.css").toExternalForm());  

        // super-mega-duper theme by mkpaz - atlantafx
        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());


        borderPane.setCenter(mainWindow);

        scene.setOnMousePressed(mouseEvent -> {
            dragDelta.x = primaryStage.getX() - mouseEvent.getScreenX();
            dragDelta.y = primaryStage.getY() - mouseEvent.getScreenY();
        });

        scene.setOnMouseDragged(mouseEvent -> {
            primaryStage.setX(mouseEvent.getScreenX() + dragDelta.x);
            primaryStage.setY(mouseEvent.getScreenY() + dragDelta.y);
        });


        
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("PaperFFeditor - FFmpeg GUI");
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }

    class Delta { double x, y; }
    final Delta dragDelta = new Delta();
}
