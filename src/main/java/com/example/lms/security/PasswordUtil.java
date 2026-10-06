package com.example.lms.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public final class PasswordUtil {
    public static final int ITERATIONS=120_000;
    public static final int KEY_LENGTH_BITS=256;
    private static final int SALT_BYTES=16;
    private PasswordUtil(){}

    public static PasswordData hash(char[] password) {
        byte[] salt=new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        byte[] hash=derive(password,salt);
        return new PasswordData(Base64.getEncoder().encodeToString(salt),
            Base64.getEncoder().encodeToString(hash));
    }

    public static boolean verify(char[] password,String saltBase64,String hashBase64) {
        byte[] salt=Base64.getDecoder().decode(saltBase64);
        byte[] expected=Base64.getDecoder().decode(hashBase64);
        return MessageDigest.isEqual(derive(password,salt),expected);
    }

    private static byte[] derive(char[] password,byte[] salt) {
        PBEKeySpec spec=new PBEKeySpec(password,salt,ITERATIONS,KEY_LENGTH_BITS);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch(Exception e) {
            throw new IllegalStateException("Unable to hash password.",e);
        } finally {
            spec.clearPassword();
        }
    }

    public record PasswordData(String saltBase64,String hashBase64){}
}
