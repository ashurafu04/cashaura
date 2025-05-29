package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

public class DashboardView extends JFrame {
    private JLabel welcomeLabel;
    private JLabel accountSummaryLabel;
    private JButton accountButton;
    private JButton transferButton;
    private JButton historyButton;
    private JButton beneficiariesButton;
    private JButton savingAccountButton;
    private JButton logoutButton;
    // High-contrast color scheme
    private final Color darkBlue = new Color(13, 26, 38); // #0D1A26
    private final Color background = Color.WHITE;
    private final Color textBlack = Color.BLACK;
    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 28);
    private final Font mainFont = new Font("Segoe UI", Font.PLAIN, 16);
    private final Font navFont = new Font("Segoe UI", Font.BOLD, 15);
    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("fr", "MA"));
    private Color disabledButtonText = new Color(80, 90, 110); // dark blue-gray for disabled

    public DashboardView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Cashaura - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(background);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header with welcome and logout
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 32, 0));

        // Welcome label on the left
        welcomeLabel = new JLabel("Welcome!");
        welcomeLabel.setFont(titleFont);
        welcomeLabel.setForeground(darkBlue);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        // Logout button on the right
        logoutButton = createNavButton("Logout", true);
        logoutButton.setPreferredSize(new Dimension(120, 40));
        headerPanel.add(logoutButton, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center: Account summary card
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(darkBlue, 2, true),
            BorderFactory.createEmptyBorder(40, 48, 40, 48)
        ));
        cardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel cardTitle = new JLabel("Account Summary");
        cardTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        cardTitle.setForeground(darkBlue);
        cardTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.add(cardTitle);
        cardPanel.add(Box.createVerticalStrut(24));

        accountSummaryLabel = new JLabel();
        accountSummaryLabel.setFont(mainFont);
        accountSummaryLabel.setForeground(textBlack);
        accountSummaryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.add(accountSummaryLabel);

        centerPanel.add(cardPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Navigation panel
        JPanel navigationPanel = new JPanel();
        navigationPanel.setOpaque(false);
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        navigationPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        accountButton = createNavButton("Account");
        transferButton = createNavButton("Transfer");
        historyButton = createNavButton("History");
        beneficiariesButton = createNavButton("Beneficiaries");
        savingAccountButton = createNavButton("Saving Account");

        navigationPanel.add(accountButton);
        navigationPanel.add(Box.createHorizontalStrut(28));
        navigationPanel.add(transferButton);
        navigationPanel.add(Box.createHorizontalStrut(28));
        navigationPanel.add(historyButton);
        navigationPanel.add(Box.createHorizontalStrut(28));
        navigationPanel.add(beneficiariesButton);
        navigationPanel.add(Box.createHorizontalStrut(28));
        navigationPanel.add(savingAccountButton);

        mainPanel.add(navigationPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private JButton createNavButton(String text) {
        return createNavButton(text, false);
    }

    private JButton createNavButton(String text, boolean isLogout) {
        JButton button = new JButton(text);
        button.setFont(navFont);
        if (isLogout) {
            button.setBackground(new Color(220, 53, 69)); // Bootstrap red
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(isLogout ? Color.WHITE : darkBlue);
            button.setForeground(isLogout ? darkBlue : Color.WHITE);
        }
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(isLogout ? new Color(220, 53, 69) : darkBlue, 2, true),
            BorderFactory.createEmptyBorder(10, 32, 10, 32)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                super.paint(g, c);
                if (!button.isEnabled()) {
                    button.setForeground(disabledButtonText);
                    button.setBackground(new Color(230, 235, 245));
                    button.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(darkBlue, 2, true),
                        BorderFactory.createEmptyBorder(10, 32, 10, 32)
                    ));
                }
            }
        });
        return button;
    }

    public void setWelcomeMessage(String message) {
        welcomeLabel.setText(message);
    }

    public void setAccountSummary(String summary) {
        StringBuilder html = new StringBuilder();
        html.append("<html><body style='width: 350px; padding: 10px;'>");
        if (summary.contains("No account found")) {
            html.append("<div style='color: #757575; text-align: center;'>");
            html.append(summary);
            html.append("</div>");
        } else {
            html.append(summary);
        }
        html.append("</body></html>");
        accountSummaryLabel.setText(html.toString());
    }

    public void addAccountListener(ActionListener listener) {
        accountButton.addActionListener(listener);
    }
    public void addTransferListener(ActionListener listener) {
        transferButton.addActionListener(listener);
    }
    public void addHistoryListener(ActionListener listener) {
        historyButton.addActionListener(listener);
    }
    public void addBeneficiariesListener(ActionListener listener) {
        beneficiariesButton.addActionListener(listener);
    }
    public void addSavingAccountListener(ActionListener listener) {
        savingAccountButton.addActionListener(listener);
    }
    public void addLogoutListener(ActionListener listener) {
        logoutButton.addActionListener(listener);
    }
    public void setTransferEnabled(boolean enabled) {
        transferButton.setEnabled(enabled);
    }
    public void setHistoryEnabled(boolean enabled) {
        historyButton.setEnabled(enabled);
    }
    public void setBeneficiariesEnabled(boolean enabled) {
        beneficiariesButton.setEnabled(enabled);
    }
    public void setSavingAccountEnabled(boolean enabled) {
        savingAccountButton.setEnabled(enabled);
    }
} 