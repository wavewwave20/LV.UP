package com.alltogether.lvupbackend.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "level")
@Getter
@NoArgsConstructor
public class Level {
    @Id
    @Column(name = "level")
    private Integer level;

    @Column(name = "exp_required")
    private Integer expRequired;

    @Column(name = "exp")
    private Integer exp;

    @Column(name = "coin")
    private Integer coin;

    @Column(name = "ticket")
    private Integer ticket;
}