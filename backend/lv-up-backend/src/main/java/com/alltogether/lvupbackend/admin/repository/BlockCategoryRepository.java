package com.alltogether.lvupbackend.admin.repository;

import com.alltogether.lvupbackend.admin.entity.Block;
import com.alltogether.lvupbackend.admin.entity.BlockCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlockCategoryRepository extends JpaRepository<BlockCategory, Integer> {
}
