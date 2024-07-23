package com.cloud.shopping.user.web;

import com.cloud.shopping.user.pojo.User;
import com.cloud.shopping.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * This class is used to verify user login status
 * Flow: check if the username and phone number are available,
 * send verification code, register user
 *
 **/
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * Check if the username and phone number are available
     * @param data
     * @param type
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkData(
            @PathVariable("data") String data, @PathVariable(value = "type") Integer type){
        return ResponseEntity.ok(userService.checkData(data,type));
    }

    /**
     * Send verification code
     * @param phone
     * @return
     */
    @PostMapping("code")
    public ResponseEntity<Void> checkData(@RequestParam("phone")String phone){
        userService.sendCode(phone);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Register user
     * @param user
     * @param code
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, BindingResult result,
                                         @RequestParam("code") String code) {
//        if(result.hasFieldErrors()){
//            throw new RuntimeException(result.getFieldErrors().stream()
//                    .map(e->e.getDefaultMessage()).collect(Collectors.joining("|")));
//        }
        userService.register(user, code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Query user by username and password
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/query")
    public ResponseEntity<User> queryUserByUsernameAndPassword(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        return ResponseEntity.ok( userService.queryUserByUsernameAndPassword(username, password));
    }

    /**
     * Determine if the user is an administrator, return true if it is
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/queryAdmin")
    public ResponseEntity<Boolean> queryAdmin(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        return ResponseEntity.ok( userService.isAdmin(username, password));
    }
}
