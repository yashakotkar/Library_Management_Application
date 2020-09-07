package com.yashakotkar.ui.addBook;

import com.yashakotkar.database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddBookWindowController implements Initializable {

    @FXML
    private AnchorPane mainContainer;
    @FXML
    private TextField   title;
    @FXML
    private TextField   id;
    @FXML
    private TextField   author;
    @FXML
    private TextField   publisher;
    @FXML
    private TextField   quantity;
    @FXML
    private Button      saveButton;
    @FXML
    private Button      cancelButton;

    DatabaseHandler databaseHandler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseHandler = DatabaseHandler.getInstance();
        checkData();
    }


    @FXML
    private void AddBook(ActionEvent event) throws SQLException {
        String bookId           = id.getText().toUpperCase().trim();
        String bookTitle        = title.getText().toUpperCase().trim();
        String bookAuthor       = author.getText().toUpperCase().trim();
        String bookPublisher    = publisher.getText().toUpperCase().trim();
        int    bookQuantity     = Integer.parseInt( quantity.getText());

//        Check if any filed is Empty
        boolean flag = bookId.isEmpty() || bookTitle.isEmpty() || bookAuthor.isEmpty() || bookPublisher.isEmpty();
        if(flag) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Fields are empty");
            alert.setContentText("Please Enter in all fields.");
            alert.showAndWait();
            return;
       }

//        Checking if book is already present
        String bookCheckQuery = "SELECT * FROM Books WHERE _id = '" + bookId + "'";
        ResultSet rsBookInfo = databaseHandler.executeQuery(bookCheckQuery);
        if(rsBookInfo.next()) {
//            If already present then add the quantity of books to previous quantity
            int availableQuantity   = rsBookInfo.getInt("quantity");
            int newQuantity         = availableQuantity + bookQuantity;

            String quantityUpdateQuery = "UPDATE Books SET quantity = " +
                    newQuantity + " WHERE _ID = '" + bookId + "'";

            if(databaseHandler.executeAction(quantityUpdateQuery)) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Book Already Present");
                alert.setContentText("New quantity is " + newQuantity + ".");
                alert.showAndWait();
                clearCache();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Something went Wrong.");
                alert.showAndWait();
            }
        } else {
//            If book not already present insert it as new book in database
            String query = "INSERT INTO Books VALUES (" +
                    "   '" + bookId         + "'," +
                    "   '" + bookTitle      + "'," +
                    "   '" + bookAuthor     + "'," +
                    "   '" + bookPublisher  + "'," +
                    "    " + bookQuantity   + "  " +
                    "   )";

            System.out.println(query);
            if(databaseHandler.executeAction(query)) {
                Alert.AlertType alertAlertType;
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Success");
                alert.setContentText("Values inserted");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Already Present");
                alert.setContentText("Go To update Book to add new Book.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void closeDialog() {
        Stage stage = (Stage) mainContainer.getScene().getWindow();
        stage.close();
    }

    private void checkData() {
        String query = "SELECT * FROM Books";
        ResultSet rs = databaseHandler.executeQuery(query);
        try {
            while (rs.next()) {
                String title = rs.getString("title");
                System.out.println(title);
            }
        } catch (SQLException e) {
            Logger.getLogger(AddBookWindowController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void clearCache() {
        id.setText("");
        title.setText("");
        author.setText("");
        publisher.setText("");
        quantity.setText("");
    }
}













