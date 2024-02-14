package GDSC.gamzamap.Dto;


import GDSC.gamzamap.Entity.Chat;
import GDSC.gamzamap.Enum.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {


    private MessageType type;
    //채팅방 ID
    private Long roomId;
    //보내는 사람 id
    private Long sender;
    private String nickName;
    //내용
    private String message;
    private LocalDateTime time;

    public Chat toEntity(){
        return Chat.builder()
                .type(type)
                .roomId(roomId)
                .memberId(sender)
                .nickName(nickName)
                .message(message)
                .sendTime(time)
                .build();
    }
}