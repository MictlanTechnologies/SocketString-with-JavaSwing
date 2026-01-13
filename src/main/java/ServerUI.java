import java.awt.Color;
import javax.swing.SwingUtilities;

public class ServerUI extends ChatWindowBase {
    private static final Color LILAC_START = new Color(214, 210, 255);
    private static final Color LILAC_END = new Color(240, 228, 255);
    private static final Color BACKGROUND = new Color(247, 246, 255);
    private static final Color CARD_BACKGROUND = new Color(255, 255, 255);
    private static final Color PRIMARY = new Color(108, 99, 255);
    private static final Color PRIMARY_HOVER = new Color(90, 80, 240);
    private static final Color TEXT_PRIMARY = new Color(47, 44, 88);
    private static final Color TEXT_MUTED = new Color(126, 122, 160);
    private static final Color BUBBLE_ME = new Color(228, 224, 255);
    private static final Color BUBBLE_OTHER = new Color(242, 241, 247);
    private static final Color INPUT_BORDER = new Color(215, 212, 238);
    private static final Color INPUT_FILL = new Color(255, 255, 255);

    private static final Theme SERVER_THEME = new Theme(
            "Server",
            false,
            "\uD83D\uDDA5\uFE0F",
            140,
            "Connected to: localhost",
            LILAC_START,
            LILAC_END,
            new Color(255, 255, 255),
            new Color(250, 248, 255),
            new Color(94, 219, 164),
            new Color(19, 65, 44),
            BACKGROUND,
            CARD_BACKGROUND,
            INPUT_FILL,
            INPUT_BORDER,
            TEXT_PRIMARY,
            TEXT_MUTED,
            PRIMARY,
            PRIMARY_HOVER,
            new Color(255, 255, 255),
            BUBBLE_ME,
            BUBBLE_OTHER,
            TEXT_PRIMARY,
            TEXT_PRIMARY,
            TEXT_MUTED,
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
