package com.alltogether.lvupbackend.login.controller;

import com.alltogether.lvupbackend.login.util.JwtTokenProvider;
import com.alltogether.lvupbackend.login.util.NanoidGenerator;
import com.alltogether.lvupbackend.mission.service.MissionService;
import com.alltogether.lvupbackend.user.domain.User;
import com.alltogether.lvupbackend.login.dto.KakaoProfileDto;
import com.alltogether.lvupbackend.login.dto.RegisterResponseDto;
import com.alltogether.lvupbackend.login.prop.KakaoProp;
import com.alltogether.lvupbackend.user.repository.UserRepository;
import com.alltogether.lvupbackend.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/login")
public class KakaoLoginController {

    private final UserRepository userRepository;
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    String profileUri;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    String tokenUri;

    @Value("${kakao.login.redirect-front-uri}")
    String redirectFrontUri;

    private final JwtTokenProvider jwtProvider;

    private final NanoidGenerator nanoidGenerator;

    private final RestTemplate restTemplate;

    private final KakaoProp kakaoProp;

    private final UserService userService; //
    private final MissionService missionService; //

    @Autowired
    public KakaoLoginController(RestTemplate restTemplate, KakaoProp kakaoProp, UserRepository userRepository, JwtTokenProvider jwtProvider, NanoidGenerator nanoidGenerator, UserService userService, MissionService missionService) {
        this.restTemplate = restTemplate;
        this.kakaoProp = kakaoProp;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.nanoidGenerator = nanoidGenerator;
        this.userService = userService;

        this.missionService = missionService;
    }

    @GetMapping("")
    public ResponseEntity<?> login(@RequestHeader Map<String, String> headers) {
        log.info("=== Request Headers ===");
        headers.forEach((key, value) -> {
            log.info("{} : {} \n\n", key, value);
        });
        return ResponseEntity.ok("로그인 페이지");
    }

    @GetMapping("/oauth2/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam("code") String code) {
        log.info("code: {}", code);

        // 1. code로 카카오에 token 요청
        String token = getKakaoToken(code);

        // 2. token으로 카카오 프로필 조회
        KakaoProfileDto kakaoProfile = getKakaoProfile(token);

        // 3. id로 회원 조회
        boolean isExist = userRepository.existsByLoginIdAndLoginType(kakaoProfile.getId(), 0);

        if(!isExist) {
            // 3-1.회원 가입
            User newUser = new User();
            newUser.setLoginId(kakaoProfile.getId());
            newUser.setLoginType(0);
            if(kakaoProfile.getGender().equals("male")) {
                newUser.setGender('M');
            }else {
                newUser.setGender('F');
            }
            newUser.setBirthyear(kakaoProfile.getBirthyear());
            newUser.setUserNanoId(nanoidGenerator.generateNanoid());
            newUser.setEmail("reg_not_cm");
            newUser.setNickname("reg_not_cm");
            newUser.setIntroduction("안녕하세요.");
            newUser.setRegisterCompleted(false);

            User savedUser = userRepository.save(newUser);
            userService.addInitialAvatars(savedUser.getUserId());


            String jwt = jwtProvider.createToken(newUser.getUserNanoId(), newUser.getRole());
            String redirectUrl = redirectFrontUri + "/login-success?register-completed=false&token=" + jwt;

            return ResponseEntity.status(HttpStatus.FOUND) // 302 Found (리다이렉트)
                    .header(HttpHeaders.LOCATION, redirectUrl)
                    .build();

        }else {
            // 3-2.로그인
            //jwt 발급
            User user = userRepository.findByLoginId(kakaoProfile.getId());
            String jwt = jwtProvider.createToken(user.getUserNanoId(), user.getRole());
            StringBuilder redirectUrl = new StringBuilder(redirectFrontUri + "/login-success?register-completed=");

            // 기존 사용자 출석 처리
            log.info("Attempting to process attendance for existing user: {}", user.getUserId());
            try {
//                missionService.processAutoAttendance(user.getUserId());
                log.info("Attendance processing completed for existing user {}", user.getUserId());
            } catch (Exception e) {
                log.error("Failed to process auto attendance for existing user {}: {}", user.getUserId(), e.getMessage(), e);
            }


            if(user.isRegisterCompleted()) {
                redirectUrl.append("true");
            }else {
                redirectUrl.append("false");
            }
            redirectUrl.append("&token=").append(jwt);

            return ResponseEntity.status(HttpStatus.FOUND) // 302 Found (리다이렉트)
                    .header(HttpHeaders.LOCATION, redirectUrl.toString())
                    .build();
        }
    }

    private String getKakaoToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoProp.getClientId());
        params.add("redirect_uri", kakaoProp.getRedirectUri());
        params.add("code", code);
        params.add("client_secret", kakaoProp.getClientSecret());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUri, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            return (String) responseBody.get("access_token");
        } else {
            throw new RuntimeException("카카오 토큰 요청 실패");
        }
    }

    private KakaoProfileDto getKakaoProfile(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(profileUri, HttpMethod.GET, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            log.info("profile: {}", response.getBody());
            Map<String, Object> kakaoAccount = (Map<String, Object>) responseBody.get("kakao_account");

            KakaoProfileDto profile = new KakaoProfileDto();
            profile.setId(responseBody.get("id").toString());
            profile.setGender(kakaoAccount.get("gender").toString());
            profile.setBirthyear(Integer.parseInt(kakaoAccount.get("birthyear").toString()));

            return profile;
        } else {
            throw new RuntimeException("카카오 프로필 조회 실패");
        }
    }
}
