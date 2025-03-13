package com.alltogether.lvupbackend.smalltalk.ai.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ai_personality", schema = "lvupdb")
public class AiPersonality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_personality_id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Size(max = 255)
    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Size(max = 1000)
    @NotNull
    @Column(name = "prompt", nullable = false, length = 1000)
    private String prompt;

    @Size(max = 10)
<<<<<<< HEAD
    @NotNull
    @Column(name = "op_name", nullable = false, length = 10)
    private String opName;

    @NotNull
    @Column(name = "op_gender", nullable = false)
    private Character opGender;

    @NotNull
    @Column(name = "op_age", nullable = false)
    private Integer opAge;

=======
    @Column(name = "emoji", length = 10)
    private String emoji;
>>>>>>> develop
}