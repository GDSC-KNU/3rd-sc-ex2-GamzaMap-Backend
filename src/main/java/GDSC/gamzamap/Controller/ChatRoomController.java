package GDSC.gamzamap.Controller;
import GDSC.gamzamap.Service.ChattingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChattingService chattingService;

    // 내가 참여중인 채팅방 목록 반환
    @GetMapping("/rooms/{member_id}")
    public List<Long> myChatRoomList(@PathVariable Long memberId) {
        List<Long> myChatRooms=chattingService.chattingList(memberId);
        return myChatRooms;
    }

    // 채팅방 입장했을때 뜨는 채팅창
    // 입장과 동시에 내가 참여중인 채팅방에 관계 추가됨
    @GetMapping("/room/enter/{roomId}")
    public ResponseEntity<HttpStatus> roomEnter(@PathVariable Long roomId) {
        chattingService.addChatting(roomId);
        log.info("채팅방에 들어왔습니다.");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //채팅방 나갔을때
    @GetMapping("/room/out/{roomId}")
    public ResponseEntity<HttpStatus> deleteChatting(@PathVariable Long roomId){
        chattingService.deleteChatting(roomId);
        log.info("채팅방을 나갔습니다.");

        return new ResponseEntity<>(HttpStatus.OK);
    }


}