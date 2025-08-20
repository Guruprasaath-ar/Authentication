package dev.guru.AuthenticationSystem.controller;

import dev.guru.AuthenticationSystem.io.*;
import dev.guru.AuthenticationSystem.model.UserEntity;
import dev.guru.AuthenticationSystem.service.AuthService;
import dev.guru.AuthenticationSystem.service.UserEntityService;
import dev.guru.AuthenticationSystem.util.AuthFunctionsLibrary;
import dev.guru.AuthenticationSystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final UserEntityService userEntityService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, AuthService authService, JwtUtil jwtUtil, UserEntityService userEntityService) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.userEntityService = userEntityService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            // Authenticate using Spring Security
            Authentication authentication = AuthFunctionsLibrary
                    .authenticate(authenticationManager,authRequest.getEmail(), authRequest.getPassword());

            UserDetails userDetails = authService.loadUserByUsername(authRequest.getEmail());
            UserEntity user = userEntityService.getUserByEmail(authRequest.getEmail()).orElseThrow(()->new UsernameNotFoundException("User not found"));

            if(!user.getIsAccountVerified())
                throw new DisabledException("Account is not verified yet.");

            String jwtToken = jwtUtil.generateToken(userDetails);

            ResponseCookie cookie = ResponseCookie.from("JWT_TOKEN", jwtToken)
                                    .httpOnly(true)
                                    .maxAge(60*60*24)
                                    .sameSite("Strict")
                                    .path("/")
                                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(new AuthResponse(authRequest.getEmail(), jwtToken));
        }
        catch (BadCredentialsException e) {
            Map<String,Object> error = new HashMap<>();
            error.put("error", "Bad Credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        catch (DisabledException d){
            Map<String,Object> error = new HashMap<>();
            error.put("error", "Account is disabled");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        catch (Exception e) {
            Map<String,Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/verify-account")
    public ResponseEntity<?> verifyAccount(@RequestBody AccountVerificationRequest request) {
        try {
            validateRequest(request);

            UserEntity user = userEntityService.getUserByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Invalid email"));

            validateOtp(user, request.getOtp());

            user.setAccountVerified(true);
            user.setOtp(0);
            user.setOtpExpiration(0);
            userEntityService.saveUserEntity(user);

            return ResponseEntity.ok("Account verified successfully");
        } catch (IllegalArgumentException | UsernameNotFoundException ex) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    private void validateRequest(AccountVerificationRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (request.getOtp() < 0) {
            throw new IllegalArgumentException("OTP cannot be negative");
        }
    }

    private void validateOtp(UserEntity user, long otp) {
        if (System.currentTimeMillis() >= user.getOtpExpiration()) {
            throw new IllegalArgumentException("OTP expired");
        }
        if (otp != user.getOtp()) {
            throw new IllegalArgumentException("Invalid OTP");
        }
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", message);
        return ResponseEntity.status(status).body(error);
    }


    @PostMapping("/resend-otp")
    public void sendOTP(@RequestBody String email){
        userEntityService.sendOTP(email);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody AuthRequest authRequest) {

        UserEntity user = userEntityService.getUserByEmail(authRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Invalid email"));

        if(user == null)
            return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid email");

        user.setPassword(authRequest.getPassword());
        userEntityService.saveUserEntity(user);
        return ResponseEntity.ok("Password reset successfully");
    }
}
