package com.meetingoneline.meeting_one_line.meeting.entity;

import com.meetingoneline.meeting_one_line.global.entity.SoftDeletableEntity;
import com.meetingoneline.meeting_one_line.user.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "meetings")
@SQLRestriction("deleted_at IS NULL")
public class MeetingEntity extends SoftDeletableEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @Column(columnDefinition = "TEXT")
    private String summary;

    // 연관관계
    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SpeakerEntity> speakers = new ArrayList<>();

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KeywordEntity> keywords = new ArrayList<>();

    public enum Status {
        UPLOADED, PROCESSING, COMPLETED
    }

    public MeetingEntity(UserEntity user, String title, LocalDateTime date, String filePath) {
        this.user = user;
        this.title = title;
        this.date = date;
        this.filePath = filePath;
        this.status = Status.UPLOADED;
    }
}
