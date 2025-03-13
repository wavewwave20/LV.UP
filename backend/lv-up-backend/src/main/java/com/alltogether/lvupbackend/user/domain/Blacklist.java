package com.alltogether.lvupbackend.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Entity
@Table(name = "blacklist")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Blacklist {
    @EmbeddedId
    private BlacklistId id;

    @MapsId("blacker")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "blacker", nullable = false)
    private User blacker;

    @MapsId("blackee")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "blackee", nullable = false)
    private User blackee;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    public static Blacklist create(User blacker, User blackee) {
        return Blacklist.builder()
                .id(new BlacklistId(blacker.getUserId(), blackee.getUserId()))
                .blacker(blacker)
                .blackee(blackee)
                .createdAt(Instant.now())
                .build();
    }
}