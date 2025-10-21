package com.meetingoneline.meeting_one_line.meeting.repository;

import com.meetingoneline.meeting_one_line.meeting.entity.MeetingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MeetingRepository extends JpaRepository<MeetingEntity, UUID> {
}
