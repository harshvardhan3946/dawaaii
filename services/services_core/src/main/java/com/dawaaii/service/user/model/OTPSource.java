package com.dawaaii.service.user.model;

public enum OTPSource {
    MOBILE,
    WEBSITE,
    ADMIN_PORTAL;

    public static OTPSource getSource(UserCreationSource creationSource) {
        switch (creationSource) {
            case WEBSITE:
                return WEBSITE;
            case ADMIN_PORTAL:
                return ADMIN_PORTAL;
            case MOBILE:
                return MOBILE;
            default:
                return null;
        }
    }
}
