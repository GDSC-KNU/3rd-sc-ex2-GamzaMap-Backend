package GDSC.gamzamap.Repository;

import GDSC.gamzamap.Entity.Member;
import GDSC.gamzamap.Entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findById(Long id);
}
