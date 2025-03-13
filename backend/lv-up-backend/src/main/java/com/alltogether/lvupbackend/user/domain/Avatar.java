package com.alltogether.lvupbackend.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avatar_id")
    private Integer avatarId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "avatar_image_path", nullable = false, length = 1000)
    private String avatarImagePath;

    @Column(nullable = false, length = 500)
    private String description;

    private Boolean enabled = false;
    private Character gender;
}