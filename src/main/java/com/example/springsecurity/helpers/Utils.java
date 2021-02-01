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
            byte[] encodedhash = digest.digest(given.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            return given;
        }
    }
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
