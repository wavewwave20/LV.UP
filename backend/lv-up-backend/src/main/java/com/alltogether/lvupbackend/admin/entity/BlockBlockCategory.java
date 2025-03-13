package com.alltogether.lvupbackend.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "block_block_category")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockBlockCategory {

    @EmbeddedId
    private BlockBlockCategoryId id;

    @MapsId("blockId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_id")
    private Block block;

    @MapsId("blockCategoryId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_category_id")
    private BlockCategory blockCategory;
}
