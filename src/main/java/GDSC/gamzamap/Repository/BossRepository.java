package GDSC.gamzamap.Repository;

import GDSC.gamzamap.Entity.Boss;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BossRepository extends JpaRepository<Boss, Long> {
    boolean existsByStoreName(String store_name);
    boolean existsByStoreNum(String store_num);
}
