package com.meetingoneline.meeting_one_line.auth.refresh_token;

import com.meetingoneline.meeting_one_line.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    /**
     * @Modifying 어노테이션을 추가하여 벌크 삭제 쿼리임을 명시합니다.
     * Spring Data JPA에서 파생된 delete 쿼리는 @Transactional과 함께 사용될 때
     * 이 어노테이션이 있어야 안전하게 실행됩니다.
     */
    @Modifying
    void deleteByUser(UserEntity user);
}
