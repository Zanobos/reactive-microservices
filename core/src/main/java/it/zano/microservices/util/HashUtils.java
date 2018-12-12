package it.zano.microservices.util;

import it.zano.microservices.exception.MicroServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashUtils {

    private static Logger logger = LoggerFactory.getLogger(HashUtils.class);

    private HashUtils() {}

    private static MessageDigest initializeSha256() throws NoSuchAlgorithmException {
        String digestAlgorithm = "SHA-256";
        try {
            return MessageDigest.getInstance(digestAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Digest algorithm "+digestAlgorithm+" not found!", e);
            throw e;
        }
    }

    public static String hash(String hexString) throws MicroServiceException {
        byte[] bytes = HexUtils.fromHexString(hexString);
        byte[] hashedBytes = hash(bytes);
        return HexUtils.toHexString(hashedBytes);
    }

    public static byte[] hash(byte[] bytes) throws MicroServiceException {
        try {
            MessageDigest sha256 = initializeSha256();
            return sha256.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new MicroServiceException(e);
        }
    }

}
