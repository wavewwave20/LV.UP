package com.alltogether.lvupbackend.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
<<<<<<< HEAD
@Table(name = "mission_progress",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "mission_id"}))
=======
@Table(name = "mission_progress")
>>>>>>> develop
public class MissionProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_progress_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Column(nullable = false)
    private Double progress = 0.0;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 미션 보상
    @Column(name = "reward_claimed", nullable = false)
    private Boolean rewardClaimed = false;


    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public MissionProgress(User user, Mission mission) {
        this.user = user;
        this.mission = mission;
        this.progress = 0.0;
        this.isCompleted = false;
        this.createdAt = LocalDateTime.now();
    }

    public void complete() {
        this.isCompleted = true;
    }

    // 진행 상황 업데이트 메서드
    public void updateProgress(double progress) {
        this.progress = progress;
        if (this.progress >= 100.0) {
            this.isCompleted = true;
        }
    }

    public void setRewardClaimed(Boolean rewardClaimed) {
        this.rewardClaimed = rewardClaimed;
    }
}
