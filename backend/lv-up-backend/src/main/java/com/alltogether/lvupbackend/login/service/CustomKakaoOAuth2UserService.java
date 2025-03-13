package com.alltogether.lvupbackend.login.service;

import com.alltogether.lvupbackend.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
public class CustomKakaoOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Spring이 제공하는 기본 OAuth2UserService 사용해 userInfo 가져옴
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate =
                new DefaultOAuth2UserService();

        // 카카오로부터 userInfo 조회
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // registrationId 확인(KAKAO, GOOGLE 등 구분)
        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();

        // 카카오의 경우 userNameAttributeName = "id" (위 application.yml 설정에 따라)
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // 카카오가 보내주는 JSON 구조 파싱

        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        String kakaoId = String.valueOf(attributes.get(userNameAttributeName)); // "1234567890"
        String email = (String) kakaoAccount.get("email");
        String nickname = (String) kakaoProfile.get("nickname");
        String profileImageUrl = (String) kakaoProfile.get("profile_image_url");

        log.info("kakaoId: {}", kakaoId);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2User.getAttributes(),
                userNameAttributeName
        );
    }
}
