package com.alltogether.lvupbackend.smalltalk.ai.service;

<<<<<<< HEAD
import com.alltogether.lvupbackend.error.AppException;
import com.alltogether.lvupbackend.smalltalk.ai.dao.*;
import com.alltogether.lvupbackend.smalltalk.ai.dto.*;
import com.alltogether.lvupbackend.smalltalk.ai.entity.AiPersonality;
import com.alltogether.lvupbackend.smalltalk.ai.entity.AiScenario;
import com.alltogether.lvupbackend.smalltalk.ai.entity.AiSmalltalk;
import com.alltogether.lvupbackend.smalltalk.ai.entity.AiSmalltalkScore;
import com.alltogether.lvupbackend.smalltalk.ai.repository.AiPersonalityRepository;
import com.alltogether.lvupbackend.smalltalk.ai.repository.AiScenarioRepository;
import com.alltogether.lvupbackend.smalltalk.ai.repository.AiSmalltalkRepository;
import com.alltogether.lvupbackend.smalltalk.ai.repository.AiSmalltalkScoreRepository;
=======
import com.alltogether.lvupbackend.common.dto.PageResponseDto;
import com.alltogether.lvupbackend.error.AppException;
import com.alltogether.lvupbackend.smalltalk.ai.dao.*;
import com.alltogether.lvupbackend.smalltalk.ai.dto.*;
import com.alltogether.lvupbackend.smalltalk.ai.entity.*;
import com.alltogether.lvupbackend.smalltalk.ai.repository.*;
import com.alltogether.lvupbackend.smalltalk.user.dto.MatchDatesResponseDTO;
import com.alltogether.lvupbackend.smalltalk.user.dto.MatchHistoryResponseDto;
>>>>>>> develop
import com.alltogether.lvupbackend.user.domain.User;
import com.alltogether.lvupbackend.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
<<<<<<< HEAD
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
=======
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
>>>>>>> develop

@Slf4j
@Service
public class SmalltalkAiService {

    private final UserRepository userRepository;

    private final AiPersonalityRepository aiPersonalityRepository;

    private final AiSmalltalkRepository aiSmalltalkRepository;

    private final AiScenarioRepository aiScenarioRepository;

    private final AiSmalltalkScoreRepository aiSmalltalkScoreRepository;

    private final FastAPIService fastAPIService;

    private final SmalltalkAiRedisService redis;
    private final ObjectMapper objectMapper;


