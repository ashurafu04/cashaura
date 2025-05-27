package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RegisterView extends JFrame {
    private JTextField firstnameField;
    private JTextField lastnameField;
    private JTextField cinField;
    private JTextField birthdateField;
    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton backButton;

    public RegisterView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Register");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 420);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Create your account");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(titleLabel);
        formPanel.add(Box.createVerticalStrut(16));

        int fieldWidth = 220;
        firstnameField = addLabeledField(formPanel, "First Name", fieldWidth);
        lastnameField = addLabeledField(formPanel, "Last Name", fieldWidth);
        cinField = addLabeledField(formPanel, "CIN", fieldWidth);
        birthdateField = addLabeledField(formPanel, "Birthdate (YYYY-MM-DD)", fieldWidth);
        emailField = addLabeledField(formPanel, "Email", fieldWidth);
        phoneField = addLabeledField(formPanel, "Phone Number", fieldWidth);
        passwordField = new JPasswordField();
        addLabeledPasswordField(formPanel, "Password", passwordField, fieldWidth);

        formPanel.add(Box.createVerticalStrut(12));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        registerButton = new JButton("Register");
        backButton = new JButton("Back");
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(registerButton);
        buttonPanel.add(Box.createHorizontalStrut(16));
        buttonPanel.add(backButton);
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

    public void addRegisterListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public String getFirstname() { return firstnameField.getText(); }
    public String getLastname() { return lastnameField.getText(); }
    public String getCin() { return cinField.getText(); }
    public String getBirthdate() { return birthdateField.getText(); }
    public String getEmail() { return emailField.getText(); }
    public String getPhone() { return phoneField.getText(); }
    public String getPassword() { return new String(passwordField.getPassword()); }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public void clearFields() {
        firstnameField.setText("");
        lastnameField.setText("");
        cinField.setText("");
        birthdateField.setText("");
        emailField.setText("");
        phoneField.setText("");
        passwordField.setText("");
    }
} 