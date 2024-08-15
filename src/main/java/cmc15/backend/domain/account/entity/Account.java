package cmc15.backend.domain.account.entity;

import cmc15.backend.domain.qnaboard.entity.QnaBoard;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Account {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long accountId;

    @NotBlank(message = "필드가 null이 아니고, 비어 있지 않으며, 공백 문자만으로 이루어지지 않아야 합니다.")
    private String email;

    @NotBlank(message = "필드가 null이 아니고, 비어 있지 않으며, 공백 문자만으로 이루어지지 않아야 합니다.")
    private String password;

    private String nickName;

    private Integer age;

    @Column(length = 1)
    private String gender;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    private String appleAccount;

    @OneToMany(mappedBy = "account")
    private Collection<QnaBoard> qnaBoard;

    public Collection<QnaBoard> getQnaBoard() {
        return qnaBoard;
    }

    public void setQnaBoard(Collection<QnaBoard> qnaBoard) {
        this.qnaBoard = qnaBoard;
    }

    public void updateAge(Integer age) {
        this.age = age;
    }

    public void updateGender(String gender) {
        this.gender = gender;
    }

    public void updateNickname(String nickname) {
        this.nickName = nickname;
    }
}
