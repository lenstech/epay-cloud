package com.lens.epay.constant;

/**
 * Created by Emir Gökdemir
 * on 12 Eki 2019
 */

public class MailConstants {
    //HEADER
    public static final String CONFIRM_ACCOUNT_HEADER = "Epay Email Confirmation";
    public static final String RESET_PASSWORD_HEADER  = "Epay Reset Password Request";

    //BODY
    public static final String CONFIRM_ACCOUNT_BODY = "To confirm your account, please click here : ";
    public static final String RESET_PASSWORD_BODY  = "To reset your password, please click here : ";

    //URL
    public static final String CONFIRM_ACCOUNT_URL = "/register/confirm-register?token=";
    public static final String RESET_PASSWORD_URL  = "/reset-password/confirm?token=" ;

}
