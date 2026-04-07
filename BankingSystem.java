import java.io.*;
import java.util.Scanner;

// Custom Exception for CID range [cite: 40, 43]
class InvalidIDException extends Exception {
    public InvalidIDException(String message) { super(message); }
}

// Custom Exception for Minimum Balance [cite: 40, 41]
class MinimumBalanceException extends Exception {
    public MinimumBalanceException(String message) { super(message); }
}

// Custom Exception for Overdrawing [cite: 40, 42]
class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) { super(message); }
}

// Custom Exception for Negative Amounts [cite: 40, 44]
class InvalidAmountException extends Exception {
    public InvalidAmountException(String message) { super(message); }
}

public class BankingSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int cid = 0;
        String cname = "";
        double amount = 0;

        try {
            // 1. Get Customer ID with range check 
            System.out.print("Enter Customer ID (1-20): ");
            cid = sc.nextInt();
            if (cid < 1 || cid > 20) {
                throw new InvalidIDException("Error: CID must be between 1 and 20.");
            }

            // 2. Get Name
            System.out.print("Enter Customer Name: ");
            sc.nextLine(); // consume newline
            cname = sc.nextLine();

            // 3. Get Initial Deposit with min balance check 
            System.out.print("Enter Initial Deposit (Min Rs. 1000): ");
            amount = sc.nextDouble();
            if (amount < 1000) {
                throw new MinimumBalanceException("Error: Account must start with at least Rs. 1000.");
            }

            System.out.println("--- Account Created Successfully ---");

            // Menu for Transactions 
            boolean exit = false;
            while (!exit) {
                System.out.println("\n1. Deposit  2. Withdraw  3. Save & Exit");
                System.out.print("Choice: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1: // Deposit 
                        System.out.print("Enter deposit amount: ");
                        double dep = sc.nextDouble();
                        if (dep <= 0) throw new InvalidAmountException("Amount must be positive.");
                        amount += dep;
                        System.out.println("Balance updated: " + amount);
                        break;

                    case 2: // Withdraw 
                        System.out.print("Enter withdrawal amount: ");
                        double draw = sc.nextDouble();
                        if (draw <= 0) throw new InvalidAmountException("Amount must be positive.");
                        if (draw > amount) throw new InsufficientFundsException("Insufficient funds!");
                        amount -= draw;
                        System.out.println("Withdrawal successful. Remaining: " + amount);
                        break;

                    case 3: // Save to File 
                        saveToFile(cid, cname, amount);
                        exit = true;
                        break;
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            sc.close();
        }
    }

    // Using Character Streams (FileWriter) for storage 
    public static void saveToFile(int id, String name, double bal) {
        try (FileWriter fw = new FileWriter("BankRecords.txt", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            
            String record = "ID: " + id + " | Name: " + name + " | Balance: " + bal;
            bw.write(record);
            bw.newLine();
            System.out.println("Record saved to BankRecords.txt");
            
        } catch (IOException e) {
            System.out.println("File Error: " + e.getMessage());
        }
    }
} 

