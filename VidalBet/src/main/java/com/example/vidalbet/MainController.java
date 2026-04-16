package com.example.vidalbet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MainController {

    @FXML private Label lblUserInfo;
    @FXML private TableView<?> tablaEventos; // El "?" és perquè encara no hem definit una classe de dades
    @FXML private TextField txtImportAposta;
    @FXML private Label lblPremiPotencial;

    @FXML
    private void handleConfirmarAposta(ActionEvent event) {
        String importAposta = txtImportAposta.getText();
        System.out.println("Aposta confirmada de: " + importAposta + "€");
    }

    // Mètodes del menú lateral (de moment només fan un print)
    @FXML
    private void navInici() { System.out.println("Navegant a Inici"); }

    @FXML
    private void navEsports() { System.out.println("Navegant a Esports"); }

    @FXML
    private void navCasino() { System.out.println("Navegant a Casino"); }

    @FXML
    private void navHistorial() { System.out.println("Navegant a Historial"); }

    @FXML
    private void navPerfil() { System.out.println("Navegant a Perfil"); }
}