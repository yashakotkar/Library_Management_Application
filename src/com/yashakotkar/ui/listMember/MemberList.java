package com.yashakotkar.ui.listMember;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MemberList extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MemberListWindow.fxml"));
        primaryStage.setTitle("Member List");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
