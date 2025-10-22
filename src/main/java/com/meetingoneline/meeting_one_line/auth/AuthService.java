package com.meetingoneline.meeting_one_line.auth;

import com.meetingoneline.meeting_one_line.auth.dto.AuthRequestDto;
import com.meetingoneline.meeting_one_line.auth.dto.AuthResponseDto;
import com.meetingoneline.meeting_one_line.auth.refresh_token.RefreshTokenEntity;
import com.meetingoneline.meeting_one_line.auth.refresh_token.RefreshTokenRepository;
import com.meetingoneline.meeting_one_line.global.config.jwt.JwtTokenProvider;
import com.meetingoneline.meeting_one_line.global.config.jwt.JwtType;
import com.meetingoneline.meeting_one_line.global.exception.BusinessException;
import com.meetingoneline.meeting_one_line.global.exception.ErrorCode;
import com.meetingoneline.meeting_one_line.user.UserEntity;
import com.meetingoneline.meeting_one_line.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    @Transactional
    public AuthResponseDto.Signup signup(AuthRequestDto.SignupRequest request){
        // 1. 이메일 중복 확인
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 2. 닉네임 중복 확인
        if(userRepository.existsByNickname(request.getNickname())){
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }

        // 3. 비빌번호 인코더
        String encoderPassword = passwordEncoder.encode(request.getPassword());

        // 4. 유저 생성
        UserEntity userEntity = UserEntity.create(request.getEmail(), encoderPassword, request.getNickname());

        // 5. 유저 정보 저장
        UserEntity savedUserEntity = userRepository.save(userEntity);

        String at = jwtTokenProvider.createToken(savedUserEntity.getId().toString(), JwtType.ACCESS);

        // TODO RT Header 추가
        String rt = jwtTokenProvider.createToken(savedUserEntity.getId().toString(), JwtType.REFRESH);

        // 4. rt token 저장
        RefreshTokenEntity rtEntity = RefreshTokenEntity.create(savedUserEntity, rt);
        refreshTokenRepository.save(rtEntity);

        return AuthResponseDto.Signup.builder().accessToken(at)
                                     .build();
    }

    /**
     * 로그인
     */
    @Transactional
    public AuthResponseDto.Login login(AuthRequestDto.EmailLogin request){
        // 1. 이메일 존재 유무 체크
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 2. 비밀번호 매치 체크
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_LOGIN);
        }

        // 3. at, rt 생성
        String at = jwtTokenProvider.createToken(user.getId().toString(), JwtType.ACCESS);

        // TODO RT Header 추가
        String rt = jwtTokenProvider.createToken(user.getId().toString(), JwtType.REFRESH);

        // 4. rt token 저장
        RefreshTokenEntity rtEntity = RefreshTokenEntity.create(user, rt);
        refreshTokenRepository.save(rtEntity);

        return AuthResponseDto.Login.builder().accessToken(at)
                                     .build();
    }

    /**
     * 닉네임 중복확인
     */
    public AuthResponseDto.NicknameDuplicate checkNickname(String nickname){
        boolean exists = userRepository.existsByNickname(nickname);

        return AuthResponseDto.NicknameDuplicate.builder().isDuplicate(exists)
                                                .build();
    }

    /**
     * 재석님 > 내정보 조회
     */
    public AuthResponseDto.UserInfo getMyInfo(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return AuthResponseDto.UserInfo.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

    /**
     * 재석님 > 로그아웃
     */
    @Transactional
    public void logout(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        refreshTokenRepository.deleteByUser(user);
    }
}
