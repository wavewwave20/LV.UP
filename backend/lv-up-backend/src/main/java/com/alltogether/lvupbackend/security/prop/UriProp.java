package com.alltogether.lvupbackend.security.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "com.alltogether.lvupbackend")
@Data
public class UriProp {
    private String frontUrls;

    public List<String> getFrontUrls() {
        return List.of(frontUrls.split(","));
    }
}
