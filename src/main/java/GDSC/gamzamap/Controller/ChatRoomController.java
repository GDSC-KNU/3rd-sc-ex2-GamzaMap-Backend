package GDSC.gamzamap.Controller;
import GDSC.gamzamap.Dto.MemberDto;
import GDSC.gamzamap.Service.ChattingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "채팅방 관련 API")
public class ChatRoomController {

    private final ChattingService chattingService;

    // 내가 참여중인 채팅방 목록 반환
    @GetMapping("/rooms")
    @Operation(summary = "참여중인 채팅방 반환 API", description = "참여중인 채팅방")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "내부 서버 오류",
                    content = @Content(schema = @Schema(implementation = HttpStatus.class)))
    })
    public List<Long> myChatRoomList() {
        List<Long> myChatRooms=chattingService.chattingList();
        return myChatRooms;
    }

    // 채팅방 입장했을때 뜨는 채팅창
    // 입장과 동시에 내가 참여중인 채팅방에 관계 추가됨
    @GetMapping("/room/enter/{room_id}")
    @Operation(summary = "채팅방 입장 API", description = "채팅방에 입장하고 참여 중인 채팅방에 관계를 추가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 채팅방에 입장했습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 채팅방입니다.")
    })
    public ResponseEntity<String> roomEnter(@PathVariable Long room_id) {
        chattingService.addChatting(room_id);
        log.info("채팅방에 들어왔습니다.");

        String message = room_id+"채팅방 입장 완료";
        return ResponseEntity.ok().body(message);
    }

    //채팅방 나갔을때
    @GetMapping("/room/out/{room_id}")
    @Operation(summary = "채팅방 나가기 API", description = "채팅방에서 나가고 참여 중인 채팅방 관계를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 채팅방을 나갔습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 채팅방입니다.")
    })
    public ResponseEntity<String> deleteChatting(@PathVariable Long room_id){
        chattingService.deleteChatting(room_id);
        log.info("채팅방을 나갔습니다.");

        String message = room_id+"채팅방 나가기 완료";
        return ResponseEntity.ok().body(message);
    }


}