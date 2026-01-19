package app;

import ui.ClientUI;
import ui.ServerUI;

import javax.swing.*;
import java.awt.*;

/**
 * Punto de entrada: levanta 3 ventanas (Servidor y 2 clientes) en la misma máquina.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            int port = 5050;
            ServerChatController serverController = new ServerChatController(port, 2);
            ServerUI serverUI = new ServerUI(serverController);
            serverController.setServerUI(serverUI);

            ClientChatController clientOneController = new ClientChatController("localhost", port, "Cliente 1");
            ClientUI clientOneUI = new ClientUI(clientOneController, "Cliente 1", "Connected to: localhost:" + port);
            clientOneController.setClientUI(clientOneUI);

            ClientChatController clientTwoController = new ClientChatController("localhost", port, "Cliente 2");
            ClientUI clientTwoUI = new ClientUI(clientTwoController, "Cliente 2", "Connected to: localhost:" + port);
            clientTwoController.setClientUI(clientTwoUI);

            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            int width = 840;
            int height = 650;
            int gap = 16;

            int totalW = width * 3 + gap * 2;
            int startX = Math.max(0, (screen.width - totalW) / 2);
            int startY = Math.max(0, (screen.height - height) / 2);

            serverUI.setBounds(startX, startY, width, height);
            clientOneUI.setBounds(startX + width + gap, startY, width, height);
            clientTwoUI.setBounds(startX + (width + gap) * 2, startY, width, height);

            serverUI.setVisible(true);
            clientOneUI.setVisible(true);
            clientTwoUI.setVisible(true);
        });
    }
}
