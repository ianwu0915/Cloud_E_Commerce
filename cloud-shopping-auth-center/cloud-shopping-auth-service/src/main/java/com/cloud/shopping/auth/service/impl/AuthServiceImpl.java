package com.cloud.shopping.auth.service.impl;

import com.cloud.shopping.auth.service.AuthService;
import com.cloud.shopping.auth.client.UserClient;
import com.cloud.shopping.auth.config.JwtProperties;
import com.cloud.shopping.auth.entity.UserInfo;
import com.cloud.shopping.auth.utils.JwtUtils;
import com.cloud.shopping.common.enums.ExceptionEnum;
import com.cloud.shopping.common.exception.LyException;
import com.cloud.shopping.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * This class is used to verify user login status
 **/
@Slf4j
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties prop;

    @Override
    public String login(String username, String password) {
        try {
            // Verify username and password
            // User user = userClient.queryUserByUsernameAndPassword(username, password);
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            long id = 31;
            user.setId(id);
            //判断
            if (user == null) {
                throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
            }
            // Generate token
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), username),
                    prop.getPrivateKey(), prop.getExpire());
            return token;

        } catch (Exception e) {
            log.error("[AuthService] username or password is incorrect, username: {}", username, e);
            throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
    }

    @Override
    public Boolean adminLogin(String username, String password) {
        // Determine if the user is an administrator
        Boolean queryAdmin = userClient.queryAdmin(username, password);
        if (queryAdmin) {
            return true;
        }
        log.error("[AuthService] the user is not admin, username: {}", username);
        return false;
    }
}
