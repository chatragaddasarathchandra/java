package lab_project;

import java.util.regex.Pattern;

public class PasswordStrengthValidator {

    // At least 8 chars, one uppercase, one digit, one symbol
    private static final Pattern UPPER = Pattern.compile(".*[A-Z].*");
    private static final Pattern DIGIT = Pattern.compile(".*\\d.*");
    private static final Pattern SYMBOL = Pattern.compile(".*[@#$%^&+=!].*");

    public static boolean isStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        if (!UPPER.matcher(password).matches()) {
            return false;
        }
        if (!DIGIT.matcher(password).matches()) {
            return false;
        }
        if (!SYMBOL.matcher(password).matches()) {
            return false;
        }
        return true;
    }
}
