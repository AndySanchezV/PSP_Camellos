@echo off
REM
set "PROYECTO_DIR=%~dp0"
cd /d "%PROYECTO_DIR%"

REM
start cmd /k mvnd exec:java -Dexec.mainClass="es.andy.psp.mvc.model.ServidorCarrera"

timeout /t 2

REM
start cmd /k mvnd exec:java -Dexec.mainClass="es.andy.psp.mvc.App"
start cmd /k mvnd exec:java -Dexec.mainClass="es.andy.psp.mvc.App"
start cmd /k mvnd exec:java -Dexec.mainClass="es.andy.psp.mvc.App"
start cmd /k mvnd exec:java -Dexec.mainClass="es.andy.psp.mvc.App" 