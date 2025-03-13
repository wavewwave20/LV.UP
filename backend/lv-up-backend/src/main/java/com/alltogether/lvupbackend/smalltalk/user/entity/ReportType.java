package com.alltogether.lvupbackend.smalltalk.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "report_type")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_type_id", nullable = false)
    private Integer reportTypeId;

    @Column(name = "description", nullable = false, length = 500)
    private String description;


}
