package com.alltogether.lvupbackend.board.controller;

import com.alltogether.lvupbackend.board.dto.BoardRequestDto;
import com.alltogether.lvupbackend.board.entity.Board;
import com.alltogether.lvupbackend.board.service.BoardService;
import com.alltogether.lvupbackend.user.domain.User;
import com.alltogether.lvupbackend.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    @Autowired
    private BoardService boardService;
    @Autowired
    private UserService userService;

    @GetMapping("/event")
    public ResponseEntity<List<Board>> getEventList() {
        return ResponseEntity.ok(boardService.getBoardsByType('E'));
    }


    @GetMapping("/view/{articleId}")
    public ResponseEntity<Board> getArticle(@PathVariable Integer articleId) {
        return ResponseEntity.ok(boardService.getArticle(articleId));
    }


    @PostMapping
    public ResponseEntity<?> createArticle(@RequestBody BoardRequestDto boardRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = Integer.parseInt(authentication.getName());
        User user = userService.findUser(userId);

        boardRequestDto.setUserId(user.getUserId());
        Board createdBoard = boardService.createArticle(boardRequestDto);
        return ResponseEntity.ok(createdBoard);
    }
    @GetMapping("/announcement")
    public ResponseEntity<?> getAnnouncementListPaginated(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int size) {
        Page<Board> announcementPage = boardService.getBoardsByTypePaginated('A', page - 1, size);

        Map<String, Object> response = new HashMap<>();
        response.put("announcements", announcementPage.getContent());
        response.put("currentPage", announcementPage.getNumber() + 1);
        response.put("totalItems", announcementPage.getTotalElements());
        response.put("totalPages", announcementPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<Board> updateArticle(@PathVariable Integer articleId, @RequestBody BoardRequestDto boardRequestDto) {
        return ResponseEntity.ok(boardService.updateArticle(articleId, boardRequestDto));
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Integer articleId) {
        boardService.deleteArticle(articleId);
        return ResponseEntity.ok().build();
    }
}