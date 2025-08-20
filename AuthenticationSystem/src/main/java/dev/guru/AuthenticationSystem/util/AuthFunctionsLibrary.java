package dev.guru.AuthenticationSystem.util;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class AuthFunctionsLibrary {

    public static Authentication authenticate(AuthenticationManager manager, String email, String password) throws AuthenticationException {
        return manager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

}
