package dev.guru.AuthenticationSystem.io;

public class AccountVerificationRequest {

    private String email;
    private long otp;

    public String getEmail() {
        return email;
    }

    public long getOtp() {
        return otp;
    }

    public AccountVerificationRequest() {

    }

    public AccountVerificationRequest(String email, long otp) {
        this.email = email;
        this.otp = otp;
    }
}
