package com.alltogether.lvupbackend.smalltalk.ai.entity;

import com.alltogether.lvupbackend.user.domain.User;
import io.netty.handler.codec.socksx.v4.Socks4CommandRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "ai_smalltalk", schema = "lvupdb")
public class AiSmalltalk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_smalltalk_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ai_personality_id", nullable = false)
    private AiPersonality aiPersonality;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ai_scenario_id", nullable = false)
    private AiScenario aiScenario;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "overall_score", nullable = false)
    private Byte overallScore;

    @NotNull
    @Column(name = "dialogue", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> dialogue;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "end_flag")
    private Boolean endFlag;

<<<<<<< HEAD
=======
    @Column(name = "prompt", columnDefinition = "text", nullable = false)
    private String initialPrompt;

    @Size(max = 10)
    @Column(name = "op_name", length = 10)
    private String opName;

    @Column(name = "op_gender")
    private Character opGender;

    @Column(name = "op_age")
    private Integer opAge;

>>>>>>> develop
}