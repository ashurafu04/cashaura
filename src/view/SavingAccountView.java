package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;
import java.math.BigDecimal;
import java.util.Date;
import model.SavingAccount;

public class SavingAccountView extends JFrame {
    private JTextField initialDepositField;
    private JTextField interestRateField;
    private JButton createButton;
    private JButton calculateInterestButton;
    private JButton closeAccountButton;
    private JButton backButton;
    private JLabel balanceLabel;
    private JLabel interestRateLabel;
    private JLabel lastCalculationLabel;

    // CashAura theme colors
    private final Color darkBlue = new Color(13, 26, 38);
    private final Color background = Color.WHITE;
    private final Color textBlack = Color.BLACK;
    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 24);
    private final Font mainFont = new Font("Segoe UI", Font.PLAIN, 15);
    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("fr", "MA"));
    private Color disabledButtonText = new Color(80, 90, 110);

    public SavingAccountView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Cashaura - Saving Account");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 700);
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
        mainPanel.setLayout(new BorderLayout(0, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Title
        JLabel titleLabel = new JLabel("Saving Account", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(darkBlue);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Center panel to hold form and details
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Account Details Section
        JPanel detailsPanel = new JPanel();
        detailsPanel.setOpaque(false);
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(darkBlue),
                "Account Details",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                mainFont.deriveFont(Font.BOLD)
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        balanceLabel = new JLabel("Current Balance: MAD 0.00");
        balanceLabel.setFont(mainFont);
        detailsPanel.add(balanceLabel);
        detailsPanel.add(Box.createVerticalStrut(10));

        interestRateLabel = new JLabel("Interest Rate: 0.00%");
        interestRateLabel.setFont(mainFont);
        detailsPanel.add(interestRateLabel);
        detailsPanel.add(Box.createVerticalStrut(10));

        lastCalculationLabel = new JLabel("Last Interest Calculation: Never");
        lastCalculationLabel.setFont(mainFont);
        detailsPanel.add(lastCalculationLabel);

        centerPanel.add(detailsPanel);
        centerPanel.add(Box.createVerticalStrut(20));

        // New Account Section
        JPanel newAccountPanel = new JPanel();
        newAccountPanel.setOpaque(false);
        newAccountPanel.setLayout(new BoxLayout(newAccountPanel, BoxLayout.Y_AXIS));
        newAccountPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(darkBlue),
                "Create New Account",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                mainFont.deriveFont(Font.BOLD)
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        addLabeledTextField(newAccountPanel, "Initial Deposit (MAD):", initialDepositField = new JTextField());
        addLabeledTextField(newAccountPanel, "Interest Rate (%):", interestRateField = new JTextField());

        centerPanel.add(newAccountPanel);
        centerPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        createButton = createStyledButton("Create Account", true);
        calculateInterestButton = createStyledButton("Calculate Interest", false);
        closeAccountButton = createStyledButton("Close Account", false);
        backButton = createStyledButton("Back", false);

        buttonPanel.add(createButton);
        buttonPanel.add(calculateInterestButton);
        buttonPanel.add(closeAccountButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void addLabeledTextField(JPanel panel, String label, JTextField field) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setOpaque(false);
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel jLabel = new JLabel(label);
        jLabel.setFont(mainFont);
        jLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPanel.add(jLabel);
        fieldPanel.add(Box.createVerticalStrut(5));

        field.setFont(mainFont);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        fieldPanel.add(field);
        
        panel.add(fieldPanel);
        panel.add(Box.createVerticalStrut(15));
    }

    private JButton createStyledButton(String text, boolean isPrimary) {
        JButton button = new JButton(text);
        button.setFont(mainFont.deriveFont(Font.BOLD));
        button.setBackground(isPrimary ? new Color(25, 118, 210) : Color.WHITE); // Primary blue for create button
        button.setForeground(darkBlue); // Always black text for all buttons
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(darkBlue, 2, true),
            BorderFactory.createEmptyBorder(12, 24, 12, 24)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(200, 45));
        return button;
    }

    public void updateAccountDetails(SavingAccount account) {
        if (account != null) {
            balanceLabel.setText("Current Balance: " + currencyFormatter.format(account.getBaseAccount().getBalance()));
            interestRateLabel.setText("Interest Rate: " + account.getInterestRate() + "%");
            lastCalculationLabel.setText("Last Interest Calculation: " + account.getLastInterestCalcDate());
            calculateInterestButton.setEnabled(true);
            closeAccountButton.setEnabled(true);
            createButton.setEnabled(false);
        } else {
            balanceLabel.setText("Current Balance: MAD 0.00");
            interestRateLabel.setText("Interest Rate: 0.00%");
            lastCalculationLabel.setText("Last Interest Calculation: Never");
            calculateInterestButton.setEnabled(false);
            closeAccountButton.setEnabled(false);
            createButton.setEnabled(true);
        }
    }

    public BigDecimal getInitialDeposit() {
        try {
            return new BigDecimal(initialDepositField.getText().trim());
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getInterestRate() {
        try {
            return new BigDecimal(interestRateField.getText().trim());
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    public void clearFields() {
        initialDepositField.setText("");
        interestRateField.setText("");
    }

    public void addCreateListener(ActionListener listener) {
        createButton.addActionListener(listener);
    }

    public void addCalculateInterestListener(ActionListener listener) {
        calculateInterestButton.addActionListener(listener);
    }

    public void addCloseAccountListener(ActionListener listener) {
        closeAccountButton.addActionListener(listener);
    }

    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
} 