package com.yashakotkar.ui.listMember;

import com.yashakotkar.database.DatabaseHandler;
import com.yashakotkar.ui.addBook.AddBookWindowController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MemberListWindowController implements Initializable {

    ObservableList<Member> memberList = FXCollections.observableArrayList();
    @FXML
    private AnchorPane mainContainer;
    @FXML
    private TableView<Member> memberListTable;
    @FXML
    private TableColumn<Member, String> idCol;
    @FXML
    private TableColumn<Member, String> nameCol;
    @FXML
    private TableColumn<Member, String> mobileCol;
    @FXML
    private TableColumn<Member, String> emailCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initColumn();
        loadData();
    }

    private void initColumn() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    }
    
    private void loadData() {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        String query = "SELECT * FROM Members";
        ResultSet rs = databaseHandler.executeQuery(query);
        try {
            while (rs.next()) {
                String  name    = rs.getString("name");
                String  id      = rs.getString("_id");
                String  mobile  = rs.getString("mobile");
                String  email   = rs.getString("email");

                memberList.add(new Member(id, name, mobile, email));
            }
        } catch (SQLException e) {
            Logger.getLogger(AddBookWindowController.class.getName()).log(Level.SEVERE, null, e);
        }

        memberListTable.getItems().setAll(memberList);
    }

    public static class Member {
        private final SimpleStringProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty mobile;
        private final SimpleStringProperty email;


        public Member(String id, String name, String mobile, String email) {
            this.id     = new SimpleStringProperty(id);
            this.name   = new SimpleStringProperty(name);
            this.mobile = new SimpleStringProperty(mobile);
            this.email  = new SimpleStringProperty(email);
        }

        public String getId() {
            return id.get();
        }

        public void setId(String id) {
            this.id.set(id);
        }

        public String getName() {
            return name.get();
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getMobile() {
            return mobile.get();
        }

        public void setMobile(String mobile) {
            this.mobile.set(mobile);
        }

        public String getEmail() {
            return email.get();
        }

        public void setEmail(String email) {
            this.email.set(email);
        }
    }
}