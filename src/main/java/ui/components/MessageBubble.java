package ui.components;

import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;

/**
 * Burbuja de mensaje con esquinas redondeadas.
 * Soporta texto principal y texto extra (enc/decrypt).
 */
public class MessageBubble extends JPanel {

    private final String mainText;
    private final String extraText;
    private final Color bg;
    private final Color textColor;
    private final Color mutedColor;

    public MessageBubble(String mainText, String extraText, Color bg, Color textColor, Color mutedColor) {
        this.mainText = mainText;
        this.extraText = extraText;
        this.bg = bg;
        this.textColor = textColor;
        this.mutedColor = mutedColor;

        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JLabel main = new JLabel("<html>" + escape(mainText) + "</html>");
        main.setFont(Theme.FONT_CHAT);
        main.setForeground(textColor);

        add(main);

        if (extraText != null && !extraText.trim().isEmpty()) {
            add(Box.createVerticalStrut(6));

            // Línea extra: puede venir como "(enc): ..." o "Client(decrypt) - ..."
            JLabel extra = new JLabel("<html>" + escape(extraText) + "</html>");
            extra.setFont(Theme.FONT_CHAT_SMALL);
            extra.setForeground(mutedColor);
            add(extra);
        }
    }

    @Override
    public Dimension getMaximumSize() {
        // Limita el ancho para que parezca chat (no ocupar todo)
        Dimension pref = getPreferredSize();
        return new Dimension(520, pref.height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // sombra suave
        g2.setColor(new Color(0, 0, 0, 25));
        g2.fillRoundRect(3, 4, w - 2, h - 2, 18, 18);

        // burbuja
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, w - 4, h - 4, 18, 18);

        // borde tenue
        g2.setColor(new Color(0, 0, 0, 30));
        g2.drawRoundRect(0, 0, w - 4, h - 4, 18, 18);

        g2.dispose();
        super.paintComponent(g);
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\n", "<br/>");
    }
}
