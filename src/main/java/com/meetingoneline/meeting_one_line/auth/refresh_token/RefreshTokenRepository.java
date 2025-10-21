package com.meetingoneline.meeting_one_line.auth.refresh_token;

import com.meetingoneline.meeting_one_line.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    void deleteByUser(UserEntity user);
}
