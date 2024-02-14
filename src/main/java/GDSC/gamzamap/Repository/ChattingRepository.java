package GDSC.gamzamap.Repository;

import GDSC.gamzamap.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChattingRepository extends JpaRepository<Chatting, Long> {
    Optional<Chatting> findByChattingRelationshipMemberAndChattingRelationshipChatRoom(Member member, ChatRoom chatRoom);
    List<Chatting> findAllByChattingRelationshipMemberId(Long memberId);
}
