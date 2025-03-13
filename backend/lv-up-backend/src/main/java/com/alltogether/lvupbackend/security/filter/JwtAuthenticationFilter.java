package com.alltogether.lvupbackend.security.filter;

import com.alltogether.lvupbackend.login.constants.JwtContstants;
import com.alltogether.lvupbackend.login.util.JwtTokenProvider;
import com.alltogether.lvupbackend.user.domain.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${kakao.login.redirect-front-uri}")
    private String redirectFrontUri;

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        // 1. 헤더에서 Authorization 값을 가져옵니다.
        String authHeader = request.getHeader("Authorization");

        // 2. Authorization 헤더가 있고, "Bearer " 로 시작하면 토큰 추출
        if (authHeader != null && authHeader.startsWith(JwtContstants.TOKEN_PREFIX)) {
            // 헤더에 있는 "Bearer " 접두어를 제거해서 순수 토큰 문자열을 얻음
            String token = authHeader.replace(JwtContstants.TOKEN_PREFIX, "");

            // 3. 토큰 유효성 검사 (내부에서 파싱 및 만료 체크 등)
            if (jwtTokenProvider.validateToken(token)) {
                /*
                 * 4. 토큰으로부터 사용자 정보를 획득합니다.
                 *    getAuthentication() 내부에서는 authHeader에서 접두어를 제거한 후 파싱합니다.
                 *    (이미 토큰의 유효성을 검사했으므로, 여기서 반환된 user가 null이 아니라면 인증 성공)
                 */
                User user = jwtTokenProvider.getAuthentication(authHeader);
                if (user != null) {

                    String requestURI = request.getRequestURI();
                    log.info("#########################requestURI : " + requestURI);

                    /*
                    이건 302 처리 안함
                    /api/users/interest/options
                        /api/users/avatar/all
                        /api/users/nickname
                        /api/users/register
                     */
                    if(!user.isRegisterCompleted()
                            && !requestURI.equals("/api/users/interest/options")
                            && !requestURI.equals("/api/users/avatar/all")
                            && !requestURI.equals("/api/users/nickname")
                            && !requestURI.equals("/api/users/register")){
                        log.info("회원가입이 완료되지 않은 사용자입니다.");
                        response.setStatus(HttpServletResponse.SC_FOUND); // 302
                        response.setHeader("Location", redirectFrontUri);
                        return;
                    }

                    // 5. Spring Security의 Authentication 객체 생성 (여기서는 UsernamePasswordAuthenticationToken 사용)
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user.getUserId(),        // 인증된 사용자 (principal)
                                    null,        // credentials (필요없으므로 null)
                                    user.getAuthorities() // 사용자의 권한 목록 (User 엔티티에 getAuthorities()가 구현되어 있어야 함)
                            );
                    // 6. SecurityContext 에 Authentication 객체를 등록
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        // 7. 다음 필터로 체인 진행
        chain.doFilter(request, response);
    }
}
