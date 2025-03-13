package com.alltogether.lvupbackend.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "mission")
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_id")
    private Integer missionId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Integer goal;

    @Column(nullable = false, length = 1)
    private Character category; // 'D': 일일, 'W': 주간, 'S': 특별

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private Integer coin = 0;

    @Column(nullable = false)
    private Integer ticket = 0;

    @Column(nullable = false)
    private Integer exp = 0;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;
}
