package ui.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * JTextField con placeholder simple (sin libs).
 */
public class PlaceholderTextField extends JTextField {

    private final String placeholder;
    private boolean showingPlaceholder = true;

    public PlaceholderTextField(String placeholder) {
        this.placeholder = placeholder;

        setText(placeholder);
        setForeground(new Color(140, 140, 150));
        setBackground(Color.WHITE);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0,0,0,40), 1, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (showingPlaceholder) {
                    setText("");
                    setForeground(new Color(25, 25, 28));
                    showingPlaceholder = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().trim().isEmpty()) {
                    setText(placeholder);
                    setForeground(new Color(140, 140, 150));
                    showingPlaceholder = true;
                }
            }
        });
    }

    /**
     * Devuelve el texto real (si está placeholder, devuelve "").
     */
    public String getRealText() {
        return showingPlaceholder ? "" : getText();
    }

    public void clear() {
        setText("");
        showingPlaceholder = false;
        // Si no tiene foco, vuelve a placeholder
        if (!isFocusOwner()) {
            setText(placeholder);
            setForeground(new Color(140, 140, 150));
            showingPlaceholder = true;
        }
    }
}
