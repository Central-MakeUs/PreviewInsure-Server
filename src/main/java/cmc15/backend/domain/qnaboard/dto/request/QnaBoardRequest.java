package cmc15.backend.domain.qnaboard.dto.request;

import cmc15.backend.domain.account.entity.InsuranceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class QnaBoardRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Input {
        private String quesion;
        private Boolean isShare;
        private InsuranceType insuranceType;
    }
}
