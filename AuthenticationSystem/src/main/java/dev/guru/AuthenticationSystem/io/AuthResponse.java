package dev.guru.AuthenticationSystem.io;

import org.springframework.stereotype.Component;

@Component
public class AuthResponse {

    private String email;
    private String token;

    public AuthResponse(){

    }

    public AuthResponse(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }


}
