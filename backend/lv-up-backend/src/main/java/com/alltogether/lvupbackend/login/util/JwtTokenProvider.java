package com.alltogether.lvupbackend.login.util;

import com.alltogether.lvupbackend.login.constants.JwtContstants;
import com.alltogether.lvupbackend.login.prop.JwtProp;
import com.alltogether.lvupbackend.user.domain.User;
import com.alltogether.lvupbackend.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/*
 * JWT 토큰 관련 기능을 제공하는 클래스
 * - 토큰 생성
 * - 토큰 해석
 * - 토큰 유효성 검사
 */
@Component
@Slf4j
public class JwtTokenProvider {

    private JwtProp jwtProp;

    private UserRepository userRepository;

    public JwtTokenProvider(JwtProp jwtProp, UserRepository userRepository) {
        this.jwtProp = jwtProp;
        this.userRepository = userRepository;
    }

    //JWT 토큰 생성
    public String createToken(String nanoid, int role) {

        String jwt = Jwts.builder()
                .signWith(getShaKey(), Jwts.SIG.HS512)
                .header()                                                      // update (version : after 1.0)
                .add("typ", JwtContstants.TOKEN_TYPE)              // 헤더 설정
                .and()
                .expiration(new Date(System.currentTimeMillis() + 604800000))  // 토큰 만료 시간 설정 (7일)
                .claim("rol", role)                                      // 클레임 설정: 권한
                .claim("nid", nanoid)
                .compact();
        return jwt;
    }

    /*
     * 토큰 해석
     *
     * Authorization : Bearer + {jwt}  (authHeader)
     * ➡ jwt 추출
     * ➡ User
     */
    public User getAuthentication(String authHeader) {

        log.info("authHeader : " + authHeader);

        if(authHeader == null || authHeader.length() == 0 )
            return null;

        try {
            // jwt 추출
            String jwt = authHeader.replace(JwtContstants.TOKEN_PREFIX, "");

            // 🔐➡👩‍💼 JWT 파싱
            Jws<Claims> parsedToken = Jwts.parser()
                    .verifyWith(getShaKey())
                    .build()
                    .parseSignedClaims(jwt);

            log.info("parsedToken : " + parsedToken);

            // 인증된 사용자 아이디
            String userNanoId = parsedToken.getPayload().get("nid").toString();
            log.info("userNanoId : " + userNanoId);

            // 토큰에 userId 있는지 확인
            if( userNanoId == null || userNanoId.isEmpty())
                return null;

            // 토큰 유효하면
            // name, enabled 추가 정보 조회
            try {
                User userInfo = userRepository.findByUserNanoId(userNanoId);
                if( userInfo != null ) {
                    // 사용자가 삭제된경우 null 반환
                    if(!userInfo.isEnabled()) {
                        return null;
                    }
                    else {
                        return userInfo;
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                log.error("토큰 유효 -> DB 추가 정보 조회시 에러 발생...");
            }

        } catch (ExpiredJwtException exception) {
            log.warn("Request to parse expired JWT : {} failed : {}", authHeader, exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.warn("Request to parse unsupported JWT : {} failed : {}", authHeader, exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.warn("Request to parse invalid JWT : {} failed : {}", authHeader, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.warn("Request to parse empty or null JWT : {} failed : {}", authHeader, exception.getMessage());
        }
        return null;
    }



    //
    /**
     * 🔐❓ 토큰 유효성 검사
     * @param jwt
     * @return
     *  ⭕ true     : 유효
     *  ❌ false    : 만료
     */
    public boolean validateToken(String jwt) {

        try {
            // 🔐➡👩‍💼 JWT 파싱
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(getShaKey())
                    .build()
                    .parseSignedClaims(jwt);

            log.info("::::: 토큰 만료기간 :::::");
            log.info("-> " + claims.getPayload().getExpiration());

            return !claims.getPayload().getExpiration().before(new Date());
        } catch (ExpiredJwtException exception) {
            log.error("Token Expired");                 // 토큰 만료
            return false;
        } catch (JwtException exception) {
            log.error("Token Tampered");                // 토큰 손상
            return false;
        } catch (NullPointerException exception) {
            log.error("Token is null");                 // 토큰 없음
            return false;
        } catch (Exception e) {
            return false;
        }
    }


    private byte[] getSigningKey() {
        return jwtProp.getSecretKey().getBytes();
    }

    private SecretKey getShaKey() {
        return Keys.hmacShaKeyFor(getSigningKey());
    }

}
