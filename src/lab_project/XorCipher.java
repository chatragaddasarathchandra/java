package lab_project;

import java.util.Base64;

public class XorCipher implements Cipher {
    private String salt; // simple key

    public XorCipher(String salt) {
        this.salt = salt;
    }

    @Override
    public String encrypt(String plainText) {
        if (plainText == null) return null;
        byte[] textBytes = plainText.getBytes();
        byte[] saltBytes = salt.getBytes();
        byte[] result = new byte[textBytes.length];

        for (int i = 0; i < textBytes.length; i++) {
            result[i] = (byte) (textBytes[i] ^ saltBytes[i % saltBytes.length]);
        }
        return Base64.getEncoder().encodeToString(result);
    }

    @Override
    public String decrypt(String cipherText) {
        if (cipherText == null) return null;
        byte[] encBytes = Base64.getDecoder().decode(cipherText);
        byte[] saltBytes = salt.getBytes();
        byte[] result = new byte[encBytes.length];

        for (int i = 0; i < encBytes.length; i++) {
            result[i] = (byte) (encBytes[i] ^ saltBytes[i % saltBytes.length]);
        }
        return new String(result);
    }
}

