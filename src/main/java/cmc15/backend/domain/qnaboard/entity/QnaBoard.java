package cmc15.backend.domain.qnaboard.entity;

import cmc15.backend.domain.account.entity.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

}
