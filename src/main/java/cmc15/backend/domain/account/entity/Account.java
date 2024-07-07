package cmc15.backend.domain.account.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Account {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long accountId;

    @NotBlank(message = "필드가 null이 아니고, 비어 있지 않으며, 공백 문자만으로 이루어지지 않아야 합니다.")
    private String name;

    @NotBlank(message = "필드가 null이 아니고, 비어 있지 않으며, 공백 문자만으로 이루어지지 않아야 합니다.")
    private String nickName;

    @NotBlank(message = "필드가 null이 아니고, 비어 있지 않으며, 공백 문자만으로 이루어지지 않아야 합니다.")
    private String email;

    @NotBlank(message = "필드가 null이 아니고, 비어 있지 않으며, 공백 문자만으로 이루어지지 않아야 합니다.")
    private String password;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public Account(Long accountId, String name, String nickName, String email, String password, Authority authority) {
        this.accountId = accountId;
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.password = password;
        this.authority = authority;
    }
}
