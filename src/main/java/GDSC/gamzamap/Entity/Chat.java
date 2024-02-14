package GDSC.gamzamap.Entity;

import GDSC.gamzamap.Enum.MessageType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.awt.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table
public class Chat {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @Column(name = "chatroom_id")
    private Long roomId;

    @Column(name = "member_id")
    private Long memberId;

    private String nickName;

    @Column(columnDefinition = "TEXT")
    private String message;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime sendTime;

    @Enumerated(EnumType.STRING)
    private MessageType type;


    @Builder
    public Chat(Long id, Long roomId, Long memberId, String nickName,String message, LocalDateTime sendTime, MessageType type) {
        this.id = id;
        this.roomId = roomId;
        this.nickName=nickName;
        this.memberId = memberId;
        this.message = message;
        this.sendTime = sendTime;
        this.type = type;
    }
}