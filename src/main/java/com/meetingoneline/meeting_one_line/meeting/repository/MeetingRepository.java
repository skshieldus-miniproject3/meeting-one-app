package com.meetingoneline.meeting_one_line.meeting.repository;

import com.meetingoneline.meeting_one_line.meeting.entity.MeetingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface MeetingRepository extends JpaRepository<MeetingEntity, UUID>, JpaSpecificationExecutor<MeetingEntity> {
    Optional<MeetingEntity> findByIdAndDeletedAtIsNull(UUID id);
}
