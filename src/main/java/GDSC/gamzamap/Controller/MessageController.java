package GDSC.gamzamap.Controller;
import GDSC.gamzamap.Dto.ChatDto;
import GDSC.gamzamap.Entity.Chat;
import GDSC.gamzamap.Enum.MessageType;
import GDSC.gamzamap.Entity.Member;
import GDSC.gamzamap.Repository.ChatRepository;
import GDSC.gamzamap.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MemberRepository memberRepository;
    private final SimpMessageSendingOperations sendingOperations;
    private final ChatRepository chatRepository;


    @MessageMapping("/chat/message")
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
