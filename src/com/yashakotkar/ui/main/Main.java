package com.yashakotkar.ui.main;

import com.yashakotkar.database.DatabaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        primaryStage.setTitle("Library Management Application");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

//        Since loading of database slows down the startup of application so use a new thread to load up database
        new Thread(DatabaseHandler::getInstance).start();
    }
}
