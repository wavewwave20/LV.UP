package com.alltogether.lvupbackend.smalltalk.checklist.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "checklist_master", schema = "lvupdb")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChecklistMaster {

    @Id
    @Column(name = "checklist_master_id", nullable = false)
    private Integer checklistMasterId;

    @Column(name = "name", nullable = false, length = 21)
    private String name;

    @Column(name = "score", nullable = false)
    private Float score;

    @Column(name = "checker", nullable = false, length = 1)
    private String checker; // A: ai, U: user
}
