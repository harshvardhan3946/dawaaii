package com.dawaaii.service.api.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;


public class ClientBasicAuthManager implements AuthenticationManager {

    private final ClientDetailsService clientDetailsService;

    public ClientBasicAuthManager(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId((String) authentication.getPrincipal());
        if (clientDetails.getClientSecret().equals(authentication.getCredentials()))
            return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), null);
        return null;
    }
}
