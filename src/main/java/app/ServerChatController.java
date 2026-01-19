package app;

import ui.ServerUI;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Controlador de servidor para chat con sockets (Stream TCP).
 */
public class ServerChatController {

    private static final String NAME_PREFIX = "NAME:";
    private static final DateTimeFormatter LOG_LINE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FILE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private final int port;
    private final int maxClients;
    private final List<String> serverHistory = Collections.synchronizedList(new ArrayList<>());
    private final AtomicBoolean shutdownStarted = new AtomicBoolean(false);
    private final AtomicBoolean running = new AtomicBoolean(true);

    private ServerUI serverUI;
    private ServerSocket serverSocket;
    private ClientConnection clientOne;
    private ClientConnection clientTwo;
    private Thread acceptThread;

    public ServerChatController(int port, int maxClients) {
        this.port = port;
        this.maxClients = maxClients;
        startServer();
    }

    public void setServerUI(ServerUI serverUI) {
        this.serverUI = serverUI;
    }

    public void sendFromServer(String plainText) {
        if (plainText == null || plainText.trim().isEmpty()) {
            return;
        }

        String message = plainText.trim();
        String outgoing = "Servidor: " + message;
        broadcast(outgoing, null);
        addServerMessage("Yo: " + message);
    }

    public void shutdownAndSave() {
        if (!shutdownStarted.compareAndSet(false, true)) {
            return;
        }

        running.set(false);
        saveHistory("server_chat", "Servidor", serverHistory);
        closeResources();

        SwingUtilities.invokeLater(() -> {
            if (serverUI != null) {
                serverUI.dispose();
            }
        });
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException exception) {
            showError("No se pudo iniciar el servidor en el puerto " + port + ": " + exception.getMessage());
            return;
        }

        acceptThread = new Thread(this::acceptLoop, "server-accept-thread");
        acceptThread.start();
    }

    private void acceptLoop() {
        int connected = 0;
        while (running.get() && connected < maxClients) {
            try {
                Socket socket = serverSocket.accept();
                ClientConnection connection = buildConnection(socket, connected + 1);
                if (connected == 0) {
                    clientOne = connection;
                } else if (connected == 1) {
                    clientTwo = connection;
                }
                connected++;
                connection.startReader();
            } catch (IOException exception) {
                if (!shutdownStarted.get()) {
                    showError("Error aceptando cliente: " + exception.getMessage());
                }
                break;
            }
        }
    }

    private ClientConnection buildConnection(Socket socket, int index) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

        String name = "Cliente " + index;
        try {
            socket.setSoTimeout(2000);
            String firstLine = reader.readLine();
            if (firstLine != null && firstLine.startsWith(NAME_PREFIX)) {
                String provided = firstLine.substring(NAME_PREFIX.length()).trim();
                if (!provided.isEmpty()) {
                    name = provided;
                }
            }
        } catch (IOException ignored) {
        } finally {
            socket.setSoTimeout(0);
        }

        return new ClientConnection(index, name, socket, reader, writer);
    }

    private void handleMessageFromClient(ClientConnection sender, String message) {
        String formatted = sender.name + ": " + message;
        SwingUtilities.invokeLater(() -> {
            if (serverUI != null) {
                serverUI.addRightBubble(formatted, null);
            }
        });
        logLine(serverHistory, formatted);

        broadcast(formatted, sender);
    }

    private void broadcast(String message, ClientConnection exclude) {
        if (clientOne != null && clientOne != exclude) {
            clientOne.writer.println(message);
        }
        if (clientTwo != null && clientTwo != exclude) {
            clientTwo.writer.println(message);
        }
    }

    private void addServerMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            if (serverUI != null) {
                serverUI.addLeftBubble(message, null);
            }
        });
        logLine(serverHistory, message);
    }

    private void logLine(List<String> history, String message) {
        String timestamp = LocalDateTime.now().format(LOG_LINE_FORMAT);
        history.add("[" + timestamp + "] " + message);
    }

    private void saveHistory(String prefix, String title, List<String> history) {
        LocalDateTime now = LocalDateTime.now();
        String fileTimestamp = now.format(FILE_FORMAT);
        Path directory = Paths.get("chat_logs");
        Path file = directory.resolve(prefix + "_" + fileTimestamp + ".txt");
        List<String> lines = new ArrayList<>();
        lines.add("Conversación guardada: " + now.format(LOG_LINE_FORMAT));
        lines.add("Usuario: " + title);
        lines.add("");
        lines.addAll(history);
        try {
            Files.createDirectories(directory);
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            showError("No se pudo guardar el historial (" + prefix + "): " + exception.getMessage());
        }
    }

    private void closeResources() {
        closeConnection(clientOne);
        closeConnection(clientTwo);
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException ignored) {
            }
        }
    }

    private void closeConnection(ClientConnection connection) {
        if (connection == null) {
            return;
        }
        connection.close();
    }

    private void showError(String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, message, "Chat Socket", JOptionPane.ERROR_MESSAGE));
    }

    private final class ClientConnection {
        private final int index;
        private final String name;
        private final Socket socket;
        private final BufferedReader reader;
        private final PrintWriter writer;
        private Thread readerThread;

        private ClientConnection(int index, String name, Socket socket, BufferedReader reader, PrintWriter writer) {
            this.index = index;
            this.name = name;
            this.socket = socket;
            this.reader = reader;
            this.writer = writer;
        }

        private void startReader() {
            readerThread = new Thread(() -> {
                try {
                    String line;
                    while (running.get() && (line = reader.readLine()) != null) {
                        handleMessageFromClient(this, line);
                    }
                } catch (IOException exception) {
                    if (!shutdownStarted.get()) {
                        showError("Error de lectura en " + name + ": " + exception.getMessage());
                    }
                }
            }, "client-reader-" + index);
            readerThread.start();
        }

        private void close() {
            writer.close();
            try {
                reader.close();
            } catch (IOException ignored) {
            }
            if (socket != null && !socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
