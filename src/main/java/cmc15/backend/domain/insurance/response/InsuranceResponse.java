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
        private String insuranceImage;
        private String insuranceCompany;
        private String insuranceContent;
        private Double insuranceRate;
        private Integer price;
        private String link;

        public static Recommend to(final Insurance insurance, final String gender) {

            String insuranceImage = "";
            if (insurance.getInsuranceCompany().equals("삼성생명")) {
                insuranceImage = "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%89%E1%85%A1%E1%86%B7%E1%84%89%E1%85%A5%E1%86%BC%E1%84%92%E1%85%AA%E1%84%8C%E1%85%A2.png";
            }

            return Recommend.builder()
                    .insuranceId(insurance.getInsuranceId())
                    .insuranceImage(insuranceImage)
                    .insuranceCompany(insurance.getInsuranceCompany())
                    .insuranceContent(insurance.getInsuranceContent())
                    .insuranceRate(insurance.getInsuranceRate())
                    .price((gender.equals("M")) ? insurance.getMalePrice() : insurance.getFemalePrice())
                    .link(insurance.getLink())
                    .build();
        }
    }
}
