package com.yashakotkar.ui.main;

import com.yashakotkar.database.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainWindowController implements Initializable {

    @FXML
    private TextField           bookIdField;
    @FXML
    private Text                bookName;
    @FXML
    private Text                bookAuthor;
    @FXML
    private Text                bookStatus;
    @FXML
    private TextField           memberIdField;
    @FXML
    private Text                memberName;
    @FXML
    private Text                memberContact;
    @FXML
    private Text                memberEmail;
    @FXML
    private HBox                bookInfo;
    @FXML
    private HBox                memberInfo;
    @FXML
    private Button              issueButton;
    @FXML
    private TextField           renewBookIdField;
    @FXML
    private TextField           renewMemberField;
    @FXML
    private ListView<String>    issueDataList;
    @FXML
    private Button              renewButton;
    @FXML
    private Button              submissionButton;

    DatabaseHandler databaseHandler;

    private boolean memberIsValid               = false;
    private boolean bookIsAvailable             = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseHandler = DatabaseHandler.getInstance();
    }

    @FXML
    private void loadAddBook(ActionEvent event) {
        loadWindow("/com/yashakotkar/ui/addBook/AddBookWindow.fxml", "Add New Book");
    }

    @FXML
    private void loadAddMember(ActionEvent event) {
        loadWindow("/com/yashakotkar/ui/addMember/AddMemberWindow.fxml", "Add New Member");
    }

    @FXML
    private void loadBookTable(ActionEvent event) {
        loadWindow("/com/yashakotkar/ui/listBook/bookListWindow.fxml", "Books List");
    }

    @FXML
    private void loadMemberTable(ActionEvent event) {
        loadWindow("/com/yashakotkar/ui/listMember/memberListWindow.fxml", "Members List");
    }

    @FXML
    private void loadSettings(ActionEvent event) {
        loadWindow("/com/yashakotkar/settings/settingsWindow.fxml", "Setting");
    }

    private void loadWindow(String loc, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(loc));
            StageStyle stageStyle;
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("title");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    private void loadBookInfo(ActionEvent event) {
        clearBookCache();
        String      id      = bookIdField.getText().toUpperCase();
        String      query   = "SELECT * FROM Books WHERE _id = '" + id + "'";
        ResultSet   rs      = databaseHandler.executeQuery(query);
        boolean     flag    = false;
        try {
            while(rs.next()) {
                String  title       = rs.getString("title");
                String  author      = rs.getString("author");
                int     quantity    = rs.getInt("quantity");
                bookName.setText(title);
                bookAuthor.setText(author);
                bookStatus.setText(quantity>0?"Available" : "Not Available");

                flag            = true;
                bookIsAvailable = quantity>0;
            }

            if(!flag) {
                bookName.setText("No such Book Available");
                bookAuthor.setText("-");
                bookStatus.setText("-");
            }
        } catch (SQLException e) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if(bookIsAvailable && memberIsValid) {
                issueButton.setDisable(false);
            }
        }
    }

    @FXML
    private void loadMemberInfo(ActionEvent event) {
        clearMemberCache();
        String      id      = memberIdField.getText().toUpperCase();
        String      query   = "SELECT * FROM Members WHERE _id = '" + id + "'";
        ResultSet   rs      = databaseHandler.executeQuery(query);
        boolean     flag    = false;
        try {
            while(rs.next()) {
                String name     = rs.getString("name");
                String mobile   = rs.getString("mobile");
                String email    = rs.getString("email");

                memberName.setText(name);
                memberContact.setText(mobile);
                memberEmail.setText(email);

                flag            = true;
                memberIsValid   = true;
            }

            if(!flag) {
                memberName.setText("No such Member Id present.");
                memberContact.setText("-");
                memberEmail.setText("-");
            }
        } catch (SQLException e) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if(bookIsAvailable && memberIsValid) {
                issueButton.setDisable(false);
            }
        }
    }

    public void clearBookCache() {
        bookName.setText("");
        bookAuthor.setText("");
        bookStatus.setText("");
    }

    public void clearMemberCache() {
        memberName.setText("");
        memberContact.setText("");
        memberEmail.setText("");
    }

    @FXML
    private void loadIssueOperation(ActionEvent event) {
        String bookId   = bookIdField.getText().toUpperCase().trim();
        String memberId = memberIdField.getText().toUpperCase().trim();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Issue Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to issue the book " + bookId + " to " + memberId + "?");
        Optional<ButtonType> response = alert.showAndWait();

        if(response.get() == ButtonType.OK) {

            String query1 = "INSERT INTO Issues (bookId, memberId) VALUES (" +
                    "   '" +    bookId     + "',"  +
                    "   '" +    memberId   + "' "  +
                    ")";
            String query2 = "UPDATE Books SET quantity = quantity - 1 WHERE _id = '" + bookId + "'";
            System.out.println(query1 + "\n" + query2);

            if(databaseHandler.executeAction(query1) && databaseHandler.executeAction(query2)) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Book " + bookId + "  issued to " + memberId + "?");
                alert1.showAndWait();
                loadBookInfo(null);
                loadMemberInfo(null);
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed");
                alert1.setHeaderText(null);
                alert1.setContentText("Book " + bookId + "  failed to issue to " + memberId + "?");
                alert1.showAndWait();
            }
        } else {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Cancelled");
            alert1.setHeaderText(null);
            alert1.setContentText("Book " + bookId + "  cancelled to issue to " + memberId + "?");
            alert1.showAndWait();
        }
    }

    @FXML
    private void loadIssueInfo(ActionEvent event) {
        setButtonDisableStatus(true);

        ObservableList<String> issueData = FXCollections.observableArrayList();

        boolean bookPresent = false;
        boolean memberPresent = false;

        String bookId   = renewBookIdField.getText().toUpperCase().trim();
        String memberId = renewMemberField.getText().toUpperCase().trim();

        if(bookId.equals("") || memberId.equals("")) return;
//        "' AND memberId = '" + memberId +
        String query = "SELECT * FROM Issues WHERE bookId = '" + bookId +  "'";
        System.out.println(query);
        ResultSet rs = databaseHandler.executeQuery(query);
        try {
            while(rs.next()) {
                String      issueTime   = rs.getString("issueTime");
                int         renewCount  = rs.getInt("renewCount");
                System.out.println(issueTime + " " + renewCount);

                issueData.add("Issue Date and Time  : " + issueTime.toString());
                issueData.add("Renew Count          : " + renewCount);
                issueData.add("Book Information     :---------- ");

                String query1 = "SELECT * FROM Books WHERE _id = '" + bookId + "'";
                ResultSet rs1 = databaseHandler.executeQuery(query1);
                while(rs1.next()) {
                    issueData.add("     Book Id          :   " + rs1.getString("_id"));
                    issueData.add("     Book Name        :   " + rs1.getString("title"));
                    issueData.add("     Book Author      :   " + rs1.getString("author"));
                    issueData.add("     Book Publisher   :   " + rs1.getString("publisher"));
                    bookPresent = !rs1.getString("_id").equals("");
                    System.out.println(bookPresent);
                }

                issueData.add("Member Information   :---------- ");

                String query2 = "SELECT * FROM Members WHERE _id = '" + memberId + "'";
                ResultSet rs2 = databaseHandler.executeQuery(query2);
                while(rs2.next()) {
                    issueData.add("     Member Id        : " + rs2.getString("_id"));
                    issueData.add("     Member Name      : " + rs2.getString("name"));
                    issueData.add("     Member Mobile    : " + rs2.getString("mobile"));
                    issueData.add("     Member Email     : " + rs2.getString("email"));
                    memberPresent = !rs2.getString("_id").equals("");
                }
            }
            setButtonDisableStatus(!(bookPresent && memberPresent));
        } catch (SQLException e) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, e);
        }
        issueDataList.getItems().setAll(issueData);
    }

    @FXML
    private void loadRenew(ActionEvent event) {

        String bookId   = renewBookIdField.getText().toUpperCase().trim();
        String memberId = renewMemberField.getText().toUpperCase().trim();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Renew Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to renew the book " + bookId + " to " + memberId + "?");
        Optional<ButtonType> response = alert.showAndWait();

        if(response.get() == ButtonType.OK) {

            String query = "UPDATE Issues SET renewCount = renewCount + 1 , issueTime = CURRENT_TIMESTAMP WHERE " +
                            " bookId = '" + bookId + "' AND memberId = '" + memberId + "'";
            if(databaseHandler.executeAction(query)) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Book " + bookId + " has been Renewed to " + memberId + ".");
                alert1.showAndWait();
                renewBookIdField.setText("");
                renewMemberField.setText("");
                issueDataList.getItems().setAll("");
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed");
                alert1.setHeaderText(null);
                alert1.setContentText("Failed to Renewed book.");
                alert1.showAndWait();
            }
        } else {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Cancelled");
            alert1.setHeaderText(null);
            alert1.setContentText("Book " + bookId + "  cancelled to Renewed by " + memberId + "?");
            alert1.showAndWait();
        }



    }

    @FXML
    private void loadSubmission(ActionEvent event) {

        String bookId   = renewBookIdField.getText().toUpperCase().trim();
        String memberId = renewMemberField.getText().toUpperCase().trim();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Submission Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to submit the book " + bookId + " returned by " + memberId + "?");
        Optional<ButtonType> response = alert.showAndWait();

        if(response.get() == ButtonType.OK) {

            String query1 = "DELETE FROM Issues WHERE bookId = '" + bookId + "' AND memberId = '" + memberId + "'";
            String query2 = "UPDATE Books SET quantity = quantity + 1 WHERE _id = '" + bookId + "'";

            if(databaseHandler.executeAction(query1) && databaseHandler.executeAction(query2)) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Book has been Submitted.");
                alert1.showAndWait();
                renewBookIdField.setText("");
                renewMemberField.setText("");
                issueDataList.getItems().setAll("");
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed");
                alert1.setHeaderText(null);
                alert1.setContentText("Failed to Submit book.");
                alert1.showAndWait();
            }
        } else {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Cancelled");
            alert1.setHeaderText(null);
            alert1.setContentText("Book " + bookId + "  cancelled to Submitted by " + memberId + "?");
            alert1.showAndWait();
        }

    }

    private void setButtonDisableStatus(boolean status) {
        renewButton.setDisable(status);
        submissionButton.setDisable(status);
    }


}