package dev.guru.AuthenticationSystem.util;

import dev.guru.AuthenticationSystem.io.UserRequest;
import dev.guru.AuthenticationSystem.io.UserResponse;
import dev.guru.AuthenticationSystem.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserFunctionsLibrary {

    static PasswordEncoder passwordEncoder;

    @Autowired
    public UserFunctionsLibrary(PasswordEncoder passwordEncoder) {
        UserFunctionsLibrary.passwordEncoder = passwordEncoder;
    }


    public static UserEntity convertToUserEntity(UserRequest userRequest) {
        return new UserEntity.UserBuilder()
                .withUserId(UUID.randomUUID().toString())
                .withUsername(userRequest.getUsername())
                .withEmail(userRequest.getEmail())
                .withPassword(passwordEncoder.encode(userRequest.getPassword()))
                .withIsAccountVerified(false)
                .withOtp(0)
                .withOtpExpiration(0)
                .build();
    }

    public static UserResponse convertToUserResponse(UserEntity userEntity) {
        return new UserResponse.UserResponseBuilder()
                .withUserId(userEntity.getUserId())
                .withUserEmail(userEntity.getEmail())
                .withUserName(userEntity.getUsername())
                .withIsAccountVerified(userEntity.getIsAccountVerified())
                .build();
    }
}
