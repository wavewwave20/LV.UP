package com.alltogether.lvupbackend.smalltalk.user.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OpenViduService {

    private final WebClient openViduWebClient;

    public OpenViduService(@Qualifier("openViduWebClient") WebClient openViduWebClient) {
        this.openViduWebClient = openViduWebClient;
    }

    // OpenVidu 서버에 세션 생성 요청
    public String createSession() {
        return openViduWebClient.post()
                .uri("/openvidu/api/sessions")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public void closeSession(String sessionId) {
        openViduWebClient.delete()
                .uri("/openvidu/api/sessions/" + sessionId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
<<<<<<< HEAD
=======

    public String createToken(String sessionId) {
        String requestBody = "{"
                + "\"role\":\"PUBLISHER\","
                + "\"kurentoOptions\": {"
                + "    \"videoMaxRecvBandwidth\": 1000,"  // 1000kbps로 제한
                + "    \"videoMaxSendBandwidth\": 1000,"
                + "    \"allowedFilters\": []"
                + "}}";

        return openViduWebClient.post()
                .uri("/openvidu/api/sessions/" + sessionId + "/connection")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
>>>>>>> develop
}
