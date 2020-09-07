package com.yashakotkar.ui.listBook;

import com.yashakotkar.database.DatabaseHandler;
import com.yashakotkar.ui.addBook.AddBookWindowController;
import javafx.beans.property.SimpleIntegerProperty;
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

public class BookListWindowController implements Initializable {

    ObservableList<Book> bookList = FXCollections.observableArrayList();

    @FXML
    private AnchorPane                  mainContainer;
    @FXML
    private TableView<Book>             bookListTable;
    @FXML
    private TableColumn<Book, String>   idCol;
    @FXML
    private TableColumn<Book, String>   titleCol;
    @FXML
    private TableColumn<Book, String>   authorCol;
    @FXML
    private TableColumn<Book, String>   publisherCol;
    @FXML
    private TableColumn<Book, Integer>      quantityCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initColumn();
        loadData();
    }

    private void initColumn() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    private void loadData() {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        String query = "SELECT * FROM Books";
        ResultSet rs = databaseHandler.executeQuery(query);
        try {
            while (rs.next()) {
                String  title        = rs.getString("title");
                String  id           = rs.getString("_id");
                String  author       = rs.getString("author");
                String  publisher    = rs.getString("publisher");
                int     quantity     = rs.getInt("quantity");

                bookList.add(new Book(id, title, author, publisher, quantity));
            }
        } catch (SQLException e) {
            Logger.getLogger(AddBookWindowController.class.getName()).log(Level.SEVERE, null, e);
        }

        bookListTable.getItems().setAll(bookList);
    }

    public static class Book {
        private final SimpleStringProperty  id;
        private final SimpleStringProperty  title;
        private final SimpleStringProperty  author;
        private final SimpleStringProperty  publisher;
        private final SimpleIntegerProperty quantity;



        public Book(String id, String title, String author, String publisher, int quantity) {
            this.id         = new SimpleStringProperty(id);
            this.title      = new SimpleStringProperty(title);
            this.author     = new SimpleStringProperty(author);
            this.publisher  = new SimpleStringProperty(publisher);
            this.quantity   = new SimpleIntegerProperty(quantity);
        }

        public String getId() {
            return id.get();
        }

        public void setId(String id) {
            this.id.set(id);
        }

        public String getTitle() {
            return title.get();
        }

        public void setTitle(String title) {
            this.title.set(title);
        }

        public String getAuthor() {
            return author.get();
        }

        public void setAuthor(String author) {
            this.author.set(author);
        }

        public String getPublisher() {
            return publisher.get();
        }

        public void setPublisher(String publisher) {
            this.publisher.set(publisher);
        }

        public int getQuantity() {
            return quantity.get();
        }

        public void setQuantity(int quantity) {
            this.quantity.set(quantity);
        }
    }

}
