package ui.components;

import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de encabezado con degradado, título grande, subtítulo y badge de estado.
 */
public class HeaderPanel extends JPanel {

    private final String title;
    private final String subtitle;
    private final Color top;
    private final Color bottom;

    public HeaderPanel(
            String title,
            String subtitle,
            boolean lightTitle,
            Color top,
            Color bottom,
            Color titleColor,
            Color subtitleColor,
            String badgeText,
            Color badgeBg,
            Color badgeFg
    ) {
        this.title = title;
        this.subtitle = subtitle;
        this.top = top;
        this.bottom = bottom;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        setPreferredSize(new Dimension(900, 150));
        setOpaque(false);

        // Zona izquierda: títulos
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(Theme.FONT_TITLE);
        titleLbl.setForeground(titleColor);

        JLabel subLbl = new JLabel(subtitle);
        subLbl.setFont(Theme.FONT_SUBTITLE);
        subLbl.setForeground(subtitleColor);

        left.add(titleLbl);
        left.add(Box.createVerticalStrut(6));
        left.add(subLbl);

        // Zona derecha: badge
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);

        JLabel badge = new JLabel(badgeText);
        badge.setFont(Theme.FONT_BADGE);
        badge.setOpaque(true);
        badge.setBackground(badgeBg);
        badge.setForeground(badgeFg);
        badge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255,255,255,60), 1, true),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));

        right.add(badge);

        add(left, BorderLayout.WEST);
        add(right, BorderLayout.EAST);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        GradientPaint gp = new GradientPaint(0, 0, top, 0, h, bottom);
        g2.setPaint(gp);
        g2.fillRoundRect(10, 10, w - 20, h - 20, 26, 26);

        // Sombra ligera
        g2.setColor(new Color(0, 0, 0, 30));
        g2.drawRoundRect(10, 10, w - 20, h - 20, 26, 26);

        g2.dispose();
    }
}
