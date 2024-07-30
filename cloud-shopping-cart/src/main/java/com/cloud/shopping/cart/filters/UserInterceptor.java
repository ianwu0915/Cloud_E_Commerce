package com.cloud.shopping.cart.filters;

import com.cloud.shopping.auth.entity.UserInfo;
import com.cloud.shopping.auth.utils.JwtUtils;
import com.cloud.shopping.cart.config.JwtProperties;
import com.cloud.shopping.common.utils.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerInterceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * User Authentication Interceptor
 * Validates JWT tokens and maintains user context
 */
@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    private JwtProperties prop;

    // ThreadLocal storage for user information
    private static final ThreadLocal<UserInfo> tl = new ThreadLocal<>();

    public UserInterceptor(JwtProperties prop) {
        this.prop = prop;
    }


    // It is an interceptor method that runs before any request is processed by the controller
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Extract JWT token from cookie
        String token = CookieUtils.getCookieValue(request, prop.getCookieName());
        try {
            // Parse token and verify user
            UserInfo user = JwtUtils.getInfoFromToken(token, prop.getPublicKey());
            // Store user info in a thread local context
            // Since each Http request is handled by a separate thread,
            // We need to ensure that the current user info are available throughout the request lifecycle for the current thread
            tl.set(user);
            return true;
        } catch(Exception e) {
            log.error("[Cart Service] Failed to parse user authentication.", e);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        // Clean up thread local storage
        tl.remove();
    }

    /**
     * Get current user from thread context
     * @return UserInfo of current user
     */
    public static UserInfo getUser() {
        return tl.get();
    }
}