package es.andy.psp.mvc.model;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class Carrera implements Runnable {
    private static final String NOTI_CONF = "CONFIRM:";
    private static final String MSJ_INICIO = "¡Contrincante encontrado! La carrera comienza";
    private static final String LOG_ERROR_CARRERA = "Error en carrera: ";
    private static final String MSJ_AVANCE = "AVANCE:";
    private static final int META = 100;
    private static final int TIEMPO_ESPERA = 1000;
    private static final String MSJ_GANADOR = "GANADOR:";
    private static final String MSJ_PERDEDOR = "PERDEDOR";
    private static final String LOG_ERROR_SOCKET = "Error al cerrar conexión: ";

    private final List<Socket> jugadores;
    private final GestorCarreras gestor;
    private final int numeroCarrera;
    private static int contadorCarreras = 1;
    private final Map<String, Integer> progreso = new ConcurrentHashMap<>();
    private final Map<Socket, String> idMap = new ConcurrentHashMap<>();
    private final Random random = new Random();

    private static final Logger LOGGER = Logger.getLogger(Carrera.class.getName());

    public Carrera(List<Socket> jugadores, GestorCarreras gestor) {
        this.jugadores = jugadores;
        this.gestor = gestor;
        this.numeroCarrera = contadorCarreras++;
    }

    @Override
    public void run() {
        try {
            for (Socket jugador : jugadores) {
                DataInputStream in = new DataInputStream(jugador.getInputStream());
                String confirmacion = in.readUTF();
                if (confirmacion.startsWith(NOTI_CONF)) {
                    String id = confirmacion.substring(8);
                    String camelloId = "Camello " + id;
                    progreso.put(camelloId, 0);
                    idMap.put(jugador, camelloId);
                }
            }
            
            for (Socket jugador : jugadores) {
                DataOutputStream out = new DataOutputStream(jugador.getOutputStream());
                out.writeUTF(MSJ_INICIO);
            }
            iniciarAvances();
        } catch (IOException | InterruptedException e) {
            LOGGER.severe(LOG_ERROR_CARRERA + numeroCarrera + e.getMessage());
        } finally {
            cerrarConexiones();
        }
    }

    private void iniciarAvances() throws IOException, InterruptedException {
        boolean carreraActiva = true;
        while (carreraActiva) {
            List<String> camellos = new ArrayList<>(progreso.keySet());
            Collections.shuffle(camellos);
            for (String camello : camellos) {
                int avance = random.nextInt(10) + 1;
                int nuevoProgreso = progreso.get(camello) + avance;
                progreso.put(camello, nuevoProgreso);

                for (Socket jugador : jugadores) {
                    DataOutputStream out = new DataOutputStream(jugador.getOutputStream());
                    out.writeUTF(MSJ_AVANCE + camello + ":" + avance + ":" + nuevoProgreso);
                }
                if (nuevoProgreso >= META) {
                    gestor.registrarGanador(numeroCarrera, camello);
                    notificarResultados(camello);
                    carreraActiva = false;
                    break;
                }
            }
            Thread.sleep(TIEMPO_ESPERA);
        }
    }

    private void notificarResultados(String ganador) throws IOException {
        for (Socket jugador : jugadores) {
            DataOutputStream out = new DataOutputStream(jugador.getOutputStream());
            String camelloId = idMap.get(jugador);
            if (ganador.equals(camelloId)) {
                out.writeUTF(MSJ_GANADOR + ganador);
            } else {
                out.writeUTF(MSJ_PERDEDOR);
            }
        }
    }

    private void cerrarConexiones() {
        for (Socket jugador : jugadores) {
            try {
                jugador.close();
            } catch (IOException e) {
                LOGGER.severe(LOG_ERROR_SOCKET + e.getMessage());
            }
        }
    }
}