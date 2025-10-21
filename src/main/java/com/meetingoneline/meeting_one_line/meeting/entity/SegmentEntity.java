package com.meetingoneline.meeting_one_line.meeting.entity;

import com.meetingoneline.meeting_one_line.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "segments")
public class SegmentEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speaker_id", nullable = false)
    private SpeakerEntity speaker;

    @Column(name = "start_time", nullable = false)
    private Float startTime;

    @Column(name = "end_time", nullable = false)
    private Float endTime;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;
}
