package GDSC.gamzamap.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "boss_id")
    private Long bossId;

    private String roomName;



    @Builder
    public ChatRoom(Long id, Long bossId, String roomName) {
        this.id = id;
        this.bossId=bossId;
        this.roomName = roomName;
    }
}
