package com.yashakotkar.settings;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsWindowController implements Initializable {
    @FXML
    private AnchorPane mainContainer;
    @FXML
    private TextField       noDaysWithoutFine;
    @FXML
    private TextField       finePerDay;
    @FXML
    private TextField       noOfBooksMemberCanKeep;
    @FXML
    private TextField       username;
    @FXML
    private PasswordField   password;
    @FXML
    private Button          saveButton;
    @FXML
    private Button          cancelButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initDefaultValues();

    }

    private void initDefaultValues() {
        Preferences preferences = Preferences.getPreference();
        noDaysWithoutFine.setText(String.valueOf(preferences.getNoDaysWithoutFine()));
        finePerDay.setText(String.valueOf(preferences.getFinePerDay()));
        noOfBooksMemberCanKeep.setText(String.valueOf(preferences.getNoOfBooksMemberCanKeep()));
        username.setText(preferences.getUsername());
        password.setText(preferences.getPassword());
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        Preferences preferences = Preferences.getPreference();
        preferences.setNoDaysWithoutFine(Integer.parseInt(noDaysWithoutFine.getText()));
        preferences.setFinePerDay(Float.parseFloat(finePerDay.getText()));
        preferences.setNoOfBooksMemberCanKeep(Integer.parseInt(noOfBooksMemberCanKeep.getText()));
        preferences.setUsername(username.getText());
        preferences.setPassword(password.getText());

        Preferences.writePreferencesToFile(preferences);

        Alert.AlertType alertAlertType;
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText("New settings saved successfully");
        alert.showAndWait();
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        Stage stage = (Stage) mainContainer.getScene().getWindow();
        stage.close();
    }


}
