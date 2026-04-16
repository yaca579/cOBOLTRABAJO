package com.example.vidalbet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtUsuari;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        // De moment, anem directes a la pantalla principal
        cambiarEscena(event, "MainView.fxml", "VidalBet - Inici");
    }

    @FXML
    private void handleAnarARegistre(ActionEvent event) throws IOException {
        cambiarEscena(event, "RegisterView.fxml", "VidalBet - Registre");
    }

    private void cambiarEscena(ActionEvent event, String fxml, String titol) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(titol);
        stage.setScene(scene);
        stage.show();
    }
}