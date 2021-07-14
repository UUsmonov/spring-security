package com.example.springsecurity.helpers;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class Utils {

    public static String generateToken(){
        return String.format("%s-%s", UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }

    public static String hashSha3512WithGivenSalt(String given, String salt) {
        try {
//            String salt = "@D`!s*Ro-WAkD:mr@+3%T$";
            MessageDigest digest = MessageDigest.getInstance("SHA3-512");
            digest.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] encodedHash = digest.digest(given.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            return given;
        }
    }
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
