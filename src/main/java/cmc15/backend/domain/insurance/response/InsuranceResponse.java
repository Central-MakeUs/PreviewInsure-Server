package cmc15.backend.domain.insurance.response;

import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.entity.AccountInsurance;
import cmc15.backend.domain.insurance.entity.Insurance;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;
import static lombok.AccessLevel.PRIVATE;

public class InsuranceResponse {

    @JsonInclude(NON_NULL)
    @AllArgsConstructor(access = PRIVATE)
    @NoArgsConstructor(access = PRIVATE)
    @Getter
    @Builder
    public static class MapDetail {
        private String nickName;
        private Boolean isSubscribed;
        private String insuranceCompany;
        private String insuranceLink;
        private List<Recommend> insuranceRecommends;

        public static MapDetail to(final Account account, final AccountInsurance accountInsurance, final List<Recommend> recommends) {
            return MapDetail.builder()
                    .nickName(account.getNickName())
                    .isSubscribed(accountInsurance.getAccountInsuranceId() != -1)
                    .insuranceCompany((accountInsurance.getAccountInsuranceId() != -1) ? accountInsurance.getInsuranceCompany() : null)
                    .insuranceLink((accountInsurance.getAccountInsuranceId() != -1) ? "link" : null)
                    .insuranceRecommends(recommends)
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor(access = PRIVATE)
    @Getter
    @Builder
    public static class Recommend {
        private Long insuranceId;
        private String insuranceCompany;
        private String insuranceContent;
        private Double insuranceRate;
        private Integer price;
        private String link;

        public static Recommend to(final Insurance insurance, final String gender) {

            return Recommend.builder()
                    .insuranceId(insurance.getInsuranceId())
                    .insuranceCompany(insurance.getInsuranceCompany())
                    .insuranceContent(insurance.getInsuranceContent())
                    .insuranceRate(insurance.getInsuranceRate())
                    .price((gender.equals("M")) ? insurance.getMalePrice() : insurance.getFemalePrice())
                    .link(insurance.getLink())
                    .build();
        }
    }
}
