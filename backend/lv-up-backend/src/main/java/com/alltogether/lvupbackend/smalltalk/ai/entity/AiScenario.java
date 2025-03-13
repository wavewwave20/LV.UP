package com.alltogether.lvupbackend.smalltalk.ai.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "ai_scenario", schema = "lvupdb")
public class AiScenario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_scenario_id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Size(max = 255)
    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "prompt", columnDefinition = "text", nullable = false)
    private String prompt;

    @NotNull
    @Column(name = "version", nullable = false)
    private Byte version;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "first_speaker_type")
    private Character firstSpeakerType;

    @Size(max = 50)
    @Column(name = "first_text", length = 50)
    private String firstText;

<<<<<<< HEAD
=======
    @Size(max = 3)
    @Column(name = "emoji", length = 3)
    private String emoji;

    @Size(max = 255)
    @Column(name = "init_op_speaking", length = 255)
    private String initOpSpeaking;
>>>>>>> develop
}
