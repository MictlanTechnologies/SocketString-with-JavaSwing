package app;

import ui.ClientUI;
import ui.ServerUI;

import javax.swing.*;
import java.awt.*;

/**
 * Punto de entrada: levanta 3 ventanas (Server y 2 Clients) lado a lado.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Look & Feel nativo para que no se vea "viejo"
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            ChatController controller = new ChatController();

            ServerUI serverUI = new ServerUI(controller);
            ClientUI clientOneUI = new ClientUI(controller, 1, "Cliente 1", "Connected to: localhost");
            ClientUI clientTwoUI = new ClientUI(controller, 2, "Cliente 2", "Connected to: localhost");

            controller.setServerUI(serverUI);
            controller.setClientOneUI(clientOneUI);
            controller.setClientTwoUI(clientTwoUI);

            // Posicionarlas lado a lado
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
