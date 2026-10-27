
import java.sql.*;
import java.util.Scanner;

public class Main {

    static final String URL = "jdbc:mysql://localhost:3306/bankdb";
    static final String USER = "root";
    static final String PASSWORD = "root";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    URL, USER, PASSWORD);

            while (true) {
                System.out.println("\n--- BANK MANAGEMENT ---");
                System.out.println("1. Create Account");
                System.out.println("2. Deposit Money");
                System.out.println("3. Withdraw Money");
                System.out.println("4. Display Balance");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");

                int choice = sc.nextInt();

                switch (choice) {

                    case 1: {
                        System.out.print("Enter account number: ");
                        int acc = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter customer name: ");
                        String name = sc.nextLine();

                        System.out.print("Enter initial balance: ");
                        double balance = sc.nextDouble();

                        if (balance < 0) {
                            System.out.println("Invalid balance");
                            break;
                        }

                        PreparedStatement ps = con.prepareStatement(
                                "INSERT INTO accounts VALUES (?, ?, ?)");

                        ps.setInt(1, acc);
                        ps.setString(2, name);
                        ps.setDouble(3, balance);

                        ps.executeUpdate();
                        System.out.println("Account created successfully!");
                        break;
                    }

                    case 2: {
                        System.out.print("Enter account number: ");
                        int acc = sc.nextInt();

                        System.out.print("Enter deposit amount: ");
                        double amount = sc.nextDouble();

                        if (amount <= 0) {
                            System.out.println("Invalid amount");
                            break;
                        }

                        PreparedStatement ps = con.prepareStatement(
                                "UPDATE accounts SET balance = balance + ? " +
                                        "WHERE account_no = ?");

                        ps.setDouble(1, amount);
                        ps.setInt(2, acc);

                        if (ps.executeUpdate() > 0)
                            System.out.println("Deposit successful!");
                        else
                            System.out.println("Account not found");
                        break;
                    }

                    case 3: {
                        System.out.print("Enter account number: ");
                        int acc = sc.nextInt();

                        System.out.print("Enter withdrawal amount: ");
                        double amount = sc.nextDouble();

                        if (amount <= 0) {
                            System.out.println("Invalid amount");
                            break;
                        }

                        PreparedStatement ps = con.prepareStatement(
                                "UPDATE accounts SET balance = balance - ? " +
                                        "WHERE account_no = ? AND balance >= ?");

                        ps.setDouble(1, amount);
                        ps.setInt(2, acc);
                        ps.setDouble(3, amount);

                        if (ps.executeUpdate() > 0)
                            System.out.println("Withdrawal successful!");
                        else
                            System.out.println(
                                    "Account not found or insufficient balance");
                        break;
                    }

                    case 4: {
                        System.out.print("Enter account number: ");
                        int acc = sc.nextInt();

                        PreparedStatement ps = con.prepareStatement(
                                "SELECT * FROM accounts WHERE account_no = ?");

                        ps.setInt(1, acc);
                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {
                            System.out.println("Account Number: "
                                    + rs.getInt("account_no"));
                            System.out.println("Customer Name: "
                                    + rs.getString("customer_name"));
                            System.out.println("Balance: "
                                    + rs.getDouble("balance"));
                        } else {
                            System.out.println("Account not found");
                        }
                        break;
                    }

                    case 5:
                        con.close();
                        sc.close();
                        System.out.println("Thank you!");
                        return;

                    default:
                        System.out.println("Invalid choice");
                }

            }

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC driver not found");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
