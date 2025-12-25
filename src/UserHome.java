import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.math.*;


public class UserHome extends JFrame {
    private String userAccountNumber; 
    CardLayout cardLayout;
    JPanel cards; // Panel that uses CardLayout
    JButton statementButton, cardButton, supportButton, signOutButton;

    public UserHome(String accountNumber) {
        this.userAccountNumber = accountNumber;
        setTitle("Rupt Bank");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        cards.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Side Menu
        JPanel sideMenu = new JPanel();
        sideMenu.setLayout(new BoxLayout(sideMenu, BoxLayout.Y_AXIS));
        sideMenu.setBackground(Color.DARK_GRAY);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.GRAY);
        JLabel welcomeLabel = new JLabel("WELCOME USER!", SwingConstants.LEFT);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        signOutButton = new JButton("Sign out");

        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        headerPanel.add(signOutButton, BorderLayout.EAST);

        // Buttons
        statementButton = createButton("Generate Statement");
        cardButton = createButton("Card");
        supportButton = createButton("Customer Support");

        sideMenu.add(Box.createVerticalGlue());
        sideMenu.add(statementButton);
        sideMenu.add(cardButton);
        sideMenu.add(supportButton);
        sideMenu.add(Box.createVerticalGlue());

        // Panels
        JPanel statementPanel = createStatementPanel();
        JPanel cardPanel = createCardPanel();
        JPanel supportPanel = createPanel("customer support:91-9831636131");

        cards.add(statementPanel, "Statement");
        cards.add(cardPanel, "Card");
        cards.add(supportPanel, "Support");

