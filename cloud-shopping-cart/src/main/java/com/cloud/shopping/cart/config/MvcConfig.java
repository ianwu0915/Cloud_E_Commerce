package com.cloud.shopping.cart.config;

import com.cloud.shopping.cart.filters.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// This class configures the MVC framework for the shopping cart service by registering the UserInterceptor with jwt properties
// How it works:
// - The class is annotated with @Configuration to indicate that it is a configuration class
// - The class implements the WebMvcConfigurer interface, which provides methods to configure the MVC framework
// - The class is annotated with @EnableConfigurationProperties to enable the use of configuration properties
// Whenever a request is made to the shopping cart service, the UserInterceptor will intercept the request and validate the JWT token
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
//The @EnableConfigurationProperties(JwtProperties.class) annotation in Spring Boot is used
// to enable support for @ConfigurationProperties-annotated beans.
// This allows the JwtProperties class to be automatically configured with properties defined in the application's configuration files
// (such as application.yml or application.properties).
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtProperties prop; // JwtProperties bean is automatically injected by Spring with the properties defined in the application.yml


    // This ensures that the UserInterceptor is added to the list of interceptors
    // So that it can intercept requests and validate the JWT token whenever a request is made to the shopping cart service
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInterceptor(prop)).addPathPatterns("/**");
    }
}