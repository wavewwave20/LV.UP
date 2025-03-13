package com.alltogether.lvupbackend.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "block")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id")
    private Integer blockId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "start_at", nullable = false, columnDefinition = "datetime default CURRENT_TIMESTAMP")
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @OneToMany(mappedBy = "block")
    private Set<BlockBlockCategory> blockBlockCategories = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.startAt = LocalDateTime.now();
    }
}
