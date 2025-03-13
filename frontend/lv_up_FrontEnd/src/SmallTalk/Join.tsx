<<<<<<< HEAD
import React, { useState } from "react";
=======
// Join.js
import React, { useState } from "react";
import BeatLoader from "react-spinners/BeatLoader";
>>>>>>> develop
import "./Join.css";
import back_button from "../assets/imageFile/backButton.png";
import first_rule from "../assets/imageFile/first_rule.png";
import second_rule from "../assets/imageFile/second_rule.png";
import third_rule from "../assets/imageFile/third_rule.png";
import forth_rule from "../assets/imageFile/forth_rule.png";
import not_checked from "../assets/imageFile/not_checked.png";
import checked from "../assets/imageFile/checked.png";
import { useLocation, useNavigate } from "react-router-dom";

<<<<<<< HEAD
/**
 * 부모(SmallTalk)로부터 받는 props:
 *  - onMatchRequest: (userId: string, score: string) => void
 *  - loading: 매칭 중 여부
 *  - matchResult: 서버/매칭 결과 표시
 */
type JoinProps = {
  onMatchRequest: (userId: string, score: string) => void;
  loading: boolean;
  matchResult: string | null;
};

function Join({ onMatchRequest, loading, matchResult }: JoinProps) {
  const location = useLocation();
  const navigate = useNavigate();

  // 🔸 userId, score를 임의로 생성
  const [userId] = useState("User" + Math.floor(Math.random() * 1000));
  const [score] = useState(String(Math.floor(Math.random() * 100)));

  const [isChecked, setIsChecked] = useState(false);
=======
type JoinProps = {
  onMatchRequest: () => void;
  onCancelMatch: () => void;
  loading: boolean;
  isMatching: boolean;
};

function Join({
  onMatchRequest,
  onCancelMatch,
  loading,
  isMatching,
}: JoinProps) {
  const location = useLocation();
  const navigate = useNavigate();

  const [isChecked, setIsChecked] = useState(true);
>>>>>>> develop

  // 1) 규칙 체크박스 토글
  const handleCheckToggle = () => {
    setIsChecked(!isChecked);
  };

  // 2) "통화 시작하기" 버튼 클릭
  const handleClick = () => {
<<<<<<< HEAD
    if (!isChecked) {
      // 규칙 동의 안 했으면 막기
      return;
    }
    onMatchRequest(userId, score);
=======
    if (!isChecked) return;
    onMatchRequest();
>>>>>>> develop
  };

  // 3) 뒤로가기 버튼
  const handleClose = () => {
    if (isMatching) {
      onCancelMatch();
    }
    navigate("/main");
  };

  const [isOpen, setIsOpen] = useState(false);

  return (
    <div className="loading-page">
      {/* 뒤로가기 버튼 */}
      <button className="loading-close-button" onClick={handleClose}>
        <img src={back_button} alt="back_button" />
      </button>

      <div className="loading-title">
        <h1>우리 모두가 지키면 <br />더 즐거운 스몰톡!</h1>
          <br />
      </div>

      {/* 이용 수칙 */}
      <div className="rules">
        <div className="rule-item">
          <img src={first_rule} alt="first_rule" />
          <p>1. 서로를 배려하며 대화해요</p>
        </div>
        <div className="rule-item">
          <img src={second_rule} alt="second_rule" />
          <p>2. 개인정보 질문 금지</p>
        </div>
        <div className="rule-item">
          <img src={third_rule} alt="third_rule" />
          <p>3. 공격적 / 폭력적인 말투 및 언행 금지</p>
        </div>
        <div className="rule-item">
          <img src={forth_rule} alt="forth_rule" />
          <p>4. 성적인 대화 유도 금지</p>
        </div>
      </div>

<<<<<<< HEAD
      <div className="rule-agreement">
        <p className="agreement-content">
          위 수칙을 어길시, 이용에 제한이 생길 수 있으며, 
          
          <br />
          코인과 티켓은 환불 및 복구되지 않습니다.
          <br />
          <br />
          통화 중 강제 종료 시 페널티가 주어질 수 있습니다.
        </p>
        <div className="check-agreement" onClick={handleCheckToggle}>
          <img src={isChecked ? checked : not_checked} alt="check" />
          <p className="checking-agreement">위 내용에 동의합니다.</p>
        </div>
      </div>

      {/* userId, score 입력 박스 제거됨 */}

      {/* 매칭 버튼 */}
      <div>
        <button
          className={`matching-button ${isChecked ? "active" : ""}`}
=======
      {/* 매칭 버튼 */}
      <div>
        <button
          className={`matching-button ${
            isMatching ? "matching" : isChecked ? "active" : ""
          }`}
>>>>>>> develop
          onClick={handleClick}
          disabled={!isChecked || loading}
          style={{ marginTop: 10 }}
        >
          {loading ? "매칭 중..." : "통화 시작하기"}
        </button>
      </div>

<<<<<<< HEAD
      {/* 매칭 결과 */}
      {matchResult && (
        <p style={{ marginTop: 20, fontWeight: "bold" }}>{matchResult}</p>
      )}
=======
      {/* <div className="rule-agreement">
        <p className="notice-title" onClick={() => setIsOpen(!isOpen)}>
          ⚠️ 주의사항
        </p>

        {isOpen && (
          <div className="agreement-content">
            <p>
              위 수칙을 어길 시,
              <br />
              이용에 제한이 생길 수 있으며
              <br />
              코인과 티켓은 환불 및 복구되지 않습니다.
              <br />
              <br />
              통화 중 강제 종료 시
              <br />
              패널티가 주어질 수 있습니다.
            </p>
          </div>
        )}

        <div className="check-agreement" onClick={handleCheckToggle}>
          <img src={isChecked ? checked : not_checked} alt="check" />
          <p className="checking-agreement" disabled={isMatching}>
            위 내용에 동의합니다.
          </p>
        </div>
      </div> */}
>>>>>>> develop
    </div>
  );
}

export default Join;
