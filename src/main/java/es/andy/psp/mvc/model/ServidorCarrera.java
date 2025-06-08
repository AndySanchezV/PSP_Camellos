package es.andy.psp.mvc.model;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Logger;

public class ServidorCarrera {
    private static final int PUERTO = 8008;
    private static final String LOG_INICIO = "Servidor iniciado, sperando camellos..";
    private static final String ARCHIVO_REGISTRO = "registro_carreras.txt";
    private static final String NOTI_ID = "IDENT:";
    private static final String LOG_ERROR = "Error en el servidor: ";

    private static final Logger LOGGER = Logger.getLogger(ServidorCarrera.class.getName());
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            LOGGER.info(LOG_INICIO);
            GestorCarreras gestor = new GestorCarreras(ARCHIVO_REGISTRO);
            
            int contadorCamellos = 1;
            while (true) {
                List<Socket> jugadores = new ArrayList<>();

                while (jugadores.size() < 2) {
                    Socket cliente = serverSocket.accept();
                    DataOutputStream out = new DataOutputStream(cliente.getOutputStream());
                    out.writeUTF(NOTI_ID + contadorCamellos);
                    jugadores.add(cliente);
                    contadorCamellos++;
                }
                
                Carrera carrera = new Carrera(jugadores, gestor);
                new Thread(carrera).start(); 
            }
        } catch (IOException e) {
            LOGGER.severe(LOG_ERROR + e.getMessage());
        }
    }
}