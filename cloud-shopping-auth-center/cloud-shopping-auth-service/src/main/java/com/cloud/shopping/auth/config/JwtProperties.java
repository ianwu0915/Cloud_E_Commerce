package com.cloud.shopping.auth.config;

import com.cloud.shopping.auth.utils.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;

@Data
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties {

    private String secret;

    private String pubKeyPath;

    private String priKeyPath;

    private int expire; // token expiration time

    private PublicKey publicKey;

    private PrivateKey privateKey;

    private  String cookieName;
    // Once the object is instantiated, read the public key and private key
    @PostConstruct
    public void init() throws Exception{
        // if the public and private keys do not exist, generate them first
        // File pubPath = new File(pubKeyPath);
        //  File priPath = new File(priKeyPath);

        // generate public and private keys
        RsaUtils.generateKey(pubKeyPath,priKeyPath,secret);

        // get public and private keys
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);

    }

}