    public SmalltalkAiService(UserRepository userRepository, AiPersonalityRepository aiPersonalityRepository, AiSmalltalkRepository aiSmalltalkRepository, AiScenarioRepository aiScenarioRepository, FastAPIService fastAPIService, SmalltalkAiRedisService smalltalkAiRedisService, ObjectMapper objectMapper, AiSmalltalkScoreRepository aiSmalltalkScoreRepository) {

        this.userRepository = userRepository;
        this.aiPersonalityRepository = aiPersonalityRepository;
        this.aiSmalltalkRepository = aiSmalltalkRepository;
        this.aiScenarioRepository = aiScenarioRepository;
        this.fastAPIService = fastAPIService;
        this.redis = smalltalkAiRedisService;
        this.objectMapper = objectMapper;
        this.aiSmalltalkScoreRepository = aiSmalltalkScoreRepository;
    }

<<<<<<< HEAD
    public ConversationTextResponseDto conversationText(int userId, ConversationTextRequestDto request) {
        AiPersonality aiPersonality = aiPersonalityRepository.findById(request.getAiPersonalityId())
                .orElseThrow(() -> new IllegalArgumentException("초기 인격을 찾을 수 없습니다."));
        AiScenario aiScenario = aiScenarioRepository.findById(request.getAiScenarioId())
                .orElseThrow(() -> new IllegalArgumentException("초기 시나리오를 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        boolean isInit = false;

        // 대화 세션 조회, 없으면 생성
        Optional<AiSmalltalk> optionalAiSmalltalk = aiSmalltalkRepository.findFirstByUserAndAiPersonalityAndAiScenarioOrderByCreatedAtDesc(user, aiPersonality, aiScenario);
        AiSmalltalk aiSmalltalk;

        if (optionalAiSmalltalk.isPresent() && !optionalAiSmalltalk.get().getEndFlag()) {
            // 기존 세션이 있고, endFlag가 false일때 그대로 사용
            aiSmalltalk = optionalAiSmalltalk.get();
        } else {
            // 기존 세션이 없으면 새로 생성

            aiSmalltalk = new AiSmalltalk();
            aiSmalltalk.setAiPersonality(aiPersonality);
            aiSmalltalk.setAiScenario(aiScenario);
            aiSmalltalk.setUser(user);
            aiSmalltalk.setOverallScore((byte) 0);
            aiSmalltalk.setDialogue(Map.of());
            aiSmalltalk.setEndFlag(false);

            aiSmalltalk = aiSmalltalkRepository.save(aiSmalltalk);
            isInit = true;
        }

        ConversationData conversationData;
        // 첫 번째 대화인지에 따라 분기
        if (isInit) {
            // 초기 대화 텍스트 생성
            String[] initialConversationHistory = conversationHistoryInit(user, aiScenario, aiPersonality, request);
            String opName = initialConversationHistory[0];
            String conversationHistory = initialConversationHistory[1];
            List<ConversationRecord> conversationRecords = List.of(
                    new ConversationRecord(
                            1,
                            aiPersonality.getOpName(),
                            aiScenario.getFirstSpeakerType()=='u'?"user":"opponant",
                            request.getUserAnswer(),
                            0));

            // redis에 대화 히스토리 저장
            conversationData = new ConversationData();
            conversationData.setConversationHistory(conversationHistory);
            conversationData.setConversationRecords(conversationRecords);
            conversationData.setSentimentScoreSum(0);
            conversationData.setEndFlag(false);
            conversationData.setEndReason(null);
            redis.saveConversation(aiSmalltalk.getId().toString(), conversationData);
        } else {
            // redis에서 대화 히스토리 조회
            conversationData = redis.getConversation(aiSmalltalk.getId().toString());
            if (conversationData == null) {
                throw new AppException("대화 기록을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
            }
        }
        // fastAPI 호출
        MakeConversationRequestDto makeConversationRequestDto = new MakeConversationRequestDto(
                aiSmalltalk.getId(),
                conversationData,
                user.getNickname(),
                aiPersonality.getOpName(),
                request.getUserAnswer(),
                conversationData.getConversationRecords().size() + 1
                );

        MakeConversationResponseDto response = fastAPIService.makeConversation(makeConversationRequestDto);

        if(response.isEndFlag()) {
            //rdb 업데이트
            aiSmalltalk.setEndFlag(true);
            aiSmalltalkRepository.save(aiSmalltalk);
        }else {
            //redis 업데이트
            redis.saveConversation(aiSmalltalk.getId().toString(), response.toConversationData());
        }

        //response return
        return new ConversationTextResponseDto(
                response.getOpAnswer(),
                response.getNextSuggestedAnswer(),
                response.isEndFlag(),
                response.getEndReason()
        );
    }

    public String[] conversationHistoryInit(User user, AiScenario aiScenario, AiPersonality aiPersonality, ConversationTextRequestDto request) {
        // 성격 프롬프트 작성
        Map<String, String> personalityValuesMap = Map.of(
                "op_name", aiPersonality.getOpName(),
                "op_gender", aiPersonality.getOpGender() == 'M' ? "남자" : "여자",
                "op_age", String.valueOf(aiPersonality.getOpAge())
        );
        StringSubstitutor personalitySub = new StringSubstitutor(personalityValuesMap, "{", "}");
        String personalityPrompt = personalitySub.replace(aiPersonality.getPrompt());

        // 시나리오 프롬프트 작성
        Map<String, String> scenarioValuesMap = Map.of(
                "personality_prompt", personalityPrompt,
                "u_name", user.getNickname(),
                "u_gender", user.getGender() == 'M' ? "남자" : "여자",
                "u_age", String.valueOf(LocalDate.now().getYear() - user.getBirthyear() + 1),
                "op_name", aiPersonality.getOpName(),
                "user_answer", request.getUserAnswer()
        );
        StringSubstitutor scenarioSub = new StringSubstitutor(scenarioValuesMap, "{", "}");
        String scenarioPrompt = scenarioSub.replace(aiScenario.getPrompt());

        StringBuilder prompt = new StringBuilder();
        prompt.append("[시나리오]\n").append(scenarioPrompt).append("\n").append("[성격정보]\n").append(personalityPrompt);


        return new String[]{aiPersonality.getOpName(), prompt.toString()};
    }


    //callback
    public void refinementExplanationWhenCallback(int aiSmalltalkId, RefinementExplanationResponseDto request) {
        log.info("callback 메서드 시작");
        redis.saveCallbackExplanation(String.valueOf(aiSmalltalkId), request);
        try{
            AiSmalltalk aiSmalltalk = aiSmalltalkRepository.findById(aiSmalltalkId).orElseThrow(() -> new IllegalArgumentException("대화를 찾을 수 없습니다."));
            if(request.isEndFlag() && aiSmalltalk.getEndFlag()) {
                saveRefinmentExplanation(aiSmalltalk);
            }
        } catch (Exception e) {
            log.error("대화 평가 중 오류 발생", e);
        }
    }

    /* TODO: RDB에서는 aiSmalltalk의 endFlag를 true로 변경했지만, callback이 작동하지 않는 경우도 생각해야 함
    *
    */
    public void endConversationAll(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        List<AiSmalltalk> optionalAiSmalltalk = aiSmalltalkRepository.findAllByUserAndEndFlagIsFalse(user);
        for(AiSmalltalk aiSmalltalk : optionalAiSmalltalk) {
            // 마지막 업데이트가 10분 전인경우 세션 종료 및 redis 정리
            if(ChronoUnit.SECONDS.between(aiSmalltalk.getUpdatedAt(), LocalDate.now()) > 600) {
                aiSmalltalk.setEndFlag(true);
                aiSmalltalkRepository.save(aiSmalltalk);
                saveRefinmentExplanation(aiSmalltalk);
            }
        }
    }
    
    //가장 최근 대화 종료
    public void endConversationRecent(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Optional<AiSmalltalk> aiSmalltalk = aiSmalltalkRepository.findFirstByUserAndEndFlagIsFalseOrderByCreatedAtDesc(user);
                
        if(aiSmalltalk.isPresent()) {
            aiSmalltalk.get().setEndFlag(true);
            aiSmalltalkRepository.save(aiSmalltalk.get());
            saveRefinmentExplanation(aiSmalltalk.get());
        }
    }

    // redis 정리 및 대화기록 rdb 업데이트
    private void saveRefinmentExplanation(AiSmalltalk aiSmalltalk) {
        AiSmalltalkDialogue dialogue = new AiSmalltalkDialogue();
        dialogue.setRefinements(redis.getRefinements(aiSmalltalk.getId().toString()));

        log.info("평가기록 가져오기: {}", dialogue.getRefinements());

        ConversationData conversationData = redis.getConversation(aiSmalltalk.getId().toString());

        log.info("대화기록 가져오기: {}", conversationData);

        dialogue.setConversationRecords(conversationData.getConversationRecords());
        dialogue.setEndReason(conversationData.getEndReason());
        dialogue.setSentimentScoreSum(conversationData.getSentimentScoreSum());
        dialogue.setConversationHistory(conversationData.getConversationHistory());

        redis.endConversation(aiSmalltalk.getId().toString());

        //점수 계산
        int overallScore = 0;
        int[] score = new int[5];
        for(RefinementExplanationResponseDto refinement : dialogue.getRefinements()) {
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

=======
>>>>>>> develop
    public ScenarioListResponseDto getScenarios() {
        List<AiScenario> aiScenarios = aiScenarioRepository.findAll();
        ScenarioListResponseDto scenarioListResponseDto = new ScenarioListResponseDto(new ArrayList<>());
        for(AiScenario aiScenario : aiScenarios) {
            scenarioListResponseDto.getScenarios().add(new ScenarioResponseDto(
                    aiScenario.getId(),
                    aiScenario.getName(),
                    aiScenario.getMName(),
                    aiScenario.getFName(),
                    aiScenario.getEmoji(),
                    aiScenario.getDescription(),
                    aiScenario.getInitOpSpeaking()
            ));
        }
        return scenarioListResponseDto;
    }

    public PersonalityListResponseDto getPersonalities() {
        List<AiPersonality> aiPersonalities = aiPersonalityRepository.findAll();
        PersonalityListResponseDto personalityListResponseDto = new PersonalityListResponseDto(new ArrayList<>());
        for(AiPersonality aiPersonality : aiPersonalities) {
            personalityListResponseDto.getPersonalities().add(new PersonalityResponseDto(
                    aiPersonality.getId(),
                    aiPersonality.getName(),
                    aiPersonality.getPrompt(),
                    aiPersonality.getEmoji()
            ));
        }
        return personalityListResponseDto;
    }
<<<<<<< HEAD
=======

    //대화 시작 세팅
    public StartConversationResponseDto startConversation(int userId, StartConversationRequestDto request) {
        AiPersonality aiPersonality = aiPersonalityRepository.findById(request.getAiPersonalityId())
                .orElseThrow(() -> new IllegalArgumentException("초기 인격을 찾을 수 없습니다."));
        AiScenario aiScenario = aiScenarioRepository.findById(request.getAiScenarioId())
                .orElseThrow(() -> new IllegalArgumentException("초기 시나리오를 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String opName = request.getAiGender().equals("M") ? aiScenario.getMName() :  aiScenario.getFName();
        String opGender = request.getAiGender().equals("M") ? "남자" : "여자";


        // 성격 프롬프트 작성
        StringBuilder personalityPrompt = new StringBuilder();
        personalityPrompt.append("[성격정보]\n")
                .append("상대(").append(opName).append(")의 정보:\n")
                .append("- ").append(request.getAiAge()).append("세, ").append(opGender).append(" 한국인입니다.\n")
                .append(aiPersonality.getPrompt());

        // 시나리오 프롬프트 작성
        Map<String, String> scenarioValuesMap = Map.of(
                "u_name", user.getNickname(),
                "u_gender", user.getGender() == 'M' ? "남자" : "여자",
                "u_age", String.valueOf(LocalDate.now().getYear() - user.getBirthyear() + 1),
                "op_name", opName,
                "op_gender", opGender
        );
        StringSubstitutor scenarioSub = new StringSubstitutor(scenarioValuesMap, "{", "}");
        String scenarioPrompt = scenarioSub.replace(aiScenario.getPrompt());

        StringBuilder prompt = new StringBuilder();
        prompt.append("[처음 상황 정보]\n")
                .append(scenarioPrompt)
                .append("\n")
                .append(personalityPrompt.toString())
                .append("\n")
                .append("[지금까지의 대화]\n")
                .append(opName + " : " + aiScenario.getInitOpSpeaking());

        AiSmalltalk aiSmalltalk = new AiSmalltalk();
        aiSmalltalk.setAiPersonality(aiPersonality);
        aiSmalltalk.setAiScenario(aiScenario);
        aiSmalltalk.setUser(user);
        aiSmalltalk.setOverallScore((byte) 0);
        aiSmalltalk.setDialogue(Map.of());
        aiSmalltalk.setEndFlag(false);
        aiSmalltalk.setInitialPrompt(prompt.toString());
        aiSmalltalk.setOpName(opName);
        aiSmalltalk.setOpGender(request.getAiGender().charAt(0));
        aiSmalltalk.setOpAge(request.getAiAge());
        AiSmalltalk saved = aiSmalltalkRepository.save(aiSmalltalk);

        return new StartConversationResponseDto(saved.getId(), aiScenario.getName(), opName, aiScenario.getDescription(), aiScenario.getInitOpSpeaking());
    }

    public ConversationTextResponseDto continueConversation(ConversationContinueRequestDto request) {
        AiSmalltalk aiSmalltalk = aiSmalltalkRepository.findById(request.getAiConversationId())
                .orElseThrow(() -> new IllegalArgumentException("대화를 찾을 수 없습니다."));

        if(aiSmalltalk.getEndFlag()) {
            throw new AppException("대화가 이미 종료되었습니다.", HttpStatus.BAD_REQUEST);
        }

        ConversationData conversationData = redis.getConversation(aiSmalltalk.getId().toString());
        MakeConversationRequestDto requestDto = new MakeConversationRequestDto();
        if(conversationData == null) {
            // 대화의 처음 시작인경우
            requestDto.setAiSmalltalkId(aiSmalltalk.getId());
            requestDto.setConversationHistory(aiSmalltalk.getInitialPrompt());
            ArrayList<ConversationRecord> conversationRecords = new ArrayList<>();
            conversationRecords.add(new ConversationRecord(0, aiSmalltalk.getOpName(), "opponent", aiSmalltalk.getAiScenario().getInitOpSpeaking(), 0));
            requestDto.setConversationRecords(conversationRecords);
            requestDto.setSentimentScoreSum(0);
            requestDto.setUName(aiSmalltalk.getUser().getNickname());
            requestDto.setOpName(aiSmalltalk.getOpName());
            requestDto.setUserInput(request.getUserAnswer());
            requestDto.setThisTurn(request.getTurn());
        }else {
            //대화가 진행중이었던 경우 redis에서 대화 기록을 가져옴
            requestDto.setFromRedis(
                    request.getAiConversationId(),
                    conversationData,
                    aiSmalltalk.getUser().getNickname(),
                    aiSmalltalk.getOpName(),
                    request.getUserAnswer(),
                    request.getTurn());
        }

        MakeConversationResponseDto response = fastAPIService.makeConversation(requestDto);

        if(response.isEndFlag()) {
            //rdb 업데이트
            aiSmalltalk.setEndFlag(true);
            aiSmalltalkRepository.save(aiSmalltalk);
        }

        //redis 업데이트
        redis.saveConversation(aiSmalltalk.getId().toString(), response.toConversationData());

        return new ConversationTextResponseDto(
                response.getOpAnswer(),
                response.isEndFlag(),
                response.getEndReason(),
                response.getSentimentScoreSum()
        );
    }

    public String getHint(ConversationHintRequestDto request) {
        AiSmalltalk aiSmalltalk = aiSmalltalkRepository.findById(request.getAiConversationId())
                .orElseThrow(() -> new IllegalArgumentException("대화를 찾을 수 없습니다."));

        Optional<AiHint> aiHint = aiHintRepository.findByAiSmalltalkAndTurn(aiSmalltalk, request.getTurn());
        if(aiHint.isPresent()) {
            return aiHint.get().getHint();
        }else {
            return null;
        }
    }

    public void endConversation(int aiConversationId) {
        Optional<AiSmalltalk> aiSmalltalk = aiSmalltalkRepository.findById(aiConversationId);
        if(aiSmalltalk.isPresent()) {
            AiSmalltalk smalltalk = aiSmalltalk.get();
            smalltalk.setEndFlag(true);
            aiSmalltalkRepository.save(smalltalk);

            if(redis.isAllRefinementsArrived(String.valueOf(aiConversationId))) {
                fastAPIService.saveRefinmentExplanation(aiSmalltalkRepository.findById(aiConversationId).get());
            }
        }else {
            throw new AppException("대화를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getHistory(LocalDate date, Integer page, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        try {
            // 날짜의 시작과 끝 (ex. 2025-02-14 00:00:00 ~ 2025-02-15 00:00:00)
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

            // 페이지 번호, 사이즈 및 정렬 기준 설정 (여기서는 5개씩, 생성일 내림차순)
            Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

            log.info("ai대화 기록 조회 시작: date={}, userId={}, page={}", date, userId, page);

            // Repository 메서드 호출
            Page<AiSmalltalk> history = aiSmalltalkRepository.findAllByUserAndCreatedAtBetween(
                    user, startOfDay, endOfDay, pageable
            );

            List<AiSmalltalkHistoryDto> historyList = new ArrayList<>();
            for(AiSmalltalk aiSmalltalk : history) {
                if(aiSmalltalk.getDialogue().isEmpty()) {
                    continue;
                }
                historyList.add(new AiSmalltalkHistoryDto(
                        aiSmalltalk.getId(),
                        aiSmalltalk.getOverallScore(),
                        !aiSmalltalk.getDialogue().isEmpty(),
                        aiSmalltalk.getCreatedAt(),
                        aiSmalltalk.getOpName(),
                        aiSmalltalk.getOpAge(),
                        ""+aiSmalltalk.getOpGender(),
                        aiSmalltalk.getAiPersonality().getName(),
                        aiSmalltalk.getOpName() + chooseParticle(aiSmalltalk.getOpName()) + "의 " + aiSmalltalk.getAiScenario().getName()
                ));
            }
            
            return ResponseEntity.ok(historyList);
        } catch (Exception e) {
            throw new AppException("대화 기록 조회에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getReview(Integer userId, Integer id) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        AiSmalltalk aiSmalltalk = aiSmalltalkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("대화를 찾을 수 없습니다."));

        if(!aiSmalltalk.getUser().equals(user)) {
            throw new AppException("대화를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> dialogue = aiSmalltalk.getDialogue();
        dialogue.put("op_name", aiSmalltalk.getOpName());
        dialogue.put("op_age", aiSmalltalk.getOpAge());
        dialogue.put("op_gender", aiSmalltalk.getOpGender());
        return ResponseEntity.ok(dialogue);
    }

    public static String chooseParticle(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }
        Matcher matcher = Pattern.compile("([가-힣])$").matcher(word);
        if (matcher.find()) {
            char lastChar = matcher.group(1).charAt(0);
            // 한글 범위: 0xAC00(가) ~ 0xD7A3(힣)
            if (lastChar < 0xAC00 || lastChar > 0xD7A3) {
                return "";
            }
            int offset = lastChar - 0xAC00;
            int jong = offset % 28; // 종성 값 (0이면 없음)
            return (jong == 0) ? "와" : "과";
        }
        return "";
    }

    public void endAllConversations(Integer userId) {
        List<AiSmalltalk> aiSmalltalks = aiSmalltalkRepository.findAllByUserAndEndFlagFalse(userRepository.findById(userId).get());

        for(AiSmalltalk aiSmalltalk : aiSmalltalks) {
            if(aiSmalltalk.getEndFlag()) {
                continue;
            }
            aiSmalltalk.setEndFlag(true);
            aiSmalltalkRepository.save(aiSmalltalk);

            if(redis.isAllRefinementsArrived(String.valueOf(aiSmalltalk.getId()))) {
                fastAPIService.saveRefinmentExplanation(aiSmalltalkRepository.findById(aiSmalltalk.getId()).get());
            }
        }
    }

    public ResponseEntity<?> getHistoryCount(Integer userId, LocalDate date) {
        try {
            // 시작일 마지막일 계산
            LocalDateTime startOfMonth = date.withDayOfMonth(1).atStartOfDay();
            LocalDateTime endOfMonth = date.withDayOfMonth(date.lengthOfMonth()).atTime(23, 59, 59);

            log.info("월별 AI 날짜 조회 시작: userId: {}, 시작일: {}, 종료일: {}", userId, startOfMonth, endOfMonth);

            // 날짜 조회 시작
            List<String> matchDates = aiSmalltalkRepository.findDistinctAIDatesByUserIdAndPeriod(
                    userId,
                    startOfMonth,
                    endOfMonth);

            if (matchDates.isEmpty()) {
                log.info("해당 월에 AI 기록이 없습니다. userId: {}, 월: {}", userId, date);
                return ResponseEntity.ok(Collections.emptyList());
            }

            log.info("월별 AI 기록 조회 완료. {} 건", matchDates.size());
            MatchDatesResponseDTO response = new MatchDatesResponseDTO(matchDates, matchDates.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("월별 AI 날짜 조회 중 오류 발생", e);
            return ResponseEntity.internalServerError().body("월별 AI 날짜 조회 중 오류가 발생했습니다.");
        }
    }
>>>>>>> develop
}
