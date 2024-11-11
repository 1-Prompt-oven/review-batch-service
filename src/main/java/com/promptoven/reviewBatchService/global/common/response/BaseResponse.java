package com.promptoven.reviewBatchService.global.common.response;

import static com.promptoven.reviewBatchService.global.common.response.BaseResponseStatus.SUCCESS;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public record BaseResponse<T>(HttpStatusCode httpStatus, Boolean isSuccess, String message, int code, T result) {
    // 필요값 : Http 상태코드, 성공여부, 메시지, 에러코드, 결과값

    /**
     * 1. Return 객체가 필요한 경우 -> 성공.
     *
     * @param result 반환해야 하는 결과 객체
     */
    public BaseResponse(T result) {
        this(HttpStatus.OK, true, SUCCESS.getMessage(), SUCCESS.getCode(), result);
    }

    /**
     * 2. Return 객체가 필요 없는 경우 -> 성공.
     */
    public BaseResponse() {
        this(HttpStatus.OK, true, SUCCESS.getMessage(), SUCCESS.getCode(), null);
    }

    /**
     * 요청에 실패한 경우. 에러 메시지를 result에 추가할 수 있는 생성자
     *
     * @param status 요청 상태
     * @param result 에러 메시지
     */
    public BaseResponse(BaseResponseStatus status, T result) {
        this(status.getHttpStatusCode(), false, status.getMessage(), status.getCode(), result);
    }

    /**
     * 3. 요청에 실패한 경우.
     *
     * @param status 요청 상태
     */
    public BaseResponse(BaseResponseStatus status) {
        this(status.getHttpStatusCode(), false, status.getMessage(), status.getCode(), null);
    }
}
