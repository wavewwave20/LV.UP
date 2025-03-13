package com.alltogether.lvupbackend.board.service;

import com.alltogether.lvupbackend.board.dto.BoardRequestDto;
import com.alltogether.lvupbackend.board.entity.Board;
import com.alltogether.lvupbackend.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    public List<Board> getBoardsByType(Character type) {
        return boardRepository.findByType(type);
    }

    public Board getArticle(Integer articleId) {
        return boardRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));
    }

    public Board createArticle(BoardRequestDto boardRequestDto) {
        Board board = new Board();
        board.setUserId(boardRequestDto.getUserId());
        board.setType(boardRequestDto.getType().charAt(0));
        board.setBoardTitle(boardRequestDto.getBoardTitle());
        board.setBoardContent(boardRequestDto.getBoardContent());
        board.setVisionable(boardRequestDto.getVisionable());
        board.setCreatedAt(LocalDateTime.now());
        board.setUpdatedAt(LocalDateTime.now());
        return boardRepository.save(board);
    }

    public Board updateArticle(Integer articleId, BoardRequestDto boardRequestDto) {
        Board board = getArticle(articleId);
        board.setBoardTitle(boardRequestDto.getBoardTitle());
        board.setBoardContent(boardRequestDto.getBoardContent());
        board.setVisionable(boardRequestDto.getVisionable());
        board.setUpdatedAt(LocalDateTime.now());
        return boardRepository.save(board);
    }

    public Page<Board> getBoardsByTypePaginated(Character type, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return boardRepository.findByType(type, pageRequest);
    }
    public void deleteArticle(Integer articleId) {
        boardRepository.deleteById(articleId);
    }
}