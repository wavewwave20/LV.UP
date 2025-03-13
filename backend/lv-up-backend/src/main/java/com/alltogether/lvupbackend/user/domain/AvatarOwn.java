package com.alltogether.lvupbackend.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "avatar_own")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AvatarOwn {

    @EmbeddedId
    private AvatarOwnId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("avatarId") // EmbeddedId 내부의 avatarId와 매핑
    @JoinColumn(name = "avatar_id", referencedColumnName = "avatar_id") // 명시적으로 물리 컬럼 지정
    private Avatar avatar;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId") // EmbeddedId 내부의 userId와 매핑
    @JoinColumn(name = "user_id", referencedColumnName = "user_id") // 명시적으로 물리 컬럼 지정
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
