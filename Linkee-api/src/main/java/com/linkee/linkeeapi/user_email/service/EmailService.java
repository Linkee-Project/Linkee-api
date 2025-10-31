package com.linkee.linkeeapi.user_email.service;


public interface EmailService {
    void sendVerificationEmail(String to);
    boolean verifyCode(String email, String code);
}
