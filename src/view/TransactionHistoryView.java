package view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionHistoryView extends JFrame {
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private JButton backButton;
    private JComboBox<String> filterComboBox;
    private JPanel mainPanel;
    // High-contrast color scheme
    private final Color primaryBlue = new Color(25, 118, 210); // #1976D2
    private final Color accentBlue = new Color(100, 181, 246); // #64B5F6
    private final Color darkBlue = new Color(13, 26, 38); // #0D1A26
    private final Color background = Color.WHITE;
    private final Color textBlack = Color.BLACK;
    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 24);
    private final Font mainFont = new Font("Segoe UI", Font.PLAIN, 15);
    private Color disabledButtonText = new Color(80, 90, 110); // dark blue-gray for disabled

    public TransactionHistoryView() {
        initializeUI();
        setupWindowBehavior();
    }

    private void setupWindowBehavior() {
        setTitle("Transaction History - Cashaura");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);
        setResizable(true);
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                adjustTableColumns();
            }
        });
    }

    private void initializeUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(background);

        // Center everything vertically
        mainPanel.add(Box.createVerticalGlue());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));
        JLabel titleLabel = new JLabel("Transaction History");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(darkBlue);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createHorizontalGlue());
        JLabel filterLabel = new JLabel("Filter: ");
        filterLabel.setFont(mainFont);
        filterLabel.setForeground(textBlack);
        headerPanel.add(filterLabel);
        String[] filterOptions = {"All Transactions", "Transfers Only", "Deposits Only", "Withdrawals Only"};
        filterComboBox = new JComboBox<>(filterOptions);
        filterComboBox.setFont(mainFont);
        filterComboBox.setBackground(Color.WHITE);
        filterComboBox.setForeground(Color.BLACK);
        filterComboBox.setPreferredSize(new Dimension(220, 32));
        filterComboBox.setMaximumSize(new Dimension(220, 32));
        filterComboBox.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        headerPanel.add(filterComboBox);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table card
        JPanel tableCard = new JPanel();
        tableCard.setLayout(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(darkBlue, 2, true),
            BorderFactory.createEmptyBorder(28, 28, 28, 28)
        ));

        String[] columns = {"Date", "Type", "Description", "Amount", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 3) return Double.class;
                return String.class;
            }
        };
        transactionTable = new JTable(tableModel);
        transactionTable.setFont(mainFont);
        transactionTable.setRowHeight(36);
        transactionTable.setShowGrid(true);
        transactionTable.setGridColor(new Color(220, 220, 220));
        transactionTable.setSelectionBackground(darkBlue);
        transactionTable.setSelectionForeground(Color.WHITE);
        JTableHeader header = transactionTable.getTableHeader();
        header.setFont(mainFont.deriveFont(Font.BOLD));
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        transactionTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                    c.setForeground(textBlack);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
        transactionTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected && value != null) {
                    double amount = (Double) value;
                    setForeground(amount < 0 ? new Color(244, 67, 54) : new Color(76, 175, 80));
                    setText(String.format("%,.2f DH", amount));
                }
                setHorizontalAlignment(SwingConstants.RIGHT);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        tableCard.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(tableCard, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(24, 0, 0, 0));
        backButton = new JButton("Back");
        backButton.setFont(mainFont.deriveFont(Font.BOLD));
        backButton.setBackground(darkBlue);
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(darkBlue, 2, true),
            BorderFactory.createEmptyBorder(10, 32, 10, 32)
        ));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setOpaque(true);
        backButton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                super.paint(g, c);
                if (!backButton.isEnabled()) {
                    backButton.setForeground(Color.DARK_GRAY);
                    backButton.setBackground(new Color(230, 235, 245));
                    backButton.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(darkBlue, 2, true),
                        BorderFactory.createEmptyBorder(10, 32, 10, 32)
                    ));
                }
            }
        });
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(Box.createVerticalGlue());

        setContentPane(mainPanel);
    }

    private void adjustTableColumns() {
        if (transactionTable != null) {
            TableColumnModel columnModel = transactionTable.getColumnModel();
            
            // Date column (fixed width)
            columnModel.getColumn(0).setPreferredWidth(150);
            columnModel.getColumn(0).setMinWidth(150);
            
            // Type column (fixed width)
            columnModel.getColumn(1).setPreferredWidth(100);
            columnModel.getColumn(1).setMinWidth(100);
            
            // Description column (flexible, but with minimum)
            columnModel.getColumn(2).setPreferredWidth(300);
            columnModel.getColumn(2).setMinWidth(200);
            
            // Amount column (fixed width)
            columnModel.getColumn(3).setPreferredWidth(120);
            columnModel.getColumn(3).setMinWidth(120);
            
            // Status column (fixed width)
            columnModel.getColumn(4).setPreferredWidth(100);
            columnModel.getColumn(4).setMinWidth(100);
        }
    }

    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
    public void addFilterListener(ActionListener listener) {
        filterComboBox.addActionListener(listener);
    }
    public String getSelectedFilter() {
        return (String) filterComboBox.getSelectedItem();
    }
    public void clearTransactions() {
        tableModel.setRowCount(0);
    }
    public void addTransaction(Date date, String type, String description, double amount, String status) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String formattedDate = date != null ? sdf.format(date) : "N/A";
        String formattedType = type != null ? type.toUpperCase() : "N/A";
        String formattedDesc = description != null ? description : "N/A";
        String formattedStatus = status != null ? status.toUpperCase() : "N/A";
        
        tableModel.addRow(new Object[]{
            formattedDate,
            formattedType,
            formattedDesc,
            amount,
            formattedStatus
        });
    }
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}