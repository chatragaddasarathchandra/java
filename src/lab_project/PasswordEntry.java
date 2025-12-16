package lab_project;
import java.io.Serializable;

public class PasswordEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    private String serviceName;   // e.g., "Gmail"
    private String username;      // e.g., "hari@gmail.com"
    private String encryptedPassword; // password after encryption

    public PasswordEntry(String serviceName, String username, String encryptedPassword) {
        this.serviceName = serviceName;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getUsername() {
        return username;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    @Override
    public String toString() {
        return "Service: " + serviceName + ", Username: " + username;
    }
}



