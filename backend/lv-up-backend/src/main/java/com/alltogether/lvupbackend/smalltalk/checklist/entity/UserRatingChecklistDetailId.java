package com.alltogether.lvupbackend.smalltalk.checklist.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRatingChecklistDetailId implements Serializable {

    @Column(name = "checklist_master_id", nullable = false)
    private Integer checklistMasterId;

    @Column(name = "matching_user_id", nullable = false)
    private Integer matchingUserId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRatingChecklistDetailId)) return false;
        UserRatingChecklistDetailId that = (UserRatingChecklistDetailId) o;
        return Objects.equals(checklistMasterId, that.checklistMasterId) &&
                Objects.equals(matchingUserId, that.matchingUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checklistMasterId, matchingUserId);
    }
}