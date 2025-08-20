package dev.guru.AuthenticationSystem.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Component
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String userId;
    private String username;
    private String password;
    private String email;
    private long otp;
    private long otpExpiration;
    private Boolean IsAccountVerified;
    @Column(updatable = false)
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public UserEntity() {

    }

    public UserEntity(UserBuilder userBuilder) {
        this.userId = userBuilder.userId;
        this.username = userBuilder.username;
        this.password = userBuilder.password;
        this.email = userBuilder.email;
        this.otp = userBuilder.otp;
        this.otpExpiration = userBuilder.otpExpiration;
        this.IsAccountVerified = userBuilder.IsAccountVerified;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOtp(long otp) {
        this.otp = otp;
    }

    public void setOtpExpiration(long otpExpiration) {
        this.otpExpiration = otpExpiration;
    }

    public void setAccountVerified(Boolean accountVerified) {
        IsAccountVerified = accountVerified;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Boolean getIsAccountVerified() {
        return IsAccountVerified;
    }

    public long getOtp() {
        return otp;
    }

    public long getOtpExpiration() {
        return otpExpiration;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getUserId() {
        return userId;
    }

    //Builder for generating a user entity.
    public static class UserBuilder{
        private String userId;
        private String username;
        private String password;
        private String email;
        private long otp;
        private long otpExpiration;
        private Boolean IsAccountVerified;
        @CreatedDate
        private Instant createdAt;
        @LastModifiedDate
        private Instant updatedAt;

        public UserBuilder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public UserBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder withOtp(long otp) {
            this.otp = otp;
            return this;
        }

        public UserBuilder withOtpExpiration(long otpExpiration) {
            this.otpExpiration = otpExpiration;
            return this;
        }

        public UserBuilder withIsAccountVerified(Boolean IsAccountVerified) {
            this.IsAccountVerified = IsAccountVerified;
            return this;
        }

        public UserEntity build() {
            return new UserEntity(this);
        }
    }
}
