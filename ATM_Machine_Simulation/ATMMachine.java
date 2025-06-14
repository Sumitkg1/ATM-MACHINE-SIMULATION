import java.util.*;
import java.text.SimpleDateFormat;

// Account class to represent a user account
class Account {     // Represents a bank account with basic details and transaction history     
    private String accountNumber;
    private String pin;
    private double balance;
    private String customerName;
    private List<String> transactionHistory; // List of transaction IDs

    public Account(String accountNumber, String pin, double balance, String customerName) { // Constructor to initialize account details
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
        this.customerName = customerName;
        this.transactionHistory = new ArrayList<>();
    }

    // Getters and Setters
    public String getAccountNumber() { // Returns the account number
        return accountNumber;
    }

    public String getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }
}

// ATM class to handle ATM operations
class ATM {
    private Map<String, Account> accounts; // Map to store accounts with account number as key, time complexityO(1) for access, O(n) for iteration
    // 
    private Account currentUser; // Currently logged-in user
    private Scanner scanner;
    private SimpleDateFormat dateFormat;    // Date format for transaction timestamps

    public ATM() {
        accounts = new HashMap<>();
        scanner = new Scanner(System.in);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        initializeAccounts();
    }

    private void initializeAccounts() { // Initialize some accounts for testing
        accounts.put("123456", new Account("123456", "1234", 1000.0, "John Doe"));
        accounts.put("654321", new Account("654321", "4321", 1500.0, "Jane Smith"));
        accounts.put("456789", new Account("456789", "4567", 2000.0, "John Adeja"));
        accounts.put("987654", new Account("987654", "9876", 2500.0, "Jane alex"));
    }

    public void start() { // Start the ATM simulation
        System.out.println("=========================================");
        System.out.println("Welcome to the ATM Machine Simulation System:"); 
        System.out.println("=========================================");
        System.out.println("Please follow the instructions to use the ATM.");
        System.out.println("=========================================");

        while (true) { // Main loop to keep the ATM running
            if (currentUser == null) {
                login();
            } else {
                if (!showMenu()) {
                    break;
                }
            }
        }
        scanner.close();
    }

    private void login() { // Handle user login
        System.out.println("=========================================");
        System.out.println("\nPlease enter your account number:");
        String accountNumber = scanner.nextLine();
        System.out.println("Please enter your PIN:");
        String pin = scanner.nextLine();
        Account account = accounts.get(accountNumber);

        if (account != null && account.getPin().equals(pin)) {
            currentUser = account;
            System.out.println("Login successful! Welcome " + currentUser.getCustomerName());
            // Record login transaction
            recordTransaction("User logged in");
        } else {
            System.out.println("Invalid account number or PIN. Please try again.");
        }
    }

    private boolean showMenu() { // Display the ATM menu and handle user choices
        System.out.println("=========================================");
        System.out.println("\nATM Menu: Please Select an Option:");
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. View Transaction History");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) { // Handle user choices based on input
            case 1:
                checkBalance();
                return true;
            case 2:
                depositMoney();
                return true;
            case 3:
                withdrawMoney();
                return true;
            case 4:
                viewTransactionHistory();
                return true;
            case 5:
                // Log out the user
                System.out.println("Thank you for using the ATM. Goodbye!");
                // Record logout transaction
                recordTransaction("User logged out");
                currentUser = null;
                return false;
            default:
                System.out.println("Invalid choice. Please try again.");    
                return true;
        }
    }

    private void checkBalance() { // Display the current balance of the user
        System.out.println("=========================================");
        System.out.println("Your current balance is: $" + currentUser.getBalance());
        recordTransaction("Checked balance - $" + currentUser.getBalance());
    }

    private void depositMoney() {   // Handle money deposit
        System.out.println("=========================================");
        System.out.println("Your current balance is: $" + currentUser.getBalance());
        System.out.print("Enter the amount to deposit: ");
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        if (amount > 0) {
            currentUser.setBalance(currentUser.getBalance() + amount);
            System.out.println("Deposit successful! New balance: $" + currentUser.getBalance());
            recordTransaction("Deposited $" + amount);
        } else {
            System.out.println("Invalid deposit amount.");
            recordTransaction("Failed deposit attempt - Invalid amount: $" + amount);
        }
    }

    private void withdrawMoney() { // Handle money withdrawal
        System.out.println("=========================================");
        System.out.println("Your current balance is: $" + currentUser.getBalance());
        System.out.print("Enter the amount to withdraw: ");
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount.");
            recordTransaction("Failed withdrawal attempt - Invalid amount: $" + amount);
        } else if (amount > currentUser.getBalance()) {
            System.out.println("Insufficient funds. Your current balance is: $" + currentUser.getBalance());
            recordTransaction("Failed withdrawal attempt - Insufficient funds for $" + amount);
        } else {
            currentUser.setBalance(currentUser.getBalance() - amount);
            System.out.println("$" + amount + " withdrawn successfully.");
            System.out.println("New balance: $" + currentUser.getBalance());
            recordTransaction("Withdrew $" + amount);
        }
    }

    private void viewTransactionHistory() { // Display the transaction history of the user
        System.out.println("=========================================");
        System.out.println("Transaction History:");
        System.out.println("\nTransaction History for " + currentUser.getCustomerName() + ":");
        System.out.println("Account Number: " + currentUser.getAccountNumber());
        System.out.println("=========================================");
        List<String> transactions = currentUser.getTransactionHistory();
        if (transactions.isEmpty()) {
            System.out.println("No transactions recorded.");
        } else {
            for (String transaction : transactions) {
                System.out.println(transaction);
            }
        }
    }

    private void recordTransaction(String action) { // Record a transaction with a timestamp

        String timestamp = dateFormat.format(new Date());
        // Create a transaction record with timestamp, user details, and action
        String transactionRecord = timestamp + " - " + currentUser.getCustomerName() + 
                                 " (" + currentUser.getAccountNumber() + "): " + action;
        currentUser.addTransaction(transactionRecord);
    }
}

// Main class to run the ATM simulation
public class ATMMachine {
    public static void main(String[] args) {
        ATM atm = new ATM(); // Create an instance of the ATM class
        atm.start();    // Start the ATM simulation
        System.exit(0); // Exit the program
    }
}