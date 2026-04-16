package com.example.vidalbet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class RegisterController {

    // Referències als camps del FXML (fx:id)
    @FXML private TextField txtNom;
    @FXML private TextField txtCognoms;
    @FXML private DatePicker dpNaixement;
    @FXML private TextField txtPais;
    @FXML private TextField txtComunitat;
    @FXML private TextField txtCP;
    @FXML private TextField txtTargeta;
    @FXML private TextField txtTitular;
    @FXML private TextField txtVenciment;
    @FXML private TextField txtCVV;

    @FXML
    private void handleRegistrar(ActionEvent event) {
        // Aquí podríem guardar les dades
        System.out.println("Registrant a: " + txtNom.getText() + " " + txtCognoms.getText());
        System.out.println("Mètode de pagament: " + txtTargeta.getText());
        // Pots afegir un missatge d'èxit aquí
    }

    @FXML
    private void handleTornarLogin(ActionEvent event) throws IOException {
        // Torna a la pantalla de Login
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("VidalBet - Inicia Sessió");
        stage.setScene(scene);
        stage.show();
    }
}