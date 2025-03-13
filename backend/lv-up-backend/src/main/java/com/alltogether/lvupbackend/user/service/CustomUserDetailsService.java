package com.alltogether.lvupbackend.user.service;

import com.alltogether.lvupbackend.user.domain.User;
import com.alltogether.lvupbackend.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    // 생성자 주입 방식
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(username);

        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        System.out.println("User Role: " + user.getRole());
        System.out.println("User Authorities: " + user.getAuthorities());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getLoginId())
                .password("") // 카카오 로그인이므로 빈 비밀번호
                .authorities(user.getAuthorities())  // ROLE_USER or ROLE_ADMIN 부여
                .build();
    }
}