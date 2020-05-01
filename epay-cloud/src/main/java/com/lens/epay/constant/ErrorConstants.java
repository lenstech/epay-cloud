package com.lens.epay.constant;


/**
 * Created by Emir Gökdemir
 * on 13 Eki 2019
 */

public class ErrorConstants {

    //Token exceptions
    public static final String INVALID_TOKEN = "Invalid Token!";
    public static final String EXPIRED_TOKEN = "Token is expired!";

    //Mail Exception
    public static final String MAIL_ALREADY_EXISTS = "Mail already exists!";
    public static final String MAIL_SEND_FAILED = "Mail couldn't be sent!";

    //Password Exception
    public static final String WRONG_EMAIL_OR_PASSWORD = "The given email or password is wrong!";
    public static final String NEW_PASSWORD_CANNOT_BE_SAME_AS_OLD = "New password cannot be same as old";
    public static final String OLD_PASSWORD_IS_WRONG = "Old password is wrong!";
    public static final String PLEASE_CONFIRM_YOUR_EMAIL_ADDRESS = "Please confirm your mail address!";

    //Unauthorization Exception
    public static final String NOT_AUTHORIZED_FOR_OPERATION = "You are not authorized for this operation!";
    public static final String USER_NOT_EXIST = "User could not be found!";
    public static final String THIS_OPERATION_IS_NOT_BELONG_TO_THIS_USER = "This Operation is not belong to this user!";

    public static final String ID_IS_NOT_EXIST = "Id is not exist";

    public static final String PRODUCT_NOT_EXIST = "Product could not be found!";

    //Department
    public static final String USER_ALREADY_ADDED_TO_DEPARTMENT = "User already added to department!";
    public static final String USER_IS_NOT_EXIST = "User is not exist!";

    //Branch
    public static final String DEPARTMENT_ALREADY_ADDED_TO_BRANCH = "Department already added to department!";
    public static final String DEPARTMENT_IS_NOT_EXIST = "Department is not exist!";

    //General
    public static final String DTO_CANNOT_BE_EMPTY = "Dto cannot be Empty";
    public static final String ID_CANNOT_BE_EMPTY = "Id cannot be Empty";

    //Photo
    public static final String THERE_IS_NO_PROFILE_PHOTO_OF_THIS_USER = "There is no photo of this user!";

    //Order
    public static final String NOT_APPROPRIATE_CANCEL_AT_THIS_POINT = "It is not appropriate for cancel at this point";
    public static final String NOT_APPROPRIATE_FOR_THIS_PAYMENT_TYPE = "Operation is not appropriate for this payment type!";
    public static final String NOT_APPROPRIATE_ORDER_STATUS = "Order Status is not appropriate for this operation!";
    public static final String PAYMENT_IS_UNSUCCESSFUL = "Payment is unsuccessful!";
    public static final String PAYMENT_IS_NOT_COMPLETED_YET = "Payment type is not Pay At The Door and payment is not completed yet!";
    public static final String REPAYMENT_ALREADY_COMPLETED = "Repayment was already completed.";
    public static final String PAYMENT_IS_NOT_OCCURED = "Payment is not occured yet was already completed.";
    public static final String TOTAL_PRICE_IS_NOT_CORRECT = "Total price is not correct!";

}
