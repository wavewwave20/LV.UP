package com.alltogether.lvupbackend.board.repository;

import com.alltogether.lvupbackend.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    List<Board> findByType(Character type);
    Page<Board> findByType(Character type, Pageable pageable);
}