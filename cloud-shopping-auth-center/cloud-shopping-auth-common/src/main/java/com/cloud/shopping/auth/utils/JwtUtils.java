package com.cloud.shopping.auth.utils;

import com.cloud.shopping.auth.entity.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.joda.time.DateTime;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Utility class for JWT (JSON Web Token) operations including token generation and parsing.
 */
public class JwtUtils {

    /**
     * Generates a JWT token signed with an RSA private key.
     */
    public static String generateToken(UserInfo userInfo, PrivateKey privateKey, int expireMinutes) {
        return Jwts.builder()
                .claim(JwtConstants.JWT_KEY_ID, userInfo.getId())
                .claim(JwtConstants.JWT_KEY_USER_NAME, userInfo.getUsername())
                .setExpiration(DateTime.now().plusMinutes(expireMinutes).toDate())
                .signWith(privateKey, SignatureAlgorithm.RS256) // Updated signWith method
                .compact();
    }

    /**
     * Generates a JWT token using a private key provided as a byte array.
     */
    public static String generateToken(UserInfo userInfo, byte[] privateKey, int expireMinutes) throws Exception {
        return Jwts.builder()
                .claim(JwtConstants.JWT_KEY_ID, userInfo.getId())
                .claim(JwtConstants.JWT_KEY_USER_NAME, userInfo.getUsername())
                .setExpiration(DateTime.now().plusMinutes(expireMinutes).toDate())
                .signWith(RsaUtils.getPrivateKey(privateKey), SignatureAlgorithm.RS256) // Updated signWith method
                .compact();
    }

    /**
     * Parses and validates a JWT token using an RSA public key.
     */
    private static Jws<Claims> parserToken(String token, PublicKey publicKey) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build() // Required in JJWT 0.11.x
                .parseClaimsJws(token);
    }

    /**
     * Parses and validates a JWT token using a public key provided as a byte array.
     */
    private static Jws<Claims> parserToken(String token, byte[] publicKey) throws Exception {
        return Jwts.parserBuilder()
                .setSigningKey(RsaUtils.getPublicKey(publicKey))
                .build() // Required in JJWT 0.11.x
                .parseClaimsJws(token);
    }

    /**
     * Extracts user information from a JWT token using an RSA public key.
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
