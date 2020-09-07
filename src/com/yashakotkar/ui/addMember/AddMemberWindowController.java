package com.yashakotkar.ui.addMember;

import com.yashakotkar.database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddMemberWindowController implements Initializable {

    @FXML
    private AnchorPane mainContainer;
    @FXML
    private TextField name;
    @FXML
    private TextField id;
    @FXML
    private TextField mobile;
    @FXML
    private TextField email;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    DatabaseHandler databaseHandler;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseHandler = DatabaseHandler.getInstance();
    }

    @FXML
    private void AddMember(ActionEvent event) {


        String memberName   = name.getText().toUpperCase().trim();
        String memberId     = id.getText().toUpperCase().trim();
        String memberMobile = mobile.getText().toUpperCase().trim();
        String memberEmail  = email.getText().toUpperCase().trim();

        boolean flag = memberId.isEmpty() || memberName.isEmpty() || memberMobile.isEmpty() || memberEmail.isEmpty();
        if(flag) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Fields are empty");
            alert.setContentText("Please Enter in all fields.");
            alert.showAndWait();
            return;
        }

        String query = "INSERT INTO Members VALUES (" +
                "   '" + memberId       + "'," +
                "   '" + memberName     + "'," +
                "   '" + memberMobile   + "'," +
                "   '" + memberEmail    + "' " +
                "   )";

        System.out.println(query);
        if(databaseHandler.executeAction(query)) {
            Alert.AlertType alertAlertType;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Success");
            alert.setContentText("Member Added to Database");
            alert.showAndWait();
            clearCache();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Already Present");
            alert.setContentText("Member already present, If want to update details goto update member details option.");
            alert.showAndWait();
        }
    }

    @FXML
    private void closeDialog(ActionEvent event) {
        Stage stage = (Stage) mainContainer.getScene().getWindow();
        stage.close();
    }

    private void clearCache() {
        name.setText("");
        id.setText("");
        mobile.setText("");
        email.setText("");
    }
}
