import React from "react";
import { useNavigate } from "react-router-dom";
import BeatLoader from "react-spinners/BeatLoader";
import smile from "../assets/imageFile/smile.png";
import smiling from "../assets/imageFile/smiling.png";
import frown from "../assets/imageFile/frown.png";
import { Pagination } from "../RecordPage/Pagination";

const AITalkRecords = ({
  records,
  loading,
  currentPage,
  totalPages,
  onPageChange,
}) => {
  const navigate = useNavigate();

  if (loading) {
    return <p className="no-records">기록을 불러오는 중입니다...</p>;
  }

  if (!records || records.length === 0) {
    return <p className="no-records">해당 날짜의 AI 대화 기록이 없습니다.</p>;
  }

  const getEmoticonByScore = (score) => {
    if (score >= 71) return smile;
    if (score >= 51) return smiling;
    return frown;
  };

  return (
    <>
      {records.map((record) => (
        <div
          key={record.ai_conversation_id}
          className="user-match-card"
          onClick={() =>
            navigate("/ai-talk-result", {
              state: {
                ai_conversation_id: record.ai_conversation_id,
                op_personality: record.op_personality,
                description: record.description,
              },
            })
          }
        >
          {record.processed ? (
            <>
              <img
                className="match-icon"
                src={getEmoticonByScore(record.overall_score)}
                alt="평가 이모지"
              />
              <div className="match-info">
                <div className="match-info-top">
                  <span className="match-name">{record.description}</span>
                </div>
                <div className="match-info-bottom">
                  <div className="match-tags">
                    <span className="match-mode">#{record.op_personality}</span>
                    <span className="match-mode">
                      #{record.op_gender === "M" ? "남성" : "여성"}
                    </span>
                    <span className="match-mode">#{record.op_age}대</span>
                  </div>
                  <span className="match-time">
                    {new Date(record.created_at).toLocaleTimeString("ko-KR", {
                      hour: "2-digit",
                      minute: "2-digit",
                      hour12: true,
                    })}
                  </span>
                </div>
              </div>
            </>
          ) : (
            <div className="loading-container">
              <BeatLoader color="#fa400d" />
              <p>첨삭 진행중...</p>
            </div>
          )}
        </div>
      ))}

      <Pagination
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={onPageChange}
      />
    </>
  );
};

export default AITalkRecords;