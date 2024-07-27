package cmc15.backend.domain.account.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class AccountInsurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountInsuranceId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @Enumerated(STRING)
    private InsuranceType insuranceType;

    private String insuranceCompany;
}
