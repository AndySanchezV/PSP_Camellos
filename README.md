# Carrera de Camellos: Análisis y Prototipo
## **Diagrama de clase**
```mermaid
---
title: Carrera de camellos - Extraordinaria
---
classDiagram
    direction TB

    class Juego {
        -String idPartida
        -Jugador[] jugadores
        -Jugador ganador
        +iniciarPartida()
        +moverCamellos()
        +registrarJugador(Jugador)
    }

    class Jugador {
        -String nombre
        -String ip
        +solicitarAvanceExtra()
    }

    class Camello {
        -String nombre
        -int posicion
        +avanzar(int pasos)
    }

    class Certificado {
        -String idPartida
        +generarPDF() byte[]
    }

    Juego "1" *-- "2" Jugador : Contiene
    Jugador "1" *-- "1" Camello : Controla
    Juego "1" --> "1" Certificado : Genera
```
## **Diagrama de secuencia**
```mermaid
---
title: Carrera de camellos - Extraordinaria
---
sequenceDiagram
    participant J1 as Jugador 1
    participant J2 as Jugador 2
    participant S as Juego
    participant IG1 as Interfaz J1
    participant IG2 as Interfaz J2

    J1->>IG1: Ingresa nombre
    J2->>IG2: Ingresa nombre
    IG1->>S: Registra jugador 1
    IG2->>S: Registra jugador 2
    S->>S: Inicia partida
    loop Hasta que haya ganador
        S->>S: Mover camellos
        S->>IG1: Actualizar posición
        S->>IG2: Actualizar posición
    end
    S->>IG1: Notificar ganador
    S->>IG2: Notificar ganador
    alt Si es ganador
        IG1->>S: Solicitar certificado
        S->>IG1: Enviar PDF
    end
```
## **Anotaciones**
1. **Diagrama de clase**:
El método solicitarAvancesExtra() de la clase Jugador no es definitivo, si el desarrollo se complica no se incluirá.
No me queda claro si las clases camello y certificado son realmente necesarias, ya que en el caso de la clase camellos al tener jugadores creo que sería suficiente, pero las he incluido esperando Feedback.
