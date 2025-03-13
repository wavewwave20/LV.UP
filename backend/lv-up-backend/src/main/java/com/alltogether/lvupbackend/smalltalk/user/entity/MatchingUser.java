package com.alltogether.lvupbackend.smalltalk.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "matching_user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matching_user_id")
    private Integer matchingUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matching_id")
    private Matching matchingId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "user_avatar", nullable = false)
    private Integer userAvatar;

    @Column(name = "extend")
    private Boolean extend;

    @Column(name = "accept")
    private Boolean accept;

    @Column(name = "report")
    private Boolean report;

    @Column(name = "rating_score", nullable = false)
    private Byte ratingScore;

    @Column(name = "rating_content", columnDefinition = "json", nullable = false)
    private String ratingContent;

    @Column(name = "ratee_id", nullable = false)
    private Integer rateeId;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

} 