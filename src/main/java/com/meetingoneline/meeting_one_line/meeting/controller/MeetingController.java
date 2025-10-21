package com.meetingoneline.meeting_one_line.meeting.controller;

import com.meetingoneline.meeting_one_line.global.dto.ApiErrorResponse;
import com.meetingoneline.meeting_one_line.meeting.dto.MeetingRequestDto;
import com.meetingoneline.meeting_one_line.meeting.dto.MeetingResponseDto;
import com.meetingoneline.meeting_one_line.meeting.service.MeetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings")
@Tag(name = "Meetings", description = "회의 관리 API")
public class MeetingController {

    private final MeetingService meetingService;

    @Operation(
            summary = "회의 녹음 업로드 및 생성",
            description = "사용자가 회의 음성 파일을 업로드하면 서버가 로컬에 저장 후 AI 서버에 분석 요청을 전송합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = MeetingRequestDto.CreateRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "업로드 성공",
                            content = @Content(schema = @Schema(implementation = MeetingResponseDto.CreateResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MeetingResponseDto.CreateResponse> uploadMeeting(
            @AuthenticationPrincipal UUID userId,
            @RequestPart("title") String title,
            @RequestPart("date") String date,
            @RequestPart("file") MultipartFile file
    ) {
        LocalDateTime localDateTime = OffsetDateTime.parse(date).toLocalDateTime();

        MeetingRequestDto.CreateRequest req = new MeetingRequestDto.CreateRequest(title, localDateTime, file);
        MeetingResponseDto.CreateResponse response = meetingService.uploadMeeting(userId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "AI 분석 결과 콜백",
            description = "AI 서버가 분석 완료 후 회의 결과 데이터를 전달합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MeetingRequestDto.AiCallbackRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "회의록 결과 저장 성공",
                            content = @Content(schema = @Schema(implementation = MeetingResponseDto.AiCallbackResponse.class))),
                    @ApiResponse(responseCode = "404", description = "회의 ID를 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
            }
    )
    @PostMapping("/{id}/callback")
    public ResponseEntity<MeetingResponseDto.AiCallbackResponse> callbackMeeting(
            @PathVariable("id") UUID meetingId,
            @RequestBody MeetingRequestDto.AiCallbackRequest request
    ) {
        MeetingResponseDto.AiCallbackResponse response = meetingService.processCallback(meetingId, request);
        return ResponseEntity.ok(response);
    }
}
