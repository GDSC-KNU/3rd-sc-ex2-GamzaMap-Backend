package GDSC.gamzamap.Entity.Embeded;

import GDSC.gamzamap.Entity.ChatRoom;
import GDSC.gamzamap.Entity.Member;
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
public class ChoiceRelationship implements Serializable {
    @ManyToOne
    private Member member;

    @ManyToOne
    private ChatRoom chatRoom;

    @Builder
    public ChoiceRelationship(Member member, ChatRoom chatRoom){
        this.member = member;
        this.chatRoom = chatRoom;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChoiceRelationship that = (ChoiceRelationship) o;
        return Objects.equals(member, that.member) && Objects.equals(chatRoom, that.chatRoom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, chatRoom);
    }
}
