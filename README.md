# Chat con Java Swing + Sockets (Stream TCP)

Este proyecto permite comunicar **Servidor, Cliente 1 y Cliente 2** por sockets TCP (stream). Se puede ejecutar en **una sola PC** o en **dispositivos distintos dentro de la misma red**. Al cerrar la aplicación, **cada usuario guarda su conversación** en un archivo de texto con fecha y hora.

## Requisitos

- Java 11+
- Maven 3+

## Cómo ejecutar

### Opción A: Todo en una sola máquina (3 ventanas)

```bash
mvn -q clean package
mvn -q exec:java -Dexec.mainClass=app.Main
```

### Opción B: Servidor en una PC y clientes en otras

#### 1) En la PC del **Servidor**

```bash
mvn -q clean package
mvn -q exec:java -Dexec.mainClass=app.ServerMain -Dexec.args="5050 2"
```

- `5050` = puerto
- `2` = número máximo de clientes

#### 2) En la PC del **Cliente 1**

```bash
mvn -q clean package
mvn -q exec:java -Dexec.mainClass=app.ClientMain -Dexec.args="<IP_DEL_SERVIDOR> 5050 \"Cliente 1\""
```

#### 3) En la PC del **Cliente 2**

```bash
mvn -q clean package
mvn -q exec:java -Dexec.mainClass=app.ClientMain -Dexec.args="<IP_DEL_SERVIDOR> 5050 \"Cliente 2\""
```

> Reemplaza `<IP_DEL_SERVIDOR>` por la IP real de la PC servidor (por ejemplo `192.168.1.10`).

## Uso del programa

- Cada ventana tiene una caja de texto y botón **Send**.
- Mensajes enviados desde un **cliente**:
  - Se ven en el **servidor**.
  - Se reenvían al **otro cliente**.
- Mensajes enviados desde el **servidor**:
  - Se envían a **todos los clientes**.

## Guardado de conversaciones

- Al cerrar cualquier ventana:
  - Se cierran sockets e hilos.
  - Se guarda el historial en `chat_logs/`.

Ejemplos de archivo:
- `server_chat_YYYYMMDD_HHMMSS.txt`
- `client_Cliente_1_YYYYMMDD_HHMMSS.txt`

Cada archivo incluye fecha/hora y todas las líneas con sello de tiempo. El nombre del cliente se normaliza para el archivo (espacios y caracteres especiales se reemplazan por `_`).

## Notas de funcionamiento interno

- El servidor escucha en el puerto configurado (por defecto `5050`).
- Se usan hilos dedicados:
  - 1 hilo de aceptación en el servidor.
  - 1 hilo lector por cliente conectado.
- Los nombres de cliente se envían al conectarse con el prefijo `NAME:`.

Si deseas cambiar el puerto o el número de clientes, ajusta los parámetros de `ServerMain`.
