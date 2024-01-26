package GDSC.gamzamap.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table

public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Boss boss;

    private String storeName;

    @Builder
    public ChatRoom(Long id, Boss boss, String storeName) {
        this.id = id;
        this.boss = boss;
        this.storeName = storeName;
    }
}
