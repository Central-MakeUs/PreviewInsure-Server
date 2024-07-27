package cmc15.backend.domain.account.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InsuranceCompany {

    MERITZ_FIRE("메리츠 화재"),
    HANA("하나 보험"),
    ETC("그 외");

    private final String companyName;
}
