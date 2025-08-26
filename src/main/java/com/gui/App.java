package com.gui;

import com.model.Admin;
import com.model.BankovniRacun;
import com.model.Klijent;
import com.model.Vlasnik;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(BankovniRacun[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        System.out.println("Starting application...");
        loadData();
        SceneManager.init(stage);
        SceneManager.showLoginScene();
    }

    private void loadData() {
        System.out.println("Loading all data...");

        System.out.println("Loading Admin...");
        Admin.createAdminsList();
        
        System.out.println("Loading Klijent...");
        Klijent.createKlijentsList();

        System.out.println("Loading Vlasnik...");
        Vlasnik.createVlasniksList();

        System.out.println("Loading BankovniRacun...");
        BankovniRacun.createBankovniRacunsList();
    }
}
