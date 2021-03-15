package com.lens.epay.constant;


/**
 * Created by Emir Gökdemir
 * on 13 Eki 2019
 */

public class ErrorConstants {

    //Token exceptions
    public static final String INVALID_TOKEN = "Geçersiz token, tekrar giriş yapınız";
    public static final String EXPIRED_TOKEN = "Zaman aşımına uğradı, tekrar giriş yapınız!";

    //Mail Exception
    public static final String MAIL_ALREADY_EXISTS = "Girmiş olduğunuz mail sistemimizde daha önce kaydedilmiş, farklı bir mail deneyiniz";
    public static final String MAIL_SEND_FAILED = "Mail gönderilemedi!";
    public static final String MAIL_NOT_EXIST = "Mail is not exists!";

    //Password Exception
    public static final String WRONG_EMAIL_OR_PASSWORD = "Verilen email şifre kayıtlarımızda bulunamadı, şifrenizi ve mailinizi kontrol ediniz.";
    public static final String NEW_PASSWORD_CANNOT_BE_SAME_AS_OLD = "Yeni şifre eskisiyle aynı olamaz!";
    public static final String OLD_PASSWORD_IS_WRONG = "Eski şifre hatalı!";
    public static final String PLEASE_CONFIRM_YOUR_EMAIL_ADDRESS = "Mail adresinize gelen link üzerinden mailinizi doğrulayınız!";

    //Unauthorization Exception
    public static final String NOT_AUTHORIZED_FOR_OPERATION = "Bu işlemi yapmaya yetkiniz bulunmamaktadır";
    public static final String USER_NOT_EXIST = "Kullanıcı bulunamadı";
    public static final String THIS_OPERATION_IS_NOT_BELONG_TO_THIS_USER = "Bu işlem bu kullanıcıya ait değil.!";

    public static final String ID_IS_NOT_EXIST = "Id is not exist";

    public static final String PRODUCT_NOT_EXIST = "Ürün bulunamadı!";
    public static final String ADDRESS_NOT_EXIST = "Adres bulunamadı!";

    //Department
    public static final String USER_ALREADY_ADDED_TO_DEPARTMENT = "Kullanıcı daha önce bu bölüme eklenmiş!";
    public static final String USER_IS_NOT_EXIST = "Kullanıcı mevcut değil";

    //Branch
    public static final String DEPARTMENT_ALREADY_ADDED_TO_BRANCH = "Department already added to department!";
    public static final String DEPARTMENT_IS_NOT_EXIST = "Bölüm mevcut değil";

    //General
    public static final String DTO_CANNOT_BE_EMPTY = "Dto boş olmamalı";
    public static final String ID_CANNOT_BE_EMPTY = "Id boş olmamalı";

    //Photo
    public static final String THERE_IS_NO_PROFILE_PHOTO_OF_THIS_USER = "There is no photo of this user!";

    //Order
    public static final String NOT_APPROPRIATE_CANCEL_AT_THIS_POINT = "Bu noktada iptale uygun değil.";
    public static final String NOT_APPROPRIATE_FOR_THIS_PAYMENT_TYPE = "Ödeme yöntemi bu işlem için uygun değil!";
    public static final String NOT_APPROPRIATE_ORDER_STATUS = "Sipariş statüsü bu işlem için uygun değil.";
    public static final String PAYMENT_IS_UNSUCCESSFUL = "Ödeme başarısız";
    public static final String PAYMENT_IS_NOT_COMPLETED_YET = "Ödeme henüz tamamlanmadı.";
    public static final String REPAYMENT_ALREADY_COMPLETED = "Geri ödeme zaten gerçekleşmiş.";
    public static final String PAYMENT_IS_NOT_OCCURED = "Ödeme gerçekleşmedi";
    public static final String TOTAL_PRICE_IS_NOT_CORRECT = "Toplam fiyat doğru değil.";
    public static final String CARD_EXPIRED = "Kart kullanım süresi geçmiş.";

    public static final String CATEGORY_CANNOT_BE_DELETED_WHEN_HAS_PRODUCT = "Ürünü olan kategori silinemez.";
    public static final String PRODUCT_CANNOT_BE_DELETED_WHEN_HAS_ORDER = "Product cannot be deleted when some orders have this product";

    public static final String FIRM_NAME_OF_CORPORATE_INVOICE_CAN_NOT_BE_NULL = "Kurumsal faturada firma ismi boş olmamalı.";
    public static final String TAX_NO_OF_CORPORATE_INVOICE_SHOULD_BE_10_CHARACTERS = "Kurumsal faturada vergi numarası boş olmamalı";
    public static final String TAX_ADMINISTRATOR_OF_CORPORATE_INVOICE_CAN_NOT_BE_NULL = "Vergi Dairesi boş bırakılmamalı";
    public static final String IDENTITY_NO_OF_INDIVIDUAL_INVOICE_SHOULD_BE_11_CHARACTERS = "Kimlik numarası 11 haneli olmalı.";

    public static final String PROVIDE_VALID_MAIL = "Geçerli bir mail adresi giriniz.";
}
