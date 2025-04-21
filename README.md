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
        -int meta
        +registrarJugador()
        +iniciarPartida()
        +moverJugador()
        +validarGanador()
    }

    class Jugador {
        -String nombre
        -String ip
        -int posicion
        +avanzar()
    }

    class Certificado {
        -String idPartida
        -String nombreGanador
        +generarPDF() byte[]
    }

    Juego "1" *-- "2" Jugador : Contiene
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
# Plan de Pruebas

## 1. Pruebas del Juego
| Prueba          | Cómo probarlo                          | Resultado esperado                  |
|---------------------|----------------------------------------|--------------------------------------|
| Registrar jugadores | 1. Ingresar nombre del jugador 1<br>2. Ingresar nombre del jugador 2 | El juego se debe iniciar cuando hay 2 jugadores |
| Movimientos del jugador | Ver posición en pantalla | La posición del jugador aumenta (ej: de 0 a 3) |
| Detectar al ganador | Mover al jugador hasta pasar la meta | Muestra: "¡Ganaste!" y el botón para generar PDF |

## 2. Pruebas del Certificado
| Qué probar          | Cómo probarlo                          | Resultado esperado                  |
|---------------------|----------------------------------------|--------------------------------------|
| Generar el PDF   | 1. Ganar una partida<br>2. Hacer clic en "Generar certificado" | Se descarga el `certificado_ganador.pdf` |
| Contenido del PDF   | Abrir el PDF | El PDF muestra:<br>- Nombre del ganador<br>- ID de la partida |

## 3. Pruebas de Errores
| Qué probar          | Cómo probarlo                          | Resultado esperado                  |
|---------------------|----------------------------------------|--------------------------------------|
| Registro inválido   | Intentar iniciar con 1 jugador | Muestra: "Se necesitan 2 jugadores" |
| Nombre vacío        | Registrar jugador sin nombre | Muestra: "Ingresa un nombre válido" |
| Desconexión     | Durante la partida, apagar red de un jugador | Muestra: "Partida cancelada - Jugador desconectado, no hay ganador |
