package com.alltogether.lvupbackend.login.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.kakao")
@Data
public class KakaoProp {
    private String clientId;
    private String scope;
    private String clientName;
    private String provider;
    private String authorizationGrantType;
    private String redirectUri;
    private String clientAuthenticationMethod;
    private String clientSecret;
}
