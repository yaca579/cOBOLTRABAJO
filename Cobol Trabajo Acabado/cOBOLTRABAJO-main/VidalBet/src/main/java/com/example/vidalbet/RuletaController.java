package com.example.vidalbet;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Random;

public class RuletaController {

    @FXML private Label lblSaldo;
    @FXML private Label lblNumeroGuanyador;
    @FXML private Label lblCrupierStatus;
    @FXML private Label lblMissatge;
    @FXML private TextField txtQuantitat;
    @FXML private TextField txtNumero;
    @FXML private StackPane wheelVisualContainer;
    @FXML private Circle circuloResultado; // El círculo de color

    // Bloqueador para evitar que el usuario pulse botones mientras la ruleta gira
    private boolean isSpinning = false;

    @FXML
    public void initialize() {
        actualitzarInterficie();
    }

    private void actualitzarInterficie() {
        lblSaldo.setText(String.format("Saldo: %.2f €", SessioUsuari.saldo).replace(".", ","));
    }

    @FXML
    private void allIn() {
        txtQuantitat.setText(String.format("%.2f", SessioUsuari.saldo).replace(",", "."));
    }

    // MÉTOD0 NUEVO: Se ejecuta al hacer clic en un número del tapete (0 al 36)
    @FXML
    private void seleccionarNumero(ActionEvent event) {
        if (isSpinning) return; // Si está girando, ignoramos el clic

        Button btnClicado = (Button) event.getSource();
        txtNumero.setText(btnClicado.getText());
    }

    private Object[] cridaLogicaCobol() {
        int numGuanyador = 0;
        String colorGuanyador = "VERD";
        String paritat = "ZERO";
        String rang = "ZERO";

        try {
            String os = System.getProperty("os.name").toLowerCase();
            String executablePath = os.contains("win") ? "./ruleta_logic.exe" : "./ruleta_logic";

            ProcessBuilder pb = new ProcessBuilder(executablePath);
            pb.redirectErrorStream(true);
            Process p = pb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String numStr = reader.readLine();
                String colorStr = reader.readLine();
                String parStr = reader.readLine();
                String rangStr = reader.readLine();

                if (numStr != null) numGuanyador = Integer.parseInt(numStr.trim());
                if (colorStr != null) colorGuanyador = colorStr.trim().toUpperCase();
                if (parStr != null) paritat = parStr.trim().toUpperCase();
                if (rangStr != null) rang = rangStr.trim().toUpperCase();
            }
            p.waitFor();

        } catch (Exception e) {
            System.err.println("COBOL Call fallida, usamos Fallback: " + e.getMessage());
            Random r = new Random();
            numGuanyador = r.nextInt(37);
            if (numGuanyador == 0) {
                colorGuanyador = "VERD"; paritat = "ZERO"; rang = "ZERO";
            } else {
                colorGuanyador = esVermell(numGuanyador) ? "VERMELL" : "NEGRE";
                paritat = (numGuanyador % 2 == 0) ? "PAR" : "IMPAR";
                rang = (numGuanyador <= 18) ? "FALTA" : "PASA";
            }
        }

