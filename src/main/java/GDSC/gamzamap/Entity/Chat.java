package GDSC.gamzamap.Entity;

import GDSC.gamzamap.Enum.MessageType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table

public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @OneToOne
    private Member member;

    @Column(columnDefinition = "TEXT")
    private String message;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime sendData;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Builder
    public Chat(ChatRoom chatRoom, Member member, String message, LocalDateTime sendDate, MessageType type) {
        this.chatRoom = chatRoom;
        this.member = member;
        this.message = message;
        this.sendData = LocalDateTime.now();
        this.type = type;
    }

    public static Chat createChat(ChatRoom chatRoom, Member member, String message, LocalDateTime sendDate, MessageType type) {
        return Chat.builder()
                .chatRoom(chatRoom)
                .member(member)
                .message(message)
                .sendDate(sendDate)
                .type(type)
                .build();
    }
}