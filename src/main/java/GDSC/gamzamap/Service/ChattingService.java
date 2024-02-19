package GDSC.gamzamap.Service;

import GDSC.gamzamap.Entity.*;
import GDSC.gamzamap.Entity.Embeded.ChattingRelationship;
import GDSC.gamzamap.Repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChattingService {
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChattingRepository chattingRepository;

    public ChattingService(MemberRepository memberRepository, ChatRoomRepository chatRoomRepository, ChattingRepository chattingRepository) {
        this.memberRepository = memberRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chattingRepository = chattingRepository;
    }


    public List<Long> chattingList(Long member_id){
        // 내가참여중인 채팅방 목록 불러오기
        List<Chatting> chattingList = chattingRepository.findAllByChattingRelationshipMemberId(member_id);

        return chattingList.stream()
                .map(choice -> choice.getChattingRelationship().getChatRoom().getId())
                .collect(Collectors.toList());
    }


    public ResponseEntity<HttpStatus> addChatting(Long room_id){

        // 내가 참여중인 채팅방 추가
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email).orElse(null);
        ChatRoom chatRoom = chatRoomRepository.findById(room_id).orElse(null);

        ChattingRelationship chattingRelationship = new ChattingRelationship(member, chatRoom);
        Chatting chatting=new Chatting();
        chatting.setChattingRelationship(chattingRelationship);
        chattingRepository.save(chatting);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> deleteChatting(Long room_id){

        // 내가 참여중인 채팅방 나가기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email).orElse(null);
        ChatRoom chatRoom = chatRoomRepository.findById(room_id).orElse(null);

        Chatting chatting = chattingRepository.findByChattingRelationshipMemberAndChattingRelationshipChatRoom(member, chatRoom).orElse(null);
        if (chatting != null) {
            chattingRepository.delete(chatting);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
