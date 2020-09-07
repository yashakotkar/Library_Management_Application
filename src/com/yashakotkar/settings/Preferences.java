package com.yashakotkar.settings;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;

public class Preferences {
    public static final String CONFIG_FILE = "config.txt";

    private int     noDaysWithoutFine;
    private float   finePerDay;
    private int     noOfBooksMemberCanKeep;
    private String  username;
    private String  password;

    public Preferences() {
        this.noDaysWithoutFine      = 15;
        this.finePerDay             = 5;
        this.noOfBooksMemberCanKeep = 4;
        this.username               = "admin";
        this.password               = "admin";
    }

    public Preferences(int noDaysWithoutFine, float finePerDay, int noOfBooksMemberCanKeep, String username, String password) {
        this.noDaysWithoutFine = noDaysWithoutFine;
        this.finePerDay = finePerDay;
        this.noOfBooksMemberCanKeep = noOfBooksMemberCanKeep;
        this.username = username;
        this.password = password;
    }

    public static void initConfig() {
        try(Writer writer = new FileWriter(CONFIG_FILE)) {
            Preferences preferences = new Preferences();
            Gson        gson        = new Gson();
            gson.toJson(preferences, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Preferences getPreference() {
        Preferences preferences = new Preferences();
        try (Reader reader = new FileReader(CONFIG_FILE)) {
            Gson gson = new Gson();
            preferences = gson.fromJson(reader, Preferences.class);
        } catch (FileNotFoundException e) {
            initConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return preferences;
    }

    public static void writePreferencesToFile(Preferences preferences) {
        try(Writer writer = new FileWriter(CONFIG_FILE)) {
            Gson        gson        = new Gson();
            gson.toJson(preferences, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNoDaysWithoutFine() {
        return noDaysWithoutFine;
    }

    public void setNoDaysWithoutFine(int noDaysWithoutFine) {
        this.noDaysWithoutFine = noDaysWithoutFine;
    }

    public float getFinePerDay() {
        return finePerDay;
    }

    public void setFinePerDay(float finePerDay) {
        this.finePerDay = finePerDay;
    }

    public int getNoOfBooksMemberCanKeep() {
        return noOfBooksMemberCanKeep;
    }

    public void setNoOfBooksMemberCanKeep(int noOfBooksMemberCanKeep) {
        this.noOfBooksMemberCanKeep = noOfBooksMemberCanKeep;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
