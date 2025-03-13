package com.alltogether.lvupbackend.security.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "redis.match.key")
public class MatchSessionProperties {
    private String session = "MATCH_SESSION";
    private String queue = "MATCH_QUEUE";
    private String extend = "MATCH_EXTEND";
    private String stream = "STREAM_READY";
}
