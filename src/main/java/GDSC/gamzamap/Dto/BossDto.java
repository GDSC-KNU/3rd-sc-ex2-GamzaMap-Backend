package GDSC.gamzamap.Dto;


import GDSC.gamzamap.Entity.Boss;
import GDSC.gamzamap.Entity.Member;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BossDto {
    private Long id;
    private Long memberId; //dto는 데이터 전송이 목적. 따로 memberId를 생성하여 Member에서 id만 전송
    private String storeNum;
    private String storeName;

    public static BossDto toDto(Boss boss) {
        return BossDto.builder()
                .id(boss.getId())
                .memberId(boss.getMember().getId())
                .storeName(boss.getStoreName())
                .storeNum(boss.getStoreNum())
                .build();
    }

    public Boss toEntity(){
        //엔티티에는 Long이 아닌 Member가 필요하므로 memberId만 저장된 Member 엔티티 생성
        //외부클래스에서 Member엔티티 생성 불가능
        Member member = Member.createMember(memberId);

        return Boss.builder()
                .id(id)
                .member(member)
                .storeName(storeName)
                .storeNum(storeNum)
                .build();
    }
}
