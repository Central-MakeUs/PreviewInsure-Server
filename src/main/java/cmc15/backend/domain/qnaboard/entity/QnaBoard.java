package cmc15.backend.domain.qnaboard.entity;

import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.entity.InsuranceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class QnaBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qnaBoardId;

    @ManyToOne(fetch = LAZY)
    private Account account;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String quesion;

    @NotBlank
    @Lob
    @Column(columnDefinition = "TEXT")
    private String answer;

    @NotNull
    private Boolean isShare;

    @Enumerated(STRING)
    private InsuranceType insuranceType;
}
