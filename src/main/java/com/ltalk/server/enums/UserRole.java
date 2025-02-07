package com.ltalk.server.enums;

public enum UserRole {

    USER(Authority.USER),  // 사용자 권한
    ADMIN(Authority.ADMIN),  // 관리자 권한
    UNKNOWN_USER(Authority.UNKNOWN_USER);//

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }
    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String UNKNOWN_USER = "UNKNOWN_USER";
    }
}