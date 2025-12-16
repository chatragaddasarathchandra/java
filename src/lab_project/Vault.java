package lab_project;

import java.util.Map;

public abstract class Vault {
    public abstract void addEntry(String key, String username, String password) throws WeakPasswordException;
    public abstract PasswordEntry getEntry(String key);
    public abstract boolean deleteEntry(String key);
    public abstract Map<String, PasswordEntry> getAllEntries();
    public abstract void saveToFile();
    public abstract void loadFromFile();
}

