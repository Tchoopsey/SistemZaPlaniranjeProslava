package com.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dao.AdminDAO;
import com.dao.ObavjestenjeDAO;
import com.dao.ObjekatDAO;
import com.model.Admin;
import com.model.Meni;
import com.model.Obavjestenje;
import com.model.Objekat;
import com.model.StanjeObjekta;
import com.util.MeniWrapper;
import com.util.SceneManager;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert.AlertType;

public class AdminController {

    private static Admin trenutniAdmin;
    @FXML Label lblIme;
    @FXML Label lblPrezime;
    @FXML Label lblKorisnickoIme;
    @FXML ListView<Objekat> lvObjekti;
    @FXML Button btnNoviAdmin;
    @FXML Button btnOdobri;
    @FXML Button btnOdbij;


    @FXML
    private void initialize() {
        setAllUserData();
    }

    @FXML
    private void handleLogout() {
        try {
            trenutniAdmin = null;
            SceneManager.showLoginScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNoviAdmin() {
        try {
            SceneManager.showCreateAdminScene(trenutniAdmin);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePogledajObjekat() {
        Objekat objekat = lvObjekti.getSelectionModel().getSelectedItem();
        infoObjekat(objekat);
    }

    private void infoObjekat(Objekat objekat) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Rezervacija");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);

        VBox vBox = new VBox(10);
        TextArea taObjekat = new TextArea();
        taObjekat.setEditable(false);
        List<MeniWrapper> meniji = getMeniji(objekat);
        String objekatInfo = "";
        objekatInfo += objekat.toString() + "\n";
        objekatInfo += "Broj mjesta: " + objekat.getBroj_mjesta() + "\n";
        objekatInfo += "Broj stolova: " + objekat.getBroj_stolova() + "\n";
        objekatInfo += "Cijena rezervacije: " + objekat.getCijena_rezervacije() + "\n\n";
        for (MeniWrapper meniWrapper : meniji) {
            objekatInfo += meniWrapper + "\n\n";
        }

        taObjekat.setText(objekatInfo);

        vBox.getChildren().add(taObjekat);
        dialog.getDialogPane().setContent(vBox);
        
        dialog.showAndWait();
    }

    private List<MeniWrapper> getMeniji(Objekat objekat) {
        List<MeniWrapper> meniji = new ArrayList<>();
        int id = 1;
        for (Meni meni : Meni.getSviMeniji()) {
            if (objekat.getId() == meni.getObjekat().getId()) {
                meniji.add(new MeniWrapper(id, meni));
                id++;
            }
        }
        return meniji;
    }

    @FXML
    private void handleIzmjeniLozinku() {
        Optional<String> res = promjenaLozinke();
        Alert alert = new Alert(AlertType.INFORMATION);

        if (res.isEmpty() || res.get().trim().isEmpty()) {
            alert.setAlertType(AlertType.WARNING);
            alert.setHeaderText("GRESKA!");
            alert.setContentText("Nekorektan unos!");
            alert.showAndWait();
            return;
        } else if (res.get().equals("GRESKA1")) {
            alert.setAlertType(AlertType.WARNING);
            alert.setHeaderText("GRESKA!");
            alert.setContentText("Stara lozinka nije ispravna!");
            alert.showAndWait();
            return;
        } else if (res.get().equals("GRESKA2")) {
            alert.setAlertType(AlertType.WARNING);
            alert.setHeaderText("GRESKA!");
            alert.setContentText("Potvrdite novu lozinku!");
            alert.showAndWait();
            return;
        } else if (res.get().equals("GRESKA3")) {
            alert.setAlertType(AlertType.WARNING);
            alert.setHeaderText("GRESKA!");
            alert.setContentText("Unesite novu lozinku!");
            alert.showAndWait();
            return;
        }

        String novaLozinka = res.get().trim();

        trenutniAdmin.setPassword(novaLozinka);
        AdminDAO dao = new AdminDAO();
        dao.updateAdmin(trenutniAdmin, trenutniAdmin.getKorisnicko_ime());

        alert.setHeaderText("Uspjesno ste promjenili lozinku!!!");
        alert.showAndWait();
    }

    private Optional<String> promjenaLozinke() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Promjena Lozinke");
        dialog.setHeaderText("Promjenite lozinku:");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        PasswordField pfStaraLozinka = new PasswordField();
        PasswordField pfNovaLozinka = new PasswordField();
        PasswordField pfPotvrda = new PasswordField();
        Label lblStaraLozinka = new Label("Stara Lozinka:");
        Label lblNovaLozinka = new Label("Nova Lozinka:");
        Label lblPotvrda = new Label("Potvrda Lozinke:");

        GridPane gridPane = new GridPane(3, 3);
        gridPane.setPadding(new Insets(4));
        gridPane.add(lblStaraLozinka, 0, 0);
        gridPane.add(lblNovaLozinka, 0, 1);
        gridPane.add(lblPotvrda, 0, 2);
        gridPane.add(pfStaraLozinka, 1, 0);
        gridPane.add(pfNovaLozinka, 1, 1);
        gridPane.add(pfPotvrda, 1, 2);

        dialog.getDialogPane().setContent(gridPane);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                if (!pfStaraLozinka.getText().equals(trenutniAdmin.getPassword())) {
                    return "GRESKA1";
                } else if (!pfNovaLozinka.getText().equals(pfPotvrda.getText())) {
                    return "GRESKA2";
                } else if (pfNovaLozinka.getText().length() == 0) {
                    return "GRESKA3";
                }

                return pfNovaLozinka.getText();
            }
            return null;
        });
        
