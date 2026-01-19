package app;

import ui.ClientUI;
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
 * Controlador de chat con sockets (Stream TCP) para 3 usuarios.
 */
public class ChatController {

    private static final int PORT = 5050;
    private static final DateTimeFormatter LOG_LINE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FILE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final String SERVER_NAME = "Servidor";
    private static final String CLIENT_ONE_NAME = "Cliente 1";
    private static final String CLIENT_TWO_NAME = "Cliente 2";

    private final List<String> serverHistory = Collections.synchronizedList(new ArrayList<>());
    private final List<String> clientOneHistory = Collections.synchronizedList(new ArrayList<>());
    private final List<String> clientTwoHistory = Collections.synchronizedList(new ArrayList<>());
    private final AtomicBoolean shutdownStarted = new AtomicBoolean(false);
    private final AtomicBoolean running = new AtomicBoolean(true);

    private ServerUI serverUI;
    private ClientUI clientOneUI;
    private ClientUI clientTwoUI;

    private ServerSocket serverSocket;
    private Socket serverSideClientOne;
    private Socket serverSideClientTwo;
    private Socket clientOneSocket;
    private Socket clientTwoSocket;
    private PrintWriter serverToClientOne;
    private PrintWriter serverToClientTwo;
    private PrintWriter clientOneWriter;
    private PrintWriter clientTwoWriter;

    private Thread serverThread;
    private Thread clientOneReaderThread;
    private Thread clientTwoReaderThread;

    public ChatController() {
        initSockets();
    }

    public void setServerUI(ServerUI serverUI) {
        this.serverUI = serverUI;
    }

    public void setClientOneUI(ClientUI clientOneUI) {
        this.clientOneUI = clientOneUI;
    }

    public void setClientTwoUI(ClientUI clientTwoUI) {
        this.clientTwoUI = clientTwoUI;
    }

