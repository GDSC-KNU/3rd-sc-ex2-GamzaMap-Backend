package GDSC.gamzamap.Controller;
import GDSC.gamzamap.Dto.ChatDto;
import GDSC.gamzamap.Entity.Chat;
import GDSC.gamzamap.Enum.MessageType;
import GDSC.gamzamap.Entity.Member;
import GDSC.gamzamap.Repository.ChatRepository;
import GDSC.gamzamap.Repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "메시지 관련 API")
@CrossOrigin(origins = "http://192.168.0.13:5173")
public class MessageController {

    private final MemberRepository memberRepository;
    private final SimpMessageSendingOperations sendingOperations;
    private final ChatRepository chatRepository;


    @MessageMapping("/chat/message")
    @Operation(summary = "채팅 메시지 전송", description = "채팅 메시지를 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전송 성공", content = @Content(schema = @Schema(implementation = ChatDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public void enter(ChatDto message) {
        if (message.getType().equals(MessageType.ENTER)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Member member = memberRepository.findByEmail(email).orElse(null);

            message.setMessage(message.getNickName() + "님이 입장하였습니다.");

        }

        Chat chatEntity=message.toEntity();
        chatRepository.save(chatEntity);

        sendingOperations.convertAndSend("/topic/chat/room/" + message.getRoomId(), message);
    }
}
