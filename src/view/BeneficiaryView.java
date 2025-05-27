package view;

import model.Beneficiaries;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class BeneficiaryView extends JFrame {
    private JList<String> beneficiaryList;
    private DefaultListModel<String> listModel;
    private JTextField accountRibField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton backButton;
    // CashAura logo blue
    private final Color primaryBlue = new Color(25, 118, 210); // #1976D2
    private final Color accentBlue = new Color(100, 181, 246); // #64B5F6
    private final Color darkBlue = new Color(13, 26, 38); // #0D1A26
    private final Color background = Color.WHITE;
    private final Color textBlack = Color.BLACK;
    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 24);
    private final Font mainFont = new Font("Segoe UI", Font.PLAIN, 15);
    private List<Beneficiaries> currentBeneficiaries;
    private Color disabledButtonText = new Color(80, 90, 110); // dark blue-gray for disabled

    public BeneficiaryView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Cashaura - Manage Beneficiaries");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 420);
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
        JLabel titleLabel = new JLabel("Manage Beneficiaries", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(darkBlue);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Center card panel
        JPanel cardPanel = new JPanel();
        cardPanel.setOpaque(false);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.X_AXIS));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(darkBlue, 2, true),
            BorderFactory.createEmptyBorder(28, 28, 28, 28)
        ));
        cardPanel.setMaximumSize(new Dimension(600, 220));

        // Left: Beneficiary list
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(350, 0));
        JLabel listTitle = new JLabel("Your Beneficiaries");
        listTitle.setFont(mainFont.deriveFont(Font.BOLD));
        listTitle.setForeground(darkBlue);
        leftPanel.add(listTitle);
        leftPanel.add(Box.createVerticalStrut(10));
        listModel = new DefaultListModel<>();
        beneficiaryList = new JList<>(listModel);
        beneficiaryList.setFont(mainFont);
        beneficiaryList.setBorder(new LineBorder(new Color(200, 200, 200)));
        beneficiaryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        beneficiaryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                deleteButton.setEnabled(beneficiaryList.getSelectedIndex() != -1);
            }
        });
        JScrollPane scrollPane = new JScrollPane(beneficiaryList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        leftPanel.add(scrollPane);
        cardPanel.add(leftPanel);
        cardPanel.add(Box.createHorizontalStrut(32));

        // Right: Add form
        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        JLabel addTitle = new JLabel("Add New Beneficiary");
        addTitle.setFont(mainFont.deriveFont(Font.BOLD));
        addTitle.setForeground(darkBlue);
        rightPanel.add(addTitle);
        rightPanel.add(Box.createVerticalStrut(16));
        JLabel ribLabel = new JLabel("Beneficiary RIB:");
        ribLabel.setFont(mainFont);
        rightPanel.add(ribLabel);
        accountRibField = new JTextField(20);
        accountRibField.setFont(mainFont);
        accountRibField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        accountRibField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        rightPanel.add(accountRibField);
        rightPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(rightPanel);
        mainPanel.add(cardPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(24, 0, 0, 0));
        addButton = new JButton("Add Beneficiary");
        addButton.setFont(mainFont.deriveFont(Font.BOLD));
        addButton.setBackground(darkBlue);
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(darkBlue, 2, true),
            BorderFactory.createEmptyBorder(10, 32, 10, 32)
        ));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.setOpaque(true);
        addButton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                super.paint(g, c);
                if (!addButton.isEnabled()) {
                    addButton.setForeground(disabledButtonText);
                    addButton.setBackground(new Color(230, 235, 245));
                    addButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(darkBlue, 2, true),
                        BorderFactory.createEmptyBorder(10, 32, 10, 32)
                    ));
                }
            }
        });
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createHorizontalStrut(16));
        deleteButton = new JButton("Delete");
        deleteButton.setFont(mainFont.deriveFont(Font.BOLD));
        deleteButton.setBackground(Color.WHITE);
        deleteButton.setForeground(darkBlue);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(darkBlue, 2, true),
            BorderFactory.createEmptyBorder(10, 32, 10, 32)
        ));
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.setOpaque(true);
        deleteButton.setEnabled(false);
        deleteButton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                super.paint(g, c);
                if (!deleteButton.isEnabled()) {
                    deleteButton.setForeground(disabledButtonText);
                    deleteButton.setBackground(new Color(230, 235, 245));
                    deleteButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(darkBlue, 2, true),
                        BorderFactory.createEmptyBorder(10, 32, 10, 32)
                    ));
                }
            }
        });
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createHorizontalStrut(16));
        backButton = new JButton("Back");
        backButton.setFont(mainFont.deriveFont(Font.BOLD));
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(darkBlue);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(darkBlue, 2, true),
            BorderFactory.createEmptyBorder(10, 32, 10, 32)
        ));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setOpaque(true);
        backButton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                super.paint(g, c);
                if (!backButton.isEnabled()) {
                    backButton.setForeground(disabledButtonText);
                    backButton.setBackground(new Color(230, 235, 245));
                    backButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(darkBlue, 2, true),
                        BorderFactory.createEmptyBorder(10, 32, 10, 32)
                    ));
                }
            }
        });
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    public void setBeneficiaries(List<Beneficiaries> beneficiaries) {
        this.currentBeneficiaries = beneficiaries;
        listModel.clear();
        for (Beneficiaries beneficiary : beneficiaries) {
            listModel.addElement("Account ID: " + beneficiary.getIdBeneficiary());
        }
    }
    public String getAccountRib() {
        return accountRibField.getText().trim();
    }
    public Beneficiaries getSelectedBeneficiary() {
        int selectedIndex = beneficiaryList.getSelectedIndex();
        return selectedIndex != -1 ? currentBeneficiaries.get(selectedIndex) : null;
    }
    public void clearFields() {
        accountRibField.setText("");
        beneficiaryList.clearSelection();
        deleteButton.setEnabled(false);
    }
    public void addAddListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }
    public void addDeleteListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
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