    private void initSockets() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException exception) {
            showError("No se pudo iniciar el servidor en el puerto " + PORT + ": " + exception.getMessage());
            return;
        }

        startServerThread();
        connectClients();
        startClientThreads();
    }

    private void startServerThread() {
        serverThread = new Thread(() -> {
            try {
                serverSideClientOne = serverSocket.accept();
                serverSideClientTwo = serverSocket.accept();

                serverToClientOne = new PrintWriter(new OutputStreamWriter(serverSideClientOne.getOutputStream(), StandardCharsets.UTF_8), true);
                serverToClientTwo = new PrintWriter(new OutputStreamWriter(serverSideClientTwo.getOutputStream(), StandardCharsets.UTF_8), true);

                BufferedReader readerOne = new BufferedReader(new InputStreamReader(serverSideClientOne.getInputStream(), StandardCharsets.UTF_8));
                BufferedReader readerTwo = new BufferedReader(new InputStreamReader(serverSideClientTwo.getInputStream(), StandardCharsets.UTF_8));

                while (running.get()) {
                    boolean handled = false;
                    if (readerOne.ready()) {
                        String line = readerOne.readLine();
                        if (line != null) {
                            handleMessageFromClient(1, line);
                            handled = true;
                        }
                    }
                    if (readerTwo.ready()) {
                        String line = readerTwo.readLine();
                        if (line != null) {
                            handleMessageFromClient(2, line);
                            handled = true;
                        }
                    }
                    if (!handled) {
                        try {
                            Thread.sleep(40);
                        } catch (InterruptedException ignored) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            } catch (IOException exception) {
                if (!shutdownStarted.get()) {
                    showError("Error en el servidor: " + exception.getMessage());
                }
            }
        }, "socket-server-thread");
        serverThread.start();
    }

    private void connectClients() {
        try {
            clientOneSocket = new Socket("localhost", PORT);
            clientTwoSocket = new Socket("localhost", PORT);

            clientOneWriter = new PrintWriter(new OutputStreamWriter(clientOneSocket.getOutputStream(), StandardCharsets.UTF_8), true);
            clientTwoWriter = new PrintWriter(new OutputStreamWriter(clientTwoSocket.getOutputStream(), StandardCharsets.UTF_8), true);
        } catch (IOException exception) {
            showError("No se pudo conectar uno de los clientes: " + exception.getMessage());
        }
    }

    private void startClientThreads() {
        clientOneReaderThread = new Thread(() -> readClientLoop(clientOneSocket, 1), "client-one-reader");
        clientTwoReaderThread = new Thread(() -> readClientLoop(clientTwoSocket, 2), "client-two-reader");

        clientOneReaderThread.start();
        clientTwoReaderThread.start();
    }

    private void readClientLoop(Socket clientSocket, int clientId) {
        if (clientSocket == null) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while (running.get() && (line = reader.readLine()) != null) {
                handleMessageForClient(clientId, line);
            }
        } catch (IOException exception) {
            if (!shutdownStarted.get()) {
                showError("Error de lectura en cliente " + clientId + ": " + exception.getMessage());
            }
        }
    }

    /**
     * Envío real desde Server hacia ambos clientes.
     */
    public void sendFromServer(String plainText) {
        if (plainText == null || plainText.trim().isEmpty()) {
            return;
        }

        String message = plainText.trim();
        if (serverToClientOne == null || serverToClientTwo == null) {
            showError("Servidor no conectado todavía.");
            return;
        }

        String outgoing = SERVER_NAME + ": " + message;
        serverToClientOne.println(outgoing);
        serverToClientTwo.println(outgoing);

        addServerMessage("Yo: " + message);
    }

    /**
     * Envío real desde Client hacia el servidor.
     */
    public void sendFromClient(int clientId, String plainText) {
        if (plainText == null || plainText.trim().isEmpty()) {
            return;
        }

        String message = plainText.trim();
        if (clientId == 1) {
            if (clientOneWriter == null) {
                showError("Cliente 1 no conectado todavía.");
                return;
            }
            clientOneWriter.println(message);
            addClientMessage(1, "Yo: " + message);
            return;
        }

        if (clientTwoWriter == null) {
            showError("Cliente 2 no conectado todavía.");
            return;
        }
        clientTwoWriter.println(message);
        addClientMessage(2, "Yo: " + message);
    }

    private void handleMessageFromClient(int clientId, String message) {
        String senderName = clientId == 1 ? CLIENT_ONE_NAME : CLIENT_TWO_NAME;
        String formatted = senderName + ": " + message;
        SwingUtilities.invokeLater(() -> {
            if (serverUI != null) {
                serverUI.addRightBubble(formatted, null);
            }
        });
        logLine(serverHistory, formatted);

        if (clientId == 1) {
            sendToClient(2, formatted);
        } else {
            sendToClient(1, formatted);
        }
    }

    private void handleMessageForClient(int clientId, String message) {
        SwingUtilities.invokeLater(() -> {
            if (clientId == 1 && clientOneUI != null) {
                clientOneUI.addRightBubble(message, null);
            } else if (clientId == 2 && clientTwoUI != null) {
                clientTwoUI.addRightBubble(message, null);
            }
        });
        logLine(clientId == 1 ? clientOneHistory : clientTwoHistory, message);
    }

    private void sendToClient(int clientId, String message) {
        if (clientId == 1 && serverToClientOne != null) {
            serverToClientOne.println(message);
        } else if (clientId == 2 && serverToClientTwo != null) {
            serverToClientTwo.println(message);
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

    private void addClientMessage(int clientId, String message) {
        SwingUtilities.invokeLater(() -> {
            if (clientId == 1 && clientOneUI != null) {
                clientOneUI.addLeftBubble(message, null);
            } else if (clientId == 2 && clientTwoUI != null) {
                clientTwoUI.addLeftBubble(message, null);
            }
        });
        logLine(clientId == 1 ? clientOneHistory : clientTwoHistory, message);
    }

    private void logLine(List<String> history, String message) {
        String timestamp = LocalDateTime.now().format(LOG_LINE_FORMAT);
        history.add("[" + timestamp + "] " + message);
    }

    public void shutdownAndSave() {
        if (!shutdownStarted.compareAndSet(false, true)) {
            return;
        }

        running.set(false);
        saveHistory("server_chat", SERVER_NAME, serverHistory);
        saveHistory("client_one_chat", CLIENT_ONE_NAME, clientOneHistory);
        saveHistory("client_two_chat", CLIENT_TWO_NAME, clientTwoHistory);
        closeResources();

        SwingUtilities.invokeLater(() -> {
            if (serverUI != null) {
                serverUI.dispose();
            }
            if (clientOneUI != null) {
                clientOneUI.dispose();
            }
            if (clientTwoUI != null) {
                clientTwoUI.dispose();
            }
        });
        System.exit(0);
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
        closeWriter(serverToClientOne);
        closeWriter(serverToClientTwo);
        closeWriter(clientOneWriter);
        closeWriter(clientTwoWriter);
        closeSocket(serverSideClientOne);
        closeSocket(serverSideClientTwo);
        closeSocket(clientOneSocket);
        closeSocket(clientTwoSocket);
        closeSocket(serverSocket);
    }

    private void closeWriter(PrintWriter writer) {
        if (writer != null) {
            writer.close();
        }
    }

    private void closeSocket(Socket socket) {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    private void closeSocket(ServerSocket socket) {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    private void showError(String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, message, "Chat Socket", JOptionPane.ERROR_MESSAGE));
    }
}
