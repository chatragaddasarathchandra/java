package lab_project;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileVault extends Vault {

    private Map<String, PasswordEntry> entries = new HashMap<>();
    private final String vaultFileName = "vault.dat";
    private Cipher cipher;

    public FileVault(Cipher cipher) {
        this.cipher = cipher;
        loadFromFile();
    }

    @Override
    public void addEntry(String key, String username, String password) throws WeakPasswordException {
        if (!PasswordStrengthValidator.isStrong(password)) {
            throw new WeakPasswordException("Password is too weak! Use uppercase, digit, and symbol.");
        }
        String encrypted = cipher.encrypt(password);
        PasswordEntry entry = new PasswordEntry(key, username, encrypted);
        entries.put(key, entry);
        AuditLogger.log("Added/Updated entry for key: " + key);
    }

    @Override
    public PasswordEntry getEntry(String key) {
        PasswordEntry entry = entries.get(key);
        if (entry != null) {
            AuditLogger.log("Retrieved entry for key: " + key);
        } else {
            AuditLogger.log("Failed to retrieve (not found) for key: " + key);
        }
        return entry;
    }

    @Override
    public boolean deleteEntry(String key) {
        if (entries.remove(key) != null) {
            AuditLogger.log("Deleted entry for key: " + key);
            return true;
        } else {
            AuditLogger.log("Delete failed (not found) for key: " + key);
            return false;
        }
    }

    @Override
    public Map<String, PasswordEntry> getAllEntries() {
        return entries;
    }

    @Override
    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(vaultFileName))) {
            oos.writeObject(entries);
            AuditLogger.log("Saved vault to file: " + vaultFileName);
        } catch (IOException e) {
            System.out.println("Error saving vault: " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        File file = new File(vaultFileName);
        if (!file.exists()) {
            // first time, nothing to load
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(vaultFileName))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                entries = (Map<String, PasswordEntry>) obj;
            }
            AuditLogger.log("Loaded vault from file: " + vaultFileName);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading vault: " + e.getMessage());
        }
    }

    // Export decrypted passwords to text file
    public void exportToFile(String exportFileName) {
        try (PrintWriter out = new PrintWriter(new FileWriter(exportFileName))) {
            for (PasswordEntry entry : entries.values()) {
                String decrypted = cipher.decrypt(entry.getEncryptedPassword());
                out.println("Service: " + entry.getServiceName());
                out.println("Username: " + entry.getUsername());
                out.println("Password: " + decrypted);
                out.println("------------------------------");
            }
            AuditLogger.log("Exported entries to: " + exportFileName);
            System.out.println("Exported to " + exportFileName);
        } catch (IOException e) {
            System.out.println("Error exporting: " + e.getMessage());
        }
    }

    public String decryptPassword(PasswordEntry entry) {
        return cipher.decrypt(entry.getEncryptedPassword());
    }
}

