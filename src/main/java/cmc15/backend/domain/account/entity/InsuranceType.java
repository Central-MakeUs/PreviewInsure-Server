package cmc15.backend.domain.account.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InsuranceType {

    LF("생명 보험"), // = 종신 보험
    HE("건강 보험"), // = 질병 보험
    CI("CI 보험"),
    TE("치아 보험"),
    TD("간병/치매 보험"),
    PA("상해 보험"),
    PI("연금 보험"),
    ED("교육 보험"),
    SI("저축 보험"),
    DR("운전자 보험"),
    AN("애견 보험"),
    DE("그 외");

    private final String typeContent;
}
