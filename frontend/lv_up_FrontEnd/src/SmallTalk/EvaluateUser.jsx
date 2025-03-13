import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";

<<<<<<< HEAD
// api.js에서 가져온 API 함수
import { fetchFeedbackOptions, postFeedback } from "../api";
=======
// Smalltalk 평가 API
import { fetchFeedbackOptions, postFeedback } from "../api/SmalltalkAPI";
>>>>>>> develop

// **UserAPI에서 Mypage 데이터 조회 함수 추가 임포트**
import { fetchMypageData } from "../api/UserAPI";

import "./EvaluateUser.css";
import not_checked from "../assets/imageFile/not_checked_box.png";
import checked from "../assets/imageFile/check.png";
import sad_off from "../assets/imageFile/sad_off.png";
import smile_off from "../assets/imageFile/smile_off.png";
import happy_off from "../assets/imageFile/happy_off.png";
import sad_on from "../assets/imageFile/sad_on.png";
import smile_on from "../assets/imageFile/smile_on.png";
import happy_on from "../assets/imageFile/happy_on.png";

function EvaluateUser() {
  const navigate = useNavigate();
  const location = useLocation();

  // location.state로 opponentInfo를 받아오는 로직이 있다면 그대로 유지
  const { opponentInfo } = location.state || {};

  // -----------------------------
  // 1) 내 정보(myInfo) 상태
  // -----------------------------
  const [myInfo, setMyInfo] = useState(null);

  // -----------------------------
  // 2) 피드백 옵션 목록
  // -----------------------------
  const [reasons, setReasons] = useState([]);

  // 선택된 reason ID 배열
  const [selectedReasons, setSelectedReasons] = useState([]);

  // 이모지 상태(sad, smile, happy)
  const [selectedEmoji, setSelectedEmoji] = useState(null);

  // 작성한 후기 텍스트
  const [feedbackText, setFeedbackText] = useState("");

  // -----------------------------
  // (A) 컴포넌트 마운트 시 내 정보 조회
  // -----------------------------
  useEffect(() => {
    async function getMypageData() {
      try {
        const res = await fetchMypageData();
        // 예: res.data = { nickname: "reg_not_cm", introduction: "..." }
        setMyInfo(res.data);
      } catch (error) {
        console.error("마이페이지 데이터 조회 실패:", error);
      }
    }

    getMypageData();
  }, []);

  // -----------------------------
  // (B) 피드백 옵션 조회
  // -----------------------------
  useEffect(() => {
    async function getOptions() {
      try {
        const response = await fetchFeedbackOptions();
        // 예: response.data = [{ checklist_master_id: 1, name: "내 말을 잘 들어줘요" }, ...]
        setReasons(response.data);
      } catch (error) {
        console.error("피드백 옵션 불러오기 에러:", error);
      }
    }
    getOptions();
  }, []);

  // -----------------------------
  // 3) 이모지 클릭
  // -----------------------------
  const handleEmojiClick = (emoji) => {
    setSelectedEmoji(emoji);
  };

  // -----------------------------
  // 4) 피드백 체크박스 토글
  // -----------------------------
  const toggleReason = (reasonId) => {
    setSelectedReasons((prev) => {
      // 이미 선택되어 있다면 제거
      if (prev.includes(reasonId)) {
        return prev.filter((id) => id !== reasonId);
      }
      // 아니면 추가
      return [...prev, reasonId];
    });
  };

  // -----------------------------
  // 5) "평가완료" 버튼 클릭 -> 서버 전송
  // -----------------------------
  const handleComplete = async () => {
    try {
      // 이모지 -> ratingScore로 변환
      let ratingScore = 0; // sad
      if (selectedEmoji === "smile") ratingScore = 50;
      if (selectedEmoji === "happy") ratingScore = 100;

      // 서버로 전달할 데이터
      const feedbackData = {
        feedbackOptions: selectedReasons, // ex: [1,2,3]
        ratingScore, // ex: 0, 50, 100
        ratingContent: feedbackText, // 사용자가 작성한 후기
      };

      await postFeedback(feedbackData);
      console.log("📝 평가 완료:", feedbackData);

      // 완료 후 메인 페이지로 이동
      navigate("/main");
    } catch (error) {
      console.error("피드백 등록 에러:", error);
      // 필요 시 에러 안내 UI 추가
    }
  };

  return (
    <div className="evaluate-user-container">
      <div className="how-good-text">
        {/* 
          myInfo?.nickname가 있으면 그 값, 없으면 "USER"
          예: "reg_not_cm님, 상대방과의 대화 어떠셨나요?"
        */}
        {myInfo?.nickname || "USER"}님,
        <br />
        상대방과의 대화 어떠셨나요?
      </div>

      {/* 이모지 선택 영역 */}
      <div className="select-emoji">
        <div className="emoji-option" onClick={() => handleEmojiClick("sad")}>
          <img
            src={selectedEmoji === "sad" ? sad_on : sad_off}
            alt="별로에요"
          />
          <span className={selectedEmoji === "sad" ? "selected-text" : ""}>
            별로에요
          </span>
        </div>
        <div className="emoji-option" onClick={() => handleEmojiClick("smile")}>
          <img
            src={selectedEmoji === "smile" ? smile_on : smile_off}
            alt="좋아요"
          />
          <span className={selectedEmoji === "smile" ? "selected-text" : ""}>
            좋아요
          </span>
        </div>
        <div className="emoji-option" onClick={() => handleEmojiClick("happy")}>
          <img
            src={selectedEmoji === "happy" ? happy_on : happy_off}
            alt="최고에요"
          />
          <span className={selectedEmoji === "happy" ? "selected-text" : ""}>
            최고에요
          </span>
        </div>
      </div>

      {/* 피드백 옵션 체크박스 목록 */}
      <div className="select-reason-container">
        {reasons.map((reason) => (
          <div
            className="reason-item"
            onClick={() => toggleReason(reason.checklist_master_id)}
          >
            <img
              src={
                selectedReasons.includes(reason.checklist_master_id)
                  ? checked
                  : not_checked
              }
              alt="checkbox"
            />
            <span
              className={
                selectedReasons.includes(reason.checklist_master_id)
                  ? "selected-text"
                  : "reason-text"
              }
            >
              {reason.name}
            </span>
          </div>
        ))}
      </div>

      {/* 후기 텍스트 입력 */}
      <div className="reason-text-area">
        <p className="reason-item1">대화경험을 남겨주세요!</p>
        <p className="reason-item2">남겨주신 대화 후기는 상대방에게 공개돼요</p>
        <textarea
          placeholder="여기에 적어주세요"
          value={feedbackText}
          onChange={(e) => setFeedbackText(e.target.value)}
        />
      </div>

      {/* 평가완료 버튼 */}
      <div className="evaluate-complete-button" onClick={handleComplete}>
        평가완료
      </div>
    </div>
  );
}

export default EvaluateUser;
