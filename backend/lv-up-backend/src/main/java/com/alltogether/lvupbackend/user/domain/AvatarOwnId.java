package com.alltogether.lvupbackend.user.domain;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class AvatarOwnId implements Serializable {

    private Integer avatarId;
    private Integer userId;

    public AvatarOwnId() {}

    public AvatarOwnId(Integer avatarId, Integer userId) {
        this.avatarId = avatarId;
        this.userId = userId;
    }
}