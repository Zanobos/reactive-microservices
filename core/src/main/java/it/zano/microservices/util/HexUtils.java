package it.zano.microservices.util;

import org.springframework.security.crypto.codec.Hex;

public class HexUtils {

    public static String toHexString(byte[] bytes) {
        return new String(Hex.encode(bytes));
    }

    public static byte[] fromHexString(String string) {
        return Hex.decode(string.replaceAll("[^0123456789abcdefABCDEF]", ""));
    }

}
