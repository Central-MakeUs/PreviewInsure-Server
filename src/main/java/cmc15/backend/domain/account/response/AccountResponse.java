package cmc15.backend.domain.account.response;

import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.entity.AccountInsurance;
import cmc15.backend.domain.account.entity.InsuranceType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;
import static lombok.AccessLevel.PRIVATE;

public class AccountResponse {

    @AllArgsConstructor(access = PRIVATE)
    @NoArgsConstructor(access = PRIVATE)
    @Getter
    @Builder
    @JsonInclude(NON_NULL)
    public static class OAuthConnection {
        private Long accountId;
        private String atk;
        private String rtk;
        private String nickname;
        private String redirectUrl;

        public static OAuthConnection to(Account account, String atk, String rtk) {
            return OAuthConnection.builder()
                    .accountId(account.getAccountId())
                    .atk(atk)
                    .rtk(rtk)
                    .nickname(getNickname(account))
                    .build();
        }

        public static OAuthConnection toRedirect(Account account, String atk, String rtk, String redirectUrl) {
            return OAuthConnection.builder()
                    .accountId(account.getAccountId())
                    .atk(atk)
                    .rtk(rtk)
                    .nickname(getNickname(account))
                    .redirectUrl(redirectUrl)
                    .build();
        }

        private static String getNickname(Account account) {
            return account.getNickName() == null ? "null" : account.getNickName();
        }
    }

    @AllArgsConstructor(access = PRIVATE)
    @NoArgsConstructor(access = PRIVATE)
    @Getter
    @Builder
    public static class Connection {
        private Long accountId;
        private String nickName;
        private String email;
        private String atk;
        private String rtk;

        public static Connection to(Account account, String atk, String rtk) {
            return Connection.builder()
                    .accountId(account.getAccountId())
                    .nickName(account.getNickName())
                    .email(account.getEmail())
                    .atk(atk)
                    .rtk(rtk)
                    .build();
        }
    }

    @AllArgsConstructor(access = PRIVATE)
    @NoArgsConstructor(access = PRIVATE)
    @Getter
    @Builder
    public static class NickName {
        private String nickName;

        public static NickName to(String nickName) {
            return NickName.builder()
                    .nickName(nickName)
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor(access = PRIVATE)
    @Getter
    @Builder
    public static class Insurances {
        private Long accountInsuranceId;
        private InsuranceType insuranceType;
        private String insuranceCompany;

        public static AccountResponse.Insurances to(AccountInsurance accountInsurance) {
            return Insurances.builder()
                    .accountInsuranceId(accountInsurance.getAccountInsuranceId())
                    .insuranceType(accountInsurance.getInsuranceType())
                    .insuranceCompany(accountInsurance.getInsuranceCompany())
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor(access = PRIVATE)
    @Getter
    @Builder
    public static class Detail {
        private Long accountId;
        private String email;
        private String age;
        private String gender;

        public static AccountResponse.Detail to(Account account) {
            String age = "-";
            if (account.getAge() != null) {
                age = String.valueOf(account.getAge());
            }

            String gender = "선택안함";
            if (account.getGender() != null) {
                gender = account.getGender();
            }
            return Detail.builder()
                    .accountId(account.getAccountId())
                    .email(account.getEmail())
                    .age(age)
                    .gender(gender)
                    .build();
        }
    }
}
