import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.LocalTime;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MessageBubblePanel extends JPanel {
    private final String mainText;
    private final String extraText;
    private final LocalTime time;
    private final boolean fromMe;
    private final ChatWindowBase.Theme theme;

    public MessageBubblePanel(String mainText, String extraText, LocalTime time, boolean fromMe, ChatWindowBase.Theme theme) {
        this.mainText = mainText;
        this.extraText = extraText;
        this.time = time;
        this.fromMe = fromMe;
        this.theme = theme;
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel mainLabel = new JLabel(mainText);
        mainLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));
        mainLabel.setForeground(fromMe ? theme.bubbleMeText : theme.bubbleOtherText);
        add(mainLabel);

        if (extraText != null && !extraText.isEmpty()) {
            JLabel extraLabel = new JLabel(extraText);
            extraLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
            extraLabel.setForeground(theme.bubbleExtraText);
            add(Box.createVerticalStrut(6));
            add(extraLabel);
        }

        JLabel timeLabel = new JLabel(time.format(ChatWindowBase.getTimeFormatter()));
        timeLabel.setFont(ChatWindowBase.TIMESTAMP_FONT);
        timeLabel.setForeground(theme.bubbleExtraText);
        timeLabel.setAlignmentX(fromMe ? LEFT_ALIGNMENT : RIGHT_ALIGNMENT);
        add(Box.createVerticalStrut(6));
        add(timeLabel);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(520, Integer.MAX_VALUE);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(fromMe ? theme.bubbleMeBackground : theme.bubbleOtherBackground);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2d.dispose();
        super.paintComponent(graphics);
    }
}
