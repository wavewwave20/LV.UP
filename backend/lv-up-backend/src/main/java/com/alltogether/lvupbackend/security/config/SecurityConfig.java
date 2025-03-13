package com.alltogether.lvupbackend.security.config;

import com.alltogether.lvupbackend.error.UnauthEntryPoint;
import com.alltogether.lvupbackend.login.service.CustomKakaoOAuth2UserService;
import com.alltogether.lvupbackend.login.util.JwtTokenProvider;
import com.alltogether.lvupbackend.security.filter.JwtAuthenticationFilter;
import com.alltogether.lvupbackend.security.prop.UriProp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    private final UriProp uriProp;

    // JwtTokenProvider 는 생성자 주입으로 받아옵니다.
    public SecurityConfig(JwtTokenProvider jwtTokenProvider, UriProp uriProp) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.uriProp = uriProp;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CORS, CSRF, 세션 정책 설정
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // HTTPS 설정 
                //.requiresChannel(channel -> channel
               //     .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
                //    .requiresSecure())
                // 권한 설정 (예시)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")  // 관리자만 접근 가능
                        .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**","/login-success").permitAll()
                        .requestMatchers("/login/**", "/oauth2/**").permitAll()
                        .requestMatchers("/ws/**", "/api/smalltalk/**").permitAll()
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                )
                // OAuth2 로그인 설정 (기존 설정)
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService()))
                )
                // 인증되지 않은 요청에 대해 HTTP 에러(401 Unauthorized)로 응답
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new UnauthEntryPoint())
                )
                // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 카카오에서 받은 사용자 정보를 후처리할 Service
     */
    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return new CustomKakaoOAuth2UserService();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true); // 인증 정보(쿠키 등)를 허용하려면 true 설정
//        configuration.setAllowedOrigins(frontServerProp.getUrlList()); // 프론트엔드 주소
        //test용 모두 허용
        configuration.setAllowedOrigins(uriProp.getFrontUrls()); // 프론트엔드 주소 명확히 지정
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "HEAD"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}

