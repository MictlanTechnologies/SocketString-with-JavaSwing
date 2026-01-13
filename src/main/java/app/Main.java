package app;

import ui.ClientUI;
import ui.ServerUI;

import javax.swing.*;
import java.awt.*;

/**
 * Punto de entrada: levanta 2 ventanas (Server y Client) lado a lado.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Look & Feel nativo para que no se vea "viejo"
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            ChatController controller = new ChatController();

            ServerUI serverUI = new ServerUI(controller);
            ClientUI clientUI = new ClientUI(controller);

            controller.setServerUI(serverUI);
            controller.setClientUI(clientUI);

            // Posicionarlas lado a lado
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            int width = 920;
            int height = 650;
            int gap = 16;

            int totalW = width * 2 + gap;
            int startX = Math.max(0, (screen.width - totalW) / 2);
            int startY = Math.max(0, (screen.height - height) / 2);

            serverUI.setBounds(startX, startY, width, height);
            clientUI.setBounds(startX + width + gap, startY, width, height);

            serverUI.setVisible(true);
            clientUI.setVisible(true);

            // Demo inicial para que se vea como ejemplo
            controller.demoConversation();
        });
    }
}
