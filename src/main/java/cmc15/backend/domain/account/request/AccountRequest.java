package cmc15.backend.domain.account.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AccountRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Register {
        @NotBlank(message = "필드가 null이 아니고, 비어 있지 않으며, 공백 문자만으로 이루어지지 않아야 합니다.")
        private String nickName;

        @NotBlank(message = "필드가 null이 아니고, 비어 있지 않으며, 공백 문자만으로 이루어지지 않아야 합니다.")
        private String email;

        @NotBlank(message = "필드가 null이 아니고, 비어 있지 않으며, 공백 문자만으로 이루어지지 않아야 합니다.")
        private String password;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Age {
        private Integer year;
        private Integer month;
    }
}
