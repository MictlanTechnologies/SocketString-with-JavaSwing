import java.awt.Color;
import javax.swing.SwingUtilities;

public class ClientUI extends ChatWindowBase {
    private static final Theme CLIENT_THEME = new Theme(
            "Client",
            true,
            "\uD83D\uDCAC",
            160,
            new Color(9, 15, 40),
            new Color(12, 22, 58),
            new Color(255, 255, 255),
            new Color(204, 216, 255),
            new Color(77, 163, 255),
            new Color(8, 20, 40),
            new Color(11, 18, 32),
            new Color(15, 22, 40),
            new Color(20, 30, 52),
            new Color(44, 62, 96),
            new Color(235, 240, 255),
            new Color(130, 141, 166),
            new Color(77, 163, 255),
            new Color(63, 143, 234),
            new Color(11, 18, 32),
            new Color(32, 54, 90),
            new Color(23, 31, 53),
            new Color(236, 241, 255),
            new Color(188, 199, 230),
            new Color(140, 152, 190),
            "Type a message..."
    );

    public ClientUI() {
        super(CLIENT_THEME);
    }

    @Override
    protected Theme getTheme() {
        return CLIENT_THEME;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientUI().setVisible(true));
    }
}
