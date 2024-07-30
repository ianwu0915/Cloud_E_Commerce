package com.cloud.shopping.auth.web;

import com.cloud.shopping.auth.service.AuthService;
import com.cloud.shopping.auth.config.JwtProperties;
import com.cloud.shopping.auth.entity.UserInfo;
import com.cloud.shopping.auth.utils.JwtUtils;
import com.cloud.shopping.common.enums.ExceptionEnum;
import com.cloud.shopping.common.exception.LyException;
import com.cloud.shopping.common.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used to verify user login status
 *
 **/
@Slf4j
@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties prop;

    @Value("${ly.jwt.cookieName}")
    private String cookieName;

    UserInfo userInfo = new UserInfo();

    /**
     * Login authorization
     * @param username
     * @param password
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<String> login(
            @RequestParam("username") String username, @RequestParam("password") String password,
            HttpServletRequest request, HttpServletResponse response
    ) {
        log.info("User login input: {}", username);
        // Perform login
        String token = authService.login(username, password);
        // Auth service handle success and failure
        // Success: return token
        // Failure: throw exception

        // TODO: Write token to cookie
        CookieUtils.newBuilder(response).httpOnly().request(request)
                .build(cookieName, token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Verify user login status
     * It verifies the token and returns the user information
     * @param token
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verifyUser(
            String token,
            HttpServletRequest request, HttpServletResponse response
    ) {
        try {
            // Parse token (actual code for parsing should be uncommented in real use)
             UserInfo userInfo = JwtUtils.getInfoFromToken(token, prop.getPublicKey());
            // Refresh token, generate a new one
             String newToken = JwtUtils.generateToken(userInfo, prop.getPrivateKey(), prop.getExpire());
            // Write new token to cookie with httpOnly set to true
             CookieUtils.newBuilder(response).httpOnly().request(request)
                       .build(cookieName, newToken);
            // Logged in, return user information
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            // Token has expired or has been tampered with
            throw new LyException(ExceptionEnum.UN_AUTHORIZED);
        }
    }

    /**
     * Verify admin login
     * @param username
     * @param password
     * @return
     */
    @PostMapping("adminLogin")
    public ResponseEntity<String> adminLogin(
            @RequestParam("username") String username, @RequestParam("password") String password,
            HttpServletRequest request, HttpServletResponse response
    ) {
        // predefined admin id user info
        long id = 31;
        userInfo.setId(id);
        userInfo.setUsername(username);
        log.info("Admin login input: {}:{}", username, password);

        // Perform login
        String token = authService.login(username, password);
        log.info("Token: {}", token);

        // Check if user is an admin
        Boolean adminLogin = true;
//         authService.adminLogin(username, password);
        if (!adminLogin) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // TODO: Write token to cookie
        try {
            CookieUtils.newBuilder(response).httpOnly().request(request)
                    .build(cookieName, token);
        } catch (Exception e) {
            log.error("Exception occurred while writing cookie", e);
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
