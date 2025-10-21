package com.meetingoneline.meeting_one_line.auth;

import com.meetingoneline.meeting_one_line.auth.dto.AuthRequestDto;
import com.meetingoneline.meeting_one_line.auth.dto.AuthResponseDto;
import com.meetingoneline.meeting_one_line.global.dto.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "AUTH API", description = "인증/인가 API")
public class AuthController {
    private final AuthService authService;

    /**
     * 회원가입 API
     */
    @Operation(
            summary = "회원가입",
            description = "이메일, 닉네임, 비밀번호를 입력받아 새로운 회원을 생성합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "회원가입 성공",
                            content = @Content(schema = @Schema(implementation = AuthResponseDto.Signup.class))),
                    @ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "409",
                            description = "중복된 이메일 또는 닉네임",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto.Signup> signup(@Valid @RequestBody AuthRequestDto.SignupRequest request){
        AuthResponseDto.Signup response = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 로그인 API
     */
    @Operation(
            summary = "이메일 로그인",
            description = "이메일과 비밀번호를 이용해 로그인하고 Access Token을 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공",
                            content = @Content(schema = @Schema(implementation = AuthResponseDto.Login.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 로그인 요청", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto.Login> login(
            @Valid @RequestBody AuthRequestDto.EmailLogin request
    ) {
        AuthResponseDto.Login response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 닉네임 중복 확인 API
     */
    @Operation(
            summary = "닉네임 중복 확인",
            description = "닉네임이 이미 존재하는지 확인합니다. true면 중복됨, false면 사용 가능.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "중복 여부 확인 성공",
                            content = @Content(schema = @Schema(implementation = AuthResponseDto.NicknameDuplicate.class)))
            }
    )
    @GetMapping("/check-nickname")
    public ResponseEntity<AuthResponseDto.NicknameDuplicate> checkNickname(
            @RequestParam String nickname
    ) {
        AuthResponseDto.NicknameDuplicate response = authService.checkNickname(nickname);
        return ResponseEntity.ok(response);
    }

    /**
     * 재석님 > 내 정보 조회 API
     */

    /**
     * 재석님 > 로그아웃 API
     */

}