        // Add action listeners to buttons
        statementButton.addActionListener(e -> cardLayout.show(cards, "Statement"));
        cardButton.addActionListener(e -> cardLayout.show(cards, "Card"));
        supportButton.addActionListener(e -> cardLayout.show(cards, "Support"));
        signOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                dispose();
            }
        });
        // Adding Components to Frame
        add(headerPanel, BorderLayout.NORTH);
        add(sideMenu, BorderLayout.WEST);
        add(cards, BorderLayout.CENTER);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
        button.setForeground(Color.BLACK); // Set the text color to white for visibility
        button.setBackground(Color.DARK_GRAY); // Match the background color of side menu
        button.setFocusPainted(false);
        return button;
    }
    
    private JPanel createPanel(String text) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(text));
        return panel;
    }

    private JPanel createStatementPanel() {
        JPanel statementPanel = new JPanel();
        statementPanel.setLayout(new BoxLayout(statementPanel, BoxLayout.Y_AXIS));
        statementPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        // Add components for account number input
        
        // Create and add generate button
        JButton generateButton = new JButton("Generate");
        statementPanel.add(createButtonPanel(generateButton));
    
        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fetchTransactions(userAccountNumber); // Use the stored account number
            }
        });
    
        return statementPanel;
    }

    private void fetchTransactions(String accountNumber) {
        // Adjusted query to include the balance from the accounts table and calculate various statistics of the last 10 transactions
        String query = "SELECT T.Date, T.Type, T.Amount, A.Balance, " +
                       "SUM(T.Amount) OVER () AS TotalAmount, " +
                       "MIN(T.Amount) OVER () AS MinAmount, " +
                       "MAX(T.Amount) OVER () AS MaxAmount, " +
                       "AVG(T.Amount) OVER () AS AvgAmount " +
                       "FROM (SELECT * FROM TRANSACTIONS WHERE AccountNumber = ? ORDER BY Date DESC LIMIT 10) AS T " +
                       "JOIN ACCOUNT A ON A.AccountNumber = T.AccountNumber"; // Join with accounts table to get balance
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(accountNumber));
            ResultSet rs = stmt.executeQuery();
        
            StringBuilder sb = new StringBuilder();
            BigDecimal balance = BigDecimal.ZERO; // Variable to store balance
            BigDecimal totalAmount = BigDecimal.ZERO;
            BigDecimal minAmount = BigDecimal.ZERO;
            BigDecimal maxAmount = BigDecimal.ZERO;
            BigDecimal avgAmount = BigDecimal.ZERO;
            int rowCount = 0;
            while (rs.next()) {
                if (rowCount == 0) {
                    // These values are the same for all rows in the result set
                    balance = rs.getBigDecimal("Balance"); // Retrieve balance
                    totalAmount = rs.getBigDecimal("TotalAmount");
                    minAmount = rs.getBigDecimal("MinAmount");
                    maxAmount = rs.getBigDecimal("MaxAmount");
                    avgAmount = rs.getBigDecimal("AvgAmount");
                }
                sb.append("Date: ").append(rs.getDate("Date"))
                  .append(", Type: ").append(rs.getString("Type"))
                  .append(", Amount: ").append(rs.getBigDecimal("Amount"))
                  .append("\n");
                rowCount++;
            }
            // Add balance and summary information if we have any results
            if (rowCount > 0) {
                sb.append("\nBalance: ").append(balance) // Display balance
                  .append("\nTotal of last 10 transactions: ").append(totalAmount)
                  .append("\nMinimum transaction amount: ").append(minAmount)
                  .append("\nMaximum transaction amount: ").append(maxAmount)
                  .append("\nAverage transaction amount: ").append(avgAmount);
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Account Transactions Report", JOptionPane.INFORMATION_MESSAGE); // Update the dialog title to "Account Transactions Report"
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid account number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    
    private JPanel createCardPanel() {
        JPanel transferPanel = new JPanel();
        transferPanel.setLayout(new BoxLayout(transferPanel, BoxLayout.Y_AXIS));
        transferPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding
    
        JTextField employeeIdField = new JTextField();
        employeeIdField.setMaximumSize(new Dimension(150, 20)); // Thinner width
        JTextField oldBranchIdField = new JTextField();
        oldBranchIdField.setMaximumSize(new Dimension(150, 20)); // Thinner width
        JTextField newBranchIdField = new JTextField();
        newBranchIdField.setMaximumSize(new Dimension(150, 20)); // Thinner width
        JButton setButton = new JButton("Set");
        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cardType = employeeIdField.getText();
                String limit = oldBranchIdField.getText();
                String expiryDate = newBranchIdField.getText();
                updateCardDetails(userAccountNumber, cardType, limit,expiryDate);
            }
        });
    
    
        transferPanel.add(createLabeledField("Card Type", employeeIdField));
        transferPanel.add(createLabeledField("Limit", oldBranchIdField));
        transferPanel.add(createLabeledField("Expiry Date", newBranchIdField));
        transferPanel.add(Box.createVerticalStrut(10));
        transferPanel.add(createButtonPanel(setButton));
    
        return transferPanel;
    }
    private void updateCardDetails(String accountNumber, String cardType, String limit, String expiryDate) {
        // SQL query to insert or update the card details
        String query = "INSERT INTO CARD (AccountNumber, CardType, `Limit`, ExpiryDate) VALUES (?, ?, ?, ?) "
                       + "ON DUPLICATE KEY UPDATE CardType = ?, `Limit` = ?, ExpiryDate = ?";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
    
            // Set parameters for the INSERT part
            pstmt.setInt(1, Integer.parseInt(accountNumber));
            pstmt.setString(2, cardType);
            pstmt.setBigDecimal(3, new BigDecimal(limit));
            pstmt.setDate(4, java.sql.Date.valueOf(expiryDate)); // Convert the string to a SQL date
    
            // Set parameters for the ON DUPLICATE KEY UPDATE part
            pstmt.setString(5, cardType);
            pstmt.setBigDecimal(6, new BigDecimal(limit));
            pstmt.setDate(7, java.sql.Date.valueOf(expiryDate)); // Convert the string to a SQL date
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Card details updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "No changes were made.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for account number and limit.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Please enter the date in the format yyyy-mm-dd.");
        }
    }
    
    
   
    private JPanel createLabeledField(String labelText, JTextField textField) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        textField.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(textField);
        return panel;
    }

    private JPanel createButtonPanel(JButton button) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(button);
        return buttonPanel;
    }

   

// Run the application
public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            // Display the login frame from which the user home frame will be launched
            LoginFrame loginFrame = new LoginFrame(); // Assuming LoginFrame is your login window
            loginFrame.setVisible(true);
        }
    });
}
}