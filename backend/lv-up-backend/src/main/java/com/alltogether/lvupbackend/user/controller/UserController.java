package com.alltogether.lvupbackend.user.controller;

import com.alltogether.lvupbackend.asset.service.AssetHistoryService;
import com.alltogether.lvupbackend.error.AppException;
import com.alltogether.lvupbackend.user.dto.*;
import com.alltogether.lvupbackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    AssetHistoryService assetHistoryService;

    private Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("현재 인증된 사용자가 없습니다.");
        }
        return Integer.parseInt(authentication.getName());
    }

    // 회원탈퇴
    @DeleteMapping
    public ResponseEntity<?> withdrawUser() {
        Integer userId = getCurrentUserId();
        log.info("회원탈퇴 요청 - userId: {}", userId);
        userService.withdrawUser(userId);
        return ResponseEntity.ok("회원탈퇴가 완료되었습니다.");
    }

    // 관심사
    @GetMapping("/interest/options")
    public ResponseEntity<?> getInterestOptions() {
        List<InterestResponseDto> interests = userService.getInterestOptions();
        return ResponseEntity.ok(interests);
    }

    @GetMapping("/interest")
    public ResponseEntity<?> getUserInterests() {
        Integer userId = getCurrentUserId();
        List<String> userInterests = userService.getUserInterests(userId);
        return ResponseEntity.ok(userInterests);
    }

    @PostMapping("/interest")
    public ResponseEntity<?> selectUserInterests(@RequestBody List<Integer> interestIds) {
        Integer userId = getCurrentUserId();
        userService.selectUserInterests(userId, interestIds);
        return ResponseEntity.ok().build();
    }

    // 아바타 목록
    @GetMapping("/avatar/all")
    public ResponseEntity<?> getAllAvatars() {
        List<AvatarResponseDto> avatars = userService.getAvatarList();
        return ResponseEntity.ok(avatars);
    }

    // 대표 아바타 선택
    @PostMapping("/avatar")
    public ResponseEntity<?> selectMainAvatar(@RequestBody Integer avatarId) {
        Integer userId = getCurrentUserId();
        userService.selectMainAvatar(userId, avatarId);

        return ResponseEntity.ok().build();
    }

    // 사용자의 보유 아바타 목록 조회
    @GetMapping("/avatar")
    public ResponseEntity<?> getUserAvatarList() {
        Integer userId = getCurrentUserId();
        List<AvatarResponseDto> avatars = userService.getUserAvatarList(userId);
        return ResponseEntity.ok(avatars);
    }

    // 선택된 아바타 id조회
    @GetMapping("/avatar/selected")
    public ResponseEntity<?> getSelectedAvatarId() {
        Integer userId = getCurrentUserId();
        Integer selectedAvatarId = userService.getSelectedAvatarId(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("avatarId", selectedAvatarId);
        return ResponseEntity.ok(response);
    }

    // Coin 관련
    @GetMapping("/coin")
    public ResponseEntity<?> getCoinAmount() {
        Integer userId = getCurrentUserId();
        int coins = userService.getCoinAmount(userId);
        return ResponseEntity.ok(coins);
    }

    // Ticket 관련
    @GetMapping("/ticket")
    public ResponseEntity<?> getTicketAmount() {
        Integer userId = getCurrentUserId();
        int tickets = userService.getTicketAmount(userId);
        return ResponseEntity.ok(tickets);
    }

    // Main 페이지 정보
    @GetMapping("/main")
    public ResponseEntity<?> getMainInfo() {
        Integer userId = getCurrentUserId();
        UserResponseDto mainInfo = userService.getMainInfo(userId);
        return ResponseEntity.ok(mainInfo);
    }
    
    // User 페이지 정보
    @GetMapping("")
    public ResponseEntity<?> getUserInfo() {
        Integer userId = getCurrentUserId();
        UserInfoDto userInfo = userService.getUserInfo(userId);
        return ResponseEntity.ok(userInfo);
    }

    // 닉네임 중복 확인 후 변경
    @PostMapping("/nickname")
    public ResponseEntity<Boolean> checkAndUpdateNickname(@RequestBody String nickname) {
        Integer userId = getCurrentUserId();
        boolean updated = userService.checkAndUpdateNickname(userId, nickname);
        return ResponseEntity.ok(updated);
    }
    // 닉네임 중복 확인
    @PostMapping("/checknickname")
    public ResponseEntity<Boolean> checkNickname(@RequestBody String nickname) {
        Integer userId = getCurrentUserId();
        boolean check = userService.checkNickname(userId, nickname);
        return ResponseEntity.ok(check);
    }


    // 자기소개 변경
    @PostMapping("/introduction")
    public ResponseEntity<?> updateIntroduction(@RequestBody String introduction) {
        Integer userId = getCurrentUserId();
        userService.updateIntroduction(userId, introduction);
        return ResponseEntity.ok().build();
    }

    // 회원가입 최종
    @PostMapping("/register")
    public ResponseEntity<?> updateRegister(@RequestBody RegisterCompleteDto registerDto) {
        Integer userId = getCurrentUserId();
        userService.completeRegister(userId, registerDto);
        return ResponseEntity.ok().build();
    }

    // 관리자인지
    @GetMapping("/role")
    public ResponseEntity<?> getUserRole(){
        Integer userId = getCurrentUserId();
        boolean isAdmin = userService.isAdmin(userId);
        return ResponseEntity.ok(isAdmin);
    }

    
    @GetMapping("/command/showmethemoney")
    public ResponseEntity<?> showMeTheMoney() {
        assetHistoryService.showMeTheMoney(getCurrentUserId());
        return ResponseEntity.ok("Show me the money !!");
    }
}