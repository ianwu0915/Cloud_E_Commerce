package com.cloud.shopping.auth.utils;

import com.cloud.shopping.auth.entity.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Utility class for JWT (JSON Web Token) operations including token generation and parsing.
 * Supports both PrivateKey/PublicKey objects and byte arrays for RSA-based token signing
 * and verification.
 */
public class JwtUtils {
    /**
     * Generates a JWT token signed with an RSA private key.
     *
     * @param userInfo User information to be embedded in token claims
     * @param privateKey RSA private key for signing the token
     * @param expireMinutes Token expiration time in minutes
     * @return Signed JWT token string
     * @throws Exception If token generation fails
     */
    public static String generateToken(UserInfo userInfo, PrivateKey privateKey, int expireMinutes) throws Exception {
        return Jwts.builder()
                .claim(JwtConstants.JWT_KEY_ID, userInfo.getId())
                .claim(JwtConstants.JWT_KEY_USER_NAME, userInfo.getUsername())
                .setExpiration(DateTime.now().plusDays(expireMinutes).toDate())
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    /**
     * Generates a JWT token using a private key provided as a byte array.
     *
     * @param userInfo User information to be embedded in token claims
     * @param privateKey RSA private key as byte array
     * @param expireMinutes Token expiration time in minutes
     * @return Signed JWT token string
     * @throws Exception If token generation or key conversion fails
     */
    public static String generateToken(UserInfo userInfo, byte[] privateKey, int expireMinutes) throws Exception {
        return Jwts.builder()
                .claim(JwtConstants.JWT_KEY_ID, userInfo.getId())
                .claim(JwtConstants.JWT_KEY_USER_NAME, userInfo.getUsername())
                .setExpiration(DateTime.now().plusMinutes(expireMinutes).toDate())
                .signWith(SignatureAlgorithm.RS256, RsaUtils.getPrivateKey(privateKey))
                .compact();
    }

    /**
     * Parses and validates a JWT token using an RSA public key.
     *
     * @param token JWT token string to parse
     * @param publicKey RSA public key for verification
     * @return Parsed JWT claims
     * @throws Exception If token is invalid or verification fails
     */
    private static Jws<Claims> parserToken(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    /**
     * Parses and validates a JWT token using a public key provided as a byte array.
     *
     * @param token JWT token string to parse
     * @param publicKey RSA public key as byte array
     * @return Parsed JWT claims
     * @throws Exception If token is invalid or key conversion fails
     */
    private static Jws<Claims> parserToken(String token, byte[] publicKey) throws Exception {
        return Jwts.parser().setSigningKey(RsaUtils.getPublicKey(publicKey))
                .parseClaimsJws(token);
    }

    /**
     * Extracts user information from a JWT token using an RSA public key.
     *
     * @param token JWT token string
     * @param publicKey RSA public key for verification
     * @return UserInfo object containing extracted user details
     * @throws Exception If token parsing or verification fails
     */
    public static UserInfo getInfoFromToken(String token, PublicKey publicKey) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        return new UserInfo(
                ObjectUtils.toLong(body.get(JwtConstants.JWT_KEY_ID)),
                ObjectUtils.toString(body.get(JwtConstants.JWT_KEY_USER_NAME))
        );
    }

    /**
     * Extracts user information from a JWT token using a public key byte array.
     *
     * @param token JWT token string
     * @param publicKey RSA public key as byte array
     * @return UserInfo object containing extracted user details
     * @throws Exception If token parsing or verification fails
     */
    public static UserInfo getInfoFromToken(String token, byte[] publicKey) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        return new UserInfo(
                ObjectUtils.toLong(body.get(JwtConstants.JWT_KEY_ID)),
                ObjectUtils.toString(body.get(JwtConstants.JWT_KEY_USER_NAME))
        );
    }
}