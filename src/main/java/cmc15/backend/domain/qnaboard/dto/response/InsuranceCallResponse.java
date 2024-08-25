package cmc15.backend.domain.qnaboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class InsuranceCallResponse {
    private String context;
    private List<InsuranceLink> links;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public static class InsuranceLink {
        private String insuranceCompany;
        private String link;
    }
}
