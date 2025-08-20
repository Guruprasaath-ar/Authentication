package dev.guru.AuthenticationSystem.service;

import dev.guru.AuthenticationSystem.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService implements UserDetailsService {

    private UserEntityService userEntityService;

    @Autowired
    public void setUserEntityService(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity user = userEntityService.getUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("email not found " + email));

        return new User(user.getEmail(), user.getPassword(), List.of());
    }
}
