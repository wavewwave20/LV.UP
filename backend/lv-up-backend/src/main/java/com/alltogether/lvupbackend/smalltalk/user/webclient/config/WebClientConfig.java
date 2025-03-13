package com.alltogether.lvupbackend.smalltalk.user.webclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    /**
     * WebClient Bean 등록
     * 기본적으로 포함할 헤더 설정
     * Content-Type: application/json
     *
     * @return WebClient
     */
    @Bean(name = "webClient")
    public WebClient webClient() {
        System.out.println("WebClient 생성됨");
        return WebClient.builder()
            .baseUrl("http://localhost:8080")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
}
