package dev.guru.AuthenticationSystem.util;

public class MailFunctionsLibrary {

    public static String getWelcomeSubject(){
        return "Welcome to our platform";
    }

    public static String getWelcomeMessage(String username){
        return "hello " + username
                + "\n" + "Congratulations! Your account is ready to send marketing emails to your contacts. " +"\n"
                + "best regards," + "\n" + "authentication team";
    }

    public static String getOtpSubject(){
        return "Your One-Time Password (OTP) for Verification";
    }

    public static String getOtpMessage(String username,long otp){
        return "hello " + username
                +"\n\n" + "Your One-Time Password (OTP) for completing the verification process is:"
                +"\n\n" + otp
                +"\n" + "This code is valid for the next 10 minutes. Please do not share it with anyone for security reasons."
                +"\n\n" + "Thanks,"
                +"\n" + "Security Team";
    }
}
