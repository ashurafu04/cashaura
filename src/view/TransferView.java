package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

public class TransferView extends JFrame {
    private JComboBox<String> accountComboBox;
    private JComboBox<String> beneficiaryComboBox;
    private JTextField amountField;
    private JTextArea descriptionArea;
    private JButton confirmButton;
    private JButton cancelButton;
    // CashAura logo blue
    private final Color primaryBlue = new Color(25, 118, 210); // #1976D2
    private final Color accentBlue = new Color(100, 181, 246); // #64B5F6
    private final Color darkBlue = new Color(13, 26, 38); // #0D1A26
    private final Color background = Color.WHITE;
    private final Color textBlack = Color.BLACK;
    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 24);
    private final Font mainFont = new Font("Segoe UI", Font.PLAIN, 15);
    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("fr", "MA"));
    private Color disabledButtonText = new Color(80, 90, 110); // dark blue-gray for disabled

    public TransferView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Cashaura - New Transfer");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 600);
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

        // Title
        JLabel titleLabel = new JLabel("New Transfer", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(darkBlue);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form card
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(darkBlue, 2, true),
            BorderFactory.createEmptyBorder(28, 28, 28, 28)
        ));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(360, 400));

        addLabeledComboBox(formPanel, "From Account:", accountComboBox = new JComboBox<>());
        addLabeledComboBox(formPanel, "To Beneficiary:", beneficiaryComboBox = new JComboBox<>());
        addLabeledTextField(formPanel, "Amount:", amountField = new JTextField());
        addLabeledTextArea(formPanel, "Description:", descriptionArea = new JTextArea(4, 20));

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        confirmButton = new JButton("Confirm Transfer");
        confirmButton.setFont(mainFont.deriveFont(Font.BOLD));
        confirmButton.setBackground(darkBlue);
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(darkBlue, 2, true),
            BorderFactory.createEmptyBorder(10, 32, 10, 32)
        ));
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmButton.setOpaque(true);
        confirmButton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                super.paint(g, c);
                if (!confirmButton.isEnabled()) {
                    confirmButton.setForeground(disabledButtonText);
                    confirmButton.setBackground(new Color(230, 235, 245));
                    confirmButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(darkBlue, 2, true),
                        BorderFactory.createEmptyBorder(10, 32, 10, 32)
                    ));
                }
            }
        });
        buttonPanel.add(confirmButton);
        buttonPanel.add(Box.createHorizontalStrut(16));

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(mainFont.deriveFont(Font.BOLD));
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setForeground(darkBlue);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(darkBlue, 2, true),
            BorderFactory.createEmptyBorder(10, 32, 10, 32)
        ));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setOpaque(true);
        cancelButton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                super.paint(g, c);
                if (!cancelButton.isEnabled()) {
                    cancelButton.setForeground(disabledButtonText);
                    cancelButton.setBackground(new Color(230, 235, 245));
                    cancelButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(darkBlue, 2, true),
                        BorderFactory.createEmptyBorder(10, 32, 10, 32)
                    ));
                }
            }
        });
        buttonPanel.add(cancelButton);

        formPanel.add(Box.createVerticalStrut(18));
        formPanel.add(buttonPanel);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void addLabeledComboBox(JPanel panel, String label, JComboBox<String> comboBox) {
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(mainFont);
        jLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(jLabel);
        comboBox.setFont(mainFont);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        panel.add(comboBox);
        panel.add(Box.createVerticalStrut(10));
    }

    private void addLabeledTextField(JPanel panel, String label, JTextField field) {
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(mainFont);
        jLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(jLabel);
        field.setFont(mainFont);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        panel.add(field);
        panel.add(Box.createVerticalStrut(10));
    }

    private void addLabeledTextArea(JPanel panel, String label, JTextArea area) {
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(mainFont);
        jLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(jLabel);
        area.setFont(mainFont);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        panel.add(scrollPane);
        panel.add(Box.createVerticalStrut(10));
    }

    public void setAccounts(String[] accounts) {
        accountComboBox.setModel(new DefaultComboBoxModel<>(accounts));
    }
    public void setBeneficiaries(String[] beneficiaries) {
        beneficiaryComboBox.setModel(new DefaultComboBoxModel<>(beneficiaries));
    }
    public String getSelectedAccount() {
        return (String) accountComboBox.getSelectedItem();
    }
    public String getSelectedBeneficiary() {
        return (String) beneficiaryComboBox.getSelectedItem();
    }
    public String getAmount() {
        return amountField.getText();
    }
    public String getDescription() {
        return descriptionArea.getText();
    }
    public void addConfirmListener(ActionListener listener) {
        confirmButton.addActionListener(listener);
    }
    public void addCancelListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
    }
    public void setConfirmEnabled(boolean enabled) {
        confirmButton.setEnabled(enabled);
    }
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
} 