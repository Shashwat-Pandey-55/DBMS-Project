import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JButton userLoginButton;
    private JButton adminLoginButton;
    private JButton tellerLoginButton;

    JButton createIconButton(Color color, char iconLetter) {
        JButton button = new JButton(Character.toString(iconLetter)); // Placeholder for icon
        button.setForeground(color);
        button.setFocusPainted(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        return button;
    }

    public LoginFrame() {
        setTitle("Rupt Bank");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().setBackground(new Color(32, 32, 32));

        userLoginButton = new JButton("User Login");
        adminLoginButton = new JButton("Admin Login");
        tellerLoginButton = new JButton("Teller Login");

        // Add action listeners for buttons here...
        
        JPanel userLoginPanel = new JPanel();
        userLoginPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        userLoginPanel.add(userLoginButton);
        userLoginPanel.setOpaque(false);

        JPanel adminLoginPanel = new JPanel();
        adminLoginPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        adminLoginPanel.add(adminLoginButton);
        adminLoginPanel.setOpaque(false);

        JPanel tellerLoginPanel = new JPanel();
        tellerLoginPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        tellerLoginPanel.add(tellerLoginButton);
        tellerLoginPanel.setOpaque(false);

         userLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserLogin userLoginFrame = new UserLogin();
                userLoginFrame.setVisible(true);
                dispose();
            }
        });

        adminLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminLogin adminLoginFrame = new AdminLogin();
                adminLoginFrame.setVisible(true);
                dispose();
            }
        });
        

        tellerLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TellerLogin tellerLoginFrame = new TellerLogin();
                tellerLoginFrame.setVisible(true);
                dispose();
            }
        });
    


        JPanel socialPanel = new JPanel();
        socialPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        socialPanel.add(createIconButton(new Color(0, 172, 237), 'f')); // Facebook icon placeholder
        socialPanel.add(createIconButton(new Color(29, 161, 242), 't')); // Twitter icon placeholder
        socialPanel.add(createIconButton(new Color(194, 48, 39), 'g')); // Google icon placeholder
        socialPanel.setOpaque(false);

        // Adding wrapped buttons and social panel to the frame
        add(Box.createVerticalGlue());
        add(userLoginPanel);
        add(adminLoginPanel);
        add(tellerLoginPanel);
        add(socialPanel);
        add(Box.createVerticalGlue());

        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame mainWindow = new LoginFrame();
            mainWindow.setLocationRelativeTo(null);
            mainWindow.setVisible(true);
        });
    }
}
