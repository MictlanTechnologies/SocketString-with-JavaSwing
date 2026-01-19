package ui;

import app.ServerChatController;
import ui.components.ChatPanel;
import ui.components.HeaderPanel;
import ui.theme.Theme;
import ui.util.PlaceholderTextField;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana Server (UI).
 */
public class ServerUI extends JFrame {

    private final ServerChatController controller;
    private final ChatPanel chatPanel;
    private final PlaceholderTextField input;
    private final JButton sendBtn;

    public ServerUI(ServerChatController controller) {
        super("Servidor");
        this.controller = controller;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(860, 600));

        setLayout(new BorderLayout());

        HeaderPanel header = new HeaderPanel(
                "Servidor",
                "Now Connected",
                true,
                Theme.SERVER_HEADER_TOP,
                Theme.SERVER_HEADER_BOTTOM,
                Theme.SERVER_TITLE,
                Theme.SERVER_SUBTITLE,
                "ONLINE",
                Theme.BADGE_ONLINE_BG,
                Theme.BADGE_ONLINE_FG
        );

        add(header, BorderLayout.NORTH);

        chatPanel = new ChatPanel(
                Theme.CHAT_BG_LIGHT,
                Theme.BUBBLE_ME_SERVER,
                Theme.BUBBLE_OTHER_LIGHT,
                Theme.TEXT_DARK,
                Theme.TEXT_MUTED_DARK
        );
        add(chatPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(12, 0));
        bottom.setBorder(BorderFactory.createEmptyBorder(12, 14, 14, 14));
        bottom.setBackground(Theme.WINDOW_BG_LIGHT);

        input = new PlaceholderTextField("Type a message…");
        input.setFont(Theme.FONT_CHAT);
        input.setPreferredSize(new Dimension(520, 44));

        sendBtn = Theme.makePrimaryButton("Send", Theme.SERVER_ACCENT);
        sendBtn.setPreferredSize(new Dimension(110, 44));

        bottom.add(input, BorderLayout.CENTER);
        bottom.add(sendBtn, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);

        // Acciones
        sendBtn.addActionListener(e -> send());
        input.addActionListener(e -> send()); // Enter

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent event) {
                controller.shutdownAndSave();
            }
        });

        getContentPane().setBackground(Theme.WINDOW_BG_LIGHT);
    }

    private void send() {
        String text = input.getRealText();
        if (text == null || text.trim().isEmpty()) return;

        controller.sendFromServer(text.trim());
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