        return new Object[]{numGuanyador, colorGuanyador, paritat, rang};
    }

    private boolean esVermell(int n) {
        int[] vermells = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
        for (int v : vermells) if (v == n) return true;
        return false;
    }

    private void processarAposta(String tipusAposta, String valorApostatText) {
        if (isSpinning) return; // Ignorar clics si ya está girando

        double quantitat;
        try {
            quantitat = Double.parseDouble(txtQuantitat.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            lblMissatge.setText("Import no vàlid!");
            return;
        }

        if (quantitat <= 0 || quantitat > SessioUsuari.saldo) {
            lblMissatge.setText(quantitat <= 0 ? "Aposta > 0!" : "Saldo insuficient!");
            return;
        }

        // 1. Preparación y bloqueo
        isSpinning = true;
        txtQuantitat.setDisable(true);
        txtNumero.setDisable(true);

        SessioUsuari.saldo -= quantitat;
        actualitzarInterficie();

        lblNumeroGuanyador.setText("?");
        lblCrupierStatus.setText("GIRANT...");
        lblMissatge.setText("¡Sort en el gir!");
        lblMissatge.setStyle("-fx-background-color: #0A0F14; -fx-text-fill: white; -fx-padding: 8 20; -fx-background-radius: 20;");

        // Ponemos el círculo en un color oscuro neutral mientras gira
        circuloResultado.setStyle("-fx-fill: #0B0D11; -fx-stroke: #E5C04A; -fx-stroke-width: 4;");

        // 2. Animación de la ruleta
        RotateTransition rt = new RotateTransition(Duration.seconds(2), wheelVisualContainer);
        rt.setByAngle(1440); // 4 vueltas
        rt.setInterpolator(Interpolator.EASE_OUT);

        // 3. Resultado al terminar de girar
        rt.setOnFinished(e -> {
            Object[] resultat = cridaLogicaCobol();
            int numGuanyador = (int) resultat[0];
            String colorGuanyador = (String) resultat[1];
            String paritat = (String) resultat[2];
            String rang = (String) resultat[3];

            lblNumeroGuanyador.setText(String.valueOf(numGuanyador));
            lblCrupierStatus.setText(colorGuanyador + " | " + paritat + " | " + rang);

            // ACTUALIZACIÓN DE COLOR VISUAL DE LA RULETA (Rojo, Negro o Verde)
            if (colorGuanyador.equals("VERMELL")) {
                circuloResultado.setStyle("-fx-fill: #D32F2F; -fx-stroke: #E5C04A; -fx-stroke-width: 4;");
            } else if (colorGuanyador.equals("NEGRE")) {
                circuloResultado.setStyle("-fx-fill: #1A1A1A; -fx-stroke: #E5C04A; -fx-stroke-width: 4;");
            } else { // VERDE
                circuloResultado.setStyle("-fx-fill: #2E7D32; -fx-stroke: #E5C04A; -fx-stroke-width: 4;");
            }

            boolean haGuanyat = false;
            double multiplicador = 1.0;

            if (tipusAposta.equals("COLOR") && valorApostatText.equals(colorGuanyador)) {
                haGuanyat = true;
                multiplicador = colorGuanyador.equals("VERD") ? 35.0 : 2.0;
            } else if (tipusAposta.equals("NUMERO") && valorApostatText.equals(String.valueOf(numGuanyador))) {
                haGuanyat = true;
                multiplicador = 36.0;
            } else if (tipusAposta.equals("PARITAT") && valorApostatText.equals(paritat)) {
                haGuanyat = true;
                multiplicador = 2.0;
            } else if (tipusAposta.equals("RANG") && valorApostatText.equals(rang)) {
                haGuanyat = true;
                multiplicador = 2.0;
            }

            if (haGuanyat) {
                double premi = quantitat * multiplicador;
                SessioUsuari.saldo += premi;
                lblMissatge.setText(String.format("¡GUANYES! Premi: %.2f €", premi).replace(".", ","));
                lblMissatge.setStyle("-fx-background-color: #E5C04A; -fx-text-fill: black; -fx-padding: 8 20; -fx-background-radius: 20; -fx-font-weight: bold;");
            } else {
                lblMissatge.setText("Has perdut. Va sortir el " + numGuanyador);
                lblMissatge.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white; -fx-padding: 8 20; -fx-background-radius: 20;");
            }

            // Desbloqueo final
            txtQuantitat.setDisable(false);
            txtNumero.setDisable(false);
            isSpinning = false;
            actualitzarInterficie();
        });

        rt.play();
    }

    // Funciones de las apuestas exteriores
    @FXML private void apostarVermell() { processarAposta("COLOR", "VERMELL"); }
    @FXML private void apostarNegre() { processarAposta("COLOR", "NEGRE"); }
    @FXML private void apostarVerd() { processarAposta("COLOR", "VERD"); }
    @FXML private void apostarPar() { processarAposta("PARITAT", "PAR"); }
    @FXML private void apostarImpar() { processarAposta("PARITAT", "IMPAR"); }
    @FXML private void apostarFalta() { processarAposta("RANG", "FALTA"); }
    @FXML private void apostarPasa() { processarAposta("RANG", "PASA"); }

    @FXML
    private void apostarNumero() {
        try {
            int numTriat = Integer.parseInt(txtNumero.getText());
            if (numTriat >= 0 && numTriat <= 36) {
                processarAposta("NUMERO", String.valueOf(numTriat));
            } else {
                lblMissatge.setText("Introdueix del 0 al 36!");
            }
        } catch (NumberFormatException e) {
            lblMissatge.setText("Nº no vàlid!");
        }
    }

    @FXML
    private void navTornar(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CasinoView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}