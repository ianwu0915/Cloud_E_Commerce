package com.cloud.shopping.cart.config;

import com.cloud.shopping.auth.utils.RsaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * JWT Configuration Properties
 * Loads and manages JWT-related configuration including RSA public key
 */
@Slf4j
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties {

    private String pubKeyPath;    // Path to RSA public key file
    private PublicKey publicKey;  // Loaded RSA public key
    private String cookieName;    // Name of cookie containing JWT token

    /**
     * Initialize JWT properties
     * Loads RSA public key from file system
     * Called automatically after property injection
     */
    @PostConstruct
    public void init() {
        try {
            // Load public key from file
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            log.error("Failed to initialize public key!", e);
            throw new RuntimeException();
        }
    }

    // Getters and setters
    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }
}