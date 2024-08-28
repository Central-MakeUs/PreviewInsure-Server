package cmc15.backend.domain.account.request;

import cmc15.backend.domain.account.entity.InsuranceType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

public class AccountRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
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
    @ToString
    public static class Age {
        private Integer year;
        private Integer month;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public static class InsureBoarding {
        private String gender;

        @Valid
        private List<InsureBoard> insureBoards;

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @ToString
        public static class InsureBoard {
            @NotNull(message = "보험 유형은 필수 값 입니다.")
            private InsuranceType insuranceType;

            @NotNull(message = "보험 회사는 필수 값 입니다.")
            private String insuranceCompany;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public static class Nickname {
        @NotNull(message = "닉네임은 필수 값 입니다.")
        private String nickname;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public static class DeleteInsurance {
        private Long accountInsuranceId;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public static class UpdateAccountInsurance {
        private Long accountInsuranceId;
        private InsuranceType insuranceType;
        private String insuranceCompany;
    }
}
