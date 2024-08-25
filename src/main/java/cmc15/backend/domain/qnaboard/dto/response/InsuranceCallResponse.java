package cmc15.backend.domain.qnaboard.dto.response;

import lombok.*;

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
    @Builder
    @ToString
    public static class InsuranceLink {
        private String insuranceCompany;
        private String link;

        public static InsuranceLink to(String insuranceCompany, String link) {
            return InsuranceLink.builder()
                    .insuranceCompany(insuranceCompany)
                    .link(link)
                    .build();
        }
    }
}
