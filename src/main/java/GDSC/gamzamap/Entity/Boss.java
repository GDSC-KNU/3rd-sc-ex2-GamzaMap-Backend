package GDSC.gamzamap.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")

public class Boss {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boss_id", updatable = false, unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")  // 외부 키 설정
    private Member member;

    @Column(name = "store_num", nullable = false)
    private String storeNum;

    @Column(name = "store_name", nullable = false)
    private String storeName;

    @Builder
    public Boss(Long id, Member member, String storeNum, String storeName) {
        this.id = id;
        this.member = member;
        this.storeNum = storeNum;
        this.storeName = storeName;
    }
}
