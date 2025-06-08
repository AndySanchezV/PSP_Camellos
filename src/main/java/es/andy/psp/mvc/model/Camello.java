package es.andy.psp.mvc.model;

import java.io.*;
import java.net.*;
import java.util.logging.Logger;

import es.andy.psp.mvc.controller.CarreraController;

public class Camello implements Runnable {
    private static final String HOST = "localhost";
    private static final int PUERTO = 8008;
    private static final String NOTI_ID = "IDENT:";
    private static final String NOTI_CONF = "CONFIRM:";
    private static final String MSJ_CAMELLO = "Eres Camello ";
    private static final String LOG_ERROR_CAMELLO = "Carrera finalizada, error en el camello: ";
    private static final String MSJ_ERROR = "Carrera finalizada, error en el camello: ";
    private static final String NOTI_AVANCE = "AVANCE:";
    private static final String NOTI_GANADOR = "GANADOR:";
    private static final String MSJ_GANADOR = "¡Has ganado la carrera! Enhorabuena";
    private static final String NOTI_PERDEDOR = "PERDEDOR";
    private static final String MSJ_PERDEDOR = "Has perdido, mala suerte";

    private final CarreraController controller;
    private String idAsignado;

    private static final Logger LOGGER = Logger.getLogger(Camello.class.getName());

    public Camello(CarreraController controller) {
        this.controller = controller;
    }
    
    @Override
    public void run() {
        try (Socket socket = new Socket(HOST, PUERTO);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream())) {
            
            String mensaje = in.readUTF();
            if (mensaje.startsWith(NOTI_ID)) {
                idAsignado = mensaje.substring(6);
                controller.agregarLog(MSJ_CAMELLO + idAsignado);
                controller.setNumeroCamello(Integer.parseInt(idAsignado));
                out.writeUTF(NOTI_CONF + idAsignado);
            }
            recibirAvances(in);
        } catch (IOException e) {
            LOGGER.severe(LOG_ERROR_CAMELLO + e.getMessage());
            controller.agregarLog(MSJ_ERROR + e.getMessage());
        }
    }

    private void recibirAvances(DataInputStream in) throws IOException {
        String mensaje = in.readUTF();
        controller.agregarLog(mensaje);
        while (true) {
            mensaje = in.readUTF();
            if (mensaje.startsWith(NOTI_AVANCE)) {
                String[] partes = mensaje.split(":");
                String camello = partes[1];
                String avance = partes[2];
                String total = partes[3];
                controller.agregarLog(camello + " avanza " + avance + " puntos (" + total + "/100)");
            } else if (mensaje.startsWith(NOTI_GANADOR)) {
                String ganador = mensaje.substring(8);
                if (ganador.equals("Camello " + idAsignado)) {
                    controller.agregarLog(MSJ_GANADOR);
                    controller.habilitarBotonImprimir(ganador);
                }
                break;
            } else if (mensaje.equals(NOTI_PERDEDOR)) {
                controller.agregarLog(MSJ_PERDEDOR);
                break;
            }
        }
    }
}