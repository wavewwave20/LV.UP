package com.alltogether.lvupbackend.smalltalk.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "matching_mode")
@Getter
@NoArgsConstructor
public class MatchMode {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matching_mod_id", nullable = false)
    private Integer matchingModeId;

    @Column(name = "mod_content", nullable = false, length = 50)
    private String modeContent;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}
