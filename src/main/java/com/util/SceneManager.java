package com.util;

import java.io.IOException;

import com.model.Admin;
import com.model.Klijent;
import com.model.Korisnik;
import com.model.Objekat;
import com.model.Proslava;
import com.model.Vlasnik;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static Stage mainStage;
    private static Korisnik trenutniKorisnik;
    private static Objekat  trenutniObjekat;
    private static Proslava trenutnaProslava;


    public static void init(Stage stage) {
        mainStage = stage;
        mainStage.setTitle("Sistem Rezervacija");
        mainStage.setAlwaysOnTop(true);
    }


    public static Objekat getObjekat() {
        return trenutniObjekat;
    }

    public static void setObjekat(Objekat objekat) {
        trenutniObjekat = objekat;
    }

    public static Korisnik getKorisnik() {
        return trenutniKorisnik;
    }

    public static void setKorisnik(Korisnik korisnik) {
        trenutniKorisnik = korisnik;
    }

    public static Proslava getProslava() {
        return trenutnaProslava;
    }

    public static void setProslava(Proslava proslava) {
        trenutnaProslava = proslava;
    }

    public static void showLoginScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/views/LoginScene.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(SceneManager.class.getResource("/style.css").toExternalForm());
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();
    }

    public static void showSignUpScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/views/SignUpScene.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(SceneManager.class.getResource("/style.css").toExternalForm());
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();
    }

    public static void showVlasnikScene(Vlasnik vlasnik) throws IOException {
        trenutniKorisnik = vlasnik;
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/views/VlasnikScene.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(SceneManager.class.getResource("/style.css").toExternalForm());
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void showObjekatScene(Vlasnik vlasnik, Objekat objekat) throws IOException {
        trenutniKorisnik = vlasnik;
        trenutniObjekat = objekat;
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/views/ObjekatScene.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(SceneManager.class.getResource("/style.css").toExternalForm());
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void showAdminScene(Admin admin) throws IOException {
        trenutniKorisnik = admin;
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/views/AdminScene.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(SceneManager.class.getResource("/style.css").toExternalForm());
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();
    }

    public static void showCreateAdminScene(Admin admin) throws IOException {
        trenutniKorisnik = admin;
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/views/CreateAdminScene.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(SceneManager.class.getResource("/style.css").toExternalForm());
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();

    }

    public static void showKlijentScene(Klijent klijent) throws IOException {
        trenutniKorisnik = klijent;
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/views/KlijentScene.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(SceneManager.class.getResource("/style.css").toExternalForm());
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();
    }

    public static void showNovaRezervacijaScene(Klijent klijent, Objekat objekat) throws IOException {
        trenutniObjekat = objekat;
        trenutniKorisnik = klijent;
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/views/NovaRezervacijaScene.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(SceneManager.class.getResource("/style.css").toExternalForm());
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();
    }

    public static void showIzmjeniRezervacijuScene(Klijent klijent, Proslava proslava) throws IOException {
        trenutnaProslava = proslava;
        trenutniKorisnik = klijent;
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/views/IzmjeniRezervacijuScene.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(SceneManager.class.getResource("/style.css").toExternalForm());
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();
    }
}
