module Library.Management.Application {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
    requires com.google.gson;
    requires java.prefs;

    opens com.yashakotkar.ui.main;
    opens com.yashakotkar.ui.addBook;
    opens com.yashakotkar.ui.listBook;
    opens com.yashakotkar.ui.addMember;
    opens com.yashakotkar.ui.listMember;
    opens com.yashakotkar.settings;
}