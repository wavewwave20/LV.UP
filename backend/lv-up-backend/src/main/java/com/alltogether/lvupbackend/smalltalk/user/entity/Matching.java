package com.alltogether.lvupbackend.smalltalk.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "matching")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Matching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matching_id")
    private Integer matchingId;

    @Column(name = "matching_mod_id", nullable = false)
    private Integer matchingModeId;

    @Column(name = "state", nullable = false, length = 5)
    private String state;  // SRC, ACT, CXL, EXT, SUS

    @Column(name = "interest_id", nullable = false)
    private Integer interestId;

    @Column(name = "dialogue", columnDefinition = "json", nullable = false)
    private String dialogue;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @OneToMany(mappedBy = "matchingId", cascade = CascadeType.ALL)
    private List<MatchingUser> matchingUsers;
}
