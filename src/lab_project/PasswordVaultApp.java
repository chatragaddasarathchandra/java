package lab_project;

import java.util.Map;
import java.util.Scanner;

public class PasswordVaultApp {

    private static final String MASTER_PASSWORD = "admin123"; 

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("==== Simple Password Vault (Local Encrypted) ====");

        try {
            authenticate(scanner);
        } catch (AuthFailedException e) {
            System.out.println("Authentication failed: " + e.getMessage());
            scanner.close();
            return;
        }
        Cipher cipher = new XorCipher("mysalt123");
        FileVault vault = new FileVault(cipher);

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addPasswordFlow(scanner, vault);
                    break;
                case "2":
                    retrievePasswordFlow(scanner, vault);
                    break;
                case "3":
                    deletePasswordFlow(scanner, vault);
                    break;
                case "4":
                    listAllEntriesFlow(vault);
                    break;
                case "5":
                    exportFlow(vault);
                    break;
                case "0":
                    running = false;
                    vault.saveToFile();
                    System.out.println("Exiting... Vault saved.");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        scanner.close();
    }

    private static void authenticate(Scanner scanner) throws AuthFailedException {
        System.out.print("Enter master password: ");
        String input = scanner.nextLine();
        if (!MASTER_PASSWORD.equals(input)) {
            AuditLogger.log("Authentication failed.");
            throw new AuthFailedException("Wrong master password.");
        }
        AuditLogger.log("Authentication successful.");
        System.out.println("Login successful.\n");
    }

    private static void printMenu() {
        System.out.println("\n----- MENU -----");
        System.out.println("1. Add / Update Password");
        System.out.println("2. Retrieve Password");
        System.out.println("3. Delete Password");
        System.out.println("4. List All Entries");
        System.out.println("5. Export Entries");
        System.out.println("0. Exit");
        System.out.println("----------------");
    }

    private static void addPasswordFlow(Scanner scanner, FileVault vault) {
        try {
            System.out.print("Enter service name (key, e.g., Gmail): ");
            String key = scanner.nextLine().trim();

            System.out.print("Enter username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();

            vault.addEntry(key, username, password);
            System.out.println("Password saved successfully.");
        } catch (WeakPasswordException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void retrievePasswordFlow(Scanner scanner, FileVault vault) {
        System.out.print("Enter service name to retrieve: ");
        String key = scanner.nextLine().trim();

        PasswordEntry entry = vault.getEntry(key);
        if (entry == null) {
            System.out.println("No entry found for key: " + key);
        } else {
            String decrypted = vault.decryptPassword(entry);
            System.out.println("Service: " + entry.getServiceName());
            System.out.println("Username: " + entry.getUsername());
            System.out.println("Password: " + decrypted);
        }
    }

    private static void deletePasswordFlow(Scanner scanner, FileVault vault) {
        System.out.print("Enter service name to delete: ");
        String key = scanner.nextLine().trim();

        boolean success = vault.deleteEntry(key);
        if (success) {
            System.out.println("Entry deleted.");
        } else {
            System.out.println("No entry found for key: " + key);
        }
    }

    private static void listAllEntriesFlow(FileVault vault) {
        Map<String, PasswordEntry> map = vault.getAllEntries();
        if (map.isEmpty()) {
            System.out.println("No entries stored.");
            return;
        }
        System.out.println("\nStored entries:");
        for (PasswordEntry entry : map.values()) {
            System.out.println(entry.toString());
        }
    }

    private static void exportFlow(FileVault vault) {
        String exportFileName = "export_passwords.txt";
        vault.exportToFile(exportFileName);
    }
}

