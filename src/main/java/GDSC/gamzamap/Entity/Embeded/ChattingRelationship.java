package GDSC.gamzamap.Entity.Embeded;


import GDSC.gamzamap.Entity.ChatRoom;
import GDSC.gamzamap.Entity.Member;
import GDSC.gamzamap.Entity.Store;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@Getter
public class ChattingRelationship implements Serializable {

    @ManyToOne
    private Member member;

    @ManyToOne
    private ChatRoom chatRoom;

    @Builder
    public ChattingRelationship(Member member, ChatRoom chatRoom){
        this.member = member;
        this.chatRoom=chatRoom;
    }


    @Override
    public int hashCode() {
        return Objects.hash(member, chatRoom);
    }
}
