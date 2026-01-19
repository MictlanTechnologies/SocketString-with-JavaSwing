package app;

import ui.ClientUI;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
 * Controlador de cliente para chat con sockets (Stream TCP).
 */
public class ClientChatController {

    private static final String NAME_PREFIX = "NAME:";
    private static final DateTimeFormatter LOG_LINE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FILE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private final String host;
    private final int port;
    private final String clientName;
    private final List<String> clientHistory = Collections.synchronizedList(new ArrayList<>());
    private final String filePrefix;
    private final AtomicBoolean shutdownStarted = new AtomicBoolean(false);
    private final AtomicBoolean running = new AtomicBoolean(true);

    private ClientUI clientUI;
    private Socket socket;
    private PrintWriter writer;
    private Thread readerThread;

    public ClientChatController(String host, int port, String clientName) {
        this.host = host;
        this.port = port;
        this.clientName = clientName;
        this.filePrefix = "client_" + sanitizeName(clientName);
        connect();
    }

    public void setClientUI(ClientUI clientUI) {
        this.clientUI = clientUI;
    }

    public void sendFromClient(String plainText) {
        if (plainText == null || plainText.trim().isEmpty()) {
            return;
        }
        if (writer == null) {
            showError("Cliente no conectado todavía.");
            return;
        }

        String message = plainText.trim();
        writer.println(message);
        addClientMessage("Yo: " + message);
    }

    public void shutdownAndSave() {
        if (!shutdownStarted.compareAndSet(false, true)) {
            return;
        }

        running.set(false);
        saveHistory(filePrefix, clientName, clientHistory);
        closeResources();

        SwingUtilities.invokeLater(() -> {
            if (clientUI != null) {
                clientUI.dispose();
            }
        });
    }

    private void connect() {
        try {
            socket = new Socket(host, port);
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
            writer.println(NAME_PREFIX + clientName);

            readerThread = new Thread(this::readLoop, "client-reader-thread");
            readerThread.start();
        } catch (IOException exception) {
            showError("No se pudo conectar al servidor: " + exception.getMessage());
        }
    }

    private void readLoop() {
        if (socket == null) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while (running.get() && (line = reader.readLine()) != null) {
                handleIncoming(line);
            }
        } catch (IOException exception) {
            if (!shutdownStarted.get()) {
                showError("Error de lectura: " + exception.getMessage());
            }
        }
    }

    private void handleIncoming(String message) {
        SwingUtilities.invokeLater(() -> {
            if (clientUI != null) {
                clientUI.addRightBubble(message, null);
            }
        });
        logLine(clientHistory, message);
    }

    private void addClientMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            if (clientUI != null) {
                clientUI.addLeftBubble(message, null);
            }
        });
        logLine(clientHistory, message);
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
        if (writer != null) {
            writer.close();
        }
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    private String sanitizeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "unknown";
        }
        return name.trim().replaceAll("[^a-zA-Z0-9_-]", "_");
    }

    private void showError(String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, message, "Chat Socket", JOptionPane.ERROR_MESSAGE));
    }
}
