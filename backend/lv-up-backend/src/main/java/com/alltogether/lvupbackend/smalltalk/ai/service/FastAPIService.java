package com.alltogether.lvupbackend.smalltalk.ai.service;

import com.alltogether.lvupbackend.smalltalk.ai.dto.ConversationTextResponseDto;
import com.alltogether.lvupbackend.smalltalk.ai.dto.MakeConversationRequestDto;
import com.alltogether.lvupbackend.smalltalk.ai.dto.MakeConversationResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class FastAPIService {

    private final WebClient webClient;

    private final AiSmalltalkRepository aiSmalltalkRepository;

    private final AiHintRepository aiHintRepository;

    private final SmalltalkAiRedisService redis;

    private final MissionService missionService;

    private final ObjectMapper objectMapper;

    private final AiSmalltalkScoreRepository aiSmalltalkScoreRepository;




    private final String fastAPIServer = "YOUR_FASTAPI_URL";

    public FastAPIService(WebClient webClient) {
        this.webClient = webClient;
    }


    public MakeConversationResponseDto makeConversation(MakeConversationRequestDto makeConversationRequestDto) {
        log.info("FastAPI 대화 생성 요청 - request: {}", makeConversationRequestDto);
        redis.saveRefinementRequest(String.valueOf(makeConversationRequestDto.getAiSmalltalkId()));

        return webClient.post()
                .uri(fastAPIServer + "/conversation-turn")
                .bodyValue(makeConversationRequestDto)
                .retrieve()
                .bodyToMono(MakeConversationResponseDto.class)
                .block();
    }

    //callback
    public void hintWhenCallback(int smallTalkId, HintCallbackDto request) {
        AiHint aiHint = new AiHint();
        aiHint.setAiSmalltalk(aiSmalltalkRepository.findById(smallTalkId).orElseThrow(() -> new IllegalArgumentException("대화를 찾을 수 없습니다.")));
        aiHint.setHint(request.getHint());
        aiHint.setTurn(request.getTurn());
        aiHintRepository.save(aiHint);
    }

    //callback
    public void refinementExplanationWhenCallback(int aiSmalltalkId, RefinementExplanationCallbackDto request) {
        log.info("callback 메서드 시작");
        redis.saveCallbackExplanation(String.valueOf(aiSmalltalkId), request);
        try{
            AiSmalltalk aiSmalltalk = aiSmalltalkRepository.findById(aiSmalltalkId).orElseThrow(() -> new IllegalArgumentException("대화를 찾을 수 없습니다."));
            /* 감정 손상 엔딩
            손상 리스폰스(rdb EndFlag = true) -> 콜백 -> 뒤로가기  콜백 오는순간 저장
	        손상 리스폰스(rdb EndFlag = true) -> 뒤로가기 -> 콜백  콜백 오는순간 저장
             */
            if(request.isEndFlag()) {
                aiSmalltalk.setEndFlag(true);
                aiSmalltalkRepository.save(aiSmalltalk);
                saveRefinmentExplanation(aiSmalltalk);
            }
            /*
            이미 엔딩된 대화에 대한 평가가 왔을 때 마지막 평가가 도착하면 저장
             */
            else if (aiSmalltalk.getEndFlag() && redis.isAllRefinementsArrived(String.valueOf(aiSmalltalkId))) {
                saveRefinmentExplanation(aiSmalltalk);
            }
        } catch (Exception e) {
            log.error("대화 평가 중 오류 발생", e);
        }
    }

    // redis 정리 및 대화기록 rdb 업데이트
    public void saveRefinmentExplanation(AiSmalltalk aiSmalltalk) {
        AiSmalltalkDialogue dialogue = new AiSmalltalkDialogue();
        dialogue.setRefinements(redis.getRefinements(aiSmalltalk.getId().toString()));
        if(dialogue.getRefinements() == null) {
            log.error("평가기록이 없습니다.");
            return;
        }
        log.info("평가기록 가져오기: {}", dialogue.getRefinements());

        ConversationData conversationData = redis.getConversation(aiSmalltalk.getId().toString());
        if(conversationData == null) {
            log.error("대화기록이 없습니다.");
            return;
        }
        log.info("대화기록 가져오기: {}", conversationData);

        dialogue.setConversationRecords(conversationData.getConversationRecords());
        dialogue.setEndReason(conversationData.getEndReason());
        dialogue.setSentimentScoreSum(conversationData.getSentimentScoreSum());
        dialogue.setConversationHistory(conversationData.getConversationHistory());

        redis.endConversation(aiSmalltalk.getId().toString());

        //점수 계산
        int overallScore = 0;
        int[] score = new int[5];
        for(RefinementExplanationCallbackDto refinement : dialogue.getRefinements()) {
            for(int i = 1; i <= 5; i++) {
                score[i-1] += refinement.getScores().get(""+i).getScore();
            }
        }
        for(int i = 0; i < 5; i++) {
            score[i] /= dialogue.getRefinements().size();
        }

        for(int i = 0; i < 5; i++) {
            overallScore += score[i];
        }
        overallScore /= 5;
        aiSmalltalk.setOverallScore((byte) overallScore);

        // 미션 업데이트
        Integer userId = aiSmalltalk.getUser().getUserId();

        missionService.updateAiSmalltalkAll(userId);

        missionService.updateAiSmalltalkResult(userId);

        // 점수에 따른 미션 업데이트
        if (overallScore >= 60) {
            missionService.updateAiSmalltalkScore60(userId);
        }
        if (overallScore >= 80) {
            missionService.updateAiSmalltalkScore80(userId);
        }


        //rdb 업데이트
        aiSmalltalk.setDialogue(objectMapper.convertValue(dialogue, Map.class));

        log.info("대화 업데이트: {}", aiSmalltalk.getDialogue());
        aiSmalltalkRepository.save(aiSmalltalk);

        for(int i = 0; i < 5; i++) {
            AiSmalltalkScore aiSmalltalkScore = new AiSmalltalkScore();
            aiSmalltalkScore.setAiSmalltalk(aiSmalltalk);
            aiSmalltalkScore.setScore((byte) score[i]);
            aiSmalltalkScore.setScoreId((byte) (i+1));
            aiSmalltalkScoreRepository.save(aiSmalltalkScore);
        }
    }
}
