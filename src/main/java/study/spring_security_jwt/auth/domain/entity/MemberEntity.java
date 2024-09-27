package study.spring_security_jwt.auth.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "Member")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty @NotNull @Size(min=3, max=10)
    @Column(name = "user_name", nullable = false)
    private String username;

    @NotEmpty @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotEmpty @NotNull @Email
    @Column(name = "email", nullable = false)
    private String email;

    // role은 회원가입 시 입력하게 할지?
    // admin, 일반사용자, 일반사용자중에서도 연주자,참여자 뭐 이런거 등등..
    @Column(name = "role", nullable = false)
    private String role;

}
