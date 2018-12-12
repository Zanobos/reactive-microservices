package it.zano.microservices.util;

import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public final class RsaUtils {

    private RsaUtils() {}

    public static RsaVerifier createRsaVerifier(String hexModulus, String hexExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {

        //Get modulus and exponent of the public key
        BigInteger modulus = new BigInteger(hexModulus, 16);
        BigInteger exponent = new BigInteger(hexExponent, 16);

        //Create the public key object
        RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory rsa = KeyFactory.getInstance("RSA");
        RSAPublicKey rsaPublicKey = (RSAPublicKey) rsa.generatePublic(spec);

        return new RsaVerifier(rsaPublicKey);
    }

    public static RsaSigner createRsaSigner(String hexModulus, String hexExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {

        //Get modulus and exponent of the public key
        BigInteger modulus = new BigInteger(hexModulus, 16);
        BigInteger exponent = new BigInteger(hexExponent, 16);

        //Create the public key object
        RSAPrivateKeySpec spec = new RSAPrivateKeySpec(modulus, exponent);
        KeyFactory rsa = KeyFactory.getInstance("RSA");
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) rsa.generatePrivate(spec);

        return new RsaSigner(rsaPrivateKey);
    }

}
