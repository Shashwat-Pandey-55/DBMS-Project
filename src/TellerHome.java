import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class TellerHome extends JFrame {
    // Define your SQL connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1123581321@Fibonacci";

    CardLayout cardLayout;
    JPanel cards; // Panel that uses CardLayout
    JButton depowithButton, newAccountButton, notificationButton, lockerButton, loanApplicationButton, signOutButton;

    public TellerHome() {
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
        JLabel welcomeLabel = new JLabel("WELCOME TELLER!", SwingConstants.LEFT);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        signOutButton = new JButton("Sign out");

        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        headerPanel.add(signOutButton, BorderLayout.EAST);

        // Buttons
        depowithButton = createButton("Deposit/Withdrawal");
        newAccountButton = createButton("New Account");
        notificationButton = createButton("Notification");
        lockerButton = createButton("Locker");
        loanApplicationButton = createButton("Loan Application");

        sideMenu.add(Box.createVerticalGlue());
        sideMenu.add(depowithButton);
        sideMenu.add(newAccountButton);
        sideMenu.add(notificationButton);
        sideMenu.add(lockerButton);
        sideMenu.add(loanApplicationButton);
        sideMenu.add(Box.createVerticalGlue());

        // Panels
        JPanel depowithPanel = createDepowithPanel();
        JPanel newAccountPanel = createNewAccountPanel();
        JPanel notificationPanel = createNotificationPanel();
        JPanel lockerPanel = createLockerPanel();
        JPanel loanPanel = createLoanPanel();

        cards.add(depowithPanel, "DepositWithdrawal");
        cards.add(newAccountPanel, "NewAccount");
        cards.add(notificationPanel, "Notification");
        cards.add(lockerPanel, "Locker");
        cards.add(loanPanel, "Loan");

        // Add action listeners to buttons
        depowithButton.addActionListener(e -> cardLayout.show(cards, "DepositWithdrawal"));
        newAccountButton.addActionListener(e -> cardLayout.show(cards, "NewAccount"));
        notificationButton.addActionListener(e -> cardLayout.show(cards, "Notification"));
        lockerButton.addActionListener(e -> cardLayout.show(cards, "Locker"));
        loanApplicationButton.addActionListener(e -> cardLayout.show(cards, "Loan"));

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

    private JPanel createDepowithPanel() {
        JPanel depowithPanel = new JPanel();
        depowithPanel.setLayout(new BoxLayout(depowithPanel, BoxLayout.Y_AXIS));
        depowithPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        JTextField accountNumberField = new JTextField();
        accountNumberField.setMaximumSize(new Dimension(200, 20));
        JTextField amountField = new JTextField();
        amountField.setMaximumSize(new Dimension(200, 20));
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");

        depowithPanel.add(createLabeledField("Account Number", accountNumberField));
        depowithPanel.add(createLabeledField("Amount", amountField));
        depowithPanel.add(Box.createVerticalStrut(10));
        depowithPanel.add(createButtonPanel(depositButton, withdrawButton)); // Add both buttons to the same panel

        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String accountNumber = accountNumberField.getText();
                String amount = amountField.getText();

                if (!accountNumber.isEmpty() && !amount.isEmpty()) {
                    String depositQuery = "INSERT INTO TRANSACTIONS (Type, Amount, Date, AccountNumber) " +
                            "VALUES ('deposit', '" + amount + "', CURRENT_DATE(), '" + accountNumber + "')";

                    String updateBalanceQuery = "UPDATE ACCOUNT SET Balance = Balance + '" + amount +
                            "' WHERE AccountNumber = '" + accountNumber + "'";

                    performSQLQuery(depositQuery);
                    performSQLQuery(updateBalanceQuery);
                } else {
                    JOptionPane.showMessageDialog(TellerHome.this, "Please fill in all fields.");
                }
            }
        });

        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String accountNumber = accountNumberField.getText();
                String amount = amountField.getText();

                if (!accountNumber.isEmpty() && !amount.isEmpty()) {
                    String withdrawQuery = "INSERT INTO TRANSACTIONS (Type, Amount, Date, AccountNumber) " +
                            "VALUES ('withdrawal', '" + amount + "', CURRENT_DATE(), '" + accountNumber + "')";

                    String updateBalanceQuery = "UPDATE ACCOUNT SET Balance = Balance - '" + amount +
                            "' WHERE AccountNumber = '" + accountNumber + "'";

                    performSQLQuery(withdrawQuery);
                    performSQLQuery(updateBalanceQuery);
                } else {
                    JOptionPane.showMessageDialog(TellerHome.this, "Please fill in all fields.");
                }
            }
        });

        return depowithPanel;
    }

    private JPanel createNewAccountPanel() {
        JPanel newAccountPanel = new JPanel();
        newAccountPanel.setLayout(new BoxLayout(newAccountPanel, BoxLayout.Y_AXIS));
        newAccountPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        JTextField userIdField = new JTextField();
        userIdField.setMaximumSize(new Dimension(200, 20));
        JTextField branchIdField = new JTextField();
        branchIdField.setMaximumSize(new Dimension(200, 20));
        JTextField balanceField = new JTextField();
        balanceField.setMaximumSize(new Dimension(200, 20));
        JButton createAccountButton = new JButton("Create Account");

        newAccountPanel.add(createLabeledField("User ID", userIdField));
        newAccountPanel.add(createLabeledField("Branch ID", branchIdField));
        newAccountPanel.add(createLabeledField("Balance", balanceField));
        newAccountPanel.add(Box.createVerticalStrut(10));
        newAccountPanel.add(createButtonPanel(createAccountButton));

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = userIdField.getText();
                String branchId = branchIdField.getText();
                String balance = balanceField.getText();

                if (!userId.isEmpty() && !branchId.isEmpty() && !balance.isEmpty()) {
                    String createAccountQuery = "INSERT INTO ACCOUNT (UserID, BranchID, Balance) " +
                            "VALUES ('" + userId + "', '" + branchId + "', '" + balance + "')";

                    performSQLQuery(createAccountQuery);
                } else {
                    JOptionPane.showMessageDialog(TellerHome.this, "Please fill in all fields.");
                }
            }
        });

        return newAccountPanel;
    }

    private JPanel createNotificationPanel() {
        JPanel notificationPanel = new JPanel();
        notificationPanel.setLayout(new BoxLayout(notificationPanel, BoxLayout.Y_AXIS));
        notificationPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        JTextField userIdField = new JTextField();
        userIdField.setMaximumSize(new Dimension(200, 20));
        JTextField messageField = new JTextField();
        messageField.setMaximumSize(new Dimension(200, 20));
        JButton sendNotificationButton = new JButton("Send Notification");

        notificationPanel.add(createLabeledField("User ID", userIdField));
        notificationPanel.add(createLabeledField("Message", messageField));
        notificationPanel.add(Box.createVerticalStrut(10));
        notificationPanel.add(createButtonPanel(sendNotificationButton));

        sendNotificationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = userIdField.getText();
                String message = messageField.getText();

                if (!userId.isEmpty() && !message.isEmpty()) {
                    String sendNotificationQuery = "INSERT INTO NOTIFICATIONS (UserID, Message) " +
                            "VALUES ('" + userId + "', '" + message + "')";

                    performSQLQuery(sendNotificationQuery);
                } else {
                    JOptionPane.showMessageDialog(TellerHome.this, "Please fill in all fields.");
                }
            }
        });

        return notificationPanel;
    }

    private JPanel createLockerPanel() {
        JPanel lockerPanel = new JPanel();
        lockerPanel.setLayout(new BoxLayout(lockerPanel, BoxLayout.Y_AXIS));
        lockerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        JTextField userIdField = new JTextField();
        userIdField.setMaximumSize(new Dimension(200, 20));
        JTextField branchIdField = new JTextField();
        branchIdField.setMaximumSize(new Dimension(200, 20));
        JButton assignLockerButton = new JButton("Assign Locker");

        lockerPanel.add(createLabeledField("User ID", userIdField));
        lockerPanel.add(createLabeledField("Branch ID", branchIdField));
        lockerPanel.add(Box.createVerticalStrut(10));
        lockerPanel.add(createButtonPanel(assignLockerButton));

        assignLockerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = userIdField.getText();
                String branchId = branchIdField.getText();

                if (!userId.isEmpty() && !branchId.isEmpty()) {
                    String assignLockerQuery = "INSERT INTO LOCKER (UserID, BranchID) " +
                            "VALUES ('" + userId + "', '" + branchId + "')";

                    performSQLQuery(assignLockerQuery);
                } else {
                    JOptionPane.showMessageDialog(TellerHome.this, "Please fill in all fields.");
                }
            }
        });

        return lockerPanel;
    }

    private JPanel createLoanPanel() {
        JPanel loanPanel = new JPanel();
        loanPanel.setLayout(new BoxLayout(loanPanel, BoxLayout.Y_AXIS));
        loanPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        JTextField accountNumberField = new JTextField();
        accountNumberField.setMaximumSize(new Dimension(200, 20));
        JTextField amountField = new JTextField();
        amountField.setMaximumSize(new Dimension(200, 20));
        JTextField startDateField = new JTextField();
        startDateField.setMaximumSize(new Dimension(200, 20));
        JTextField endDateField = new JTextField();
        endDateField.setMaximumSize(new Dimension(200, 20));
        JTextField loanInterestField = new JTextField();
        loanInterestField.setMaximumSize(new Dimension(200, 20));
        JButton applyLoanButton = new JButton("Apply for Loan");

        loanPanel.add(createLabeledField("Account Number", accountNumberField));
        loanPanel.add(createLabeledField("Amount", amountField));
        loanPanel.add(createLabeledField("Start Date", startDateField));
        loanPanel.add(createLabeledField("End Date", endDateField));
        loanPanel.add(createLabeledField("Loan Interest", loanInterestField));
        loanPanel.add(Box.createVerticalStrut(10));
        loanPanel.add(createButtonPanel(applyLoanButton));

        applyLoanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String accountNumber = accountNumberField.getText();
                String amount = amountField.getText();
                String startDate = startDateField.getText();
                String endDate = endDateField.getText();
                String loanInterest = loanInterestField.getText();

                if (!accountNumber.isEmpty() && !amount.isEmpty() && !startDate.isEmpty()
                        && !endDate.isEmpty() && !loanInterest.isEmpty()) {
                    String applyLoanQuery = "INSERT INTO LOAN (AccountNumber, Amount, StartDate, EndDate, LoanInterest) " +
                            "VALUES ('" + accountNumber + "', '" + amount + "', '" + startDate + "', '" + endDate + "', '" + loanInterest + "')";

                    performSQLQuery(applyLoanQuery);
                } else {
                    JOptionPane.showMessageDialog(TellerHome.this, "Please fill in all fields.");
                }
            }
        });

        return loanPanel;
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

    private JPanel createButtonPanel(JButton... buttons) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0)); // Added horizontal gap between buttons
        for (JButton button : buttons) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonPanel.add(button);
        }
        return buttonPanel;
    }

    private void performSQLQuery(String query) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            JOptionPane.showMessageDialog(this, "Query executed successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error executing SQL query: " + e.getMessage(),
                    "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Load the MySQL JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                TellerHome app = new TellerHome();
                app.setVisible(true);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }
}