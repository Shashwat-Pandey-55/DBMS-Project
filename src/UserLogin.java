import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UserLogin extends JFrame {

    private JTextField userIDField;
    private JTextField userEmailField;
    private JButton loginButton;

    public UserLogin() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("User Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a panel for the login form
        
       

        // Add components to the login panel
        userIDField = createTextField("User ID");
        userEmailField = createTextField("Email ID");
        loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel loginPanel = new JPanel();



        loginPanel.setBackground(new Color(32, 32, 32)); // Dark background
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        loginPanel.setLayout(new BoxLayout(loginPanel,BoxLayout.Y_AXIS)); // simple grid layout


        loginPanel.add(userIDField);
        loginPanel.add(userEmailField);
        loginPanel.add(loginButton);
        

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verifyCredentials(userIDField.getText().trim(), userEmailField.getText().trim());
            }
        });

        
        
 
       

        // Social media buttons
        JPanel socialPanel = new JPanel();
        socialPanel.add(createIconButton(new Color(0, 172, 237), 'f')); // Facebook icon placeholder
        socialPanel.add(createIconButton(new Color(29, 161, 242), 't')); // Twitter icon placeholder
        socialPanel.add(createIconButton(new Color(194, 48, 39), 'g')); // Google icon placeholder
        socialPanel.setOpaque(false);

        JButton goBackButton= new JButton("Go Back");
        goBackButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                dispose();
            }
        });

              
        loginPanel.add(goBackButton);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer
        loginPanel.add(socialPanel);
        
        

        // Add the login panel to the frame
        add(loginPanel, BorderLayout.CENTER);
    }
     private JTextField createTextField(String placeholder) {
        JTextField textField = new JTextField(20);
        styleTextField(textField, placeholder);
        return textField;
    }

    private void styleTextField(JTextField textField, String placeholder) {
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, textField.getPreferredSize().height));
        textField.setAlignmentX(Component.CENTER_ALIGNMENT);
        textField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        textField.setForeground(Color.GRAY);
        textField.setText(placeholder);
    
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });
    }
    
    

    
    private void verifyCredentials(String userID, String userEmailID) {
        String query = "SELECT a.AccountNumber FROM USER u JOIN ACCOUNT a ON u.UserID = a.UserID WHERE u.UserID = ? AND u.UserEmailID = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, userID);
            pstmt.setString(2, userEmailID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String accountNumber = rs.getString("AccountNumber");
                // Launch UserHome with the retrieved account number
                UserHome userHome = new UserHome(accountNumber);
                userHome.setVisible(true);
                UserLogin.this.dispose(); // Close the login window
            } else {
                JOptionPane.showMessageDialog(UserLogin.this, "Invalid credentials.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(UserLogin.this, "Database error: " + ex.getMessage());
        }
    }


    private JButton createIconButton(Color color, char iconLetter) {
        JButton button = new JButton(Character.toString(iconLetter)); // Placeholder for icon
        button.setForeground(color);
        button.setFocusPainted(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserLogin frame = new UserLogin();
            frame.setVisible(true);
        });
    }
}
