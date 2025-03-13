package com.alltogether.lvupbackend.smalltalk.user.webclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

@Configuration
public class OpenViduWebClientConfig {

    // OpenVidu 서버 URL
    private static final String openviduUrl = "OPEN_VIDU_URL:4443";
    private static final String openviduUsername = "OPENVIDUAPP";
    private static final String openviduSecret = "Alltogether33_";


    @Bean(name = "openViduWebClient")
    public WebClient openViduWebClient() {
        System.out.println("OpenViduWebClinet 생성됨");
        String auth = openviduUsername + ":" + openviduSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        return WebClient.builder()
                .baseUrl(openviduUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + encodedAuth)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
