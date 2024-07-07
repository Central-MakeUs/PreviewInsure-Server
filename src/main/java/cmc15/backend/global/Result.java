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
    NOT_FOUND_USER(-1001, "계정을 찾을 수 없습니다.");

    private final int code;
    private final String message;

    Result(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
