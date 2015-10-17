package com.dawaaii.service.api.controller;

import com.dawaaii.web.common.controller.BaseController;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.Map;

public class APIBaseController extends BaseController {

    @SuppressWarnings("unchecked")
    protected static Long authenticatedUserId(Authentication authentication) {
        if (authentication instanceof OAuth2Authentication) {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
            Map<String, Object> details = (Map<String, Object>) oAuth2Authentication.getUserAuthentication().getDetails();
            return (Long) details.get("userId");
        }
        return null;
    }

    protected boolean checkOperationForVehicleAllowedForUser(Authentication authentication, Long vehicleUserId) {
        Long userId = authenticatedUserId(authentication);
        //Check vehicle belongs to same user
        return vehicleUserId.equals(userId);
    }

}
