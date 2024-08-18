package cmc15.backend.domain.account.response;

import cmc15.backend.domain.account.entity.FavoriteInsurance;
import cmc15.backend.domain.account.entity.InsuranceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

public class FavoriteInsuranceResponse {

    @AllArgsConstructor(access = PRIVATE)
    @NoArgsConstructor(access = PRIVATE)
    @Getter
    @Builder
    public static class Detail {
        private Long favoriteInsuranceId;
        private InsuranceType insuranceType;

        public static Detail to(FavoriteInsurance favoriteInsurance) {
            return Detail.builder()
                    .favoriteInsuranceId(favoriteInsurance.getFavoriteInsuranceId())
                    .insuranceType(favoriteInsurance.getInsuranceType())
                    .build();
        }
    }
}
