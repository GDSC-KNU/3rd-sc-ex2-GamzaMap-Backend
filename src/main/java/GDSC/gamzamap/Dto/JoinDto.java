package GDSC.gamzamap.Dto;

import GDSC.gamzamap.Entity.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class JoinDto {
    private String email;
    private String password;
    private String nickname;
    private String role;

    public Member toEntity(){
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .role(role)
                .build();
    }
}
