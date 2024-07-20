package com.cloud.shopping.auth.client;

import com.cloud.shopping.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Version 1.0.0
 * Use Feign to call the user service API
 **/
@FeignClient("user-service")
public interface UserClient extends UserApi {
}
