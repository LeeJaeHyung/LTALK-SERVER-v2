package com.ltalk.server.request;

import lombok.Getter;

@Getter
public class SignupRequest {
    String username;
    String password;
    String email;
}
