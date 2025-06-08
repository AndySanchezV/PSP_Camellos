package es.andy.psp.mvc;

import javafx.application.Application;
import javafx.stage.Stage;
import es.andy.psp.mvc.controller.CarreraController;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        CarreraController controller = new CarreraController();
        controller.iniciarVista(primaryStage);
    }
}