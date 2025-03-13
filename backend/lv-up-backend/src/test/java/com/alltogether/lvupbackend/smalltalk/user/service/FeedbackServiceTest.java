package com.alltogether.lvupbackend.smalltalk.user.service;

import com.alltogether.lvupbackend.smalltalk.checklist.dto.ChecklistMasterSimpleDto;
import com.alltogether.lvupbackend.smalltalk.checklist.dto.UserChecklistResponseDto;
import com.alltogether.lvupbackend.smalltalk.checklist.entity.UserRatingChecklistDetail;
import com.alltogether.lvupbackend.smalltalk.checklist.repository.ChecklistMasterRepository;
import com.alltogether.lvupbackend.smalltalk.user.repository.FeedbackRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FeedbackServiceTest {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private ChecklistMasterRepository checklistMasterRepository;

    @Autowired
    private FeedbackService feedbackService;

    @Test
    void getFeedbackOptions_Success() {
        // when
        List<ChecklistMasterSimpleDto> result = feedbackService.getFeedbackOptions();

        // then
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    void getUserChecklists_Success() {
        // given
        Integer matchingUserId = 17; // 실제 DB에 존재하는 matchingUserId 사용

        // when
        List<UserRatingChecklistDetail> result = feedbackService.getUserChecklists(matchingUserId);

        // then
        assertNotNull(result);
    }

    @Test
    void saveChecklist_Success() {
        // given
        Integer matchingUserId = 18; // 실제 DB에 존재하는 matchingUserId 사용
        List<String> options = Arrays.asList("1", "2", "3"); // 실제 DB에 존재하는 체크리스트 ID 사용

        // when
        //boolean result = feedbackService.saveChecklist(matchingUserId, options);

        // then
        //assertTrue(result);
    }

    @Test
    void saveChecklist_Failure() {
        // given
        Integer matchingUserId = 17;
        List<String> options = Arrays.asList("999999"); // 존재하지 않는 체크리스트 ID

        // when
        //boolean result = feedbackService.saveChecklist(matchingUserId, options);

        // then
        //assertFalse(result);
    }

    @Test
    void getUserChecklistDetail_Success() {
        // given
        Integer matchingId = 17; // 실제 DB에 존재하는 matchingId 사용

        // when
        //List<UserChecklistResponseDto> result = feedbackService.getUserChecklistDetail(matchingUserId);

        // then
        //assertNotNull(result);
    }

    @Test
    void getUserChecklists_Paging_Success() {
        // given
        Integer userId = 1; // 실제 DB에 존재하는 userId 사용
        Integer page = 1;
        String orderBy = "id";

        // when
        //List<UserChecklistResponseDto> result = feedbackService.getUserChecklists(userId, page, orderBy);

        // then
        //assertNotNull(result);
    }

    @Test
    void getUserChecklists_UserNotFound() {
        // given
        Integer nonExistentUserId = 99999;
        Integer page = 1;
        String orderBy = "id";

        // when
        //List<UserChecklistResponseDto> result = feedbackService.getUserChecklists(nonExistentUserId, page, orderBy);

        // then
        //assertTrue(result.isEmpty());
    }
}