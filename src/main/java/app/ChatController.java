package app;

import ui.ClientUI;
import ui.ServerUI;

import javax.swing.*;

/**
 * Controlador simple para "conectar" las dos UIs.
 * (Simulación: NO sockets reales)
 */
public class ChatController {

    private ServerUI serverUI;
    private ClientUI clientUI;

    public void setServerUI(ServerUI serverUI) {
        this.serverUI = serverUI;
    }

    public void setClientUI(ClientUI clientUI) {
        this.clientUI = clientUI;
    }

    /**
     * Envío simulado desde Server hacia Client.
     */
    public void sendFromServer(String plainText) {
        if (plainText == null || plainText.trim().isEmpty()) return;

        String base64Fake = RandomBase64.fakeBase64(24);

        // En Server: a la izquierda lo que escribo
        serverUI.addLeftBubble("ME(Server) - " + plainText, null);
        // En Server: a la derecha lo "encriptado" + lo que el Client "decripta"
        serverUI.addRightBubble("(enc): " + base64Fake, "Client(decrypt) - " + plainText);

        // En Client: a la derecha lo que llega "encriptado" y luego lo "decriptado" del Server
        clientUI.addRightBubble("(enc): " + base64Fake, "Server(decrypt) - " + plainText);
    }

    /**
     * Envío simulado desde Client hacia Server.
     */
    public void sendFromClient(String plainText) {
        if (plainText == null || plainText.trim().isEmpty()) return;

        String base64Fake = RandomBase64.fakeBase64(24);

        // En Client: a la izquierda lo que escribo
        clientUI.addLeftBubble("ME(Client) - " + plainText, null);
        // En Client: a la derecha lo "encriptado" + lo que el Server "decripta"
        clientUI.addRightBubble("(enc): " + base64Fake, "Server(decrypt) - " + plainText);

        // En Server: a la derecha lo que llega "encriptado" y lo "decriptado" del Client
        serverUI.addRightBubble("(enc): " + base64Fake, "Client(decrypt) - " + plainText);
    }

    /**
     * Mensajes de ejemplo al iniciar (para que la UI se vea como en el screenshot).
     */
    public void demoConversation() {
        SwingUtilities.invokeLater(() -> {
            sendFromServer("Hey");
            sendFromClient("Hiii");
            sendFromServer("im server");
            sendFromClient("im client");
        });
    }
}
