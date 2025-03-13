package com.alltogether.lvupbackend.user.controller;

import com.alltogether.lvupbackend.user.dto.UserLevelInfoDto;
import com.alltogether.lvupbackend.user.service.UserLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/level")
@RequiredArgsConstructor
public class UserLevelController {
    private final UserLevelService userLevelService;


    @GetMapping("/info")
    public ResponseEntity<UserLevelInfoDto> getUserLevelInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = Integer.parseInt(authentication.getName());

        UserLevelInfoDto levelInfo = userLevelService.calculateUserLevel(userId);
        return ResponseEntity.ok(levelInfo);
    }
}