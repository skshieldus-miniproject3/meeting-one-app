package com.meetingoneline.meeting_one_line.meeting.dto;

import com.meetingoneline.meeting_one_line.meeting.entity.MeetingEntity;
import com.meetingoneline.meeting_one_line.meeting.enums.RecordSaveStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
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

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "회의록 단일 아이템 DTO")
    public static class ListItem {
        @Schema(description = "회의 UUID")
        private UUID meetingId;

        @Schema(description = "회의 제목")
        private String title;

        @Schema(description = "회의 상태", example = "completed")
        private RecordSaveStatus status;

        @Schema(description = "회의 요약문")
        private String summary;

        @Schema(description = "생성 시각")
        private LocalDateTime createdAt;

        public static ListItem from(MeetingEntity entity) {
            return ListItem.builder()
                           .meetingId(entity.getId())
                           .title(entity.getTitle())
                           .status(entity.getStatus())
                           .summary(entity.getSummary())
                           .createdAt(entity.getCreatedAt())
                           .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "회의록 목록 응답 DTO")
    public static class ListResponse {
        private List<ListItem> content;
        private int page;
        private int size;
        private int totalPages;

        public static ListResponse from(Page<MeetingEntity> pageResult) {
            List<ListItem> items = pageResult.getContent().stream()
                                             .map(ListItem::from)
                                             .toList();

            return ListResponse.builder()
                               .content(items)
                               .page(pageResult.getNumber() + 1)
                               .size(pageResult.getSize())
                               .totalPages(pageResult.getTotalPages())
                               .build();
        }
    }
}
