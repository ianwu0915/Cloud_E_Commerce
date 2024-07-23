package com.cloud.shopping.user.service.impl;

import com.cloud.shopping.common.enums.ExceptionEnum;
import com.cloud.shopping.common.exception.LyException;
import com.cloud.shopping.common.utils.NumberUtils;
import com.cloud.shopping.user.pojo.User;
import com.cloud.shopping.user.service.UserService;
import com.cloud.shopping.user.mapper.UserMapper;
import com.cloud.shopping.user.utils.CodecUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of the UserService interface that handles user-related operations
 * including registration, authentication, and verification.
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "user:verify:phone";

    private static final String KEY_PREFIX2 = "cloudshopping:user:info";

    /**
     * Checks if the given data is unique in the system based on the specified type.
     *
     * @param data The data to check (username or phone number)
     * @param type The type of data (1 for username, 2 for phone number)
     * @return true if the data is unique (not exists in system), false otherwise
     * @throws LyException if the data type is invalid
     */
    @Override
    public Boolean checkData(String data, Integer type) {
        User user = new User();
        switch (type) {
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
            default:
                throw new LyException(ExceptionEnum.INVALID_USER_DATA_TYPE);
        }
        return userMapper.selectCount(user) == 0;
    }

    /**
     * Sends a verification code to the specified phone number.
     * The code is valid for 5 minutes and is stored in Redis.
     *
     * @param phone The phone number to send the verification code to
     */
    @Override
    public void sendCode(String phone) {
        String key = KEY_PREFIX + phone;
        String code = NumberUtils.generateCode(6);
        Map<String, String> msg = new HashMap<>();
        msg.put("phone", phone);
        msg.put("code", code);
        // Send verification code through message queue
        amqpTemplate.convertAndSend("ly.sms.exchange", "sms.verify.code", msg);
        // Save code in Redis with 5-minute expiration
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
    }

    /**
     * Registers a new user in the system after verifying the provided code.
     * The password is encrypted using salt before storing in the database.
     *
     * @param user The user object containing registration information
     * @param code The verification code to validate
     * @throws LyException if the verification code is invalid
     */
    @Override
    public void register(User user, String code) {
        // Verify the code from Redis
        String cacheCode = redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());

        if (!StringUtils.equals(code, cacheCode)) {
            throw new LyException(ExceptionEnum.INVALID_VERIFY_CODE);
        }

        // Generate salt and encrypt password
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));

        // Save user to database with creation timestamp
        user.setCreated(new Date());
        userMapper.insert(user);

    }

    /**
     * Authenticates a user based on username and password.
     *
     * @param username The username of the user
     * @param password The password to verify
     * @return User object if authentication is successful
     * @throws LyException if username or password is invalid
     */
    @Override
    public User queryUserByUsernameAndPassword(String username, String password) {
        // Query user by username
        User record = new User();
        record.setUsername(username);
        User user = userMapper.selectOne(record);

        if (user == null) {
            throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }

        // Verify password
        if (!StringUtils.equals(user.getPassword(), CodecUtils.md5Hex(password, user.getSalt()))) {
            throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }

        return user;
    }

    /**
     * Checks if the user has administrator privileges.
     * Currently implements a simple check based on user ID.
     *
     * @param username The username to check
     * @param password The password to verify
     * @return true if the user is an admin, false otherwise
     * @note This is a simplified implementation and should be enhanced with proper role-based checking
     */
    @Override
    public Boolean isAdmin(String username, String password) {
        User record = new User();
        record.setUsername(username);
        User user = userMapper.selectOne(record);

        Long id = user.getId();

        // Simple admin check based on ID
        // TODO: Implement proper role-based admin verification
        return id == 31;
    }
}