package com.dawaaii.service.api.auth;

import com.dawaaii.service.user.UserService;
import com.dawaaii.service.user.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserNamePasswordAuthenticationManager implements AuthenticationManager {

    private UserService userService;

    public UserNamePasswordAuthenticationManager(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = new User();
        user.setEmail((String) authentication.getPrincipal());
        user.setPassword((String) authentication.getCredentials());
        Optional<User> userByEmailAndPassword = userService.findUserByEmailAndPassword(user);
        if (!userByEmailAndPassword.isPresent()) return null;

        if (!userByEmailAndPassword.get().isActive()) return null;

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(((String) authentication.getPrincipal()).trim(), authentication.getCredentials(), null);
        Map<String, Object> details = new HashMap<>();
        details.put("userId", userByEmailAndPassword.get().getId());
        authenticationToken.setDetails(details);
        return authenticationToken;
    }
}