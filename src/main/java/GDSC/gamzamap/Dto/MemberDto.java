package GDSC.gamzamap.Dto;

import GDSC.gamzamap.Entity.Member;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class MemberDto {
    private Long id;
    private String email;
    private String nickname;

    static public MemberDto toDto(Member member){
        return MemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }

    public Member toEntity(){
        return Member.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .build();
    }
}
