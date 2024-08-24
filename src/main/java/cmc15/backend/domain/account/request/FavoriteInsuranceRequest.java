package cmc15.backend.domain.account.request;

import cmc15.backend.domain.account.entity.InsuranceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class FavoriteInsuranceRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public static class Add {
        private InsuranceType insuranceType;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public static class Delete {
        private Long favoriteInsuranceId;
    }
}
