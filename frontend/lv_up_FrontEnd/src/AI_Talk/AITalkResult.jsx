import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { getResult } from "../api/AITalkResultAPI";
import backButton from "../assets/imageFile/backButton.png";
import circle from "../assets/imageFile/circle.png";
import triangle from "../assets/imageFile/triangle.png";
import "./AITalkResult.css";

// 프로필 이미지 import
import boy from "../assets/imageFile/profile_boy.png";
import girl from "../assets/imageFile/profile_girl.png";
import man from "../assets/imageFile/profile_young_man.png";
import woman from "../assets/imageFile/profile_young_woman.png";
import old_man from "../assets/imageFile/profile_old_man.png";
import old_woman from "../assets/imageFile/profile_old_woman.png";

function AITalkResult() {
  const location = useLocation();
  const navigate = useNavigate();
  const { ai_conversation_id, op_personality, description } =
    location.state || {};

  const [reviewData, setReviewData] = useState(null);

  let profilePic = boy; // 기본값 설정

  if (reviewData?.op_age !== undefined && reviewData?.op_gender) {
    if (reviewData.op_age < 20) {
      profilePic = reviewData.op_gender === "M" ? boy : girl;
    } else if (reviewData.op_age <= 40) {
      profilePic = reviewData.op_gender === "M" ? man : woman;
    } else {
      profilePic = reviewData.op_gender === "M" ? old_man : old_woman;
    }
  }

  useEffect(() => {
    async function fetchReviewData() {
      if (!ai_conversation_id) return;
      try {
        const response = await getResult(ai_conversation_id);
        setReviewData(response);
      } catch (error) {
        console.error("🔴 review data fetching 오류:", error);
      }
    }
    fetchReviewData();
  }, [ai_conversation_id]);

  const handleGoMain = () => {
    navigate(-1);
  };

  // 상세 체점 내역을 토글하는 컴포넌트
  const EvaluationComponent = ({ overall_score, overall_comment, scores }) => {
    const [expanded, setExpanded] = useState(false);

    const labelMapping = {
      1: "감정/의도 파악",
      2: "상호작용",
      3: "상황 및 분위기",
      4: "목적 및 내용 구성",
      5: "예의 및 긍정적 태도",
    };

    const getScoreClass = (score) => {
      if (score >= 0 && score <= 25) return "score-danger";
      if (score >= 26 && score <= 49) return "score-warning";
      if (score >= 50 && score <= 75) return "score-good";
      if (score >= 76 && score <= 100) return "score-excellent";
      return "";
    };

    let icon = null;
    let text = "";
    if (overall_score >= 0 && overall_score <= 25) {
      icon = <span className="evaluation-icon">❌</span>;
      text = "개선이 필요해요";
    } else if (overall_score >= 26 && overall_score <= 49) {
      icon = <img className="triangle" src={triangle} alt="세모" />;
      text = "아쉬운 표현이에요";
    } else if (overall_score >= 50 && overall_score <= 75) {
      icon = <img className="circle" src={circle} alt="동그라미" />;
      text = "괜찮은 표현이에요";
    } else if (overall_score >= 76 && overall_score <= 100) {
      icon = <span className="evaluation-icon">⭐</span>;
      text = "훌륭해요";
    }

    const toggleExpanded = () => {
      setExpanded((prev) => !prev);
    };

    return (
      <div className="evaluation-block">
        <div className="score-indicator">
          {icon}{" "}
          <span className={`evaluation-text ${getScoreClass(overall_score)}`}>
            {text}
          </span>
        </div>
        <div className="my-answer-label">나의 답변</div>
        <div className="overall-comment">{overall_comment}</div>
        <div className="toggle-container" onClick={toggleExpanded}>
          <span className="toggle-arrow">{expanded ? "▲" : "▼"}</span>
          <span className="toggle-text">
            {expanded ? "상세 채점 닫기" : "상세 채점 보기"}
          </span>
        </div>
        {expanded && (
          <div className="detailed-scores">
            {Object.entries(scores).map(([key, { score, explanation }]) => (
              <div key={key} className="detailed-score-item">
                <strong>{labelMapping[key]}:</strong> {score}점 - {explanation}
              </div>
            ))}
          </div>
        )}
      </div>
    );
  };

  return (
    <div>
      <div className="top-area">
        <img
          className="ai-back-button"
          src={backButton}
          alt="backbutton"
          onClick={handleGoMain}
        />
        <p className="top-title">AI 분석 결과 (성격 : {op_personality})</p>
      </div>

      <hr className="aichat_line" />

      <div className="chat-area">
        <p className="scenario-description">{description}</p>
        {reviewData ? (
          reviewData.conversation_records.map((record, index) => {
            if (record.speaker_type === "user") {
              const refinement = reviewData.refinements.find(
                (r) => r.turn === record.turn
              );
              return (
                <div key={index} className="chat-box2">
                  <div className="chat-text2">{record.text}</div>
                  {refinement && (
                    <EvaluationComponent
                      overall_score={refinement.overall_score}
                      overall_comment={refinement.overall_comment}
                      scores={refinement.scores}
                    />
                  )}
                </div>
              );
            } else {
              return (
                <div key={index} className="ai-message-container">
                  <img
                    src={profilePic}
                    alt="AI Profile"
                    className="ai-profile-pic"
                  />
                  <div className="ai-message-details">
                    <div className="ai-name">{reviewData.op_name}</div>
                    <div className="ai-speech-bubble">{record.text}</div>
                  </div>
                </div>
              );
            }
          })
        ) : (
          <p>Loading...</p>
        )}
      </div>
    </div>
  );
}

export default AITalkResult;
