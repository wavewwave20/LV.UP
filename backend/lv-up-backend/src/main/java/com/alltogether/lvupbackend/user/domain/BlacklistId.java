package com.alltogether.lvupbackend.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlacklistId implements java.io.Serializable {
    private static final long serialVersionUID = -4801409392447816200L;
    @NotNull
    @Column(name = "blacker", nullable = false)
    private Integer blacker;

    @NotNull
    @Column(name = "blackee", nullable = false)
    private Integer blackee;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BlacklistId entity = (BlacklistId) o;
        return Objects.equals(this.blackee, entity.blackee) &&
                Objects.equals(this.blacker, entity.blacker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blackee, blacker);
    }
}