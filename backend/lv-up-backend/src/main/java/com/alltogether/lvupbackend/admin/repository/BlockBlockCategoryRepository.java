package com.alltogether.lvupbackend.admin.repository;

import com.alltogether.lvupbackend.admin.entity.BlockBlockCategory;
import com.alltogether.lvupbackend.admin.entity.BlockBlockCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockBlockCategoryRepository extends JpaRepository<BlockBlockCategory, BlockBlockCategoryId> {
}
