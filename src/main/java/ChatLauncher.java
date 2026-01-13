import javax.swing.SwingUtilities;

public class ChatLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServerUI serverUI = new ServerUI();
            ClientUI clientUI = new ClientUI();
            serverUI.setVisible(true);
            clientUI.setVisible(true);
        });
    }
}
