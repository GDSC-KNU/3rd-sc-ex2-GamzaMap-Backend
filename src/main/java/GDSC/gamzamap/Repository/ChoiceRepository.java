package GDSC.gamzamap.Repository;

import GDSC.gamzamap.Entity.Choice;
import GDSC.gamzamap.Entity.Member;
import GDSC.gamzamap.Entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChoiceRepository extends JpaRepository<Choice, Long> {
    Optional<Choice> findByChoiceRelationshipMemberAndChoiceRelationshipStore(Member member, Store store);
    List<Choice> findAllByChoiceRelationshipMemberId(Long member_id);
}
