package study.spring_security_jwt.auth.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.spring_security_jwt.auth.domain.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {

    // 중복 검증용 메소드
    Boolean existsByUsername(String username);

    MemberEntity findByUsername(String username);
}
