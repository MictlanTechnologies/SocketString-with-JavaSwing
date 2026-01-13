package app;

import java.security.SecureRandom;

/**
 * Genera texto estilo Base64 (solo para simular el "(enc): ...").
 * No es cifrado real.
 */
public final class RandomBase64 {

    private static final SecureRandom RND = new SecureRandom();
    private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    private RandomBase64() {}

    public static String fakeBase64(int length) {
        if (length < 4) length = 4;

        StringBuilder sb = new StringBuilder(length + 4);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET[RND.nextInt(ALPHABET.length)]);
        }

        // Termina con == como típico base64 (solo visual)
        if (!sb.toString().endsWith("==")) {
            sb.append("==");
        }
        return sb.toString();
    }
}
