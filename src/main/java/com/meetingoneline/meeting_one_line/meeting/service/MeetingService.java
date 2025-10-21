package com.meetingoneline.meeting_one_line.meeting.service;

import com.meetingoneline.meeting_one_line.global.exception.BusinessException;
import com.meetingoneline.meeting_one_line.global.exception.ErrorCode;
import com.meetingoneline.meeting_one_line.meeting.dto.MeetingDto;
import com.meetingoneline.meeting_one_line.meeting.entity.MeetingEntity;
import com.meetingoneline.meeting_one_line.meeting.repository.MeetingRepository;
import com.meetingoneline.meeting_one_line.user.UserEntity;
import com.meetingoneline.meeting_one_line.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;

    @Value("${file.upload-dir:./uploads/meetings}")
    private String uploadDir;

    public MeetingDto.CreateResponse uploadMeeting(UUID userId, MeetingDto.CreateRequest request) {
        UserEntity user = userRepository.findById(userId)
                                        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        MultipartFile file = request.getFile();
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_FILE);
        }

        try {
            // 1. 경로 폴더 없으면 추가
            File baseDir = new File(uploadDir).getAbsoluteFile();
            if (!baseDir.exists()) {
                boolean created = baseDir.mkdirs();
                log.info("📁 기본 업로드 폴더 생성됨: {} (성공여부: {})", baseDir, created);
            }

            // 2. 유저별 폴더
            File userDir = new File(baseDir, userId.toString());
            if (!userDir.exists()) {
                boolean created = userDir.mkdirs();
                log.info("사용자 업로드 폴더 생성됨: {} (성공여부: {})", userDir, created);
            }

            // 3. 파일명 생성
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File destination = new File(userDir, fileName);

            log.info("💾 파일 저장 시도: {}", destination.getAbsolutePath());
            file.transferTo(destination);

            // 4.DB 저장
            MeetingEntity meeting = MeetingEntity.create(user, request.getTitle(), request.getDate(), destination.getAbsolutePath());
            MeetingEntity saved = meetingRepository.save(meeting);

            // 5.AI 분석 요청 로그
            log.info("[AI REQUEST] 회의 녹음 분석 요청 전송됨 meetingId={}", saved.getId());

            return MeetingDto.CreateResponse.builder()
                                            .meetingId(UUID.fromString(saved.getId().toString()))
                                            .status("uploaded")
                                            .message("파일 업로드 완료 및 분석 요청 전송됨")
                                            .build();

        } catch (IOException e) {
            log.error("파일 저장 중 오류 발생", e);
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

}
