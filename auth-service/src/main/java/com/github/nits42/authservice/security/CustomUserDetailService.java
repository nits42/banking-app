package com.github.nits42.authservice.security;

import com.github.nits42.authservice.clients.UserServiceFeignClient;
import com.github.nits42.authservice.util.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserServiceFeignClient userServiceFeignClient;

    @Autowired
    public CustomUserDetailService(UserServiceFeignClient userServiceFeignClient) {
        this.userServiceFeignClient = userServiceFeignClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Call the user service to get the user details
        var userResponse = userServiceFeignClient.getUserByUsername(username).getBody();
        if (userResponse == null) {
            throw new UsernameNotFoundException(AppConstant.USER_NOT_FOUND);
        }
        return new CustomUserDetails(userResponse);


    }

}
