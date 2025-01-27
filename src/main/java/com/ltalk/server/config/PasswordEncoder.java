package com.ltalk.server.config;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncoder {
   String salt = "Vm0wd2VFMUdXWGhWYmxKWFlUSlNXVmxyWkZOV1JteHlWYQ==";

   public String encode(String password) throws NoSuchAlgorithmException {
       MessageDigest md = MessageDigest.getInstance("SHA-256");
       md.update((password+salt).getBytes());
       byte[] encodePasswordByte = md.digest();
       return bytesToHex(encodePasswordByte);
   }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
