package app;

import ui.ServerUI;

import javax.swing.*;

/**
 * Punto de entrada para iniciar solo el servidor.
 */
public class ServerMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            int port = args.length > 0 ? parseInt(args[0], 5050) : 5050;
            int maxClients = args.length > 1 ? parseInt(args[1], 2) : 2;

            ServerChatController controller = new ServerChatController(port, maxClients);
            ServerUI serverUI = new ServerUI(controller);
            controller.setServerUI(serverUI);
            serverUI.setVisible(true);
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
