package GDSC.gamzamap.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;
@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    private String nickname;

    @Column(nullable = false)
    @NotEmpty
    private String password;

    @Email(message = "올바른 메일 형식 입력 바람")
    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "올바른 메일 형식 입력 바람")
    @NotEmpty
    private String email;

    private String refreshToken;

    @Column
    private String role;

    @Builder
    public Member(Long id, String nickname, String password, String email, String role, String refreshToken) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.role = (role != null && !role.isEmpty()) ? role : "USER";
        this.refreshToken = refreshToken;
    }

    public static Member createMember(Long id) {
        Member member = new Member();
        member.setId(id);
        return member;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }
}
