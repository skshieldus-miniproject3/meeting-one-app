package com.meetingoneline.meeting_one_line.meeting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

public class MeetingDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "회의 업로드 요청 DTO")
    public static class CreateRequest {
        @Schema(description = "회의 제목", example = "AI 기획 회의")
        private String title;

        @Schema(description = "회의 일시 (YYYY-MM-DDTHH:mm:ss)", example = "2025-10-21T14:00:00")
        private LocalDateTime date;

        @Schema(description = "회의 음성 파일 (wav/mp3)", type = "string", format = "binary")
        private MultipartFile file;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "회의 업로드 응답 DTO")
    public static class CreateResponse {
        @Schema(description = "회의 UUID", example = "c41b89b7-0b69-4c90-9d3f-3a258fe682c4")
        private UUID meetingId;

        @Schema(description = "상태", example = "uploaded")
        private String status;

        @Schema(description = "메시지", example = "파일 업로드 완료 및 분석 요청 전송됨")
        private String message;
    }
}
