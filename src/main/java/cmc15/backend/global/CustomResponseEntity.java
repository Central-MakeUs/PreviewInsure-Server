package cmc15.backend.global;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CustomResponseEntity<T> {

    private int code;
    private String message;
    @JsonInclude(NON_NULL)
    private T data;

    public static <T> CustomResponseEntity<T> success(T data) {
        return CustomResponseEntity.<T>builder()
                .result(Result.OK)
                .data(data)
                .build();
    }

    public static <T> CustomResponseEntity<T> success() {
        return CustomResponseEntity.<T>builder()
                .result(Result.OK)
                .build();
    }

    public static <T> CustomResponseEntity<T> fail(String message) {
        return CustomResponseEntity.<T>builder()
                .code(Result.FAIL.getCode())
                .message(message)
                .build();
    }

    public static <T> CustomResponseEntity<T> fail(Result result) {
        return CustomResponseEntity.<T>builder()
                .result(result)
                .build();
    }

    @Builder
    public CustomResponseEntity(Result result, int code, String message, T data) {
        this.code = (result == null) ? code : result.getCode();
        this.message = (result == null) ? message : result.getMessage();
        this.data = data;
    }
}
