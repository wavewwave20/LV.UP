package com.alltogether.lvupbackend.user.repository;

import com.alltogether.lvupbackend.user.domain.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LevelRepository extends JpaRepository<Level, Integer> {
    List<Level> findAllByOrderByLevelAsc();
}