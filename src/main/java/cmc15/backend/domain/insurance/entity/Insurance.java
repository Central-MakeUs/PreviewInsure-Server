package cmc15.backend.domain.insurance.entity;

import cmc15.backend.domain.account.entity.InsuranceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class Insurance {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long insuranceId;

    @NotBlank
    private String insuranceCompany;

    @NotNull
    @Enumerated(STRING)
    private InsuranceType insuranceType;

    @NotBlank
    private String insuranceContent;

    @NotNull
    private Integer malePrice;

    @NotNull
    private Integer femalePrice;

    @NotNull
    private Double insuranceRate;

    @NotNull
    private Integer limitAge;

    @NotNull
    private String link;
}
