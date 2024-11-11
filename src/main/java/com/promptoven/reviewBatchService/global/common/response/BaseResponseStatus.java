package com.promptoven.reviewBatchService.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum BaseResponseStatus {

    /*
     * 응답 코드와 메시지 표준화하는 ENUM.
     * Http 상태코드, 성공여부, 메시지, 코드를 반환.
     */

    /**
     * 200: 요청 성공.
     **/
    SUCCESS(HttpStatus.OK, true, 200, "요청에 성공하였습니다."),

    /**
     * 400 : security 에러. 아직 사용 안함.
     */
    ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, false, 400, "잘못된 요청입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, false, 401, "적절하지 않은 요청값입니다."),
    WRONG_JWT_TOKEN(HttpStatus.UNAUTHORIZED, false, 401, "다시 로그인 해주세요"),
    NO_SIGN_IN(HttpStatus.UNAUTHORIZED, false, 402, "로그인을 먼저 진행해주세요"),
    NO_ACCESS_AUTHORITY(HttpStatus.FORBIDDEN, false, 403, "접근 권한이 없습니다"),
    DISABLED_USER(HttpStatus.FORBIDDEN, false, 404, "비활성화된 계정입니다. 계정을 복구하시겠습니까?"),

    /**
     * 900: 기타 에러.
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, false, 900, "요청 처리 중 에러가 발생하였습니다."),

    /**
     * 2000: users service error.
     */
    // token
    TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, false, 2001, "토큰이 유효하지 않습니다."),

    // Users
    DUPLICATED_USER(HttpStatus.CONFLICT, false, 2101, "이미 가입된 멤버입니다."),
    FAILED_TO_LOGIN(HttpStatus.UNAUTHORIZED, false, 2102, "아이디 또는 비밀번호가 일치하지 않습니다."),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, false, 2103, "이미 사용중인 이메일입니다."),
    DUPLICATED_NAME_AND_PHONE(HttpStatus.CONFLICT, false, 2104, "이미 사용중인 이름과 전화번호입니다."),
    NO_EXIST_USER(HttpStatus.NOT_FOUND, false, 2105, "존재하지 않는 회원 정보입니다."),
    PASSWORD_SAME_FAILED(HttpStatus.BAD_REQUEST, false, 2106, "현재 사용중인 비밀번호입니다."),
    FAILED_TO_SEND_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, false, 2011, "이메일 전송에 실패했습니다."),
    INVALID_EMAIL_ADDRESS(HttpStatus.BAD_REQUEST, false, 2012, "이메일을 다시 확인해주세요."),
    INVALID_EMAIL_CODE_NOT_MATCH(HttpStatus.BAD_REQUEST, false, 2013, "이메일 인증번호가 일치하지 않습니다."),
    INVALID_EMAIL_CODE_EXPIRED(HttpStatus.BAD_REQUEST, false, 2014, "이메일 인증번호가 만료되었습니다."),

    // Oauth
    NOT_FOUND_OAUTH_MEMBER(HttpStatus.NOT_FOUND, false, 2201, "존재하지 않는 소셜 계정입니다."),
    INVALID_OAUTH_INFO(HttpStatus.BAD_REQUEST, false, 2202, "소셜 계정 정보가 올바르지 않습니다."),
    DUPLICATED_SOCIAL_USER(HttpStatus.CONFLICT, false, 2103, "이미 소셜 연동된 계정입니다."),
    DUPLICATED_SOCIAL_PROVIDER_USER(HttpStatus.CONFLICT, false, 2104, "계정에 동일한 플랫폼이 이미 연동되어있습니다."),
    NO_SUPPORTED_PROVIDER(HttpStatus.BAD_REQUEST, false, 2109, "지원하지 않는 플랫폼입니다."),

    // Interest
    NO_EXIST_INTEREST(HttpStatus.NOT_FOUND, false, 2501, "존재하지 않는 관심사입니다."),

    // Brand
    NO_EXIST_BRAND(HttpStatus.NOT_FOUND, false, 2601, "존재하지 않는 브랜드입니다."),
    DUPLICATED_BRAND(HttpStatus.CONFLICT, false, 2602, "이미 등록된 브랜드입니다."),
    INVALID_BRAND_NAME_LENGTH(HttpStatus.BAD_REQUEST, false, 2603,
            "브랜드 이름은 255자를 초과할 수 없습니다."),
    INVALID_BRAND_LOGO_URL_LENGTH(HttpStatus.BAD_REQUEST, false, 2604,
            "브랜드 로고 URL은 2000자를 초과할 수 없습니다."),

    // Promotion
    NO_EXIST_PROMOTION(HttpStatus.NOT_FOUND, false, 2701, "존재하지 않는 프로모션입니다."),

    // Category
    NO_EXIST_CATEGORY(HttpStatus.NOT_FOUND, false, 2801, "존재하지 않는 카테고리입니다."),
    INVALID_CATEGORY_PATH(HttpStatus.BAD_REQUEST, false, 2802, "잘못된 카테고리 경로입니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, false, 2803, "카테고리를 찾을 수 없습니다."),

    /**
     * 3000: product service error.
     */
    NO_EXIST_PRODUCT(HttpStatus.NOT_FOUND, false, 3001, "존재하지 않는 상품입니다."),
    NO_EXIST_OPTION(HttpStatus.NOT_FOUND, false, 3002, "존재하지 않는 옵션입니다."),
    INVALID_SORT_BY_PARAMETER(HttpStatus.BAD_REQUEST, false, 3003, "잘못된 정렬 기준 파라미터입니다."),
    INVALID_FILTER_CRITERIA(HttpStatus.BAD_REQUEST, false, 3004, "잘못된 필터 조건입니다."),
    NO_EXIST_PRODUCT_SCORE(HttpStatus.NOT_FOUND, false, 3005, "존재하지 않는 상품 점수입니다."),

    // Size
    NO_EXIST_SIZE(HttpStatus.NOT_FOUND, false, 3101, "존재하지 않는 사이즈입니다."),

    // Color
    NO_EXIST_COLOR(HttpStatus.NOT_FOUND, false, 3201, "존재하지 않는 색상입니다."),

    // EtcOption
    NO_EXIST_ETC_OPTION(HttpStatus.NOT_FOUND, false, 3301, "존재하지 않는 기타 옵션입니다."),

    // Product Media
    NO_EXIST_PRODUCT_MEDIA(HttpStatus.NOT_FOUND, false, 3401, "존재하지 않는 제품 미디어입니다."),
    INVALID_MEDIA_REQUEST(HttpStatus.BAD_REQUEST, false, 3402, "잘못된 미디어 요청입니다."),

    // Media
    NO_EXIST_MEDIA(HttpStatus.NOT_FOUND, false, 3501, "존재하지 않는 미디어입니다."),
    INVALID_MEDIA_TYPE(HttpStatus.BAD_REQUEST, false, 3502, "잘못된 미디어 종류입니다."),

    /**
     * 4000: review service error.
     */

    // review
    NO_EXIST_REVIEW(HttpStatus.NOT_FOUND, false, 4001, "존재하지 않는 리뷰입니다."),
    NO_DELETE_COMMENT_AUTHORITY(HttpStatus.BAD_REQUEST, false, 4002, "댓글 삭제 권한이 없습니다."),
    NO_DELETE_RE_COMMENT_AUTHORITY(HttpStatus.BAD_REQUEST, false, 4003, "대댓글 삭제 권한이 없습니다."),
    NO_EXIST_RE_COMMENT(HttpStatus.NOT_FOUND, false, 4003, "존재하지 않는 대댓글입니다."),
    NO_EXIST_PIN_AUTHORITY(HttpStatus.BAD_REQUEST, false, 4004, "고정 권한이 없습니다."),
    // review Event
    NO_EXIST_EVENT(HttpStatus.NOT_FOUND, false, 4104, "존재하지 않는 이벤트입니다."),

    /**
     * 5000: cart service error.
     */

    // cart
    NO_EXIST_CART_ITEM(HttpStatus.NOT_FOUND, false, 5001, "존재하지 않는 장바구니 항목입니다."),
    NO_EXIST_CART(HttpStatus.NOT_FOUND, false, 5004, "장바구니가 존재하지 않습니다."),
    INVALID_CART_ACTION(HttpStatus.BAD_REQUEST, false, 5005, "유효하지 않은 장바구니 액션입니다."),

    /**
     * 6000: order service error.
     */

    // order
    NO_EXIST_ORDER(HttpStatus.NOT_FOUND, false, 6001, "존재하지 않는 주문입니다."),
    NO_UPDATE_ORDER_AUTHORITY(HttpStatus.BAD_REQUEST, false, 6002, "주문 수정 권한이 없습니다."),
    NO_DELETE_ORDER_AUTHORITY(HttpStatus.BAD_REQUEST, false, 6003, "주문 삭제 권한이 없습니다."),
    ORDER_ALREADY_CANCELLED(HttpStatus.CONFLICT, false, 6004, "이미 취소된 주문입니다."),
    ORDER_PAYMENT_FAILED(HttpStatus.BAD_REQUEST, false, 6005, "주문 결제에 실패했습니다."),
    INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST, false, 6006, "유효하지 않은 주문 상태입니다."),
    NO_ORDER_ITEM(HttpStatus.NOT_FOUND, false, 6007, "주문 항목이 존재하지 않습니다."),
    ORDER_ITEM_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, false, 6008, "주문한 상품의 재고가 부족합니다."),
    ORDER_CANNOT_BE_MODIFIED(HttpStatus.BAD_REQUEST, false, 6009, "현재 상태의 주문은 수정할 수 없습니다."),
    ORDER_ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, false, 6010, "배송 주소를 찾을 수 없습니다."),
    ORDER_PAYMENT_CANCELLED(HttpStatus.CONFLICT, false, 6011, "주문 결제가 취소되었습니다."),
    ORDER_SHIPPING_FAILED(HttpStatus.BAD_REQUEST, false, 6012, "배송 처리 중 오류가 발생했습니다.");

    private final HttpStatusCode httpStatusCode;
    private final boolean isSuccess;
    private final int code;
    private final String message;
}