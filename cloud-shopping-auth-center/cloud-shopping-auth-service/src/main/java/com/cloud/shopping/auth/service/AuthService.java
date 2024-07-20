package com.cloud.shopping.auth.service;

public interface AuthService {
    /**
     * Login authorization work based on username and password
     * @param username
     * @param password
     * @return
     */
    String login(String username, String password);

    /**
     * Admin login authorization work based on username and password
     * @param username
     * @param password
     * @return
     */
    Boolean adminLogin(String username, String password);
}
