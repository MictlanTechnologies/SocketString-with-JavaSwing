import java.awt.Color;
import javax.swing.SwingUtilities;

public class ClientUI extends ChatWindowBase {
    private static final Color NAVY_START = new Color(9, 16, 40);
    private static final Color NAVY_END = new Color(16, 29, 70);
    private static final Color BACKGROUND = new Color(12, 18, 34);
    private static final Color CARD_BACKGROUND = new Color(18, 27, 47);
    private static final Color PRIMARY = new Color(77, 163, 255);
    private static final Color PRIMARY_HOVER = new Color(63, 143, 234);
    private static final Color TEXT_PRIMARY = new Color(235, 240, 255);
    private static final Color TEXT_MUTED = new Color(152, 166, 197);
    private static final Color BUBBLE_ME = new Color(33, 56, 92);
    private static final Color BUBBLE_OTHER = new Color(25, 38, 62);
    private static final Color INPUT_BORDER = new Color(40, 56, 86);
    private static final Color INPUT_FILL = new Color(17, 25, 45);

    private static final Theme CLIENT_THEME = new Theme(
            "Client",
            true,
            "\uD83D\uDCAC",
            160,
            "Connected to: localhost",
            NAVY_START,
            NAVY_END,
            new Color(255, 255, 255),
            new Color(200, 212, 243),
            PRIMARY,
            new Color(8, 20, 40),
            BACKGROUND,
            CARD_BACKGROUND,
            INPUT_FILL,
            INPUT_BORDER,
            TEXT_PRIMARY,
            TEXT_MUTED,
            PRIMARY,
            PRIMARY_HOVER,
            new Color(9, 16, 40),
            BUBBLE_ME,
            BUBBLE_OTHER,
            TEXT_PRIMARY,
            TEXT_PRIMARY,
            TEXT_MUTED,
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
