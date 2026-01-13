package ui.components;

import javax.swing.*;
import java.awt.*;

/**
 * Panel central con scroll que permite agregar "burbujas" alineadas izquierda/derecha.
 */
public class ChatPanel extends JPanel {

    private final JPanel container;
    private final JScrollPane scroll;

    private final Color chatBg;
    private final Color meBubble;
    private final Color otherBubble;
    private final Color textColor;
    private final Color mutedColor;

    public ChatPanel(Color chatBg, Color meBubble, Color otherBubble, Color textColor, Color mutedColor) {
        super(new BorderLayout());
        this.chatBg = chatBg;
        this.meBubble = meBubble;
        this.otherBubble = otherBubble;
        this.textColor = textColor;
        this.mutedColor = mutedColor;

        setBorder(BorderFactory.createEmptyBorder(14, 14, 10, 14));
        setOpaque(true);
        setBackground(new Color(0,0,0,0));

        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(chatBg);

        scroll = new JScrollPane(container);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0,0,0,40), 1, true));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(chatBg);
        scroll.getViewport().setBackground(chatBg);

        add(scroll, BorderLayout.CENTER);
    }

    public void addLeftBubble(String mainText, String extraText) {
        addBubble(false, mainText, extraText, false);
    }

    public void addRightBubble(String mainText, String extraText) {
        addBubble(false, mainText, extraText, true);
    }

    /**
     * @param fromMe  cambia el color de burbuja (meBubble vs otherBubble)
     * @param alignRight si true, se alinea a la derecha
     */
    public void addBubble(boolean fromMe, String mainText, String extraText, boolean alignRight) {
        MessageBubble bubble = new MessageBubble(
                mainText,
                extraText,
                alignRight ? otherBubble : meBubble,
                textColor,
                mutedColor
        );

        JPanel line = new JPanel(new BorderLayout());
        line.setOpaque(false);
        line.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        if (alignRight) {
            line.add(bubble, BorderLayout.EAST);
        } else {
            line.add(bubble, BorderLayout.WEST);
        }

        container.add(line);
        container.add(Box.createVerticalStrut(2));

        revalidate();
        repaint();

        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = scroll.getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });
    }
}
