package com;

import java.io.IOException;

import com.util.SceneManager;
import com.model.*;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("Starting application...");
        loadData();
        SceneManager.init(stage);
        SceneManager.showLoginScene();
    }

    private void loadData() {
        System.out.println("Loading all data...");

        System.out.println("Loading Admin...");
        Admin.createAdminsList();

        System.out.println("Loading Vlasnik...");
        Vlasnik.createVlasniksList();

        System.out.println("Loading Klijent...");
        Klijent.createKlijentsList();

        System.out.println("Loading BankovniRacun...");
        BankovniRacun.createBankovniRacunsList();

        System.out.println("Loading Objekat...");
        Objekat.createObjekatList();

        System.out.println("Loading Meni...");
        Meni.createMeniList();

        System.out.println("Loading Sto...");
        Sto.createStoList();

        System.out.println("Loading Proslava...");
        Proslava.createProslavaList();

        System.out.println("Loading Raspored...");
        Raspored.createRasporedList();

        System.out.println("Loading Obavjestenje...");
        Obavjestenje.createObavjestenjaList();
    }
}
