<<<<<<< HEAD
import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "./AITalkPage.css";
import backButton from "../assets/imageFile/backButton.png";
import extend_button from "../assets/imageFile/extend_button.png";
import AIChatRecorder from "./AIChatRecorder"; // 마이크 녹음 컴포넌트 import
=======
import React, { useState, useEffect, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import BeatLoader from "react-spinners/BeatLoader";
import ClipLoader from "react-spinners/ClipLoader";
import "./AITalkPage.css";
import backButton from "../assets/imageFile/backButton.png";
import extend_button from "../assets/imageFile/extend_button.png";
import AIChatRecorder from "./AIChatRecorder";
import generateSpeech from "./generateSpeech";

// 프로필 이미지 import
import boy from "../assets/imageFile/profile_boy.png";
import girl from "../assets/imageFile/profile_girl.png";
import man from "../assets/imageFile/profile_young_man.png";
import woman from "../assets/imageFile/profile_young_woman.png";
import old_man from "../assets/imageFile/profile_old_man.png";
import old_woman from "../assets/imageFile/profile_old_woman.png";

// 체력(하트) 이미지 import
import heart from "../assets/imageFile/heart.png";
import broken_heart from "../assets/imageFile/broken_heart.png";
import broken_heart_fix from "../assets/imageFile/broken_heart_fix.png";

// API 함수 import
import {
  startAIConversation,
  continueAIConversation,
  getAIHint,
} from "../api/AITalkAPI";
>>>>>>> develop

function AITalkPage() {
  const location = useLocation();
  const navigate = useNavigate();

  // 대화 상태 (대화 목록을 저장하는 배열)
  const [messages, setMessages] = useState([]);
<<<<<<< HEAD

  // 확장 버튼 상태 (충고 표시 여부를 관리)
  const [expandedMessages, setExpandedMessages] = useState({});

  // 시나리오와 성격 정보를 받아오기
  const scenario = location.state?.scenario || { id: 0, name: "시나리오 선택" };
  const personality = location.state?.personality || { id: 0, name: "기본 성격" };

  // 확장 버튼 클릭 시 상태 변경
  const handleExtendClick = (index) => {
    setExpandedMessages((prev) => ({
      ...prev,
      [index]: !prev[index], // 클릭할 때마다 토글
    }));
  };

  // 사용자가 말한 내용을 추가하는 함수
  const addUserMessage = (text) => {
    if (!text) return; // 빈 문자열 방지
    setMessages((prevMessages) => [
      ...prevMessages,
      { type: "user", text }, // 사용자 메시지 추가
    ]);
  };

  // 백엔드에서 받은 AI 응답을 추가하는 함수
  const addAIResponse = (data) => {
    if (!data) return;
    setMessages((prevMessages) => [
      ...prevMessages,
      {
        type: "ai",
        text: data.opponent_answer,
        hint: data.next_suggested_answer,
        end_flag: data.end_flag,
      }, // AI 응답과 충고를 함께 저장
    ]);
=======
  const [expandedMessages, setExpandedMessages] = useState({}); // 메시지 확장 여부
  const [hintLoading, setHintLoading] = useState({}); // 힌트 로딩 상태
  const [audio, setAudio] = useState(null);
  const [loading, setLoading] = useState(false); // AI 응답 대기 여부
  const [turn, setTurn] = useState(1);
  const [hintTurn, setHintTurn] = useState(0);
  const [aiConversationId, setAiConversationId] = useState(null);
  const [aiName, setAiName] = useState(null);
  const [endFlag, setEndFlag] = useState(false);
  const [endReason, setEndReason] = useState(null);
  const [showTooltip, setShowTooltip] = useState(false);
  const tooltipRef = useRef(null);

  // ⭐ 하트(체력) 표현을 위한 sentimentScore 상태
  const [sentimentScore, setSentimentScore] = useState(0);
  const [previousScore, setPreviousScore] = useState(0);
  const [heartStates, setHeartStates] = useState([heart, heart, heart]);

  // ▼ 서버에서 받은 시나리오 제목, 설명도 저장할 state
  const [serverScenarioName, setServerScenarioName] = useState("");
  const [serverScenarioDesc, setServerScenarioDesc] = useState("");

  // props로 넘어온 값 (시나리오, 나이, 성별, 성격)
  const { scenario, age, gender, personality } = location.state || {};

  // 나이별 숫자 매핑
  const ageMapping = {
    "10대": 10,
    "20대": 20,
    "30대": 30,
    "40대": 40,
    "50대": 50,
    "60대": 60,
  };

  // 프로필 사진 결정 (나이와 성별 기준)
  const ageNumber = ageMapping[age] || 50;
  let profilePic;
  if (ageNumber < 20) {
    profilePic = gender === "male" ? boy : girl;
  } else if (ageNumber <= 40) {
    profilePic = gender === "male" ? man : woman;
  } else {
    profilePic = gender === "male" ? old_man : old_woman;
  }

  useEffect(() => {
    const updatedHearts = [0, 1, 2].map((index) => {
      if (index < sentimentScore) {
        return heartStates[index] === broken_heart_fix
          ? broken_heart_fix
          : broken_heart;
      }
      return heart;
    });

    setHeartStates(updatedHearts);

    // 새롭게 깨진 하트만 찾아서 2초 후 `broken_heart_fix`로 변경
    const newBrokenCount = sentimentScore - previousScore;
    if (newBrokenCount > 0) {
      const timer = setTimeout(() => {
        setHeartStates((prevHearts) =>
          prevHearts.map((img, index) =>
            index < sentimentScore && img === broken_heart
              ? broken_heart_fix
              : img
          )
        );
      }, 1500);

      return () => clearTimeout(timer);
    }

    setPreviousScore(sentimentScore);
  }, [sentimentScore]);

  // 1) 대화 시작 (AI 스몰톡 초기 생성)
  useEffect(() => {
    const initiateConversation = async () => {
      const requestData = {
        ai_scenario_id: scenario.id,
        ai_personality_id: personality.id,
        ai_age: ageMapping[age] || 60,
        ai_gender: gender === "male" ? "M" : "F",
      };

      try {
        // 서버에 대화 시작 요청
        const response = await startAIConversation(requestData);

        // 서버로부터 받은 정보 세팅
        setAiConversationId(response.ai_conversation_id);
        setAiName(response.name);

        // 서버에서 받은 시나리오 정보 (제목, 설명 등) 갱신
        // 주의: 서버 JSON에서 필드명이 "senario_name"인 경우 그대로 사용
        setServerScenarioName(response.senario_name);
        setServerScenarioDesc(response.description);

        // 만약 init_op_speaking이 있다면 첫 메시지로 추가
        if (response.init_op_speaking) {
          // AI 메시지 배열에 추가
          setMessages((prev) => [
            ...prev,
            { type: "ai", text: response.init_op_speaking },
          ]);

          // TTS 재생
          const audioUrl = await generateSpeech(
            response.init_op_speaking,
            gender
          );
          if (audioUrl) playAudio(audioUrl);
        }
      } catch (error) {
        console.error("대화 시작 오류:", error);
      }
    };
    initiateConversation();
  }, [scenario, age, gender, personality]);

  // 2) 사용자 메시지 추가
  const addUserMessage = async (text) => {
    if (!text || !aiConversationId) return;

    // 사용자 메시지에 turn 저장
    const currentTurn = turn;

    // 사용자 메시지를 messages에 추가
    setMessages((prev) => [...prev, { type: "user", text, turn: currentTurn }]);

    // AI 응답 대기 시작: 로딩용 AI 메시지 추가
    setLoading(true);
    setMessages((prev) => [...prev, { type: "ai", loading: true }]);

    // 서버에 사용자 입력 전송
    const requestData = {
      ai_conversation_id: aiConversationId,
      turn: turn,
      user_answer: text,
    };

    try {
      const response = await continueAIConversation(requestData);
      addAIResponse(response, currentTurn);
      setTurn((prev) => prev + 1);
      setHintTurn((prev) => prev + 1);
    } catch (error) {
      console.error("AI 응답 오류:", error);
    }
  };

  // 3) AI 응답 처리
  const addAIResponse = async (data, curTurn) => {
    setLoading(false);
    if (!data) return;

    // 마지막에 추가한 "loading" AI 메시지를 실제 AI 응답으로 교체
    setMessages((prev) => [
      ...prev.slice(0, -1),
      {
        type: "ai",
        text: data.opponent_answer,
        sentiment_score_sum: data.sentiment_score_sum,
        end_flag: data.end_flag,
        loading: false,
        turn: curTurn,
        end_reason: data.end_reason,
      },
    ]);

    // 새롭게 받은 감정 점수 반영
    if (typeof data.sentiment_score_sum === "number") {
      setSentimentScore(data.sentiment_score_sum);
    }

    // 대화 종료 상태 업데이트
    if (data.end_flag) {
      setEndFlag(true);
      setEndReason(data.end_reason);
    }

    // TTS 재생 (AI 음성)
    const audioUrl = await generateSpeech(data.opponent_answer, gender);
    if (audioUrl) playAudio(audioUrl);
  };

  // 오디오 재생
  const playAudio = (audioUrl) => {
    if (audio) audio.pause();
    const newAudio = new Audio(audioUrl);
    setAudio(newAudio);
    newAudio.play();
>>>>>>> develop
  };

  // 4) 힌트 가져오기 (extend 버튼)
  const handleExtendClick = async (index) => {
    // 이미 펼쳐져 있다면 닫기
    if (expandedMessages[index]) {
      setExpandedMessages((prev) => ({ ...prev, [index]: false }));
      return;
    }

    try {
      const message = messages[index];
      // 이미 hint가 있다면 바로 열기
      if (message.hint) {
        setExpandedMessages((prev) => ({ ...prev, [index]: true }));
        return;
      }

      const messageTurn = message.turn;

      // 힌트 로딩 시작
      setHintLoading((prev) => ({ ...prev, [index]: true }));

      // 서버에서 힌트를 가져오는 재귀 함수
      const fetchHintRecursively = async () => {
        try {
          const hintRequestData = {
            ai_conversation_id: aiConversationId,
            turn: messageTurn,
          };
          const response = await getAIHint(hintRequestData);

          // 204 or hint가 없으면 서버 생성 중 → 3초 후 재시도
          if (response?.status === 204 || !response?.answer_hint) {
            setTimeout(fetchHintRecursively, 3000);
            return;
          }

          // hint가 있다면 업데이트
          if (response.answer_hint) {
            updateHintInMessage(index, response.answer_hint);
            setHintLoading((prev) => ({ ...prev, [index]: false }));
            setExpandedMessages((prev) => ({ ...prev, [index]: true }));
          }
        } catch (error) {
          console.error("🔴 힌트 요청 중 오류:", error);
          setHintLoading((prev) => ({ ...prev, [index]: false }));
        }
      };

      fetchHintRecursively();
    } catch (err) {
      console.error("힌트 요청 오류:", err);
      setHintLoading((prev) => ({ ...prev, [index]: false }));
    }
  };

  // 메시지에 hint 업데이트
  const updateHintInMessage = (index, hintText) => {
    setMessages((prevMessages) =>
      prevMessages.map((msg, i) =>
        i === index ? { ...msg, hint: hintText } : msg
      )
    );
  };

  // 뒤로 가기
  const handleBackClick = async () => {
    if (aiConversationId) {
      const apiUrl = import.meta.env.VITE_API_URL;
      const url = `${apiUrl}/smalltalk/ai/conversation/end/${aiConversationId}`;
      try {
        await fetch(url, { method: "GET" });
      } catch (error) {
        console.error("🔴 대화 종료 오류:", error);
      }
    }
    navigate("/main");
  };

  // 대화 종료
  const handleEndChat = async () => {
    if (aiConversationId) {
      const apiUrl = import.meta.env.VITE_API_URL;
      const url = `${apiUrl}/smalltalk/ai/conversation/end/${aiConversationId}`;
      try {
        await fetch(url, { method: "GET" });
      } catch (error) {
        console.error("🔴 대화 종료 오류:", error);
      }
    }
    navigate("/main");
  };

  // 페이지 벗어날 때(새로고침, 탭 닫기 등) 대화 종료
  useEffect(() => {
    const finishChatOnUnload = () => {
      if (aiConversationId) {
        const apiUrl = import.meta.env.VITE_API_URL;
        const url = `${apiUrl}/smalltalk/ai/conversation/end/${aiConversationId}`;
        fetch(url, {
          method: "GET",
          keepalive: true,
        }).catch((error) =>
          console.error("대화 종료 요청 중 오류 발생:", error)
        );
      }
    };

    window.addEventListener("beforeunload", finishChatOnUnload);
    return () => {
      window.removeEventListener("beforeunload", finishChatOnUnload);
    };
  }, [aiConversationId]);

  // 바깥 클릭 감지 함수
  useEffect(() => {
    function handleClickOutside(event) {
      if (tooltipRef.current && !tooltipRef.current.contains(event.target)) {
        setShowTooltip(false); // 바깥 클릭 시 툴팁 닫기
      }
    }

    // 이벤트 리스너 추가
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  // **하트(체력) 표현**: sentimentScore만큼 깨진 하트로
  // 예: sentimentScore = 1 → [broken_heart, heart, heart]
  // 예: sentimentScore = 2 → [broken_heart, broken_heart, heart]
  // 예: sentimentScore = 3 → [broken_heart, broken_heart, broken_heart]
  return (
    <div>
      <div className="top-area">
<<<<<<< HEAD
        <img src={backButton} alt="backbutton" onClick={() => navigate(-1)} className="back-button" />
        <p className="top-title">
          {scenario.name} - {personality.name}
        </p>
      </div>

      <div className="chat-area">
        {/* 대화 메시지를 동적으로 표시 */}
        {messages.map((msg, index) => (
          <div key={index} className={msg.type === "user" ? "chat-box2" : "chat-box"}>
            <div className={msg.type === "user" ? "chat-text2" : "chat-text"}>{msg.text}</div>

            {/* AI 응답일 경우 확장 버튼 추가 */}
            {msg.type === "ai" && (
              <>
                <img className="extend-button" src={extend_button} alt="extend-button" onClick={() => handleExtendClick(index)} />
                {expandedMessages[index] && <div className="gray-text">{msg.hint}</div>}
              </>
=======
        {/* 첫 번째 줄 */}
        <div className="row1">
          <img
            src={backButton}
            alt="backbutton"
            onClick={handleBackClick}
            className="ai-back-button"
          />
          <p className="top-title">
            {serverScenarioName || scenario.scenario_name}
          </p>
        </div>

        {/* 두 번째 줄 */}
        <div className="row2">
          <div className="Life-area">
            LIFE :
            {heartStates.map((img, index) => (
              <img key={index} className="Life" src={img} alt="heart" />
            ))}
          </div>
        </div>
        {/* 세 번째 줄: 설명과 ? 버튼 */}
        <div className="row3">
          <div className="explain-container">
            <button
              className="tooltip-button"
              onClick={() => setShowTooltip(!showTooltip)}
            >
              ?
            </button>
            <span className="explain-text">상세설명</span>

            {/* 툴팁 */}
            {showTooltip && (
              <div className="tooltip" ref={tooltipRef}>
                대화 내용에 따라 AI의 감정지수가 변화하며, 라이프가 감소됩니다.{" "}
                <br />
                라이프가 3개 모두 소진했을 경우 대화가 종료됩니다.
              </div>
            )}
          </div>
        </div>
      </div>
      <hr className="aichat_line" />

      {/* 채팅 영역 */}
      <div className="chat-area">
        {/* 서버에서 받은 시나리오 설명이 있다면 우선 사용, 없으면 기존 scenario.description 사용 */}
        <p className="scenario-description">
          {serverScenarioDesc || scenario.description}
        </p>

        {/* 대화 메시지 표시 */}
        {messages.map((msg, index) => (
          <div
            key={index}
            className={msg.type === "user" ? "chat-box2" : "chat-box"}
          >
            {msg.type === "user" ? (
              // 사용자 메시지
              <div className="user-message">{msg.text}</div>
            ) : (
              // AI 메시지
              <div className="ai-message-container">
                <img
                  src={profilePic}
                  alt="AI Profile"
                  className="ai-profile-pic"
                />
                <div className="ai-message-details">
                  <div className="ai-name">{aiName}</div>
                  <div className="ai-speech-bubble">
                    {msg.loading ? <BeatLoader color="#fa400d" /> : msg.text}
                  </div>

                  {/* 힌트/확장 버튼 */}
                  {!msg.loading && msg.turn !== undefined && (
                    <>
                      <img
                        className="extend-button"
                        src={extend_button}
                        alt="extend-button"
                        onClick={() => handleExtendClick(index)}
                      />
                      {hintLoading[index] && (
                        <div className="gray-text">
                          <ClipLoader size={15} color="#fa400d" />
                        </div>
                      )}
                      {expandedMessages[index] && msg.hint && (
                        <div className="gray-text">{msg.hint}</div>
                      )}
                    </>
                  )}
                </div>
              </div>
>>>>>>> develop
            )}
          </div>
        ))}

        {/* 대화 종료 메시지와 결과 보기 버튼을 end_flag가 true일 때만 표시 */}
        {messages.length > 0 && messages[messages.length - 1]?.end_flag && (
          <>
            <div className="end-message">대화가 종료되었습니다.</div>
            <div className="go-AI-result" onClick={() => navigate("/ai-talk-result")}>
              결과 보러가기
            </div>
          </>
        )}

        {/* 마이크 버튼을 통한 AI 채팅 */}
        <AIChatRecorder
          scenarioId={scenario.id}
          personalityId={personality.id}
          // ***STT가 완료되면 바로 사용자 말풍선 추가***
          onTranscription={(userText) => {
            addUserMessage(userText);
          }}
          // ***백엔드 응답이 오면 AI 말풍선 추가***
          onResponseReceived={(data) => {
            console.log("백엔드 응답:", data);
            addAIResponse(data);
          }}
        />
      </div>
<<<<<<< HEAD
=======

      {/* 음성 녹음 컴포넌트 */}
      <AIChatRecorder
        onTranscription={addUserMessage}
        endFlag={endFlag}
        onEndChat={handleEndChat}
      />
      {endFlag && (
        <div className="end-popup-overlay">
          <div className="end-popup-content">
            <p className="end-chat-title">대화가 종료되었습니다.</p>
            <p className="end-chat-reason">종료 사유: {endReason}</p>
            <button className="end-chat-button" onClick={handleEndChat}>
              확인
            </button>
          </div>
        </div>
      )}
>>>>>>> develop
    </div>
  );
}

export default AITalkPage;
