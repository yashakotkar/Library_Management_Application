package com.yashakotkar.database;

import java.sql.*;

public final class DatabaseHandler {
    private static DatabaseHandler instance;

    private static final    String      DB_URL  = "jdbc:sqlite:C:\\Users\\Yash Akotkar\\OneDrive\\Desktop\\Yash\\JavaPrograms\\Library Management Application\\libraryDB.db";
    private static          Connection  conn    = null;
    private static          Statement   stmt    = null;

    private DatabaseHandler() {
        createConnection();
        setupBookTable();
        setupMemberTable();
        setupIssueTable();
    }

    public static DatabaseHandler getInstance() {
        if(instance == null) {
            instance = new DatabaseHandler();
        }
        return instance;
    }

    void createConnection() {
        try {
            conn = DriverManager.getConnection(DB_URL);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    void setupBookTable() {
        String TABLE_NAME = "Books";
        try {
            stmt = conn.createStatement();

            DatabaseMetaData    dbm     = conn.getMetaData();
            ResultSet           tables  = dbm.getTables(null, null, TABLE_NAME, null);
            if(tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists.");
            } else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "(" +
                        "   _id VARCHAR(200) PRIMARY KEY,   "   +
                        "   title VARCHAR(200),     "           +
                        "   author VARCHAR(200),    "           +
                        "   publisher VARCHAR(100), "           +
//                        "   intCode VARCHAR(100),  "            +
                        "   quantity INT DEFAULT 1  "           +
                        "   )");
            }
        } catch(SQLException e) {
            System.err.println(e.getMessage() + "   setUp Database");
        }
    }
    private void setupMemberTable() {
        String TABLE_NAME = "Members";
        try {
            stmt = conn.createStatement();

            DatabaseMetaData    dbm     = conn.getMetaData();
            ResultSet           tables  = dbm.getTables(null, null, TABLE_NAME, null);
            if(tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists.");
            } else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "(" +
                        "   _id VARCHAR(200) PRIMARY KEY,   "   +
                        "   name VARCHAR(200),      "           +
                        "   mobile VARCHAR(200),    "           +
                        "   email VARCHAR(100)      "           +
                        "   )");
            }
        } catch(SQLException e) {
            System.err.println(e.getMessage() + "   setUp Database");
        }
    }
    
    private void setupIssueTable() {
        String TABLE_NAME = "Issues";

        try {
            stmt = conn.createStatement();

            DatabaseMetaData    dbm     = conn.getMetaData();
            ResultSet           tables  = dbm.getTables(null, null, TABLE_NAME, null);
            if(tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists.");
            } else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "(" +
                        "   bookId      VARCHAR(200) NOT NULL,                  "   +
                        "   memberId    VARCHAR(200) NOT NULL,                  "   +
                        "   issueTime   timestamp default CURRENT_TIMESTAMP,    "   +
                        "   renewCount  integer default 0,                      "   +
                        "   FOREIGN KEY (bookId) REFERENCES Books(_id),         "   +
                        "   FOREIGN KEY (memberId) REFERENCES Members(_id),     "   +
                        "   PRIMARY KEY (bookId, memberId)          "   +
                        "   )");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ResultSet executeQuery(String query) {
        ResultSet result;
        try {
            stmt    = conn.createStatement();
            result  = stmt.executeQuery(query);;
        } catch (SQLException e) {
            System.out.println("Exception in ExecuteQuery: " + e.getLocalizedMessage());
            return null;
        }
        return result;
    }

    public boolean executeAction(String query) {
        try {
            stmt = conn.createStatement();;
            stmt.execute(query);
            return true;
        } catch (SQLException e) {
            System.out.println("Exception in ExecuteAction: " + e.getLocalizedMessage());
        }
        return false;
    }

}
