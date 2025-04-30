package com.github.nits42.authservice.security;

import com.github.nits42.authservice.clients.UserServiceFeignClient;
import com.github.nits42.authservice.util.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class CustomUserDetailService implements UserDetailsService {


    private final UserServiceFeignClient userServiceFeignClient;

    private final Set<GrantedAuthority> authoritySet = new HashSet<>();

    @Autowired
    public CustomUserDetailService(UserServiceFeignClient userServiceFeignClient) {
        this.userServiceFeignClient = userServiceFeignClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Set<GrantedAuthority> authoritiesSet = new HashSet<>();

        // Call the user service to get the user details
        var userResponse = userServiceFeignClient.getUserByUsername(username);
        if (userResponse.getHeaders().isEmpty()) {
            throw new UsernameNotFoundException(AppConstant.USER_NOT_FOUND);
        }
        // ROLE_USER
        // ROLE_ADMIN
        GrantedAuthority authorities = new SimpleGrantedAuthority("ROLE_" + userResponse.getBody().getRole());
        authoritySet.add(authorities);
        return new org.springframework.security.core.userdetails.User(
                userResponse.getBody().getUsername(),
                userResponse.getBody().getPassword(),
                authoritySet
        );

    }

}
