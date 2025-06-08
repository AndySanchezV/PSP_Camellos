package es.andy.psp.mvc.view;

import java.io.*;

import es.andy.psp.mvc.controller.CarreraController;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CarreraViewController {
    @FXML private Button btnIniciar;
    @FXML private Button btnImprimir;
    @FXML private Button btnSalir;
    @FXML private TextArea txtLog;

    private static final String MSJ_CONECTADO = "Conectando al servidor...\n";
    private static final String MSJ_ESPERA = "Esperando al contrincante...\n";
    private static final String NOMBRE_ARCHIVO = "ganador.md";
    private static final String CONTENIDO_ARCHIVO = "## Información de la carrera\n\nEl ganador es: ";
    private static final String PDF_CORRECTO = "Certificado generado como ganador.pdf";
    private static final String MSJ_ERROR = "Error al generar el PDF: ";

    private CarreraController controller;
    private String ganador;
    private int numeroCamello;

    public void setCarreraController(CarreraController controller) {
        this.controller = controller;
    }
    
    public void setNumeroCamello(int numero) {
        this.numeroCamello = numero;
    }
    
    @FXML
    private void iniciar() {
        btnIniciar.setDisable(true);
        btnImprimir.setDisable(true);
        txtLog.clear();
        txtLog.appendText(MSJ_CONECTADO);
        txtLog.appendText(MSJ_ESPERA);
        controller.iniciarCamello();
    }

    @FXML
    private void imprimir() {
        if (ganador != null) {
            try (FileWriter fw = new FileWriter(NOMBRE_ARCHIVO)) {
                fw.write(CONTENIDO_ARCHIVO + ganador);
                String path = System.getProperty("user.dir").replace("\\", "/");
                String cmd = "docker run -it --rm -v \"" + path + ":/workdir\" plass/mdtopdf mdtopdf ganador.md";
                Process process = Runtime.getRuntime().exec(cmd);
                process.waitFor();
                agregarLog(PDF_CORRECTO);
            } catch (IOException | InterruptedException e) {
                agregarLog(MSJ_ERROR + e.getMessage());
            }
        }
    }
    
    @FXML
    private void salir() {
        System.exit(0);
    }
    
    public void agregarLog(String mensaje) {
        javafx.application.Platform.runLater(() -> {
            txtLog.appendText(mensaje + "\n");
        });
    }
    
    public void carreraTerminada(String ganador) {
        this.ganador = ganador;
        javafx.application.Platform.runLater(() -> {
            if (ganador.equals("Camello " + numeroCamello)) {
                btnImprimir.setDisable(false);
            }
        });
    }
}