package com.alltogether.lvupbackend.login.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("com.alltogether.lvupbackend.jwt")
public class JwtProp {

    //어플리케이션프로퍼티의 시크릿키를 가져옴
    private String secretKey;

    //토큰의 만료시간을 가져옴
    private long expireTime;
}
