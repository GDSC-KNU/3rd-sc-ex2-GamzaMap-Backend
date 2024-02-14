package GDSC.gamzamap.Dto;

import GDSC.gamzamap.Entity.ChatRoom;
import GDSC.gamzamap.Entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomDto {

    private Long roomId;
    private String roomName;


    public ChatRoom toEntity(){
        return ChatRoom.builder()
                .id(roomId)
                .roomName(roomName)
                .build();
    }

}