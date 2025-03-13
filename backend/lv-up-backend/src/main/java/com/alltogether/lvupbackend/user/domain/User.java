package com.alltogether.lvupbackend.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "user_nano_id", nullable = false, unique = true, length = 21)
    private String userNanoId;

    @Column(name = "nickname", nullable = false, length = 10)
    private String nickname;

    @Column(name = "login_type", nullable = false)
    private Integer loginType;

    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 1)
    private Character gender;

    @Column(nullable = false)
    private Integer birthyear;

    @Column
    private Integer level = 0;

    @Column(name = "total_exp")
    private Integer totalExp = 0;

    @Column(name = "coin_quantity")
    private Integer coinQuantity = 0;

    @Column(name = "ticket_quantity")
    private Integer ticketQuantity = 0;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean enabled = true;  // 활성화 상태

    @Column(name = "register_completed", nullable = false)
    private boolean registerCompleted = false;  // 회원가입 완료 여부

    @Column
    private Integer role = 0;  // 0: 일반 사용자, 1: 관리자

    @Column(nullable = false)
    private String introduction;

    // avatar_id를 foreign key로 가지는 테이블
    @ManyToOne
    @JoinColumn(name = "avatar_id", referencedColumnName = "avatar_id")
    private Avatar avatar;

    @ManyToMany
    @JoinTable(
            name = "user_interest",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id")
    )
    @ToString.Exclude
    private Set<Interest> interests = new HashSet<>();


    public void updateLevel(List<Level> levels) {
        Level currentLevelInfo = levels.stream()
                .filter(level -> level.getExpRequired() <= this.totalExp)
                .max(Comparator.comparing(Level::getExpRequired))
                .orElse(levels.get(0)); // 기본적으로 첫 번째 레벨로 설정

        // 현재 레벨보다 높은 레벨로 업데이트
        if (currentLevelInfo.getLevel() > this.level) {
            // 레벨업 보상 계산
            List<Level> levelsToReward = levels.stream()
                    .filter(level -> level.getLevel() > this.level &&
                            level.getLevel() <= currentLevelInfo.getLevel())
                    .collect(Collectors.toList());

            for (Level levelToReward : levelsToReward) {
                this.coinQuantity += levelToReward.getCoin();
                this.ticketQuantity += levelToReward.getTicket();
            }

            // 레벨 업데이트
            this.level = currentLevelInfo.getLevel();
        }
    }


    /**
     * 스프링 시큐리티에서 사용할 권한 정보 반환 메서드.
     * role 이 1이면 ROLE_ADMIN, 그 외엔 ROLE_USER 로 처리.
     */
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (this.role == 1) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return authorities;
    }
}