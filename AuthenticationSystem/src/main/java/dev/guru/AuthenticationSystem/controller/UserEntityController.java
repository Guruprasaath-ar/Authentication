package dev.guru.AuthenticationSystem.controller;

import dev.guru.AuthenticationSystem.io.UserRequest;
import dev.guru.AuthenticationSystem.io.UserResponse;
import dev.guru.AuthenticationSystem.model.UserEntity;
import dev.guru.AuthenticationSystem.service.UserEntityService;
import dev.guru.AuthenticationSystem.util.UserFunctionsLibrary;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserEntityController {

    private UserEntityService userEntityService;

    @Autowired
    public void SetUserEntityService(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createProfile(@Valid @RequestBody  UserRequest userRequest) {
        if(!userEntityService.userExists(userRequest.getEmail()))
            return userEntityService.createUserProfile(userRequest);

        throw new ResponseStatusException(HttpStatus.CONFLICT, "If this email is registered, you will receive an OTP.");
    }

    @GetMapping("/profile")
    public UserResponse getProfile(@CurrentSecurityContext(expression = "authentication.name") String email){
        UserEntity user = userEntityService.getUserByEmail(email).orElseThrow(()->new UsernameNotFoundException("invalid email"));
        return UserFunctionsLibrary.convertToUserResponse(user);
    }
}