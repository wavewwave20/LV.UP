package com.alltogether.lvupbackend.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlockBlockCategoryId implements Serializable {

    @Column(name = "block_id")
    private Integer blockId;

    @Column(name = "block_category_id")
    private Integer blockCategoryId;
}
