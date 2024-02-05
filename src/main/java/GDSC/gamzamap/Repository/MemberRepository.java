package GDSC.gamzamap.Repository;

import GDSC.gamzamap.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByRefreshToken(String refreshToken);
    Optional<Member> findById(Long id);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
