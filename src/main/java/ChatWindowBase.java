import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public abstract class ChatWindowBase extends JFrame {
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 48);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    static final Font TIMESTAMP_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Insets TEXT_FIELD_PADDING = new Insets(10, 14, 10, 14);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final JPanel chatContainer = new JPanel();
    private final JScrollPane scrollPane;
    private final JTextField inputField = new JTextField();
    private final JButton sendButton = new JButton("Send");
    private final JLabel connectionLabel = new JLabel();
    private final JLabel statusBadge = new JLabel();

    protected ChatWindowBase(Theme theme) {
        super(theme.windowTitle);
        applyLookAndFeel(theme.useDarkLaf);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(820, 600));
        setSize(900, 650);
        getContentPane().setBackground(theme.backgroundColor);

        JPanel header = buildHeader(theme);
        JPanel inputPanel = buildInputPanel(theme);

        chatContainer.setLayout(new BoxLayout(chatContainer, BoxLayout.Y_AXIS));
        chatContainer.setBorder(new EmptyBorder(24, 32, 24, 32));
        chatContainer.setBackground(theme.chatBackground);

        scrollPane = new JScrollPane(chatContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(16, 32, 16, 32));
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);
        scrollPane.setBackground(theme.backgroundColor);
        scrollPane.getViewport().setBackground(theme.backgroundColor);

        add(header, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        configureInputActions(theme);
    }

    private void applyLookAndFeel(boolean dark) {
        try {
            if (dark) {
                FlatDarkLaf.setup();
            } else {
                FlatLightLaf.setup();
            }
            UIManager.put("Button.arc", 18);
            UIManager.put("Component.arc", 18);
            UIManager.put("TextComponent.arc", 18);
            UIManager.put("ScrollBar.thumbArc", 18);
            UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
        } catch (Exception ignored) {
        }
    }

    private JPanel buildHeader(Theme theme) {
        JPanel header = new GradientPanel(theme.headerGradientStart, theme.headerGradientEnd);
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(900, theme.headerHeight));
        header.setBorder(new EmptyBorder(24, 32, 24, 32));

        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(theme.headerIcon + " " + theme.windowTitle);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(theme.headerTextColor);

        connectionLabel.setText(theme.subtitleText);
        connectionLabel.setFont(SUBTITLE_FONT);
        connectionLabel.setForeground(theme.headerSubtitleColor);

        leftPanel.add(titleLabel);
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(connectionLabel);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);

        statusBadge.setText("ONLINE");
        statusBadge.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusBadge.setOpaque(true);
        statusBadge.setBackground(theme.statusBadgeBackground);
        statusBadge.setForeground(theme.statusBadgeForeground);
        statusBadge.setBorder(new EmptyBorder(6, 14, 6, 14));
        statusBadge.setHorizontalAlignment(JLabel.CENTER);
        statusBadge.setVerticalAlignment(JLabel.CENTER);

        rightPanel.add(statusBadge);

        header.add(leftPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        return header;
    }

    private JPanel buildInputPanel(Theme theme) {
        JPanel inputPanel = new JPanel(new BorderLayout(16, 0));
        inputPanel.setBorder(new EmptyBorder(16, 32, 24, 32));
        inputPanel.setBackground(theme.backgroundColor);

        inputField.setFont(BODY_FONT);
        inputField.setBorder(new RoundedBorder(18, theme.inputBorderColor));
        inputField.setForeground(theme.inputTextColor);
        inputField.setBackground(theme.inputBackground);
        inputField.setCaretColor(theme.inputTextColor);
        inputField.setMargin(TEXT_FIELD_PADDING);
        applyPlaceholderBehavior(theme);

        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sendButton.setBackground(theme.primaryButtonColor);
        sendButton.setForeground(theme.primaryButtonText);
        sendButton.setFocusPainted(false);
        sendButton.setBorder(new EmptyBorder(10, 24, 10, 24));
        sendButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sendButton.setOpaque(true);
        sendButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent event) {
                sendButton.setBackground(theme.primaryButtonHover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent event) {
                sendButton.setBackground(theme.primaryButtonColor);
            }
        });

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        return inputPanel;
    }

    private void configureInputActions(Theme theme) {
        sendButton.addActionListener(event -> handleSend());
        inputField.addActionListener(event -> handleSend());
        inputField.setText(theme.placeholderText);
        inputField.setForeground(theme.placeholderColor);
    }

    private void applyPlaceholderBehavior(Theme theme) {
        inputField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent event) {
                if (inputField.getText().equals(theme.placeholderText)) {
                    inputField.setText("");
                    inputField.setForeground(theme.inputTextColor);
                }
            }

            @Override
            public void focusLost(FocusEvent event) {
                if (inputField.getText().trim().isEmpty()) {
                    inputField.setText(theme.placeholderText);
                    inputField.setForeground(theme.placeholderColor);
                }
            }
        });
    }

    private void handleSend() {
        Theme theme = getTheme();
        String text = inputField.getText().trim();
        if (text.isEmpty() || text.equals(theme.placeholderText)) {
            return;
        }
        addMessage(true, "ME(" + theme.windowTitle + ") - " + text);
        inputField.setText(theme.placeholderText);
        inputField.setForeground(theme.placeholderColor);
    }

    protected abstract Theme getTheme();

    public void addMessage(boolean fromMe, String mainText) {
        MessageBubblePanel bubble = new MessageBubblePanel(mainText, LocalTime.now(), fromMe, getTheme());
        JPanel wrapper = new JPanel(new FlowLayout(fromMe ? FlowLayout.LEFT : FlowLayout.RIGHT));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(8, 0, 8, 0));
        wrapper.add(bubble);
        chatContainer.add(wrapper);
        chatContainer.revalidate();
        chatContainer.repaint();
        scrollToBottom();
    }

    public void addChatLineLeft(String text) {
        addMessage(true, text);
    }

    public void addChatLineRight(String text) {
        addMessage(false, text);
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }

    protected static class Theme {
        final String windowTitle;
        final boolean useDarkLaf;
        final String headerIcon;
        final int headerHeight;
        final String subtitleText;
        final Color headerGradientStart;
        final Color headerGradientEnd;
        final Color headerTextColor;
        final Color headerSubtitleColor;
        final Color statusBadgeBackground;
        final Color statusBadgeForeground;
        final Color backgroundColor;
        final Color chatBackground;
        final Color inputBackground;
        final Color inputBorderColor;
        final Color inputTextColor;
        final Color placeholderColor;
        final Color primaryButtonColor;
        final Color primaryButtonHover;
        final Color primaryButtonText;
        final Color bubbleMeBackground;
        final Color bubbleOtherBackground;
        final Color bubbleMeText;
        final Color bubbleOtherText;
        final Color bubbleExtraText;
        final String placeholderText;

        private Theme(String windowTitle,
                      boolean useDarkLaf,
                      String headerIcon,
                      int headerHeight,
                      String subtitleText,
                      Color headerGradientStart,
                      Color headerGradientEnd,
                      Color headerTextColor,
                      Color headerSubtitleColor,
                      Color statusBadgeBackground,
                      Color statusBadgeForeground,
                      Color backgroundColor,
                      Color chatBackground,
                      Color inputBackground,
                      Color inputBorderColor,
                      Color inputTextColor,
                      Color placeholderColor,
                      Color primaryButtonColor,
                      Color primaryButtonHover,
                      Color primaryButtonText,
                      Color bubbleMeBackground,
                      Color bubbleOtherBackground,
                      Color bubbleMeText,
                      Color bubbleOtherText,
                      Color bubbleExtraText,
                      String placeholderText) {
            this.windowTitle = windowTitle;
            this.useDarkLaf = useDarkLaf;
            this.headerIcon = headerIcon;
            this.headerHeight = headerHeight;
            this.subtitleText = subtitleText;
            this.headerGradientStart = headerGradientStart;
            this.headerGradientEnd = headerGradientEnd;
            this.headerTextColor = headerTextColor;
            this.headerSubtitleColor = headerSubtitleColor;
            this.statusBadgeBackground = statusBadgeBackground;
            this.statusBadgeForeground = statusBadgeForeground;
            this.backgroundColor = backgroundColor;
            this.chatBackground = chatBackground;
            this.inputBackground = inputBackground;
            this.inputBorderColor = inputBorderColor;
            this.inputTextColor = inputTextColor;
            this.placeholderColor = placeholderColor;
            this.primaryButtonColor = primaryButtonColor;
            this.primaryButtonHover = primaryButtonHover;
            this.primaryButtonText = primaryButtonText;
            this.bubbleMeBackground = bubbleMeBackground;
            this.bubbleOtherBackground = bubbleOtherBackground;
            this.bubbleMeText = bubbleMeText;
            this.bubbleOtherText = bubbleOtherText;
            this.bubbleExtraText = bubbleExtraText;
            this.placeholderText = placeholderText;
        }
    }

    protected static class GradientPanel extends JPanel {
        private final Color start;
        private final Color end;

        protected GradientPanel(Color start, Color end) {
            this.start = start;
            this.end = end;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2d = (Graphics2D) graphics.create();
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setPaint(new GradientPaint(0, 0, start, getWidth(), getHeight(), end));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
            super.paintComponent(graphics);
        }
    }

    protected static class RoundedBorder implements Border {
        private final int radius;
        private final Color color;

        protected RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public Insets getBorderInsets(java.awt.Component component) {
            return new Insets(8, 12, 8, 12);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public void paintBorder(java.awt.Component component, Graphics graphics, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) graphics.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }
    }

    static DateTimeFormatter getTimeFormatter() {
        return TIME_FORMATTER;
    }
}
