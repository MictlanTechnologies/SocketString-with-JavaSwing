package app;

import ui.ClientUI;

import javax.swing.*;

/**
 * Punto de entrada para iniciar un cliente.
 */
public class ClientMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            String host = args.length > 0 ? args[0] : "localhost";
            int port = args.length > 1 ? parseInt(args[1], 5050) : 5050;
            String name = args.length > 2 ? args[2] : "Cliente";

            ClientChatController controller = new ClientChatController(host, port, name);
            ClientUI clientUI = new ClientUI(controller, name, "Connected to: " + host + ":" + port);
            controller.setClientUI(clientUI);
            clientUI.setVisible(true);
        });
    }

    private static int parseInt(String value, int fallback) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }
}
