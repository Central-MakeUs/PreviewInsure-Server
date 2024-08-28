package cmc15.backend.domain.qnaboard.dto.request;

import cmc15.backend.domain.account.entity.InsuranceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class QnaBoardRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public static class Input {
        private String quesion;
        private Boolean isShare;
        private InsuranceType insuranceType;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public static class Update {
        private Long quesionId;
        private String quesion;
    }
}
