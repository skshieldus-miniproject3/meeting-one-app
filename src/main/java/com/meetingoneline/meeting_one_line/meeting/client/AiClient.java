package com.meetingoneline.meeting_one_line.meeting.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiClient {

    private final WebClient webClient;

    @Value("${ai.server.url}")
    private String aiServerUrl;

    /**
     * AI 서버 분석 요청 (비동기 fire-and-forget)
     * @param onErrorFailHandler 실패 시 수행할 콜백 (예: DB 상태 변경)
     */
    public void requestAnalysis(UUID meetingId, String filePath, Consumer<Throwable> onErrorFailHandler) {
        String url = aiServerUrl + "/ai/analyze";

        Map<String, Object> requestBody = Map.of(
                "meetingId", meetingId.toString(),
                "filePath", filePath
        );

        log.info("### AI 서버에 분석 요청 시작: {} {}", url, requestBody);

        webClient.post()
                 .uri(url)
                 .contentType(MediaType.APPLICATION_JSON)
                 .bodyValue(requestBody)
                 .retrieve()
                 .onStatus(
                         status -> status.is4xxClientError() || status.is5xxServerError(),
                         clientResponse -> clientResponse.bodyToMono(String.class)
                                                         .flatMap(body -> {
                                                             log.error("### AI 서버 오류 응답: {}", body);
                                                             return Mono.error(new RuntimeException("AI 서버 오류: " + body));
                                                         })
                 )
                 .bodyToMono(Void.class)
                 .doOnError(error -> {
                     log.error("### AI 서버 요청 실패 (meetingId={}): {}", meetingId, error.getMessage());
                     if (onErrorFailHandler != null) onErrorFailHandler.accept(error);
                 })
                 .doOnSuccess(v -> log.info("### AI 서버 요청 완료 (meetingId={})", meetingId))
                 .subscribe();
    }
}
