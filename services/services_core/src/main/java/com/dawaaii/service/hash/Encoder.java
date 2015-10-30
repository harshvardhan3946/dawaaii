package com.dawaaii.service.hash;


import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encoder {
    private static final String PASSWORD_HASH_ALGORITHM = "SHA-512";
    private static final String PASSWORD_HASH_ENCODING = "UTF-8";

    public static String hash(String plainText, String passwordSalt) {
        if (plainText == null) return null;
        try {
            MessageDigest msgDigest = MessageDigest.getInstance(PASSWORD_HASH_ALGORITHM);
            msgDigest.update(passwordSalt.getBytes());
            byte rawByte[] = msgDigest.digest(plainText.getBytes(PASSWORD_HASH_ENCODING));
            byte[] encodedBytes = Base64.encodeBase64(rawByte);

            return base64ToBase62(new String(encodedBytes));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            //consume this exception-caller does not need to know of the implementation for encoding and algo
        }
        return null;
    }

    protected static String base64ToBase62(String base64) {
        StringBuffer buf = new StringBuffer(base64.length() * 2);

        for (int i = 0; i < base64.length(); i++) {
            char ch = base64.charAt(i);
            switch (ch) {
                case 'i':
                    buf.append("ii");
                    break;

                case '+':
                    buf.append("ip");
                    break;

                case '/':
                    buf.append("is");
                    break;

                case '=':
                    buf.append("ie");
                    break;

                case '\n':
                    // Strip out
                    break;

                default:
                    buf.append(ch);
            }
        }
        return buf.toString();
    }
}