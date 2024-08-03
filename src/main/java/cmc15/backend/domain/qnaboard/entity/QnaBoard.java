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
    private String quesion;

    @NotBlank
    private String answer;

    @NotNull
    private Boolean isShare;

    @Enumerated(STRING)
    private InsuranceType insuranceType;

    // Join 하지 않고 하나의 String으로 관리 (해당 정보로 연관관계 및 Join이 되어서 불필요한 리소스가 추가로 듬에 따라 이와 같이 설정)
    @Column(name = "recommend_links", nullable = true)
    private String recommendLinks;
}
