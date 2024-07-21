package cmc15.backend.domain.account.response;

import cmc15.backend.domain.account.entity.Account;
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
    public static class Connection {
        private Long accountId;
        private String name;
        private String nickName;
        private String email;
        private String atk;
        private String rtk;

        public static Connection to(Account account, String atk, String rtk) {
            return Connection.builder()
                    .accountId(account.getAccountId())
                    .name(account.getName())
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
}
