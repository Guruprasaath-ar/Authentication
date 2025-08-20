package dev.guru.AuthenticationSystem.io;

import org.springframework.stereotype.Component;

@Component
public class AuthRequest {

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public AuthRequest(){

    }

    public AuthRequest(AuthReqBuilder builder){
        this.email=builder.email;
        this.password=builder.password;
    }

    public static class AuthReqBuilder{
        private String email;
        private String password;

        public AuthReqBuilder(){
        }

        public AuthReqBuilder withEmail(String email){
            this.email = email;
            return this;
        }

        public AuthReqBuilder withPassword(String password){
            this.password = password;
            return this;
        }

        public AuthRequest build(){
            return new AuthRequest(this);
        }
    }

}
