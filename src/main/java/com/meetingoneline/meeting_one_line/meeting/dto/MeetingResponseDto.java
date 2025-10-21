package com.meetingoneline.meeting_one_line.meeting.dto;

import com.meetingoneline.meeting_one_line.meeting.enums.RecordSaveStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.UUID;

public class MeetingResponseDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "회의 업로드 응답 DTO")
    public static class CreateResponse {
        @Schema(description = "회의 UUID", example = "c41b89b7-0b69-4c90-9d3f-3a258fe682c4")
        private UUID meetingId;

        @Schema(description = "상태", example = "uploaded")
        private RecordSaveStatus status;

        @Schema(description = "메시지", example = "파일 업로드 완료 및 분석 요청 전송됨")
        private String message;
    }

    /**
     * ai 분석 후 > callback api 응답
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(name = "MeetingRequestDto.AiCallbackResponse", description = "AI 분석 결과 콜백 응답 DTO")
    public static class AiCallbackResponse {
        private String message;
    }
}
