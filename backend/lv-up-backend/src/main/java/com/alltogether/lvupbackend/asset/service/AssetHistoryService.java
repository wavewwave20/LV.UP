package com.alltogether.lvupbackend.asset.service;

import com.alltogether.lvupbackend.asset.entity.AssetHistory;
import com.alltogether.lvupbackend.asset.repository.AssetHistoryRepository;
import com.alltogether.lvupbackend.smalltalk.user.entity.Matching;
import com.alltogether.lvupbackend.smalltalk.user.entity.MatchingUser;
import com.alltogether.lvupbackend.smalltalk.user.repository.MatchingRepository;
import com.alltogether.lvupbackend.smalltalk.user.repository.MatchingUserRepository;
import com.alltogether.lvupbackend.user.domain.User;
import com.alltogether.lvupbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetHistoryService {
    private final UserRepository userRepository;
    private final AssetHistoryRepository assetHistoryRepository;
    private final MatchingRepository matchingRepository;
    private final MatchingUserRepository matchingUserRepository;

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    @Transactional
    public void distributeTickets() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            // 현재 티켓 수 확인
            int currentTickets = user.getTicketQuantity();

            // 3개 미만이면 3개로 채우기
            if (currentTickets < 3) {
                int ticketsToAdd = 3 - currentTickets;

                user.setTicketQuantity(3);
                userRepository.save(user);

                AssetHistory assetHistory = AssetHistory.builder()
                        .user(user)
                        .type('T')
                        .amount(ticketsToAdd)
                        .beforeQuantity(currentTickets)
                        .afterQuantity(3)
                        .rId(0) // 매일 24시
                        .rTableType("매일 24시")
                        .reason("일일 티켓 배포")
                        .build();

                assetHistoryRepository.save(assetHistory);
            }
        }
    }

    public void createCoinHistory(User user, int amount, int beforeQuantity, int afterQuantity, int relatedId, String reason) {
        AssetHistory assetHistory = AssetHistory.builder()
                .user(user)
                .type('C')
                .amount(amount)
                .beforeQuantity(beforeQuantity)
                .afterQuantity(afterQuantity)
                .rId(relatedId)
                .rTableType("미션")
                .reason(reason)
                .build();

        assetHistoryRepository.save(assetHistory);
    }

    public void createTicketHistory(User user, int amount, int beforeQuantity, int afterQuantity, int relatedId, String reason) {
        AssetHistory assetHistory = AssetHistory.builder()
                .user(user)
                .type('T')
                .amount(amount)
                .beforeQuantity(beforeQuantity)
                .afterQuantity(afterQuantity)
                .rId(relatedId)
                .rTableType("미션")
                .reason(reason)
                .build();

        assetHistoryRepository.save(assetHistory);
    }

    // 가입 후 티켓 배부
    @Transactional
    public void distributeInitialTickets(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 티켓 3개 지급
        int beforeQuantity = user.getTicketQuantity();
        user.setTicketQuantity(beforeQuantity + 3);
        userRepository.save(user);

        // 지급 이력 생성
        AssetHistory assetHistory = AssetHistory.builder()
                .user(user)
                .type('T')
                .amount(3)
                .beforeQuantity(beforeQuantity)
                .afterQuantity(beforeQuantity + 3)
                .rId(0)
                .rTableType("회원가입")
                .reason("가입 축하 티켓")
                .build();

        assetHistoryRepository.save(assetHistory);
    }

    // 매칭 연장시 coin 소모 이력 생성
    public void insertCoin(Integer userId, Integer matchingId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new RuntimeException("매칭을 찾을 수 없습니다."));

        MatchingUser matchingUser = matchingUserRepository.findByMatchingIdAndUserId(matching, userId)
                .orElseThrow(() -> new RuntimeException("매칭 사용자를 찾을 수 없습니다."));

        int beforeQuantity = user.getCoinQuantity();
        user.setCoinQuantity(beforeQuantity - 1);
        userRepository.save(user);

        AssetHistory assetHistory = AssetHistory.builder()
                .user(user)
                .type('C')
                .amount(-1)
                .beforeQuantity(beforeQuantity)
                .afterQuantity(beforeQuantity - 1)
                .rId(matchingUser.getMatchingUserId())
                .rTableType("matching_user")
                .reason("매칭 연장 소모")
                .build();

        assetHistoryRepository.save(assetHistory);
    }


    public void showMeTheMoney(Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        int beforeCoin = user.getCoinQuantity();
        int beforeTicket = user.getTicketQuantity();
        user.setCoinQuantity(beforeCoin + 100);
        user.setTicketQuantity(beforeTicket + 100);
        userRepository.save(user);

        AssetHistory assetHistory1 = AssetHistory.builder()
                .user(user)
                .type('T')
                .amount(100)
                .beforeQuantity(beforeCoin)
                .afterQuantity(beforeCoin + 100)
                .rId(0)
                .rTableType("user")
                .reason("Show me the money")
                .build();

        AssetHistory assetHistory2 = AssetHistory.builder()
                .user(user)
                .type('C')
                .amount(100)
                .beforeQuantity(beforeCoin)
                .afterQuantity(beforeTicket + 100)
                .rId(0)
                .rTableType("user")
                .reason("Show me the money")
                .build();

        assetHistoryRepository.save(assetHistory1);
        assetHistoryRepository.save(assetHistory2);
    }
}
