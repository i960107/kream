package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_TOKEN(false, 2001, "토큰을 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false, 2003, "권한이 없는 유저의 접근입니다."),
    EXPIRED_JWT(false, 2004, "만료된 JWT입니다"),
    REQUEST_PARAM_ERROR(false, 2005, "요청 파라미터를 확인해주세요"),

    INVALID_REFRESH_TOKEN(false, 2006, "유효하지 않은 refresh token입니다."),

    // users
    TOKEN_USER_NOT_MATH(false, 2010, "토큰의 정보와 다른 유저의 접근입니다."),
    USERS_FIND_NO_MATCH(false, 2011, "일치하는 사용자가 없습니다"),
    USERS_WRONG_USER_IDX(false, 2012, "없거나 삭제된 유저입니다"),
    USERS_WRONG_ADDRESS_IDX(false, 2013, "없거나 삭제되거나 유저의 주소가 아닙니다"),
    USERS_WRONG_ACCOUNT_IDX(false, 2014, "없거나 삭제되거나 유저의 판매 정산 계좌가 아닙니다"),
    USERS_WRONG_CARD_IDX(false, 2015, "없거나 삭제되거나 유저의 결제카드가 아닙니다"),
    WRONG_TOTAL_PRICE(false, 2016, "총 결제 혹은 금액이 잘못되었습니다"),
    WRONG_SETTLEMENT_AMOUNT(false, 2017, " 정산 금액이 잘못되었습니다"),
    FAILED_TO_CERTIFICATE_WRONG_NUMBER(false, 2018, "인증번호가 일치하지 않습니다"),
    DUPLICATED_PHONE(false, 2019, "이미 등록된 휴대폰 번호입니다"),

    /**
     * 3000 : Response 오류
     */
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),
    DUPLICATED_EMAIL(false, 3013, "이미 등록된 이메일입니다."),
    FAILED_TO_LOGIN(false, 3014, "없는 이메일이거나 비밀번호가 틀렸습니다."),
    PRODUCT_WRONG_IDX(false, 3015, "없거나 삭제된 상품입니다"),
    PRODUCT_SIZE_WRONG_IDX(false, 3016, "없거나 삭제된 사이즈입니다"),
    PRODUCT_LIKE_NO_MATCH(false, 3017, "일치하는 상품 북마크 정보가 없습니다"),
    PRODUCT_LIKE_ALREADY_EXIST(false, 3018, "이미 북마크 된 상품입니다"),
    USER_OWN_ALREADY_EXIST(false, 3019, "이미 등록된 보유 상품입니다"),
    FAILED_TO_CERTIFICATE_DURATION_OUT(false, 3020, "인증 시간이 지났습니다"),
    USER_ADDRESS_NO_DEFAULT_ADDRESS(false, 3021,"기본 배송지 등록이 필요합니다"),
    DUPLICATED_ACCOUNT(false, 3022,"이미 등록된 계좌입니다"),
    DUPLICATED_PAYMENT_CARD(false, 3023,"이미 등록된 카드입니다"),
    USER_CARD_NO_DEFAULT_CARD(false, 3024,"기본 결제 카드 등록이 필요합니다"),
    TARGET_BID_WRONG_IDX(false, 3025,"거래 대상 입찰이 없거나 삭제되거나 입찰중이 아닙니다"),
    BID_REGISTERING_WRONG_IDX(false, 3026,"등록중인 입찰이 없습니다"),
    BID_PURCHASE_GREATER_THAN_MIN_SALE(false, 3027,"더 낮은 가격의 판매 입찰이 있습니다"),
    BID_NOW_WRONG_PRICE(false, 3028,"잘못된 즉시 거래가입니다"),
    BID_NOT_MATCH_PAYMENT(false, 3029,"결제 정보가 입찰 정보와 다릅니다(결제카드,결제금액)"),
    BID_NOT_MATCH_PRODUCT_SIZE(false, 3030,"판매 입찰과 구매 입찰 상품의 사이즈가 다릅니다"),
    PRICE_NOT_MATCH_BID_PRICE(false, 3031,"즉시 거래가가 대상 입찰가와 다릅니다"),
    TRANSACTION_NOT_BEFORE_PAYMENT(false, 3032,"거래가 결제 전 상태가 아닙니다"),
    BID_NOT_PROCEEDING(false, 3033,"결제 중인 입찰이 아닙니다"),
    STYLE_WRONG_IDX(false, 3034,"없거나 삭제된 스타일입니다"),
    BID_NOT_REGISTERING(false, 3035,"등록중인 입찰이 아닙니다"),
    PAYMENT_FAIL(false, 3036,"결제에 실패했습니다"),
    BID_NOT_MATCH_USER(false, 3037,"유저의 입찰이 아닙니다"),
    BID_SALE_LESS_THAN_MAX_BIDN(false, 3028,"더 높은 가격의 구매 입찰이 있습니다"),
    STYLE_COMMENT_WRONG_IDX(false, 3035,"없거나 삭제된 스타일 댓글입니다"),
    STYLE_LIKE_ALREADY_EXIST(false, 3036,"이미 공감된 스타일입니다"),
    STYLE_LIKE_NO_MATCH(false, 3037,"등록된 스타일 공감이 없습니다"),
    POST_STYLE_FAIL(false, 3038,"스타일 등록에 실패하였습니다"),


    /**
    * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
    OAUTH_LOGIN_ERROR(false, 4002, "OAuth 서비스 제공 서버와의 연결에 실패하였습니다"),
    IMAGE_UPLOAD_ERROR(false, 4003, "이미지 업로드에 실패하였습니다"),
    SERVER_EXCEPTION(false, 5000, "서버 에러 발생"),
    SERVER_RUNTIME_EXCEPTION(false, 5001, "서버 런타임 에러 발생"),
    METHOD_NOT_SUPPORTED(false, 5002, "잘못된 http method입니다"),
    COOLSMS_FAIL(false, 5003, "문자 발송 실패"),
    S3_FAIL(false, 5004, "파일 업로드 실패"),
    FILE_CONVERT_ERROR(false, 5005, "파일 변환 실패"),


    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false, 4014, "유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요;

    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
