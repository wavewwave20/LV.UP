package com.alltogether.lvupbackend.smalltalk.ai.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ai_smalltalk_score", schema = "lvupdb")
public class AiSmalltalkScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_smalltalk_score_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ai_smalltalk_id", nullable = false)
    private AiSmalltalk aiSmalltalk;

    @NotNull
    @Column(name = "score_id", nullable = false)
    private Byte scoreId;

    @NotNull
    @Column(name = "score", nullable = false)
    private Byte score;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}