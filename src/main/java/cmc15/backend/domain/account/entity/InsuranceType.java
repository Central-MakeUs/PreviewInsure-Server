package cmc15.backend.domain.account.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InsuranceType {

    LI("생명 보험"),
    HE("건강 보험"),
    PR("재산 보험"),
    TE("치아 보험"),
    TR("여행 보험"),
    RE("책임 보험"),
    AN("애견 보험"),
    ED("교육 보험"),
    DR("운전자 보험");

    private final String typeContent;
}
