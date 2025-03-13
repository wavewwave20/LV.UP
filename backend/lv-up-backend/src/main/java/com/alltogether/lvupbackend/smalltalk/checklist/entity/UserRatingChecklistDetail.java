package com.alltogether.lvupbackend.smalltalk.checklist.entity;

import com.alltogether.lvupbackend.smalltalk.user.entity.MatchingUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_rating_checklist_detail", schema = "lvupdb")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRatingChecklistDetail {

    @EmbeddedId
    private UserRatingChecklistDetailId id;

    // checklist_master와의 연관관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("checklistMasterId")
    @JoinColumn(name = "checklist_master_id", nullable = false)
    private ChecklistMaster checklistMaster;

    // matching_user 테이블과의 연관관계 매핑 (MatchingUser 엔티티가 이미 존재한다고 가정)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("matchingUserId")
    @JoinColumn(name = "matching_user_id", nullable = false)
    private MatchingUser matchingUser;
}
