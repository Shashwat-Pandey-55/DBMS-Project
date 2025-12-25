import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.math.*;

public class AdminHome extends JFrame {
    CardLayout cardLayout;
    JPanel cards; // Panel that uses CardLayout
    JButton salaryButton, interestButton, transferButton,fireButton, signOutButton;

    public AdminHome() {
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
        JLabel welcomeLabel = new JLabel("WELCOME ADMIN!", SwingConstants.LEFT);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        signOutButton = new JButton("Sign out");

        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        headerPanel.add(signOutButton, BorderLayout.EAST);

        // Buttons
        salaryButton = createButton("SALARY");
        interestButton = createButton("INTEREST");
        transferButton = createButton("TRANSFER");
        fireButton = createButton("FIRE");

        sideMenu.add(Box.createVerticalGlue());
        sideMenu.add(salaryButton);
        sideMenu.add(interestButton);
        sideMenu.add(transferButton);
        sideMenu.add(fireButton);
        sideMenu.add(Box.createVerticalGlue());

        // Panels
        JPanel salaryPanel = createSalaryPanel();
        JPanel interestPanel = createInterestPanel();
        JPanel transferPanel = createTransferPanel();
        JPanel firePanel = createFirePanel();


        cards.add(salaryPanel, "Salary");
        cards.add(interestPanel, "Interest");
        cards.add(transferPanel, "Transfer");
        cards.add(firePanel, "FIRE");

        // Add action listeners to buttons
        salaryButton.addActionListener(e -> cardLayout.show(cards, "Salary"));
        interestButton.addActionListener(e -> cardLayout.show(cards, "Interest"));
        transferButton.addActionListener(e -> cardLayout.show(cards, "Transfer"));
        fireButton.addActionListener(e -> cardLayout.show(cards, "FIRE"));
        
        
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
    private JPanel createInterestPanel() {
        JPanel interestPanel = new JPanel();
        interestPanel.setLayout(new BoxLayout(interestPanel, BoxLayout.Y_AXIS));
    
        JTextField interestRateField = new JTextField();
        interestRateField.setMaximumSize(new Dimension(200, 20)); // Width, Height
        JButton setButton = new JButton("Set");

        
    
        interestPanel.add(createLabeledField("Interest Rate", interestRateField));
        interestPanel.add(Box.createVerticalStrut(10));
        interestPanel.add(createButtonPanel(setButton));
        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newInterestRate = interestRateField.getText().trim();
                if (!newInterestRate.isEmpty()) {
                    setInterestRate(newInterestRate);
                } else {
                    JOptionPane.showMessageDialog(AdminHome.this, "Please enter a valid interest rate.");
                }
            }
        });
    
        return interestPanel;
    }
    
    private JPanel createFirePanel() {
        JPanel firePanel = new JPanel();
        firePanel.setLayout(new BoxLayout(firePanel, BoxLayout.Y_AXIS));
    
        JTextField interestRateField = new JTextField();
        interestRateField.setMaximumSize(new Dimension(200, 20)); // Width, Height
        JButton setButton = new JButton("FIRE");

        
    
        firePanel.add(createLabeledField("Employee ID", interestRateField));
        firePanel.add(Box.createVerticalStrut(10));
        firePanel.add(createButtonPanel(setButton));
        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String employeeId = interestRateField.getText().trim();
                if (!employeeId.isEmpty()) {
                    fireEmployee(employeeId);
                } else {
                    JOptionPane.showMessageDialog(AdminHome.this, "Please enter the employee ID.");
                }
            }
        });
    
    
        return firePanel;
    }
    
    private JPanel createTransferPanel() {
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
    
        transferPanel.add(createLabeledField("Employee ID", employeeIdField));
        transferPanel.add(createLabeledField("Old Branch ID", oldBranchIdField));
        transferPanel.add(createLabeledField("New Branch ID", newBranchIdField));
        transferPanel.add(Box.createVerticalStrut(10));
        transferPanel.add(createButtonPanel(setButton));
        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String employeeId = employeeIdField.getText().trim();
                String newBranchId = newBranchIdField.getText().trim();
                if (!employeeId.isEmpty() && !newBranchId.isEmpty()) {
                    transferEmployee(employeeId, newBranchId);
                } else {
                    JOptionPane.showMessageDialog(AdminHome.this, "Please enter both employee ID and new branch ID.");
                }
            }
        });
    
        return transferPanel;
    }


    private JPanel createSalaryPanel() {
        JPanel salaryPanel = new JPanel();
        salaryPanel.setLayout(new BoxLayout(salaryPanel, BoxLayout.Y_AXIS));
        salaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        JTextField employeeIdField = new JTextField();
        employeeIdField.setMaximumSize(new Dimension(200, 20)); // Make text field thinner
        JTextField salaryField = new JTextField();
        salaryField.setMaximumSize(new Dimension(200, 20)); // Make text field thinner
        JButton setButton = new JButton("Set");
        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String employeeId = employeeIdField.getText().trim();
                String salary = salaryField.getText().trim();
                if (!employeeId.isEmpty() && !salary.isEmpty()) {
                    setSalary(employeeId, salary);
                } else {
                    JOptionPane.showMessageDialog(AdminHome.this, "Please enter both employee ID and salary.");
                }
            }
        });
        


        salaryPanel.add(createLabeledField("Employee ID", employeeIdField));
        salaryPanel.add(createLabeledField("Salary", salaryField));
        salaryPanel.add(Box.createVerticalStrut(10));
        salaryPanel.add(createButtonPanel(setButton));

        return salaryPanel;
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
    private void fireEmployee(String employeeId) {
        String deleteSalaryQuery = "DELETE FROM SALARY WHERE EmployeeID = ?";
        String deleteEmployeeQuery = "DELETE FROM EMPLOYEE WHERE EmployeeID = ?";
        Connection conn= null; 
        
        try {conn = DatabaseHelper.getConnection();
             PreparedStatement pstmtSalary = conn.prepareStatement(deleteSalaryQuery);
             PreparedStatement pstmtEmployee = conn.prepareStatement(deleteEmployeeQuery); {
    
            // Start a transaction
            conn.setAutoCommit(false);
    
            // Delete salary records for the employee
            pstmtSalary.setInt(1, Integer.parseInt(employeeId));
            pstmtSalary.executeUpdate();
    
            // Now, delete the employee record
            pstmtEmployee.setInt(1, Integer.parseInt(employeeId));
            int affectedRows = pstmtEmployee.executeUpdate();
            if (affectedRows > 0) {
                // Commit the transaction
                conn.commit();
                JOptionPane.showMessageDialog(this, "Employee fired successfully and salary records removed.");
            } else {
                // Rollback the transaction
                conn.rollback();
                JOptionPane.showMessageDialog(this, "Employee ID does not exist or could not be fired.");
            }
       } } catch (SQLException ex) {
            // Handle SQL exceptions, possibly rolling back if necessary
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    JOptionPane.showMessageDialog(this, "Rollback failed: " + rollbackEx.getMessage());
                }
            }
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Employee ID must be a number.");
        } finally {
            // Reset auto-commit to true if needed
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void setSalary(String employeeId, String salary) {
        String query = "INSERT INTO SALARY (EmployeeID, Amount) VALUES (?, ?) ON DUPLICATE KEY UPDATE Amount = VALUES(Amount)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, employeeId);
            pstmt.setBigDecimal(2, new BigDecimal(salary));
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Salary entry updated successfully for employee ID: " + employeeId);
            } else {
                JOptionPane.showMessageDialog(this, "No changes made.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid salary format. Please enter a valid number for the salary.");
            ex.printStackTrace();
        }
    }
    
    

    private void setInterestRate(String newInterestRate) {
        String query = "UPDATE ACCOUNT SET InterestOnSavings = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setBigDecimal(1, new BigDecimal(newInterestRate));
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Interest rate updated successfully for " + affectedRows + " accounts.");
            } else {
                JOptionPane.showMessageDialog(this, "No changes made. Please check the input.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please ensure the interest rate is a valid number.");
            ex.printStackTrace();
        }
    }
    private void transferEmployee(String employeeId, String newBranchId) {
        String query = "UPDATE EMPLOYEE SET BranchID = ? WHERE EmployeeID = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newBranchId);
            pstmt.setString(2, employeeId);
    
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Employee branch updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "No changes made. Please check the employee ID and branch ID.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    

// Run the application
public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        AdminHome app = new AdminHome();
        app.setVisible(true);
    });
}}
