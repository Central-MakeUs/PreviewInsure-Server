package cmc15.backend.domain.account.request;

import cmc15.backend.domain.account.entity.InsuranceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FavoriteInsuranceRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Add {
        private InsuranceType insuranceType;
    }
}
