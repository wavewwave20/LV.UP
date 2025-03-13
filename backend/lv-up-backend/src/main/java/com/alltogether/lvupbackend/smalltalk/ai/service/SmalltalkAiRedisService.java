package com.alltogether.lvupbackend.smalltalk.ai.service;

import com.alltogether.lvupbackend.smalltalk.ai.dao.ConversationData;
import com.alltogether.lvupbackend.smalltalk.ai.dao.ConversationRecord;
import com.alltogether.lvupbackend.smalltalk.ai.dto.RefinementExplanationResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class SmalltalkAiRedisService {

    @Qualifier("objectRedisTemplate")
    private final RedisTemplate<Object, Object> redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public SmalltalkAiRedisService(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 대화 기록을 redis에 hash로 저장
    public void saveConversation(String smalltalkId, ConversationData conversationData) {
        String key = "c_" + smalltalkId;
        try {
            // conversation_records를 JSON 문자열로 변환
            String conversationRecordsJson = objectMapper.writeValueAsString(conversationData.getConversationRecords());

            // 각 필드를 Redis HASH에 저장
            redisTemplate.opsForHash().put(key, "conversation_history", conversationData.getConversationHistory());
            redisTemplate.opsForHash().put(key, "conversation_records", conversationRecordsJson);
            redisTemplate.opsForHash().put(key, "sentiment_score_sum", conversationData.getSentimentScoreSum());
            redisTemplate.opsForHash().put(key, "end_flag", conversationData.isEndFlag());
            redisTemplate.opsForHash().put(key, "end_reason", conversationData.getEndReason());
        } catch (JsonProcessingException e) {
            log.error("대화 저장 중 오류 발생", e);
        }
    }

    // 대화 기록을 redis에서 조회
    public ConversationData getConversation(String smalltalkId) {
        String key = "c_" + smalltalkId;
        // Redis HASH의 모든 필드와 값을 Map 형태로 가져옴
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

        if (entries.isEmpty()) {
            // 기록이 없는 경우 null 또는 기본값 반환
            return null;
        }

        ConversationData conversationData = new ConversationData();

        // conversation_history (String)
        conversationData.setConversationHistory((String) entries.get("conversation_history"));

        // conversation_records (JSON 문자열을 List<ConversationRecord>로 변환)
        String conversationRecordsJson = (String) entries.get("conversation_records");
        try {
            List<ConversationRecord> conversationRecords = objectMapper.readValue(
                    conversationRecordsJson,
                    new TypeReference<List<ConversationRecord>>() {}
            );
            conversationData.setConversationRecords(conversationRecords);
        } catch (JsonProcessingException e) {
            log.error("대화 기록(JSON) 변환 중 오류 발생", e);
        }

        // sentiment_score_sum (int)
        Object scoreSumObj = entries.get("sentiment_score_sum");
        if (scoreSumObj instanceof Integer) {
            conversationData.setSentimentScoreSum((Integer) scoreSumObj);
        } else if (scoreSumObj instanceof String) {
            conversationData.setSentimentScoreSum(Integer.parseInt((String) scoreSumObj));
        }

        // end_flag (boolean)
        Object endFlagObj = entries.get("end_flag");
        if (endFlagObj instanceof Boolean) {
            conversationData.setEndFlag((Boolean) endFlagObj);
        } else if (endFlagObj instanceof String) {
            conversationData.setEndFlag(Boolean.parseBoolean((String) endFlagObj));
        }

        // end_reason (String)
        conversationData.setEndReason((String) entries.get("end_reason"));

        return conversationData;
    }


    // 대화 상세 평가 callback을 redis에 list로 저장
    public void saveCallbackExplanation(String aiSmalltalkId, RefinementExplanationResponseDto callbackResponse) {
        String key = "e_" + aiSmalltalkId;
        try {
            // callbackResponse를 JSON 문자열로 변환
            String callbackResponseJson = objectMapper.writeValueAsString(callbackResponse);
            // List에 JSON 문자열 추가 (오른쪽에 push)
            redisTemplate.opsForList().rightPush(key, callbackResponseJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // 평가 요청시 레디스에 평가요청했음을 저장
    public void saveRefinementRequest(String smalltalkId) {
        String key = "ee_" + smalltalkId;
        redisTemplate.opsForList().rightPush(key, "1");
    }

    //평가가 모두 도착 했는지 확인
    public boolean isAllRefinementsArrived(String smalltalkId) {
        String key = "ee_" + smalltalkId;
        Long size = redisTemplate.opsForList().size(key);
        return Objects.equals(size, redisTemplate.opsForList().size("e_" + smalltalkId));
    }

    public void endConversation(String smalltalkId) {
        String key = "c_" + smalltalkId;
        redisTemplate.delete(key);

        String key2 = "e_" + smalltalkId;
        redisTemplate.delete(key2);

        String key3 = "ee_" + smalltalkId;
        redisTemplate.delete(key3);
    }

    public List<RefinementExplanationResponseDto> getRefinements(String smalltalkId) {
        String key = "e_" + smalltalkId;
        List<Object> entries = redisTemplate.opsForList().range(key, 0, -1);
        if (entries == null) {
            return null;
        }

        List<RefinementExplanationResponseDto> explanations = new ArrayList<>();
        for (Object entry : entries) {
            if (entry instanceof String) {
                try {
                    RefinementExplanationResponseDto dto = objectMapper.readValue((String) entry, RefinementExplanationResponseDto.class);
                    explanations.add(dto);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return explanations;
    }

}
