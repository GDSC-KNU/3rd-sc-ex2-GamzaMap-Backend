package GDSC.gamzamap.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table

public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private ChatRoom chatRoom;

    @ManyToOne
    private Boss boss;

    private String category;

    @Column(name = "store_name")
    private String storeName;

    @Builder
    public Store(Long id, ChatRoom chatRoom, Boss boss, String category, String storeName) {
        this.id = id;
        this.chatRoom = chatRoom;
        this.boss = boss;
        this.category = category;
        this.storeName = storeName;
    }
}
