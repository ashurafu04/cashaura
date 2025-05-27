package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import service.ClientService;

public class LoginView extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private final ClientService clientService;

    public LoginView() {
        this.clientService = new ClientService();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 420);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Logo
        JLabel logoLabel = new JLabel();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setPreferredSize(new Dimension(120, 120));
        logoLabel.setMaximumSize(new Dimension(120, 120));
        try {
            String imagePath = "src/resources/images/logo.png";
            ImageIcon logoIcon = new ImageIcon(imagePath);
            Image img = logoIcon.getImage();
            if (img != null && img.getWidth(null) > 0) {
                Image scaledImg = img.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                logoIcon = new ImageIcon(scaledImg);
                logoLabel.setIcon(logoIcon);
            } else {
                logoLabel.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
            }
        } catch (Exception e) {
            logoLabel.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
        }
        formPanel.add(logoLabel);
        formPanel.add(Box.createVerticalStrut(10));

        JLabel titleLabel = new JLabel("Login to your account");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(titleLabel);
        formPanel.add(Box.createVerticalStrut(16));

        int fieldWidth = 220;
        emailField = addLabeledField(formPanel, "Email", fieldWidth);
        passwordField = new JPasswordField();
        addLabeledPasswordField(formPanel, "Password", passwordField, fieldWidth);

        formPanel.add(Box.createVerticalStrut(12));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createHorizontalStrut(16));
        buttonPanel.add(registerButton);
        buttonPanel.add(Box.createHorizontalGlue());
        formPanel.add(buttonPanel);

        // Center the form panel in the window
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(formPanel);
        centerPanel.add(Box.createVerticalGlue());
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
        getRootPane().setDefaultButton(loginButton);
    }

    private JTextField addLabeledField(JPanel panel, String label, int width) {
        JLabel jLabel = new JLabel(label);
        jLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(jLabel);
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(width, 28));
        field.setPreferredSize(new Dimension(width, 28));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(field);
        panel.add(Box.createVerticalStrut(8));
        return field;
    }

    private void addLabeledPasswordField(JPanel panel, String label, JPasswordField field, int width) {
        JLabel jLabel = new JLabel(label);
        jLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(jLabel);
        field.setMaximumSize(new Dimension(width, 28));
        field.setPreferredSize(new Dimension(width, 28));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(field);
        panel.add(Box.createVerticalStrut(8));
    }

    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void addRegisterListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void clearFields() {
        emailField.setText("");
        passwordField.setText("");
    }
} 