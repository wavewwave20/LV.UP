package com.alltogether.lvupbackend.admin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "block_category")
@Getter
@NoArgsConstructor
public class BlockCategory {

    @Id
    @Column(name = "block_category_id")
    private Integer blockCategoryId;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @OneToMany(mappedBy = "blockCategory")
    private Set<BlockBlockCategory> blockBlockCategories = new HashSet<>();
}
