package es.andy.psp.mvc.controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import es.andy.psp.mvc.model.Camello;
import es.andy.psp.mvc.view.CarreraViewController;

public class CarreraController {

    private static final String RUTA = "/es/andy/psp/mvc/view/carreraView.fxml";
    private static final String TITULO = "Carrera Camellos";
    private CarreraViewController viewController;

    public void iniciarVista(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(RUTA));
        Parent root = loader.load();
        viewController = loader.getController();
        viewController.setCarreraController(this);
        stage.setScene(new Scene(root));
        stage.setTitle(TITULO);
        stage.show();
    }

    public void iniciarCamello() {
        new Thread(new Camello(this)).start();
    }

    public void agregarLog(String mensaje) {
        viewController.agregarLog(mensaje);
    }
    
    public void setNumeroCamello(int numero) {
        viewController.setNumeroCamello(numero);
    }

    public void habilitarBotonImprimir(String ganador) {
        viewController.carreraTerminada(ganador);
    }
}