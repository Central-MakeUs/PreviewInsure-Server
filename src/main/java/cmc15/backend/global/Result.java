package cmc15.backend.global;

import lombok.Getter;

@Getter
public enum Result {
    // 공통
    OK(0, "성공"),
    FAIL(-1, "실패"),

    // Global 실패코드
    UNEXPECTED_EXCEPTION(-500,"예상치 못한 예외가 발생했습니다."),

    // 계정 관련
    NOT_FOUND_USER(-1001, "계정을 찾을 수 없습니다."),
    NOT_EMPTY_GENDER(-1002, "성별은 NULL은 허용되지만 빈 문자열은 허용되지 않습니다."),
    ALREADY_ADD_INSURANCE(-1003, "이미 등록된 보험 입니다."),

    // 질문게시판 관련
    NOT_EMPTY_MESSAGE(-2001, "질문은 비어있을 수 없습니다."),
    MESSAGE_SIZE_ERROR(-2002, "질문은 최소 4글자 이상, 최대 300 글자 이하입니다.");

    private final int code;
    private final String message;

    Result(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
