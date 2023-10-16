package com.wjy.wantedpreonboarding.exception;

public enum ErrorCode {
    // -------- 4xx --------
    REQUEST_ERROR(400,"잘못된 요청입니다."),

    // Entity Not Found
    COMPANY_NOT_FOUND(400, "해당 회사가 존재하지 않습니다."),
    MEMBER_NOT_FOUND(400, "해당 사용자가 존재하지 않습니다."),
    RECRUITMENT_NOT_FOUND(400, "해당 채용공고가 존재하지 않습니다."),

    // -------- 5xx --------
    INTERNAL_SERVER_ERROR(500,"서버에 문제가 발생했습니다.");
    ;


    private final int status;
    private final String message;
    ErrorCode(final int status, String message){
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
