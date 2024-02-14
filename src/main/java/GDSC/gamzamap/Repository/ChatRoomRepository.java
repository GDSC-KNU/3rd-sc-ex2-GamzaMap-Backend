package GDSC.gamzamap.Repository;

import GDSC.gamzamap.Entity.ChatRoom;
import GDSC.gamzamap.Entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findById(Long id);
}
