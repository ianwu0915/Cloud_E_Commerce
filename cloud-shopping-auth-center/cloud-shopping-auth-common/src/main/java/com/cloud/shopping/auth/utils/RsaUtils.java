package com.cloud.shopping.auth.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Utility class for RSA key operations.
 * Provides methods to generate, retrieve, and read RSA public and private keys.
 *
 */
public class RsaUtils {

    /**
     * Reads the public key from a file.
     *
     * @param filename the path of the public key file, relative to the classpath
     * @return the PublicKey object
     * @throws Exception if an error occurs while reading the key
     */
    public static PublicKey getPublicKey(String filename) throws Exception {
        byte[] bytes = readFile(filename);
        return getPublicKey(bytes);
    }

    /**
     * Reads the private key from a file.
     *
     * @param filename the path of the private key file, relative to the classpath
     * @return the PrivateKey object
     * @throws Exception if an error occurs while reading the key
     */
    public static PrivateKey getPrivateKey(String filename) throws Exception {
        byte[] bytes = readFile(filename);
        return getPrivateKey(bytes);
    }

    /**
     * Converts a byte array into a PublicKey object.
     *
     * @param bytes the byte array representing the public key
     * @return the PublicKey object
     * @throws Exception if an error occurs while converting the byte array
     */
    public static PublicKey getPublicKey(byte[] bytes) throws Exception {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }

    /**
     * Converts a byte array into a PrivateKey object.
     *
     * @param bytes the byte array representing the private key
     * @return the PrivateKey object
     * @throws Exception if an error occurs while converting the byte array
     */
    public static PrivateKey getPrivateKey(byte[] bytes) throws Exception {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePrivate(spec);
    }

    /**
     * Generates an RSA key pair (public and private keys) and writes them to specified files.
     *
     * @param publicKeyFilename  the file path to save the public key
     * @param privateKeyFilename the file path to save the private key
     * @param secret             the secret string used to generate the keys
     * @throws Exception if an error occurs during key generation or writing to files
     */
    public static void generateKey(String publicKeyFilename, String privateKeyFilename, String secret) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRandom = new SecureRandom(secret.getBytes());
        keyPairGenerator.initialize(1024, secureRandom);
        KeyPair keyPair = keyPairGenerator.genKeyPair();

        // Get public key bytes and write to file
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        writeFile(publicKeyFilename, publicKeyBytes);

        // Get private key bytes and write to file
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        writeFile(privateKeyFilename, privateKeyBytes);
    }

    /**
     * Reads all bytes from a file.
     *
     * @param fileName the file path to read
     * @return a byte array of the file's content
     * @throws Exception if an error occurs while reading the file
     */
    private static byte[] readFile(String fileName) throws Exception {
        return Files.readAllBytes(new File(fileName).toPath());
    }

    /**
     * Writes a byte array to a specified file.
     *
     * @param destPath the file path to write to
     * @param bytes    the byte array to write
     * @throws IOException if an error occurs while writing to the file
     */
    private static void writeFile(String destPath, byte[] bytes) throws IOException {
        File dest = new File(destPath);
        if (!dest.exists()) {
            dest.createNewFile();
        }
        Files.write(dest.toPath(), bytes);
    }
}
