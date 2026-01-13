import java.awt.Color;
import javax.swing.SwingUtilities;

public class ServerUI extends ChatWindowBase {
    private static final Theme SERVER_THEME = new Theme(
            "Server",
            false,
            "\uD83D\uDDA5\uFE0F",
            140,
            new Color(197, 191, 255),
            new Color(227, 223, 255),
            new Color(255, 255, 255),
            new Color(245, 244, 255),
            new Color(74, 214, 157),
            new Color(22, 59, 42),
            new Color(247, 246, 255),
            new Color(255, 255, 255),
            new Color(255, 255, 255),
            new Color(214, 210, 245),
            new Color(55, 54, 69),
            new Color(169, 167, 189),
            new Color(108, 99, 255),
            new Color(92, 82, 242),
            new Color(255, 255, 255),
            new Color(214, 209, 255),
            new Color(241, 241, 246),
            new Color(48, 46, 79),
            new Color(75, 74, 92),
            new Color(117, 116, 140),
            "Type a message..."
    );

    public ServerUI() {
        super(SERVER_THEME);
    }

    @Override
    protected Theme getTheme() {
        return SERVER_THEME;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ServerUI().setVisible(true));
    }
}
