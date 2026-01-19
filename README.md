# Chat con Java Swing + Sockets (Stream TCP)

Este proyecto levanta **tres usuarios** en ventanas Swing (Servidor, Cliente 1 y Cliente 2) y comunica los mensajes mediante **sockets TCP en modo stream**. Al cerrar la aplicación, **cada usuario guarda su conversación** en un archivo de texto con fecha y hora.

## Requisitos

- Java 11+
- Maven 3+

## Cómo ejecutar

1. Compila el proyecto:
   ```bash
   mvn -q clean package
   ```

2. Ejecuta la aplicación (lanza 3 ventanas):
   ```bash
   mvn -q exec:java -Dexec.mainClass=app.Main
   ```

   > Si no tienes el plugin `exec`, puedes ejecutar desde tu IDE con la clase `app.Main`.

## Uso del programa

- Se abren 3 ventanas:
  - **Servidor**
  - **Cliente 1**
  - **Cliente 2**

- **Enviar mensajes**:
  - Cada ventana tiene una caja de texto y botón **Send**.
  - Los mensajes enviados desde un **cliente** se muestran en el **servidor** y se reenvían al **otro cliente**.
  - Los mensajes enviados desde el **servidor** se envían a **ambos clientes**.

## Guardado de conversaciones

- Al cerrar cualquier ventana, el sistema:
  - Cierra sockets y hilos.
  - Guarda **tres archivos** de historial en la carpeta `chat_logs/`.

- Los archivos se guardan con marca de tiempo:
  - `server_chat_YYYYMMDD_HHMMSS.txt`
  - `client_one_chat_YYYYMMDD_HHMMSS.txt`
  - `client_two_chat_YYYYMMDD_HHMMSS.txt`

Cada archivo incluye la fecha/hora del guardado y todas las líneas del chat con sello de tiempo.

## Notas de funcionamiento interno

- El servidor escucha en `localhost:5050`.
- Se usan **3 hilos** principales:
  - 1 hilo para aceptar conexiones y leer mensajes en el servidor.
  - 1 hilo lector para **Cliente 1**.
  - 1 hilo lector para **Cliente 2**.

Si deseas cambiar el puerto, ajusta `PORT` en `app.ChatController`.
