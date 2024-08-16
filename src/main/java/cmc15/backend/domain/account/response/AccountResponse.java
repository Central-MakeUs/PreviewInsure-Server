package cmc15.backend.domain.account.response;

import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.entity.AccountInsurance;
import cmc15.backend.domain.account.entity.InsuranceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

public class AccountResponse {

    @AllArgsConstructor(access = PRIVATE)
    @NoArgsConstructor(access = PRIVATE)
    @Getter
    @Builder
    public static class OAuthConnection {
        private Long accountId;
        private String atk;
        private String rtk;
        private Boolean isRegister;
        private String nickname;

        public static OAuthConnection to(Account account, Boolean isRegister, String atk, String rtk) {
            String nickname;
            if (account.getNickName() == null) {
                nickname = "null";
            } else {
                nickname = account.getNickName();
            }

            return OAuthConnection.builder()
                    .accountId(account.getAccountId())
                    .isRegister(isRegister)
                    .atk(atk)
                    .rtk(rtk)
                    .nickname(nickname)
                    .build();
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
}
