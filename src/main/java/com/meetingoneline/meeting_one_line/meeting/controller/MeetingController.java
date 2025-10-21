package com.meetingoneline.meeting_one_line.meeting.controller;

import com.meetingoneline.meeting_one_line.global.dto.ApiErrorResponse;
import com.meetingoneline.meeting_one_line.meeting.dto.MeetingDto;
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
                            schema = @Schema(implementation = MeetingDto.CreateRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "업로드 성공",
                            content = @Content(schema = @Schema(implementation = MeetingDto.CreateResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 오류",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MeetingDto.CreateResponse> uploadMeeting(
            @AuthenticationPrincipal UUID userId,
            @RequestPart("title") String title,
            @RequestPart("date") String date,
            @RequestPart("file") MultipartFile file
    ) {
        LocalDateTime localDateTime = OffsetDateTime.parse(date).toLocalDateTime();

        MeetingDto.CreateRequest req = new MeetingDto.CreateRequest(title, localDateTime, file);
        MeetingDto.CreateResponse response = meetingService.uploadMeeting(userId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
