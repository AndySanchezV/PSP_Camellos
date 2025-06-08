package es.andy.psp.mvc.model;

import java.io.*;
import java.util.logging.Logger;

public class GestorCarreras {

    private final String archivoRegistro;
    private static final String LOG_ERROR = "Error al registrar ganador: ";

    private static final Logger LOGGER = Logger.getLogger(GestorCarreras.class.getName());

    public GestorCarreras(String archivoRegistro) {
        this.archivoRegistro = archivoRegistro;
    }

    public synchronized void registrarGanador(int numeroCarrera, String ganador) {
        try (FileWriter fw = new FileWriter(archivoRegistro, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw)) {
            out.println("Carrera " + numeroCarrera + ": " + ganador);
        } catch (IOException e) {
            LOGGER.severe(LOG_ERROR + e.getMessage());
        }
    }
}