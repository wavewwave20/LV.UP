package com.alltogether.lvupbackend.smalltalk.user.webclient.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

// WebClient를 사용한 API 호출 예시
@Service
public class ServicTemp {

    private final WebClient webClient;

    // 생성자 주입, Bean 사용
    public ServicTemp(@Qualifier("webClient") WebClient webClient) {
        this.webClient = webClient;
    }

    // post 방식
    public Mono<String> callExternalApiWithBody() {
        // 요청 바디 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("key1", "value1");

        return webClient.post()
            .uri("/endpoint")   // 호출할 API의 URI
            .header("Content-Type", "application/json") // 헤더 설정
            .bodyValue(requestBody) // 요청 바디 설정
            .retrieve()
            .bodyToMono(String.class);  // 응답 바디를 String으로 변환
    }

    // get 방식
    // 비동기 방식: Mono, Flux 사용
    public Mono<String> callExternalApi() {
        return webClient.get()
            .uri("/endpoint")
            .retrieve()
            // 4xx 에러에 대한 처리: 클라이언트 측 에러 발생 시 사용자 정의 예외 발생
            .onStatus(status -> status.is4xxClientError(), response ->
                    response.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(
                                    new RuntimeException("클라이언트 에러: " + errorBody)
                            ))
            )
            // 5xx 에러에 대한 처리: 서버 측 에러 발생 시 사용자 정의 예외 발생
            .onStatus(status -> status.is5xxServerError(), response ->
                    response.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(
                                    new RuntimeException("서버 에러: " + errorBody)
                            ))
            )
            .bodyToMono(String.class)
            // 추가적인 사이드 이펙트를 위한 에러 핸들링 (예: 로깅)
                    .doOnError(error -> {
                System.out.println("API 호출 중 에러 발생: " + error.getMessage());
            })
            // fallback 처리: 에러 발생 시 대체 값을 반환하거나 추가 로직 수행 가능
            .onErrorResume(error -> {
                // 대체 로직 구현: 필요에 따라 기본 값 또는 별도의 처리를 할 수 있음
                return Mono.just("대체 응답 또는 에러 처리 결과");
            });
    }

    // 동기 방식: block() 사용
    public String callExternalApiSync() {
        return webClient.get()
            .uri("/endpoint")
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }


}
