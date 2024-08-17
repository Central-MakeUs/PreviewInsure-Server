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
            return Recommend.builder()
                    .insuranceId(insurance.getInsuranceId())
                    .insuranceImage(parseInsuranceImage(insurance))
                    .insuranceCompany(insurance.getInsuranceCompany())
                    .insuranceContent(insurance.getInsuranceContent())
                    .insuranceRate(insurance.getInsuranceRate())
                    .price((gender.equals("M")) ? insurance.getMalePrice() : insurance.getFemalePrice())
                    .link(insurance.getLink())
                    .build();
        }

        private static String parseInsuranceImage(Insurance insurance) {
            String insuranceImage = switch (insurance.getInsuranceCompany()) {
                case "삼성생명" ->
                        "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%89%E1%85%A1%E1%86%B7%E1%84%89%E1%85%A5%E1%86%BC%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png";
                case "ABL생명" ->
                        "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/ABL%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png";
                case "AIA생명" ->
                        "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/AIA%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png";
                case "BNP파리바카디프생명" -> "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/BNP.png";
                case "DB생명" ->
                        "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/DB%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png";
                case "iM라이프" ->
                        "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/iM%E1%84%85%E1%85%A1%E1%84%8B%E1%85%B5%E1%84%91%E1%85%B3.png";
                case "KB라이프생명" ->
                        "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/KB%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC%E1%84%87%E1%85%A9%E1%84%92%E1%85%A5%E1%86%B7.png";
                case "KDB생명" ->
                        "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/KDB%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png";
                case "NH농협생명" ->
                        "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/NH%E1%84%82%E1%85%A9%E1%86%BC%E1%84%92%E1%85%A7%E1%86%B8%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png";
                case "교보라이프플래닛생명" ->
                        "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%80%E1%85%AD%E1%84%87%E1%85%A9%E1%84%85%E1%85%A1%E1%84%8B%E1%85%B5%E1%84%91%E1%85%B3%E1%84%91%E1%85%B3%E1%86%AF%E1%84%85%E1%85%A2%E1%84%82%E1%85%B5%E1%86%BA%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png";
                case "교보생명" ->
                        "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%80%E1%85%AD%E1%84%87%E1%85%A9%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png";
                case "동양생명" ->
                        "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%83%E1%85%A9%E1%86%BC%E1%84%8B%E1%85%A3%E1%86%BC%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png";
                case "라이나생명" ->
                        "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%85%E1%85%A1%E1%84%8B%E1%85%B5%E1%84%82%E1%85%A1%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png";
                case "메트라이프생명" ->
                        "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%86%E1%85%B5%E1%84%83%E1%85%B3%E1%84%85%E1%85%A1%E1%84%8B%E1%85%B5%E1%84%91%E1%85%B3.png";
                case "미래에셋생명" ->
                        "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%86%E1%85%B5%E1%84%85%E1%85%A2%E1%84%8B%E1%85%A6%E1%84%89%E1%85%A6%E1%86%BA%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png";
                case "신한라이프생명" ->
                        "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%89%E1%85%B5%E1%86%AB%E1%84%92%E1%85%A1%E1%86%AB%E1%84%85%E1%85%A1%E1%84%8B%E1%85%B5%E1%84%91%E1%85%B3.png";
                case "푸본현대생명" ->
                        "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%91%E1%85%AE%E1%84%87%E1%85%A9%E1%86%AB%E1%84%92%E1%85%A7%E1%86%AB%E1%84%83%E1%85%A2%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png";
                case "하나생명" ->
                        "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%92%E1%85%A1%E1%84%82%E1%85%A1%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png";
                case "한화생명" ->
                        "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%92%E1%85%A1%E1%86%AB%E1%84%92%E1%85%AA%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png";
                case "흥국생명" ->
                        "https://reviewinsue-bucket.s3.ap-northeast-2.amazonaws.com/image/%E1%84%92%E1%85%B3%E1%86%BC%E1%84%80%E1%85%AE%E1%86%A8%E1%84%89%E1%85%A2%E1%86%BC%E1%84%86%E1%85%A7%E1%86%BC.png";
                default -> "";
            };
            return insuranceImage;
        }
    }
}
