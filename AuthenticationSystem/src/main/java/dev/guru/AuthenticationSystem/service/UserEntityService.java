package dev.guru.AuthenticationSystem.service;

import dev.guru.AuthenticationSystem.io.UserRequest;
import dev.guru.AuthenticationSystem.io.UserResponse;
import dev.guru.AuthenticationSystem.model.UserEntity;
import dev.guru.AuthenticationSystem.repo.UserRepo;
import dev.guru.AuthenticationSystem.util.MailFunctionsLibrary;
import dev.guru.AuthenticationSystem.util.UserFunctionsLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;


@Service
public class UserEntityService {

    private UserRepo userRepo;
    private JavaMailService mailSender;
    private static final long OTP_EXPIRATION = 1000 * 60 * 5;

    public UserEntityService() {
    }

    @Autowired
    public void setUserRepo(UserRepo userRepo, JavaMailService mailSender) {

        this.userRepo = userRepo;
        this.mailSender = mailSender;
    }

    public void saveUserEntity(UserEntity userEntity) {
        userRepo.save(userEntity);
    }

    public Boolean userExists(String email) {
        return userRepo.existsByEmail(email);
    }

    public Optional<UserEntity> getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public UserResponse createUserProfile(UserRequest userRequest) {
        UserEntity userEntity = UserFunctionsLibrary.convertToUserEntity(userRequest);
        long OTP = generateOTP();
        mailSender.sendMail(userEntity.getEmail(),
                            MailFunctionsLibrary.getOtpSubject(),
                            MailFunctionsLibrary.getOtpMessage(userRequest.getUsername(),OTP));
        userEntity.setOtp(OTP);
        userEntity.setOtpExpiration(System.currentTimeMillis() + OTP_EXPIRATION);
        saveUserEntity(userEntity);
        return UserFunctionsLibrary.convertToUserResponse(userEntity);
    }

    public void sendOTP(String email){
        System.out.println("email: " + email);
        UserEntity userEntity = userRepo.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
        long OTP = generateOTP();
        mailSender.sendMail(userEntity.getEmail(),
                MailFunctionsLibrary.getOtpSubject(),
                MailFunctionsLibrary.getOtpMessage(userEntity.getUsername(),OTP));
        userEntity.setOtp(OTP);
        userEntity.setOtpExpiration(System.currentTimeMillis() * OTP_EXPIRATION);
        saveUserEntity(userEntity);
    }

    private long generateOTP(){
        SecureRandom random = new SecureRandom();
        return 100000 + random.nextInt(900000);
    }
}
