package ui;

import app.ChatController;
import ui.components.ChatPanel;
import ui.components.HeaderPanel;
import ui.theme.Theme;
import ui.util.PlaceholderTextField;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana Client (UI).
 */
public class ClientUI extends JFrame {

    private final ChatController controller;
    private final ChatPanel chatPanel;
    private final PlaceholderTextField input;
    private final JButton sendBtn;

    public ClientUI(ChatController controller) {
        super("Client");
        this.controller = controller;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(860, 600));

        setLayout(new BorderLayout());

        HeaderPanel header = new HeaderPanel(
                "Client",
                "Connected to: localhost",
                false,
                Theme.CLIENT_HEADER_TOP,
                Theme.CLIENT_HEADER_BOTTOM,
                Theme.CLIENT_TITLE,
                Theme.CLIENT_SUBTITLE,
                "ONLINE",
                Theme.BADGE_ONLINE_BG,
                Theme.BADGE_ONLINE_FG
        );

        add(header, BorderLayout.NORTH);

        chatPanel = new ChatPanel(
                Theme.CHAT_BG_LIGHT,
                Theme.BUBBLE_ME_CLIENT,
                Theme.BUBBLE_OTHER_LIGHT,
                Theme.TEXT_DARK,
                Theme.TEXT_MUTED_DARK
        );
        add(chatPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(12, 0));
        bottom.setBorder(BorderFactory.createEmptyBorder(12, 14, 14, 14));
        bottom.setBackground(Theme.WINDOW_BG_DARK);

        input = new PlaceholderTextField("Type a message…");
        input.setFont(Theme.FONT_CHAT);
        input.setPreferredSize(new Dimension(520, 44));
        input.setBackground(new Color(255,255,255));
        input.setForeground(Theme.TEXT_DARK);

        sendBtn = Theme.makePrimaryButton("Send", Theme.CLIENT_ACCENT);
        sendBtn.setPreferredSize(new Dimension(110, 44));

        bottom.add(input, BorderLayout.CENTER);
        bottom.add(sendBtn, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);

        // Acciones
        sendBtn.addActionListener(e -> send());
        input.addActionListener(e -> send()); // Enter

        getContentPane().setBackground(Theme.WINDOW_BG_DARK);
    }

    private void send() {
        String text = input.getRealText();
        if (text == null || text.trim().isEmpty()) return;

        controller.sendFromClient(text.trim());
        input.clear();
    }

    // API simple para el controller
    public void addLeftBubble(String main, String extra) {
        chatPanel.addLeftBubble(main, extra);
    }

    public void addRightBubble(String main, String extra) {
        chatPanel.addRightBubble(main, extra);
    }

    public ChatPanel getChatPanel() {
        return chatPanel;
    }
}