        return dialog.showAndWait();
    }

    @FXML
    private void handleOdobri() {
        String tekst = "Objekat je odobren!";
        Objekat objekat = lvObjekti.getSelectionModel().getSelectedItem();
        if (objekat == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Izaberite objekat...");
            alert.setContentText("Izaberite objekat koji treba da odobrite!");
            alert.showAndWait();
            return;
        }
        objekat.setStatus(StanjeObjekta.ODOBREN);

        Obavjestenje obavjestenje = new Obavjestenje(0, objekat, tekst);
        ObjekatDAO objekatDAO = new ObjekatDAO();
        ObavjestenjeDAO obavjestenjeDAO = new ObavjestenjeDAO();
        objekatDAO.updateObjekat(objekat, objekat.getNaziv());
        obavjestenjeDAO.createObavjestenje(obavjestenje);
        lvObjekti.getItems().remove(objekat);

        Alert alert = new Alert(AlertType.INFORMATION, "Objekat odobren!");
        alert.show();

        System.out.println(tekst);
    }

    @FXML
    private void handleOdbij() {
        String tekst = "Objekat je odbijen!\n";
        Objekat objekat = lvObjekti.getSelectionModel().getSelectedItem();
        if (objekat == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Izaberite objekat...");
            alert.setContentText("Izaberite objekat koji treba da odobrite!");
            alert.showAndWait();
            return;
        }

        TextInputDialog tidObavjestenje = new TextInputDialog();
        tidObavjestenje.setTitle("Obbijanje objekta");
        tidObavjestenje.setHeaderText("Navedite razlog odbijanja: ");
        tidObavjestenje.setContentText("Razlog: ");
        tidObavjestenje.setWidth(200);
        Optional<String> res = tidObavjestenje.showAndWait();

        if (res.isEmpty() || res.get().trim().isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Greska!");
            alert.setContentText("Unesite razlog!");
            alert.setContentText("Morate unijeti razlog odbijanja objekta!!!");
            alert.showAndWait();
            return;
        }

        objekat.setStatus(StanjeObjekta.ODBIJEN);
        Obavjestenje obavjestenje = new Obavjestenje(0, objekat, tekst + res.get().trim());
        ObjekatDAO objekatDAO = new ObjekatDAO();
        ObavjestenjeDAO obavjestenjeDAO = new ObavjestenjeDAO();
        objekatDAO.updateObjekat(objekat, objekat.getNaziv());
        obavjestenjeDAO.createObavjestenje(obavjestenje);
        lvObjekti.getItems().remove(objekat);

        System.out.println(tekst + res.get().trim());

    }

    private void setAllUserData() {
        trenutniAdmin = (Admin) SceneManager.getKorisnik();
        lblIme.setText(trenutniAdmin.getIme());
        lblPrezime.setText(trenutniAdmin.getPrezime());
        lblKorisnickoIme.setText(trenutniAdmin.getKorisnicko_ime());
        objektiNaCekanju();
    }

    private void objektiNaCekanju() {
        for (Objekat objekat : Objekat.getSviObjekti()) {
            if (objekat.getStatus().equals(StanjeObjekta.NA_CEKANJU)) {
                lvObjekti.getItems().add(objekat);
            }
        }
    }

}
