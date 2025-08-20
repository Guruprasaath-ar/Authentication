package dev.guru.AuthenticationSystem.io;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Component;

@Component
public class UserRequest {

    @NotBlank(message = "user name cannot be empty")
    private String username;
    @Size(min = 8, message = "enter a password with at least 8 characters")
    private String password;
    @Email(message = "enter a valid email")
    private String email;

    public UserRequest(){

    }

    public UserRequest(UserRequestBuilder userRequestBuilder) {
        this.username = userRequestBuilder.username;
        this.password = userRequestBuilder.password;
        this.email = userRequestBuilder.email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public static class UserRequestBuilder{
        private String username;
        private String password;
        private String email;

        public UserRequestBuilder withUsername(String username){
            this.username = username;
            return this;
        }

        public UserRequestBuilder withPassword(String password){
            this.password = password;
            return this;
        }

        public UserRequestBuilder withEmail(String email){
            this.email = email;
            return this;
        }

        public UserRequest build(){
            return new UserRequest(this);
        }
    }
}
