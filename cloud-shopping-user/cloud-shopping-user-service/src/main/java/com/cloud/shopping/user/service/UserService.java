package com.cloud.shopping.user.service;

import com.cloud.shopping.user.pojo.User;
public interface UserService {

    /**
     * Check if the data is available
     * @param data
     * @param type
     * @return
     */
    Boolean checkData(String data, Integer type);

    /**
     * Send verification code
     * @param phone
     */
    void sendCode(String phone);

    /**
     * Register user
     * @param user
     * @param code
     */
    void register(User user, String code);

    /**
     * Query user by username and password
     * @param username
     * @param password
     * @return
     */
    User queryUserByUsernameAndPassword(String username, String password);

    /**
     * Check if the user is admin
     * @param username
     * @param password
     * @return
     */
    Boolean isAdmin(String username, String password);
}
