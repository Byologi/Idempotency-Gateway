package com.idempotency_gateway.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PayloadHashUtil {

    public static String hash(String payload) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] encoded =
                    digest.digest(
                            payload.getBytes(
                                    StandardCharsets.UTF_8));

            StringBuilder builder =
                    new StringBuilder();

            for (byte b : encoded) {
                builder.append(
                        String.format("%02x", b));
            }

            return builder.toString();

        } catch (NoSuchAlgorithmException ex) {

            throw new RuntimeException(ex);
        }
    }
}