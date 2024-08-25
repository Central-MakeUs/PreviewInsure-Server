package cmc15.backend.domain.account.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class FavoriteInsurance {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long favoriteInsuranceId;

    @ManyToOne(fetch = LAZY)
    private Account account;

    @Enumerated(STRING)
    private InsuranceType insuranceType;
}
