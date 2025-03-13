package com.alltogether.lvupbackend.admin.dto;

import com.alltogether.lvupbackend.admin.entity.BlockCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BlockCategoryDto {
    private Integer blockCategoryId;
    private String description;

    public static BlockCategoryDto from(BlockCategory blockCategory) {
        return BlockCategoryDto.builder()
                .blockCategoryId(blockCategory.getBlockCategoryId())
                .description(blockCategory.getDescription())
                .build();
    }
}
