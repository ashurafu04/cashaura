package util;

import javax.swing.*;
import java.awt.*;

public class UIStyler {
    // CashAura brand colors
    public static final Color PRIMARY_BLUE = new Color(25, 118, 210); // #1976D2
    public static final Color ACCENT_BLUE = new Color(100, 181, 246); // #64B5F6
    public static final Color DARK_BLUE = new Color(13, 26, 38); // #0D1A26
    public static final Color BACKGROUND = Color.WHITE;
    public static final Color TEXT_BLACK = Color.BLACK;
    
    // Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 16);
    
    /**
     * Applies the primary button style (dark background, white text)
     */
    public static void applyPrimaryButtonStyle(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setBackground(DARK_BLUE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DARK_BLUE, 2, true),
            BorderFactory.createEmptyBorder(10, 32, 10, 32)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
    }

    /**
     * Applies the secondary button style (white background, dark text)
     */
    public static void applySecondaryButtonStyle(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setBackground(Color.WHITE);
        button.setForeground(DARK_BLUE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DARK_BLUE, 2, true),
            BorderFactory.createEmptyBorder(10, 32, 10, 32)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
    }

    /**
     * Applies the danger button style (for logout, delete, etc.)
     */
    public static void applyDangerButtonStyle(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setBackground(new Color(220, 53, 69)); // Bootstrap danger red
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 53, 69), 2, true),
            BorderFactory.createEmptyBorder(10, 32, 10, 32)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
    }
} 