package dev.guru.AuthenticationSystem.io;
import org.springframework.stereotype.Component;

@Component
public class UserResponse {

    private String userId;
    private String userName;
    private String userEmail;
    private Boolean IsAccountVerified;

    public Boolean getAccountVerified() {
        return IsAccountVerified;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public UserResponse() {

    }

    public UserResponse(UserResponseBuilder builder) {
        this.userId = builder.userId;
        this.userName = builder.userName;
        this.userEmail = builder.userEmail;
        this.IsAccountVerified = builder.IsAccountVerified;
    }

    public static class UserResponseBuilder{
        private String userId;
        private String userName;
        private String userEmail;
        private Boolean IsAccountVerified;

        public UserResponseBuilder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public UserResponseBuilder withUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserResponseBuilder withUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public UserResponseBuilder withIsAccountVerified(Boolean IsAccountVerified) {
            this.IsAccountVerified = IsAccountVerified;
            return this;
        }

        public UserResponse build() {
            return new UserResponse(this);
        }
    }

}
