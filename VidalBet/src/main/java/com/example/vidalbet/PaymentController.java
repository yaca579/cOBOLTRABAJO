package com.example.vidalbet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class PaymentController {

    @FXML private TextField txtTargeta;
    @FXML private TextField txtTitular;
    @FXML private TextField txtVenciment;
    @FXML private TextField txtCVV;

    @FXML
    public void initialize() {
        // Carreguem les dades si ja n'havia posat
        txtTargeta.setText(SessioUsuari.numeroTargeta);
        txtTitular.setText(SessioUsuari.titular);
        txtVenciment.setText(SessioUsuari.venciment);
        txtCVV.setText(SessioUsuari.cvv);
    }

    @FXML
    private void handleGuardar(ActionEvent event) throws IOException {
        if (txtTargeta.getText().isEmpty() || txtCVV.getText().isEmpty()) {
            mostrarAlerta("Dades Incompletes", "Per favor, omple els camps de la targeta.");
            return;
        }

        // GUARDEM A LA SESSIÓ
        SessioUsuari.numeroTargeta = txtTargeta.getText();
        SessioUsuari.titular = txtTitular.getText();
        SessioUsuari.venciment = txtVenciment.getText();
        SessioUsuari.cvv = txtCVV.getText();

        // Tornem a la vista anterior (Inici o Registre segons el teu flux)
        anarAView(event, "MainView.fxml", "VidalBet - Inici");
    }

    // --- EL MÈTODE QUE ET FALTAVA ---
    @FXML
    private void handleTornar(ActionEvent event) throws IOException {
        anarAView(event, "MainView.fxml", "VidalBet - Inici");
    }

    // Mètode auxiliar per canviar d'escena netament
    private void anarAView(ActionEvent event, String fxml, String titol) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(titol);
        stage.setScene(scene);
    }

    private void mostrarAlerta(String titol, String msj) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titol);
        alert.setHeaderText(null);
        alert.setContentText(msj);

        // Apliquem l'estil dark a l'alerta perquè no es vegi feia i blanca
        alert.getDialogPane().getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("dialog-pane");

        alert.showAndWait();
    }
}