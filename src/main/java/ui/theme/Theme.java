package ui.theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Colores, fuentes y helpers de estilo.
 */
public final class Theme {

    private Theme() {}

    // --- Fuentes
    public static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 64);
    public static final Font FONT_SUBTITLE = new Font("SansSerif", Font.PLAIN, 18);
    public static final Font FONT_CHAT = new Font("SansSerif", Font.PLAIN, 16);
    public static final Font FONT_CHAT_SMALL = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_BADGE = new Font("SansSerif", Font.BOLD, 12);

    // --- Texto
    public static final Color TEXT_DARK = new Color(25, 25, 28);
    public static final Color TEXT_MUTED_DARK = new Color(90, 90, 98);

    public static final Color SERVER_TITLE = new Color(43, 18, 76);      // #2B124C
    public static final Color SERVER_SUBTITLE = new Color(255, 255, 255, 220);

    public static final Color CLIENT_TITLE = new Color(255, 255, 255);
    public static final Color CLIENT_SUBTITLE = new Color(255, 255, 255, 210);

    // --- Ventana
    public static final Color WINDOW_BG_LIGHT = new Color(247, 246, 255); // #F7F6FF
    public static final Color WINDOW_BG_DARK = new Color(11, 18, 32);     // #0B1220

    // --- Header gradients
    public static final Color SERVER_HEADER_TOP = new Color(207, 203, 255);  // #CFCBFF
    public static final Color SERVER_HEADER_BOTTOM = new Color(191, 215, 255); // #BFD7FF

    public static final Color CLIENT_HEADER_TOP = new Color(10, 15, 44);  // #0A0F2C
    public static final Color CLIENT_HEADER_BOTTOM = new Color(16, 27, 74); // #101B4A

    // --- Acentos
    public static final Color SERVER_ACCENT = new Color(108, 99, 255); // #6C63FF
    public static final Color CLIENT_ACCENT = new Color(77, 163, 255); // #4DA3FF

    // --- Chat
    public static final Color CHAT_BG_LIGHT = new Color(255, 255, 255);
    public static final Color BUBBLE_ME_SERVER = new Color(231, 229, 255); // #E7E5FF
    public static final Color BUBBLE_ME_CLIENT = new Color(216, 236, 255); // #D8ECFF
    public static final Color BUBBLE_OTHER_LIGHT = new Color(242, 242, 242); // #F2F2F2

    // --- Badge
    public static final Color BADGE_ONLINE_BG = new Color(46, 204, 113);
    public static final Color BADGE_ONLINE_FG = new Color(255, 255, 255);

    /**
     * Crea un botón primario con hover y bordes redondeados.
     */
    public static JButton makePrimaryButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 15));
        b.setForeground(Color.WHITE);
        b.setBackground(bg);
        b.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);

        Color hover = bg.darker();
        Color normal = bg;

        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                b.setBackground(hover);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                b.setBackground(normal);
            }
        });

        return b;
    }
}
